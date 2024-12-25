package io.opendid.web2gateway.aop;

import io.opendid.web2gateway.common.traceid.LogTraceIdConstant;
import io.opendid.web2gateway.common.utils.TraceIdeUtil;
import io.opendid.web2gateway.common.utils.UuidUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;


/**
 * EventLogTraceId
 *
 * @author Dong-Jianguo
 * @version 1.0.0
 * @Date: 2024/11/12
 * @history date, modifier,and description
 **/
@Aspect
@Component
public class GlobalTraceId {

  private Logger logger = LoggerFactory.getLogger(GlobalTraceId.class);

  @Pointcut(
      " execution(public * io.opendid.web2gateway.api..*.*(..)) "

  )

  public void eventOperation() {

  }


  @Before("eventOperation()")
  public void eventBefore(JoinPoint joinPoint) {
    logger.info("eventBefore Log aop start");
    String traceId = UuidUtil.generateUuid32();

    traceId = TraceIdeUtil.wrapId(traceId);

    MDC.put(LogTraceIdConstant.TRACE_ID, traceId);
  }

  @After("eventOperation()")
  public void eventAfter(JoinPoint joinPoint) {
    logger.info("eventAfter Log trace id aop clear");
    MDC.clear();
  }

}