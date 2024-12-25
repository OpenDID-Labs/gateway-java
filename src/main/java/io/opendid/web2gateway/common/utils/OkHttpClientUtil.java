package io.opendid.web2gateway.common.utils;

import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.traceid.LogTraceIdConstant;
import io.opendid.web2gateway.model.dto.okhttp.ResponseDTO;
import jakarta.annotation.Resource;
import java.io.File;
import java.util.Iterator;
import java.util.Map;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class OkHttpClientUtil {

  private static final Logger logger = LoggerFactory.getLogger(OkHttpClientUtil.class);

  @Resource
  private OkHttpClient okHttpClient;

  public String get(String url, Map<String, String> queries) {

    String traceId = MDC.get(LogTraceIdConstant.TRACE_ID);



    String responseBody = "";
    StringBuffer sb = new StringBuffer(url);
    if (queries != null && queries.keySet().size() > 0) {
      boolean firstFlag = true;
      Iterator iterator = queries.entrySet().iterator();
      while (iterator.hasNext()) {
        Map.Entry entry = (Map.Entry<String, String>) iterator.next();
        if (firstFlag) {
          sb.append("?" + entry.getKey() + "=" + entry.getValue());
          firstFlag = false;
        } else {
          sb.append("&" + entry.getKey() + "=" + entry.getValue());
        }
      }
    }
    Request request = new Request
        .Builder()
        .header(LogTraceIdConstant.TRACE_ID,traceId)
        .url(sb.toString())
        .build();
    Response response = null;
    try {

      response = okHttpClient.newCall(request).execute();
      int status = response.code();
      if (status == 200) {
        return response.body().string();
      }
    } catch (Exception e) {
      logger.error("okhttp put error >> ex = {}", e.getMessage());
    } finally {
      if (response != null) {
        response.close();
      }
    }
    return responseBody;
  }


  public ResponseDTO postForForm(String url, Map<String, String> params) {
    String traceId = MDC.get(LogTraceIdConstant.TRACE_ID);


    String responseBody = "";
    FormBody.Builder builder = new FormBody.Builder();
    if (params != null && params.keySet().size() > 0) {
      for (String key : params.keySet()) {
        builder.add(key, params.get(key));
      }
    }
    Request request = new Request
        .Builder()
        .url(url)
        .header(LogTraceIdConstant.TRACE_ID,traceId)
        .post(builder.build())
        .build();
    Response response = null;
    try {
      ResponseDTO responseDTO = new ResponseDTO();

      response = okHttpClient.newCall(request).execute();

      responseDTO.setCode(response.code());
      if (response.body() != null) {
        responseDTO.setBody(response.body().string());
      }

      return responseDTO;
    } catch (Exception e) {
      logger.error("okhttp post error >> ex = {}", e.getMessage());
    } finally {
      if (response != null) {
        response.close();
      }
    }
    return null;
  }


  public ResponseDTO postForJson(String url, Object params, String accessToken) {
    String traceId = MDC.get(LogTraceIdConstant.TRACE_ID);

    String responseBody = JSONObject.toJSONString(params);

    MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
    RequestBody requestBodyJson = RequestBody.create(responseBody, mediaType);
    Request request = new Request
        .Builder()
        .url(url)
        .header(LogTraceIdConstant.TRACE_ID,traceId)
        .post(requestBodyJson)
        .build();

    if (accessToken != null && !"".equals(accessToken)) {

      request = request.newBuilder()
          .header("Authorization", "Bearer " + accessToken)
          .build();
    }

    Response response = null;
    try {

      ResponseDTO responseDTO = new ResponseDTO();

      response = okHttpClient.newCall(request).execute();

      responseDTO.setCode(response.code());
      if (response.body() != null) {
        responseDTO.setBody(response.body().string());
      }

      return responseDTO;
    } catch (Exception e) {
      logger.error("okhttp post error >> ex = {}", e.getMessage());
    } finally {
      if (response != null) {
        response.close();
      }
    }

    return null;
  }


  public String postFile(String url, Map<String, Object> params, String fileType) {
    String traceId = MDC.get(LogTraceIdConstant.TRACE_ID);


    String responseBody = "";
    MultipartBody.Builder builder = new MultipartBody.Builder();
    // add params
    if (params != null && params.keySet().size() > 0) {
      for (String key : params.keySet()) {
        if (params.get(key) instanceof File) {
          File file = (File) params.get(key);
          builder.addFormDataPart(key, file.getName(),
              RequestBody.create(file,MediaType.parse(fileType)));
          continue;
        }
        builder.addFormDataPart(key, params.get(key).toString());
      }
    }
    Request request = new Request
        .Builder()
        .url(url)
        .header(LogTraceIdConstant.TRACE_ID,traceId)
        .post(builder.build())
        .build();
    Response response = null;
    try {
      response = okHttpClient.newCall(request).execute();
      int status = response.code();
      if (status == 200) {
        return response.body().string();
      }
    } catch (Exception e) {
      logger.error("okhttp postFile error >> ex = {}", e.getMessage());
    } finally {
      if (response != null) {
        response.close();
      }
    }
    return responseBody;
  }


}
