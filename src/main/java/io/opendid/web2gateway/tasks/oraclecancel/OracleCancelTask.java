package io.opendid.web2gateway.tasks.oraclecancel;

import io.opendid.web2gateway.tasks.TaskInf;
import io.opendid.web2gateway.tasks.oracleResult.OracleResultTask;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "oracle-cancel-result-task", name = "switch", havingValue = "true")
public class OracleCancelTask implements TaskInf {

  private final Logger logger = LoggerFactory.getLogger(OracleCancelTask.class);

  @Autowired
  private OracleCancelHandler oracleCancelHandler;

  @Scheduled(fixedDelayString = "${oracle-cancel-result-task.tempo}", initialDelay = 5000)
  @Override
  public void taskStart() {

    logger.info("Task:OracleCancelTask start on {}",new Date());

    oracleCancelHandler.cancelManage();

    logger.info("Task:OracleCancelTask end on {}",new Date());

  }
}
