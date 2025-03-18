alter table od_oracle_contract_eventlog add column claim_status int default 0 comment 'claim status: 0=not create 1=create pending 5=create success 10=create fail';
alter table od_oracle_contract_eventlog add column claim_error_msg text comment 'claim_error_msg';

create table claim_record
(
    claim_record_id      bigint not null auto_increment  comment 'claim记录id',
    chain_name           varchar(50)  comment 'chaiName',
    request_id           varchar(100)  comment 'request id',
    claim_id             varchar(100)  comment 'claim id',
    issuer               varchar(100)  comment 'issuer',
    request_txn_hash     varchar(100)  comment 'Request TXN Hash',
    request_data_hash    varchar(100)  comment 'Request Data Hash',
    response_txn_hash    varchar(100)  comment 'Response TXN Hash',
    response_data_hash   varchar(100)  comment 'Response Data Hash',
    customize_hash       varchar(100)  comment 'custom hash',
    id_system            varchar(50)  comment 'ID System',
    issuance_date        varchar(15)  comment 'Issuance Date',
    expiration_date      varchar(15)  comment 'Expiration Date',
    signature            varchar(1000)  comment 'Signature',
    transaction_hash     varchar(100)  comment 'transaction hash',
    version              varchar(50)  comment 'version',
    contract_params      text  comment 'contract params',
    create_date          datetime  comment 'local create time',
    update_date          datetime  comment 'local update time',
    primary key (claim_record_id)
)charset = UTF8;
alter table claim_record comment 'claim信息';
create index claim_request_id on claim_record(request_id);