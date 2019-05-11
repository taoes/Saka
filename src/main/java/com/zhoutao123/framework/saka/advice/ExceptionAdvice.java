package com.zhoutao123.framework.saka.advice;

import java.lang.reflect.Method;

/** 统一异常处理接口 */
public interface ExceptionAdvice {

  /**
   * 异常处理
   *
   * @param method 执行的方法名称
   * @param e 发生的异常信息
   */
  void handleException(Method method, RuntimeException e);
}
