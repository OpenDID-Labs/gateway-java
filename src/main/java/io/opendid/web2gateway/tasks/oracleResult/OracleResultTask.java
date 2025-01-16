package io.opendid.web2gateway.tasks.oracleResult;

import io.opendid.web2gateway.security.jwt.JWTKeyLoader;
import io.opendid.web2gateway.tasks.TaskInf;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "oracle-result-task", name = "switch", havingValue = "true")
public class OracleResultTask implements TaskInf {

  private final Logger logger = LoggerFactory.getLogger(OracleResultTask.class);

  @Autowired
  private OracleResultHandler oracleResultHandler;

  @Scheduled(fixedDelayString = "${oracle-result-task.tempo}", initialDelay = 5000)
  @Override
  public void taskStart() {
    if (JWTKeyLoader.JWTRootGenerated){
      logger.info("Task:OracleResultTask start on {}",new Date());

      oracleResultHandler.resultManage();

      logger.info("Task:OracleResultTask end on {}",new Date());
    }else{
      logger.info("ROOT JWT Token has not been generated yet ");
    }


  }

}
