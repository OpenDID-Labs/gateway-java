alter table od_oracle_contract_eventlog add column claim_execute_time bigint default 0 comment '';
alter table od_oracle_contract_eventlog add column claim_execute_count int default 0 comment '';

create unique index claim_id_uk on claim_record(claim_id);