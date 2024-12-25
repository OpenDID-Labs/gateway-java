create table vngateway_route_info
(
    route_id        bigint not null auto_increment comment '',
    vn_code         varchar(80) comment '',
    url             varchar(100) comment '',
    ssl_enabled     int default 0 comment 'ssl_enable  :1=true  0=false default 0',
    gateway_disable int default 0 comment 'gateway_disable   1= true  0=false default 0',
    client_id       varchar(100) comment '',
    client_secret   varchar(100) comment '',
    update_date     datetime comment '',
    primary key (route_id)
)charset = UTF8;

alter table vngateway_route_info comment 'vngateway';

create table vngateway_jobid_mapping
(
    vn_jobid_mappingid bigint not null auto_increment comment 'vn_jobid_mappingid',
    job_id             varchar(50) comment 'job_id',
    platform_code      varchar(50) comment 'platform_code',
    vn_code            varchar(80) comment '',
    update_date        datetime comment 'update_date',
    primary key (vn_jobid_mappingid)
)charset = UTF8;

alter table vngateway_jobid_mapping comment 'vngateway jobid mapping';

create table oracle_nonce
(
    id              bigint not null auto_increment comment 'id',
    public_key      varchar(500) comment 'public_key',
    account_address varchar(500) comment 'account_address',
    nonce_value     bigint comment '',
    create_date     datetime comment '',
    update_date     datetime comment '',
    primary key (id)
)charset = UTF8;

alter table oracle_nonce comment 'oracle nonce';

create table od_oracle_contract_eventlog
(
    log_id               bigint not null auto_increment comment 'log id',
    request_id           varchar(100) comment 'request id',
    vn_code              varchar(50) comment 'vn_code',
    job_id               varchar(100) comment '',
    job_id_fee           bigint comment 'User payment fees',
    platform_code        varchar(50) comment '',
    process_status       int    default 1 comment 'status  1=pending   5=processed  10=error    15=exceed fee  ',
    error_msg            text comment '',
    callback_oracle_hash varchar(100) comment 'Callback Oracle contract hash',
    trace_id             varchar(200) comment 'trace id',
    create_date          datetime comment '',
    update_date          datetime comment '',
    next_execute_time    bigint default 0 comment '',
    execute_count        int    default 0 comment '',
    primary key (log_id)
)charset = UTF8;

alter table od_oracle_contract_eventlog comment 'oracle contract event log';

create table oracle_msg_record
(
    msg_id        bigint not null auto_increment comment 'msg_id',
    vn_code       varchar(50) comment 'vn_code',
    request_id    varchar(100) comment 'request id',
    request_body  mediumtext comment '',
    response_body longtext comment '',
    create_date   datetime comment '',
    update_date   datetime comment '',
    primary key (msg_id)
);

alter table oracle_msg_record comment 'oracle msg record';


