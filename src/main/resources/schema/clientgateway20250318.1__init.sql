alter table od_oracle_contract_eventlog    add column  sub_id               varchar(500)  comment '订阅id';
alter table od_oracle_contract_eventlog    add column  consumer_address     varchar(200)  comment 'consumerAddress';
alter table od_oracle_contract_eventlog    add column  coin_type            int default 2  comment '币类型：1-平台币；2-代币';
alter table od_oracle_contract_eventlog    add column  payment_type         int default 1  comment '支付模式：1-实时付费；2-预付费';

create table subscription_info
(
    id                   bigint not null auto_increment  comment 'id',
    sub_id               varchar(500)  comment '订阅id',
    owner_address        varchar(200)  comment 'owner_address',
    balance              varchar(100)  comment '余额',
    create_date          datetime  comment '',
    update_date          datetime  comment '',
    primary key (id)
);

alter table subscription_info comment 'subscription_info';


create table subscription_consumer
(
    id                   bigint not null auto_increment  comment 'id',
    sub_id               varchar(500)  comment '订阅id',
    consumer_address     varchar(200)  comment 'consumer_address',
    create_date          datetime  comment '',
    update_date          datetime  comment '',
    primary key (id)
);

alter table subscription_consumer comment 'subscription consumer';