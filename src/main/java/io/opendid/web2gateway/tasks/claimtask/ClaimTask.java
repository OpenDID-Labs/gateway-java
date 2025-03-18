package io.opendid.web2gateway.tasks.claimtask;

import io.opendid.web2gateway.tasks.TaskInf;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "claim-task", name = "switch", havingValue = "true")
public class ClaimTask implements TaskInf {

  private final Logger logger = LoggerFactory.getLogger(ClaimTask.class);

  @Autowired
  private ClaimHandler claimTaskHandler;

  @Scheduled(fixedDelayString = "${claim-task.tempo}", initialDelay = 5000)
  @Override
  public void taskStart() {

    logger.info("Task:ClaimTask start on {}",new Date());

    claimTaskHandler.claimManage();

    logger.info("Task:ClaimTask end on {}",new Date());

  }

}
