package io.opendid.web2gateway.common.utils;


import io.micrometer.common.util.StringUtils;

/**
*  TraceIdeUtil
* @author Dong-Jianguo
* @Date: 2024/11/12
* @version 1.0.0
*
* @history date, modifier,and description
**/
public class TraceIdeUtil {
  public static String wrapId(String traceId){

    if (StringUtils.isNotEmpty(traceId)){
      StringBuilder stringBuilder=new StringBuilder();
      StringBuilder append = stringBuilder
          .append("ST_")
          .append(traceId)
          .append("_")
          .append(System.currentTimeMillis())
          .append("_ED");

      String join = append.toString();
      return join;
    }else{
      return traceId;
    }

  }
}
