/*==============================================================*/
/* Table: tenant_jwt_manger                                     */
/*==============================================================*/
create table tenant_jwt_manger
(
    tenant_jwt_id        bigint not null auto_increment  comment 'tenant_jwt_id',
    subject_id           varchar(70)  comment 'tenant_uuid',
    subject_name         varchar(100)  comment '',
    jwt                  varchar(600)  comment '',
    create_date          datetime  comment '',
    update_date          datetime  comment '',
    primary key (tenant_jwt_id)
);

alter table tenant_jwt_manger comment 'tenant_jwt_manger';
