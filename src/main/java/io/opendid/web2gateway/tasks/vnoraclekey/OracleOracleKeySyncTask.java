package io.opendid.web2gateway.tasks.vnoraclekey;

import io.opendid.web2gateway.security.jwt.JWTKeyLoader;
import io.opendid.web2gateway.tasks.TaskInf;
import io.opendid.web2gateway.tasks.oracleResult.OracleResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@ConditionalOnProperty(prefix = "vn-key-sync-task", name = "switch", havingValue = "true")
public class OracleOracleKeySyncTask implements TaskInf {

  private final Logger logger = LoggerFactory.getLogger(OracleOracleKeySyncTask.class);

  @Autowired
  private  OracleOracleKeySyncHandler oracleOracleKeySyncHandler;


  @Scheduled(fixedDelayString = "${vn-key-sync-task.tempo}", initialDelay = 5000)
  @Override
  public void taskStart() {
    logger.info("Starting Oracle Key Sync Task");
    oracleOracleKeySyncHandler.syncOracleKeys();
  }

}
