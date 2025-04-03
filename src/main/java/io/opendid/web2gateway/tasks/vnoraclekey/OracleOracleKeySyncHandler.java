package io.opendid.web2gateway.tasks.vnoraclekey;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.enums.request.MethodEnum;
import io.opendid.web2gateway.common.traceid.TraceIdPutUtil;
import io.opendid.web2gateway.common.vnclient.VnGatewayClient;
import io.opendid.web2gateway.model.dto.vnclient.VnClientJobIdDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.repository.model.VngatewayRouteInfo;
import io.opendid.web2gateway.service.VnKeyInfoSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

@Component
public class OracleOracleKeySyncHandler {

    private static final Logger logger = LoggerFactory.getLogger(OracleOracleKeySyncHandler.class);
    @Autowired
    private VnKeyInfoSyncService vnKeyInfoSyncService;

    @Autowired
    private VnGatewayClient vnGatewayClient;

    public void syncOracleKeys() {



        List<VngatewayRouteInfo> vngatewayRouteInfos = vnKeyInfoSyncService.selectAllVns();
        vngatewayRouteInfos.forEach(routeInfo -> {
            try {
                TraceIdPutUtil.newPutTraceId();
                JsonRpc2Response response =
                        vnGatewayClient.requestJobSend(requestParameters(routeInfo.getVnCode()));

                logger.info("Oracle Keys Sync Request: {}", JSON.toJSON(response));


                if (Objects.isNull(response)) {
                    logger.error("Oracle Keys Sync Result is null,vn not connected");
                    return;
                }
                Object result = response.getResult();


                if (!Objects.isNull(result)){
                    updateKey(routeInfo,(JSONObject) result);
                }



                TraceIdPutUtil.removeTraceId();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }

    private void updateKey(VngatewayRouteInfo routeInfo,
                           JSONObject response) {
        String accountPubKey=response.getString("accountPubKey");
        String homeChain=response.getString("homeChain").toLowerCase();
        String accountAddress=response.getString("accountAddress");


        VngatewayRouteInfo upateKeyInfo = new VngatewayRouteInfo();
        upateKeyInfo.setRouteId(routeInfo.getRouteId());
        upateKeyInfo.setVnHomechainPublicKey(accountPubKey);
        upateKeyInfo.setVnHomechainAccount(accountAddress);
        upateKeyInfo.setVnHomeChainName(homeChain);
        vnKeyInfoSyncService.updateKeyInfo(upateKeyInfo);
    }


    private VnClientJobIdDTO requestParameters(String vnCode) {

        JsonRpc2Request jsonRpc2Request = new JsonRpc2Request(
                1L,
                MethodEnum.GET_VN_INFO.getCode(),
                new LinkedHashMap(),
                ""
        );

        VnClientJobIdDTO vnClientJobIdDTO = new VnClientJobIdDTO();
        vnClientJobIdDTO.setRequestBody(jsonRpc2Request);
        vnClientJobIdDTO.setVnCode(vnCode);


        return vnClientJobIdDTO;
    }
}
