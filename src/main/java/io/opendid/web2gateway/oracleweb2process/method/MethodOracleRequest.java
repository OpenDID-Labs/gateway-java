package io.opendid.web2gateway.oracleweb2process.method;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.codes.JsonRpc2MessageCodeEnum;
import io.opendid.web2gateway.common.enums.status.ClaimStatusEnum;
import io.opendid.web2gateway.common.enums.status.ProcessStatusEnum;
import io.opendid.web2gateway.common.traceid.LogTraceIdConstant;
import io.opendid.web2gateway.common.utils.GatewayKeyVaultUtil;
import io.opendid.web2gateway.common.utils.UuidUtil;
import io.opendid.web2gateway.common.vnclient.VnGatewayClient;
import io.opendid.web2gateway.common.web2.Web2MethodName;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.chainkey.ChainKeyDTO;
import io.opendid.web2gateway.model.dto.oracle.*;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.model.jsonrpc2.method.OracleResultMethodRes;
import io.opendid.web2gateway.oraclebodyhandler.factory.HomeChainRequestBodyFactory;
import io.opendid.web2gateway.oraclebodyhandler.interfaces.HomeChainRequestBodyInterface;
import io.opendid.web2gateway.repository.mapper.VngatewayJobidMappingMapper;
import io.opendid.web2gateway.repository.model.GatewayHomechainKeyManage;
import io.opendid.web2gateway.repository.model.VngatewayJobidMapping;
import io.opendid.web2gateway.repository.model.VngatewayRouteInfo;
import io.opendid.web2gateway.security.checkaspect.MethodPrivate;
import io.opendid.web2gateway.service.*;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

@Component(Web2MethodName.ORACLE_REQUEST + Web2Method.BEAN_SUFFIX)
@MethodPrivate
public class MethodOracleRequest implements Web2Method {

  private Logger logger = LoggerFactory.getLogger(MethodOracleRequest.class);


  @Autowired
  private OracleNonceService oracleNonceService;
  @Autowired
  private VnGatewayClient vnGatewayClient;
  @Autowired
  private OracleContractEventLogService oracleContractEventLogService;
  @Autowired
  private VngatewayJobidMappingMapper vngatewayJobidMappingMapper;

  @Autowired
  private VngatewayRouteInfoService vngatewayRouteInfoService;
  @Autowired
  private HomeChainKeyManageService homeChainKeyManageService;
  @Autowired
  private VngatewayJobidMappingService vngatewayJobidMappingService;


  @Value("${oracle.callBack.url}")
  private String callBackUrl;


  @Override
  public Object process(JsonRpc2Request request) throws Exception {

    HomeChainRequestBodyInterface homeChainRequestHandler =
            HomeChainRequestBodyFactory.createRequestBodyHandler();
    if (request.getParams() == null) {
      return new OracleResultMethodRes();
    }

    LinkedHashMap params = request.getParams();
    String jobId = MapUtils.getString(params, "jobId");
    String data = MapUtils.getString(params, "data");
    boolean generateClaim = MapUtils.getBooleanValue(params, "generateClaim",false);

    List<VngatewayRouteInfo> vnGatewayRouteInfos = selectVnList(params);
    List<OracleRequestRespDTO> resultList = new ArrayList<>();
    String transactionBatchCode = "grp_"+UuidUtil.generateUuid32();

    for (VngatewayRouteInfo vnGatewayRouteInfo : vnGatewayRouteInfos) {


      String vnCode = vnGatewayRouteInfo.getVnCode();
      GatewayHomechainKeyManage gatewayHomechainKeyManage = homeChainKeyManageService.selectByVnCode(vnCode);
      String homeChainPublicKey = gatewayHomechainKeyManage.getWalletPublicKey();

      logger.info("MethodOracleRequest process jobId={}", jobId);


      //1 wrap request body
      OracleWrapRequestBodyDTO wrapReqBody = new OracleWrapRequestBodyDTO();
      wrapReqBody.setJobId(jobId);
      wrapReqBody.setPublicKey(homeChainPublicKey);
      wrapReqBody.setData(data);
      wrapReqBody.setVnCode(vnCode);
      wrapReqBody.setGenerateClaim(generateClaim);
      OracleWrapRequestBodyResDTO requestBodyResDTO = homeChainRequestHandler.wrapBody(wrapReqBody);


      // 2,Signing with a private key
      ChainKeyDTO chainKeyDTO = GatewayKeyVaultUtil.getChainKeyByVnCode(vnCode);
      ChainKeyDTO chainKeyByVnCode = GatewayKeyVaultUtil.getChainKeyByVnCode(vnCode);


      OracleRequestSignatureDTO signatureDTO=new OracleRequestSignatureDTO();
      signatureDTO.setVnWalletPrivateKey(chainKeyDTO.getPrivateKey());
      signatureDTO.setWrapData(requestBodyResDTO.getWrapData());
      OracleRequestSignatureResDTO signatureResDTO = homeChainRequestHandler.signature(signatureDTO);


      // 3，Call vn gateway for send transaction
      OracleRequestMetaDataDTO metaData = new OracleRequestMetaDataDTO();
      metaData.setVnCode(vnCode);
      metaData.setJobId(jobId);
      metaData.setPublicKey(homeChainPublicKey);
      metaData.setCallbackUrl(callBackUrl);
      metaData.setData(data);
      metaData.setGenerateClaim(generateClaim);
      metaData.setClaimFee(requestBodyResDTO.getClaimFee());
      metaData.setClaimFree(requestBodyResDTO.getClaimFeeFree());
      metaData.setJobIdFee(requestBodyResDTO.getJobIdFee());
      metaData.setJobIdFree(requestBodyResDTO.getJobIdFree());

      OracleRequestSendMessageDTO sendMessageDTO=new OracleRequestSendMessageDTO();
      sendMessageDTO.setSignatureResDTO(signatureResDTO);
      sendMessageDTO.setRequestClientBody(request);
      sendMessageDTO.setMetaData(metaData);
      JsonRpc2Response respResult = homeChainRequestHandler.sendMessage(sendMessageDTO);

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

      // after process result
      SelectJobIdMappingDTO selectJobIdMappingDTO = new SelectJobIdMappingDTO();
      selectJobIdMappingDTO.setJobId(jobId);
      selectJobIdMappingDTO.setVnCode(vnCode);
      VngatewayJobidMapping vngatewayJobidMapping = vngatewayJobidMappingMapper.selectByJobIdVnCode(selectJobIdMappingDTO);

      ContractEventLogInsertDTO insertDTO = new ContractEventLogInsertDTO();
      insertDTO.setJobId(jobId);
      insertDTO.setVnCode(vngatewayJobidMapping.getVnCode());
      insertDTO.setPlatformCode(vngatewayJobidMapping.getPlatformCode());
      insertDTO.setJobIdFee(requestBodyResDTO.getJobIdFee());
      insertDTO.setClaimFee(requestBodyResDTO.getClaimFee());
      insertDTO.setTraceId(MDC.get(LogTraceIdConstant.TRACE_ID));
      insertDTO.setRequestBody(data);
      insertDTO.setTransactionBatchCode(transactionBatchCode);

      failResultSetting(respResultJson, insertDTO);
      if (respResultJson.get("oracleRequestTxHash") != null) {
        insertDTO.setRequestOracleHash(respResultJson.getString("oracleRequestTxHash"));
      }
      if(generateClaim){
        insertDTO.setClaimStatus(ClaimStatusEnum.PENDING.getCode());
      }
      insertDTO.setKeyCode(chainKeyByVnCode.getKeyCode());
      oracleContractEventLogService.insertContractEventLog(insertDTO);


      throwFailReuslt(respResultJson);
      OracleRequestRespDTO oracleRequestRespDTO = JSONObject.parseObject(result.toString(),
          OracleRequestRespDTO.class);
      resultList.add(oracleRequestRespDTO);
    }

    return resultList;
  }



  @Override
  public String checkParams(JsonRpc2Request request) {

    Object jobId = request.getParams().get("jobId");
    if (jobId == null || StringUtils.isBlank(String.valueOf(jobId))) {
      return " jobId is empty";
    }

    List<VngatewayJobidMapping> vngatewayJobidMappings = vngatewayJobidMappingService.searchByJobId(jobId.toString());
    if (vngatewayJobidMappings.isEmpty()){
      return "jobId does not exist";
    }

    return null;
  }




  private List<VngatewayRouteInfo> selectVnList(LinkedHashMap params) throws Exception {

    String jobId = MapUtils.getString(params, "jobId");
    String[] vnList=null;
    // Query the VnCode parameter in the input parameter
    Object vnListObj = MapUtils.getObject(params, "vnList");
    if (vnListObj instanceof JSONArray){
        // Determine whether vnCode is correct
      JSONArray jsonArray = (JSONArray) vnListObj;
      vnList= jsonArray.toJavaList(String.class).toArray(new String[0]);
    }


    // Query the Vn quantity parameter in the input parameter
    Integer vnCount = MapUtils.getInteger(params, "vnCount");

    if (vnList!=null && vnList.length>0) {

      // Determine whether vnCode is correct
      List<VngatewayRouteInfo> vnGatewayRouteInfos = vngatewayRouteInfoService.selectVnRouteByCodes(vnList);
      if (vnGatewayRouteInfos.size() != vnList.length) {
        // The number of input parameters is inconsistent with the number of query results,
        // indicating that there is non-existent vnGateway information in the input parameters.
        logger.error("MethodOracleRequest selectVnList vnGateway number error");
        throw new JsonRpc2ServerErrorException(
            JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getCode(),
            MDC.get(LogTraceIdConstant.TRACE_ID),
            JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getMessage(),
            "The VnCode you entered is incorrect!");
      }

      return vnGatewayRouteInfos;
    } else if (vnCount != null){
      // Randomly select vnCode based on the number of user requests
      List<VngatewayRouteInfo> vnGatewayRouteInfos = vngatewayRouteInfoService.selectVnRouteByJobId(jobId);

      // Determine whether the quantity is reasonable
      if (vnCount <= 0
          || vnCount > vnGatewayRouteInfos.size()) {
        logger.error("MethodOracleRequest selectVnList vnGateway number error");
        throw new JsonRpc2ServerErrorException(
            JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getCode(),
            MDC.get(LogTraceIdConstant.TRACE_ID),
            JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getMessage(),
            "The number of VNs that support this JobId is:" + vnGatewayRouteInfos.size());
      }

      // Select vn method based on quantity, the current strategy is random
      // Shuffle the list order and return vnCount quantity
      Collections.shuffle(vnGatewayRouteInfos);

      return vnGatewayRouteInfos.subList(0, vnCount);
    }

    logger.error("MethodOracleRequest selectVnList no code and number");
    throw new JsonRpc2ServerErrorException(
        JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getCode(),
        MDC.get(LogTraceIdConstant.TRACE_ID),
        JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getMessage(),
        "You must specify the VnCode or Vn number");
  }

  private void throwFailReuslt(JSONObject respResultJson) throws Exception {
    if (respResultJson.get("failReason") != null && !"".equals(respResultJson.get("failReason"))) {

      OracleRequestErrorDataDTO oracleRequestErrorDataDTO = new OracleRequestErrorDataDTO();
      String errorData = JSON.toJSONString(oracleRequestErrorDataDTO);
      logger.info("MethodOracleRequest process error data={}", errorData);

      throw new JsonRpc2ServerErrorException(
              JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32001.getCode(),
              "",
              JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32001.getMessage(),
              oracleRequestErrorDataDTO);
    }
  }

  private static void failResultSetting(JSONObject respResultJson, ContractEventLogInsertDTO insertDTO) {
    if (respResultJson.get("failReason") != null && !"".equals(respResultJson.get("failReason"))) {
      // Set DB status
      insertDTO.setProcessStatus(ProcessStatusEnum.PAY_FAIL.getCode());
      insertDTO.setErrorMsg(respResultJson.get("failReason").toString());

    } else {
      insertDTO.setProcessStatus(ProcessStatusEnum.PENDING.getCode());
    }
  }
}
