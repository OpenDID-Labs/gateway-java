package io.opendid.web2gateway.oracleweb2process.method;

import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.enums.status.CallbackProcessStatus;
import io.opendid.web2gateway.common.web2.Web2MethodName;
import io.opendid.web2gateway.model.dto.oracle.OracleCallbackClaimDTO;
import io.opendid.web2gateway.model.dto.vnclient.OracleClaimCallbackRespDTO;
import io.opendid.web2gateway.model.dto.vnclient.VnCallbackSignPayloadDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.security.checkaspect.MethodPublic;
import io.opendid.web2gateway.service.ClaimEventLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component(Web2MethodName.ORACLE_REQUEST_CLAIM_CALLBACK + Web2Method.BEAN_SUFFIX)
@MethodPublic
public class MethodOracleRequestClaimCallback implements Web2CallbackMethod {


    @Autowired
    private ClaimEventLogService claimEventLogService;

    private static final Logger logger = LoggerFactory.getLogger(MethodOracleRequestClaimCallback.class);

    @Override
    public VnCallbackSignPayloadDTO buildSignPayload(String decryptStr) {

        OracleCallbackClaimDTO oracleCallbackClaimDTO =
                JSONObject.parseObject(decryptStr, OracleCallbackClaimDTO.class);

        VnCallbackSignPayloadDTO vnCallbackSignPayloadDTO = new VnCallbackSignPayloadDTO();
        vnCallbackSignPayloadDTO.setRequestId(oracleCallbackClaimDTO.getRequestId());
        vnCallbackSignPayloadDTO.setVnSignData(oracleCallbackClaimDTO.getSignData());

        String signStr = oracleCallbackClaimDTO.getChainName()
                + oracleCallbackClaimDTO.getRequestId()
                + oracleCallbackClaimDTO.getClaimId()
                + oracleCallbackClaimDTO.getIssuer()
                + oracleCallbackClaimDTO.getRequestTxnHash()
                + oracleCallbackClaimDTO.getRequestDataHash()
                + oracleCallbackClaimDTO.getResponseTxnHash()
                + oracleCallbackClaimDTO.getResponseDataHash()
                + oracleCallbackClaimDTO.getCustomizeHash()
                + oracleCallbackClaimDTO.getIdSystem()
                + oracleCallbackClaimDTO.getIssuanceDate()
                + oracleCallbackClaimDTO.getExpirationDate()
                + oracleCallbackClaimDTO.getSignature()
                + oracleCallbackClaimDTO.getTransactionHash()
                + oracleCallbackClaimDTO.getVersion()
                + oracleCallbackClaimDTO.getContractParams()
                + oracleCallbackClaimDTO.getStatus()
                ;

        vnCallbackSignPayloadDTO.setSignStr(signStr);

        LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>();

        linkedHashMap.put("chainName",oracleCallbackClaimDTO.getChainName());
        linkedHashMap.put("requestId",oracleCallbackClaimDTO.getRequestId());
        linkedHashMap.put("claimId",oracleCallbackClaimDTO.getClaimId());
        linkedHashMap.put("issuer",oracleCallbackClaimDTO.getIssuer());
        linkedHashMap.put("requestTxnHash",oracleCallbackClaimDTO.getRequestTxnHash());
        linkedHashMap.put("requestDataHash",oracleCallbackClaimDTO.getRequestDataHash());
        linkedHashMap.put("responseTxnHash",oracleCallbackClaimDTO.getResponseTxnHash());
        linkedHashMap.put("responseDataHash",oracleCallbackClaimDTO.getResponseDataHash());
        linkedHashMap.put("customizeHash",oracleCallbackClaimDTO.getCustomizeHash());
        linkedHashMap.put("idSystem",oracleCallbackClaimDTO.getIdSystem());
        linkedHashMap.put("issuanceDate",oracleCallbackClaimDTO.getIssuanceDate());
        linkedHashMap.put("expirationDate",oracleCallbackClaimDTO.getExpirationDate());
        linkedHashMap.put("signature",oracleCallbackClaimDTO.getSignature());
        linkedHashMap.put("transactionHash",oracleCallbackClaimDTO.getTransactionHash());
        linkedHashMap.put("version",oracleCallbackClaimDTO.getVersion());
        linkedHashMap.put("contractParams",oracleCallbackClaimDTO.getContractParams());
        linkedHashMap.put("status",oracleCallbackClaimDTO.getStatus());

        vnCallbackSignPayloadDTO.setMethodJsonRpc2Params(linkedHashMap);

        return vnCallbackSignPayloadDTO;
    }

    @Override
    public Object process(JsonRpc2Request request) throws Exception {
        OracleClaimCallbackRespDTO callbackRespDTO = new OracleClaimCallbackRespDTO();
        try {

            LinkedHashMap params = request.getParams();
            String requestId = params.get("requestId").toString();
            claimEventLogService.updateEventLogClaimStatus(requestId);
            claimEventLogService.insertClaim(params);
            callbackRespDTO.setSuccessfully(CallbackProcessStatus.SUCCESSFUL.getCode());
        } catch (Exception e) {
            logger.error("MethodOracleRequestClaimCallback process error:", e);
            callbackRespDTO.setSuccessfully(CallbackProcessStatus.FAILED.getCode());
            callbackRespDTO.setCallbackMessage(e.getMessage());
        }
        return callbackRespDTO;
    }

    @Override
    public String checkParams(JsonRpc2Request request) {
        return "";
    }
}
