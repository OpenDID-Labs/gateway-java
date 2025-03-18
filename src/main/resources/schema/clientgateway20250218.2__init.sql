alter table vngateway_route_info add column vn_homechain_public_key varchar(500)  comment 'vn_homechain_public_key';

alter table gateway_key_vault DROP COLUMN wallet_public_key;
alter table gateway_key_vault DROP COLUMN wallet_address;

create table gateway_homechain_key_manage
(
    key_id               int not null auto_increment  comment 'key_id',
    vn_code              varchar(80)  comment 'vn_code',
    wallet_public_key    varchar(300)  comment 'wallet_public_key',
    wallet_address       varchar(300)  comment 'wallet_address',
    update_date          datetime  comment 'update_date',
    primary key (key_id)
);

alter table gateway_homechain_key_manage comment 'gateway_homechain_key_manage';