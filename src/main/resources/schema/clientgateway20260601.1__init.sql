CREATE TABLE `oracle_request_v2_record` (
  `log_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'log id',
  `request_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'request id',
  `vn_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'vn_code',
  `job_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `platform_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `process_status` int DEFAULT '1' COMMENT 'status  1=pending   5=processed  10=error',
  `error_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `trace_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'log输出唯一id',
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `request_body` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `response_body` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='oracle v2请求记录表';