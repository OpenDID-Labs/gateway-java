/*==============================================================*/
/* Table: gateway_key_vault                                     */
/*==============================================================*/
create table gateway_key_vault
(
    key_id               int not null auto_increment  comment 'key_id',
    service_public_key   varchar(300)  comment 'service_public_key',
    wallet_public_key    varchar(300)  comment 'wallet_public_key',
    wallet_address       varchar(300)  comment 'wallet_address',
    update_date          datetime  comment 'update_date',
    primary key (key_id)
);

alter table gateway_key_vault comment 'gateway_key_vault';
