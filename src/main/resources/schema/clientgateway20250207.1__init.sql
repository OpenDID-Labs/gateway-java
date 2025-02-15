alter table od_oracle_contract_eventlog add request_body mediumtext  comment '';
alter table od_oracle_contract_eventlog add response_body longtext  comment '';

alter table od_oracle_contract_eventlog add cancel_request_body mediumtext comment '';
alter table od_oracle_contract_eventlog add cancel_response_body longtext comment '';