ALTER table  od_oracle_contract_eventlog ADD column cancel_status  int default 0  comment 'cancel status: 1=pending  5= successfully  15=fail  20=Exceeding the number of queries 0=default';
ALTER table  od_oracle_contract_eventlog ADD column cancel_error_msg  text  comment 'cancel error msg';
ALTER table  od_oracle_contract_eventlog ADD column cancel_oracle_hash  varchar(100)  comment 'cancel oracle hash';
ALTER table  od_oracle_contract_eventlog ADD column cancel_next_execute_time bigint default 0  comment '';
ALTER table  od_oracle_contract_eventlog ADD column cancel_execute_count int default 0  comment '';

ALTER table  oracle_msg_record ADD column cancel_request_body  mediumtext  comment '';
ALTER table  oracle_msg_record ADD column cancel_response_body longtext  comment '';