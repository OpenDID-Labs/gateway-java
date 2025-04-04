package io.opendid.web2gateway.common.web2;

public interface Web2MethodName {


  String ORACLE_WRAP_REQUEST_BODY="oracle_wrap_request_body";

  String ORACLE_SEND_SIGNATURE_BODY="oracle_send_signature_body";

  String ORACLE_GET_JOB_FEE="oracle_get_job_fee";

  String ORACLE_GET_VN_INFO="oracle_get_vn_info";

  String ORACLE_GET_TRANSACTION="oracle_get_transaction";

  String ORACLE_REQUEST="oracle_request";
  String ORACLE_REQUEST_BILL="oracle_request_bill";




  String ORACLE_CALL_BACK="oracle_callback";

  String ORACLE_REQUEST_CANCEL_CALLBACK ="request_cancel_callback";

  String ORACLE_GET_WALLET_ADDRESS="get_wallet_address";

  String REQUEST_CANCEL="request_cancel";

  String ORACLE_GET_CANCEL_TRANSACTION="get_cancel_transaction";

  String ORACLE_REQUEST_CLAIM_CALLBACK="request_claim_callback";

  String SUB_TOKEN_TRANSFER ="sub_token_transfer";

  String SUB_CREATE_SUBSCRIPTION= "sub_create";

  String SUB_ADD_CONSUMER= "sub_add_consumer";

  String SUB_REMOVE_CONSUMER= "sub_remove_consumer";

  String SUB_GET_SUBSCRIPTION= "sub_get_subscription";

  String JWT_ROOT_UPDATE="jwt_root_update";
  String JWT_TENANT_CREATE="jwt_tenant_create";
  String JWT_TENANT_CANCEL ="jwt_tenant_cancel";


  String GET_METHOD_ESTIMATE_ORACLE_REQUEST_TOKEN="get_method_estimate_oracle_request_token";

}
