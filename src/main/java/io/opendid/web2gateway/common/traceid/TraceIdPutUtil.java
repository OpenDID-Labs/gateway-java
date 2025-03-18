package io.opendid.web2gateway.common.traceid;

import io.opendid.web2gateway.common.utils.TraceIdeUtil;
import io.opendid.web2gateway.common.utils.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class TraceIdPutUtil {

  private static Logger logger= LoggerFactory.getLogger(TraceIdPutUtil.class);
  public static String newPutTraceId(){

    String traceId = UuidUtil.generateUuid32();
    traceId = TraceIdeUtil.wrapId(traceId);
    MDC.put(LogTraceIdConstant.TRACE_ID, traceId);

    logger.info("newPutTraceId:  {}",traceId);

    return traceId;
  }

  public static  void removeTraceId(){
    MDC.remove(LogTraceIdConstant.TRACE_ID);
  }
}
