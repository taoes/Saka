package com.zhoutao123.framework.saka.advice;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

@Slf4j
@ConditionalOnMissingBean(ExceptionAdvice.class)
public class DefaultExceptionAdvice implements ExceptionAdvice {

  @Override
  public void handleException(Method method, RuntimeException e) {
    log.error("Saka执行事件{}出现错误", method.getName());
    log.error("Saka 订阅事件执行出现异常", e);
  }
}
