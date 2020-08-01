package com.zhoutao123.framework.saka.autoconfig;

import com.zhoutao123.framework.saka.advice.ExceptionAdvice;
import com.zhoutao123.framework.saka.entity.MetaMethod;
import com.zhoutao123.framework.saka.entity.MetaMethodArray;
import com.zhoutao123.framework.saka.listener.HandleSubscribeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

/** Saka客户端的实现 */
@Slf4j
public class SakaSendClient implements ISakaClient {

  @Autowired(required = false)
  private HandleSubscribeListener listener;

  @Autowired private ExceptionAdvice exceptionAdvice;

  @Async
  @Override
  public void send() throws InvocationTargetException, IllegalAccessException {
    send(null);
  }

  @Async
  @Override
  public void send(Object message) {
    int count = 0;
    for (MetaMethod metaMethod : MetaMethodArray.getMetaMethods()) {
      if (message == null && metaMethod.getParamCount() == 0) {
        count++;
        executeSubscribe(metaMethod);
      } else if (message != null
          && metaMethod.getParamCount() == 1
          && metaMethod.getParamType()[0].equals(message.getClass())) {
        count++;
        // 执行方法
        executeSubscribe(metaMethod, message);
      }
    }
    log.info("消息发送完成，发送次数:{}", count);
  }

  /** 执行 */
  private void executeSubscribe(MetaMethod metaMethod, Object... message) {
    Method method = metaMethod.getMethod();
    Object instance = metaMethod.getInstance();
    Object resultObject = null;
    try {
      if (message == null) {
        resultObject = method.invoke(instance);
      } else {
        resultObject = method.invoke(instance, message);
      }
      if (listener != null) {
        listener.onSuccess(metaMethod, resultObject);
      }
    } catch (Exception e) {
      Optional.of(listener).ifPresent(listener -> listener.onError(e));
      // 进行统一异常处理
      if (exceptionAdvice != null) {
        exceptionAdvice.handleException(method, new RuntimeException(e));
      }
    }
  }
}
