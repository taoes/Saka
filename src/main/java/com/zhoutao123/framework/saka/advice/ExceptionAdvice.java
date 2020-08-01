package com.zhoutao123.framework.saka.advice;

import java.lang.reflect.Method;

/** 统一异常处理接口 */
public interface ExceptionAdvice {

  /** 异常处理 */
  void handleException(Method method, RuntimeException e);
}
