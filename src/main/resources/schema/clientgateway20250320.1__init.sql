
/*==============================================================*/
/* Table: subscription_transfer_token_record                    */
/*==============================================================*/
create table subscription_transfer_token_record
(
    transfer_id          bigint not null auto_increment  comment '',
    sub_id               varchar(200)  comment '',
    from_address         varchar(200)  comment '',
    to_address           varchar(200)  comment '',
    token_amounts        decimal(30,0)  comment '',
    latest_balance       decimal(30,0)  comment '',
    old_balance          decimal(30,0)  comment '',
    request_vn_code      varchar(70)  comment '',
    sign_address         varchar(200)  comment '',
    sign_key_code        varchar(50)  comment '',
    create_date          datetime  comment '',
    update_date          datetime  comment '',
    primary key (transfer_id)
);
