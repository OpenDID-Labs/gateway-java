package io.opendid.web2gateway.oracleweb2process.method;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.codes.JsonRpc2MessageCodeEnum;
import io.opendid.web2gateway.common.enums.status.ProcessStatusEnum;
import io.opendid.web2gateway.common.traceid.LogTraceIdConstant;
import io.opendid.web2gateway.common.utils.AptosUtils;
import io.opendid.web2gateway.common.utils.GatewayKeyVaultUtil;
import io.opendid.web2gateway.common.vnclient.VnGatewayClient;
import io.opendid.web2gateway.common.web2.Web2MethodName;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.oracle.*;
import io.opendid.web2gateway.model.dto.vnclient.VnClientJobIdDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.model.jsonrpc2.method.OracleResultMethodRes;
import io.opendid.web2gateway.repository.mapper.VngatewayJobidMappingMapper;
import io.opendid.web2gateway.repository.model.VngatewayJobidMapping;
import io.opendid.web2gateway.security.checkaspect.MethodPrivate;
import io.opendid.web2gateway.service.OracleContractEventLogService;
import io.opendid.web2gateway.service.OracleMsgRecordService;
import io.opendid.web2gateway.service.OracleNonceService;
import io.opendid.web2gateway.service.WarpRequestBodyService;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component(Web2MethodName.ORACLE_REQUEST + Web2Method.BEAN_SUFFIX)
@MethodPrivate
public class MethodOracleRequest implements Web2Method {

  private Logger logger = LoggerFactory.getLogger(MethodOracleRequest.class);

  @Autowired
  private WarpRequestBodyService warpRequestBodyService;
  @Autowired
  private OracleNonceService oracleNonceService;
  @Autowired
  private VnGatewayClient vnGatewayClient;
  @Autowired
  private OracleContractEventLogService oracleContractEventLogService;
  @Autowired
  private VngatewayJobidMappingMapper vngatewayJobidMappingMapper;
  @Autowired
  private OracleMsgRecordService oracleMsgRecordService;


  @Value("${wallet.privatekey}")
  private String homeChainPrivateKey;
  @Value("${oracle.callBack.url}")
  private String callBackUrl;


  @Override
  public Object process(JsonRpc2Request request) throws Exception {

    if (request.getParams() == null) {
      return new OracleResultMethodRes();
    }

    String homeChainPublicKey = GatewayKeyVaultUtil.getKey(GatewayKeyVaultUtil.walletPublicKey);

    LinkedHashMap params = request.getParams();
    String jobId = MapUtils.getString(params, "jobId");
    String data = MapUtils.getString(params, "data");

    logger.info("MethodOracleRequest process jobId={}", jobId);

    Long nonce = getNonce(homeChainPublicKey);

    logger.info("MethodOracleRequest process jobId={},nonce={}", jobId, nonce);

    WarpReqBodyRequestDTO warpReqBodyRequestDTO = new WarpReqBodyRequestDTO();
    warpReqBodyRequestDTO.setJobId(jobId);
    warpReqBodyRequestDTO.setPublicKey(homeChainPublicKey);
    warpReqBodyRequestDTO.setNonce(nonce.toString());
    warpReqBodyRequestDTO.setData(data);

    logger.info("MethodOracleRequest process warp RequestBody prams={}",
        JSON.toJSONString(warpReqBodyRequestDTO));

    // Get warp request body
    WarpReqBodyResponseDTO warpReqBody = warpRequestBodyService.getWarpReqBody(
        warpReqBodyRequestDTO);

    logger.info("MethodOracleRequest process warp RequestBody={}", JSON.toJSONString(warpReqBody));

    // 1,Signing with a private key
    byte[] signature = AptosUtils.ed25519Sign(
        AptosUtils.hexToByteArray(homeChainPrivateKey),
        AptosUtils.hexToByteArray(warpReqBody.getEncodeHash()));

    warpReqBody.getSignature().setSignature(AptosUtils.byteArrayToHexWithPrefix(signature));

    // 2，Call vn gateway
    OracleRequestMetaDataDTO metaData = new OracleRequestMetaDataDTO();
    metaData.setJobId(jobId);
    metaData.setNonce(nonce);
    metaData.setPublicKey(homeChainPublicKey);
    metaData.setCallbackUrl(callBackUrl);
    metaData.setData(data);

    LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
    linkedHashMap.put("signedTx", JSON.toJSONString(warpReqBody));
    linkedHashMap.put("metadata", metaData);

    VnClientJobIdDTO vnClientJobIdDTO = new VnClientJobIdDTO();
    vnClientJobIdDTO.setJobId(jobId);
    JsonRpc2Request jsonRpc2Request = new JsonRpc2Request(
        request.getId(),
        request.getMethod(),
        linkedHashMap,
        ""
    );
    vnClientJobIdDTO.setRequestBody(jsonRpc2Request);

    JsonRpc2Response respResult = vnGatewayClient.request(vnClientJobIdDTO);

    if (respResult == null){
      logger.error("MethodOracleRequest request vn gateway return null");
      throw new JsonRpc2ServerErrorException(
          JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getCode(),
          MDC.get(LogTraceIdConstant.TRACE_ID),
          JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getMessage(),
          "Method request call vn result is null");
    }

    JSONObject respResultJson = JSONObject.parseObject(
        JSONObject.toJSONString(respResult.getResult()));

    logger.info("MethodOracleRequest process send result={}", respResultJson);

    Object result = respResult.getResult();

    // Insert to DB
    String requesttId = AptosUtils.genRequestId(
        AptosUtils.generateAddressFromPublicKey(homeChainPublicKey), nonce);
    VngatewayJobidMapping vngatewayJobidMapping = vngatewayJobidMappingMapper.selectByJobId(jobId);

    logger.info("MethodOracleRequest process requestId={}", requesttId);

    ContractEventLogInsertDTO insertDTO = new ContractEventLogInsertDTO();
    insertDTO.setJobId(jobId);
    insertDTO.setRequestId(requesttId);
    insertDTO.setVnCode(vngatewayJobidMapping.getVnCode());
    insertDTO.setPlatformCode(vngatewayJobidMapping.getPlatformCode());
    insertDTO.setJobIdFee(warpReqBody.getJobIdFee());
    insertDTO.setTraceId(MDC.get(LogTraceIdConstant.TRACE_ID));
    insertDTO.setRequestBody(data);

    if (respResultJson.get("failReason") != null && !"".equals(respResultJson.get("failReason"))) {
      // Set DB status
      insertDTO.setProcessStatus(ProcessStatusEnum.PAY_FAIL.getCode());
      insertDTO.setErrorMsg(respResultJson.get("failReason").toString());

    } else {
      insertDTO.setProcessStatus(ProcessStatusEnum.PENDING.getCode());
    }
    if (respResultJson.get("oracleRequestHash") != null) {
      insertDTO.setRequestOracleHash(respResultJson.getString("oracleRequestHash"));
    }
    if(respResultJson.get("aptosVersion") != null){
      insertDTO.setRequestAptosVersion(respResultJson.getString("aptosVersion"));
    }

    oracleContractEventLogService.insertContractEventLog(insertDTO);

    /*MsgRecordInsertDTO msgRecordInsertDTO = new MsgRecordInsertDTO();
    msgRecordInsertDTO.setVnCode(vngatewayJobidMapping.getVnCode());
    msgRecordInsertDTO.setRequestId(requesttId);
    msgRecordInsertDTO.setRequestBody(data);

    oracleMsgRecordService.insertMsgRecord(msgRecordInsertDTO);*/

    if (respResultJson.get("failReason") != null && !"".equals(respResultJson.get("failReason"))) {

      OracleRequestErrorDataDTO oracleRequestErrorDataDTO = new OracleRequestErrorDataDTO();
      oracleRequestErrorDataDTO.setRequestId(requesttId);

      String errorData = JSON.toJSONString(oracleRequestErrorDataDTO);
      logger.info("MethodOracleRequest process error data={}", errorData);

      throw new JsonRpc2ServerErrorException(
          JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32001.getCode(),
          "",
          JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32001.getMessage(),
          oracleRequestErrorDataDTO);
    }

    OracleRequestRespDTO oracleRequestRespDTO = JSONObject.parseObject(result.toString(),
        OracleRequestRespDTO.class);

    return oracleRequestRespDTO;
  }

  @Override
  public String checkParams(JsonRpc2Request request) {
    return null;
  }


  private void insertEventLog() {

  }


  private Long getNonce(String publicKey) throws Exception {

    String address = AptosUtils.generateAddressFromPublicKey(publicKey);

    OracleNonceUpdateDTO oracleNonceUpdateDTO = new OracleNonceUpdateDTO();
    oracleNonceUpdateDTO.setPublicKey(publicKey);
    oracleNonceUpdateDTO.setAddress(address);

    Long nonceValue = oracleNonceService.updateOracleNonce(oracleNonceUpdateDTO);

    return nonceValue;
  }


}
