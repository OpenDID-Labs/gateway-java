/*
 * Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
 * (https://www.mozilla.org/en-US/MPL/2.0/)
 */

package io.opendid.web2gateway.exception.handler;

import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2InternalErrorException;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2InvalidParamsException;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2InvalidRequestException;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2MethodNotFoundException;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ParseErrorException;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2UnauthorizedRequestException;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpcException;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2ResponseError;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2ResponseError.ErrorEntity;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class JsonRpc2ExceptionHandler {

  /**
   * log
   */
  private static final Logger logger = LoggerFactory.getLogger(JsonRpc2ExceptionHandler.class);


  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(JsonRpc2MethodNotFoundException.class)
  public JsonRpc2ResponseError handler(JsonRpc2MethodNotFoundException e) {
    logger.error("JsonRpc2MethodNotFoundException logid: {} body:{}",
        e.getLogId(),
        e
        );

    JsonRpc2ResponseError error = wrapError(e);
    return error;
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(JsonRpc2InvalidParamsException.class)
  public JsonRpc2ResponseError handler(JsonRpc2InvalidParamsException e) {
    logger.error("JsonRpc2InvalidParamsException logid: {} body:{}",
        e.getLogId(),
        e
    );

    JsonRpc2ResponseError error = wrapError(e);
    return error;
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(JsonRpc2InvalidRequestException.class)
  public JsonRpc2ResponseError handler(JsonRpc2InvalidRequestException e) {
    logger.error("JsonRpc2InvalidParamsException logid: {} body:{}",
        e.getLogId(),
        e
    );

    JsonRpc2ResponseError error = wrapError(e);
    return error;
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(JsonRpc2UnauthorizedRequestException.class)
  public ResponseEntity<JsonRpc2ResponseError> handler(JsonRpc2UnauthorizedRequestException e) {
    logger.error("JsonRpc2InvalidParamsException logid: {} body:{}",
        e.getLogId(),
        e
    );
    JsonRpc2ResponseError error = wrapError(e);
    return new ResponseEntity<>(error,HttpStatus.UNAUTHORIZED);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(JsonRpc2ParseErrorException.class)
  public JsonRpc2ResponseError handler(JsonRpc2ParseErrorException e) {
    logger.error("JsonRpc2InvalidParamsException logid: {} body:{}",
        e.getLogId(),
        e
    );

    JsonRpc2ResponseError error = wrapError(e);
    return error;
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(JsonRpc2ServerErrorException.class)
  public JsonRpc2ResponseError handler(JsonRpc2ServerErrorException e) {
    logger.error("JsonRpc2InvalidParamsException logid: {} body:{}",
        e.getLogId(),
        e
    );

    JsonRpc2ResponseError error = wrapError(e);
    return error;
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(JsonRpc2InternalErrorException.class)
  public JsonRpc2ResponseError handler(JsonRpc2InternalErrorException e) {
    logger.error("JsonRpc2InvalidParamsException logid: {} body:{}",
        e.getLogId(),
        e
    );

    JsonRpc2ResponseError error = wrapError(e);

    return error;
  }

  private static @NotNull JsonRpc2ResponseError wrapError(
      JsonRpcException e) {

    logger.info("JsonRpc2ResponseError data={}",e.getData());

    JsonRpc2ResponseError error=new JsonRpc2ResponseError();
    ErrorEntity errorEntity=new ErrorEntity();
    errorEntity.setCode(e.getCode());
    errorEntity.setMessage(e.getMessage());
    errorEntity.setData(e.getData());
    errorEntity.setLogId(e.getLogId());

    error.setError(errorEntity);
    return error;
  }

}