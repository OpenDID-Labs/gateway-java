
package io.opendid.web2gateway.tasks.subscription;

import io.opendid.web2gateway.tasks.TaskInf;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "sub-token-transfer", name = "switch", havingValue = "true")
public class SubTokenTransferTask implements TaskInf {

  private final Logger logger = LoggerFactory.getLogger(SubTokenTransferTask.class);

  @Autowired
  private SubTokenTransferHandler subTokenTransferHandler;

  @Scheduled(fixedDelayString = "${sub-token-transfer.tempo}", initialDelay = 2000)
  @Override
  public void taskStart() {

    logger.info("Task:SubTokenTransferTask start on {}",new Date());

    subTokenTransferHandler.tokenTransferManage();

    logger.info("Task:SubTokenTransferTask end on {}",new Date());

  }
}
