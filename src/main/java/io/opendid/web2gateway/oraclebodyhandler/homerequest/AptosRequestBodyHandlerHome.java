package io.opendid.web2gateway.oraclebodyhandler.homerequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.homechain.HomeChainName;
import io.opendid.web2gateway.common.utils.AptosUtils;
import io.opendid.web2gateway.common.vnclient.VnGatewayClient;
import io.opendid.web2gateway.config.WalletPrivateKeyConfig;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.oracle.*;
import io.opendid.web2gateway.model.dto.vnclient.VnClientJobIdDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.oraclebodyhandler.interfaces.HomeChainRequestBodyInterface;
import io.opendid.web2gateway.oraclebodyhandler.proxyclient.HomeWrapRequestBodyClient;
import io.opendid.web2gateway.repository.model.VngatewayRouteInfo;
import io.opendid.web2gateway.service.VngatewayRouteInfoService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component(HomeChainName.APTOS + HomeChainRequestBodyInterface.BEAN_SUFFIX)
public class AptosRequestBodyHandlerHome implements HomeChainRequestBodyInterface {


    private Logger logger= LoggerFactory.getLogger(AptosRequestBodyHandlerHome.class);

    @Autowired
    private HomeWrapRequestBodyClient requestBodyClient;
    @Autowired
    private VnGatewayClient vnGatewayClient;
    @Autowired
    private WalletPrivateKeyConfig walletPrivateKeyConfig;
    @Autowired
    private VngatewayRouteInfoService vngatewayRouteInfoService;


    @Override
    public OracleWrapRequestBodyResDTO wrapBody(OracleWrapRequestBodyDTO oracleWrapRequestBodyDTO) throws Exception {


        WarpAptosReqBodyRequestDTO warpAptosReqBodyRequestDTO = new WarpAptosReqBodyRequestDTO();
        warpAptosReqBodyRequestDTO.setJobId(oracleWrapRequestBodyDTO.getJobId());
        warpAptosReqBodyRequestDTO.setPublicKey(oracleWrapRequestBodyDTO.getPublicKey());
        warpAptosReqBodyRequestDTO.setData(oracleWrapRequestBodyDTO.getData());
        warpAptosReqBodyRequestDTO.setVnCode(oracleWrapRequestBodyDTO.getVnCode());
        warpAptosReqBodyRequestDTO.setGenerateClaim(oracleWrapRequestBodyDTO.getGenerateClaim());

        logger.info("MethodOracleRequest process warp RequestBody prams={}",
                JSON.toJSONString(warpAptosReqBodyRequestDTO));

        // Get warp request body
        JsonRpc2Response request = requestBodyClient.getWarpReqBody(
                warpAptosReqBodyRequestDTO);
        WarpAptosReqBodyResponseDTO warpReqBody =
                JSONObject.parseObject(request.getResult().toString(), WarpAptosReqBodyResponseDTO.class);
        logger.info("MethodOracleRequest process warp RequestBody={}", JSON.toJSONString(warpReqBody));

        OracleWrapRequestBodyResDTO requestBodyResDTO=new OracleWrapRequestBodyResDTO();
        requestBodyResDTO.setWrapData(warpReqBody);
        requestBodyResDTO.setClaimFee(warpReqBody.getClaimFee());
        requestBodyResDTO.setClaimFeeFree(warpReqBody.getClaimFeeFree());
        requestBodyResDTO.setJobIdFee(warpReqBody.getJobIdFee());
        requestBodyResDTO.setJobIdFree(warpReqBody.getJobIdFree());

        return requestBodyResDTO;

    }

    @Override
    public OracleRequestSignatureResDTO signature(OracleRequestSignatureDTO oracleRequestSignatureDTO) throws Exception, JsonRpc2ServerErrorException {


        String walletPrivateKey = oracleRequestSignatureDTO.getVnWalletPrivateKey();

        WarpAptosReqBodyResponseDTO wrapRequestBodyResDTO=
                (WarpAptosReqBodyResponseDTO)oracleRequestSignatureDTO.getWrapData();
        byte[] signature = AptosUtils.ed25519Sign(
                AptosUtils.hexToByteArray(walletPrivateKey),
                AptosUtils.hexToByteArray(wrapRequestBodyResDTO.getEncodeHash()));

        OracleRequestSignatureResDTO requestSignatureResDTO
                =new OracleRequestSignatureResDTO();
        requestSignatureResDTO.setSignature(AptosUtils.byteArrayToHexWithPrefix(signature));
        wrapRequestBodyResDTO.getSignature().setSignature(requestSignatureResDTO.getSignature());

        requestSignatureResDTO.setReWrapOriginWrapData(wrapRequestBodyResDTO);
        return requestSignatureResDTO;
    }


    @Override
    public JsonRpc2Response sendMessage(OracleRequestSendMessageDTO sendMessageDTO) throws Exception, JsonRpc2ServerErrorException {

        OracleRequestMetaDataDTO metaData = sendMessageDTO.getMetaData();
        OracleRequestSignatureResDTO signatureResDTO = sendMessageDTO.getSignatureResDTO();
        JsonRpc2Request requestClientBody = sendMessageDTO.getRequestClientBody();

        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("signedTx", JSON.toJSONString(signatureResDTO.getReWrapOriginWrapData()));
        linkedHashMap.put("metadata", metaData);

        VnClientJobIdDTO vnClientJobIdDTO = new VnClientJobIdDTO();
        vnClientJobIdDTO.setJobId(metaData.getJobId());
        JsonRpc2Request jsonRpc2Request = new JsonRpc2Request(
                requestClientBody.getId(),
                requestClientBody.getMethod(),
                linkedHashMap,
                ""
        );
        vnClientJobIdDTO.setRequestBody(jsonRpc2Request);
        vnClientJobIdDTO.setVnCode(metaData.getVnCode());
        JsonRpc2Response respResult = vnGatewayClient.request(vnClientJobIdDTO);

        return respResult;
    }


    @Override
    public List<GeneratePubKeyAndAddrDTO> generatePublicKeyAndAddr() throws Exception {

        List<Map<String,String>> walletPrivateKeyList = walletPrivateKeyConfig.getPrivatekey();

        List<VngatewayRouteInfo> vngatewayRouteInfos = vngatewayRouteInfoService.selectAllVn();

        // The number of configured private keys must be consistent with the number of vn
        if (vngatewayRouteInfos.size() != walletPrivateKeyList.size()){
            throw new RuntimeException("In the configuration: wallet.privatekey, " +
                "the number of private keys included must be consistent with the number of Vn." +
                "Vn quantity: "+vngatewayRouteInfos.size());
        }


        List<GeneratePubKeyAndAddrDTO> resultList = new ArrayList<>();

        for (int i = 0; i < walletPrivateKeyList.size(); i++) {

            String privateKeyCode= walletPrivateKeyList.get(i).get(WalletPrivateKeyConfig.KEY_NAME);
            if (StringUtils.isBlank(privateKeyCode)){
                logger.warn(" KeyCode is missing");
                continue;
            }
            String walletPrivateKey = walletPrivateKeyList.get(i).get(WalletPrivateKeyConfig.KEY_VALUE);
            VngatewayRouteInfo vngatewayRouteInfo = vngatewayRouteInfos.get(i);

            String pubKeyStr = AptosUtils.generatePublicKeyFromPrivateKey(walletPrivateKey);
            String address = AptosUtils.generateAddressFromPublicKey(pubKeyStr);

            GeneratePubKeyAndAddrDTO generatePubKeyAndAddrDTO = new GeneratePubKeyAndAddrDTO();
            generatePubKeyAndAddrDTO.setPublicKey(pubKeyStr);
            generatePubKeyAndAddrDTO.setWalletAddress(address);
            generatePubKeyAndAddrDTO.setVnCode(vngatewayRouteInfo.getVnCode());
            generatePubKeyAndAddrDTO.setPrivateKey(walletPrivateKey);
            generatePubKeyAndAddrDTO.setPrivateKeyCode(privateKeyCode);

            resultList.add(generatePubKeyAndAddrDTO);
        }

        return resultList;
    }



}
