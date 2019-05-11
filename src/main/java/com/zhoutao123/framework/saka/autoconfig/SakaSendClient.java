package com.zhoutao123.framework.saka.autoconfig;

import com.zhoutao123.framework.saka.advice.ExceptionAdvice;
import com.zhoutao123.framework.saka.entity.MetaMethod;
import com.zhoutao123.framework.saka.entity.MetaMethodArray;
import com.zhoutao123.framework.saka.listener.HandleSubscribeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

/**
 * Saka客户端的实现
 *
 * @author zhoutao123
 */
@Slf4j
public class SakaSendClient implements ISakaClient {

  @Autowired(required = false)
  HandleSubscribeListener listener;

  @Autowired public ExceptionAdvice exceptionAdvice;

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
        if (!executeSubscribe(metaMethod, null)) {
          break;
        }
      } else if (message != null
          && metaMethod.getParamCount() == 1
          && metaMethod.getParamType()[0].equals(message.getClass())) {
        count++;
        if (!executeSubscribe(metaMethod, message)) {
          break;
        }
      }
    }
    log.info("Saka ------>  Saka has successfully sent {} times data.", count);
  }

  /**
   * execute Subscribe with metaMethod and Message
   *
   * @param metaMethod
   * @param message
   * @return a bool reault,it express wether continue execute
   */
  private boolean executeSubscribe(MetaMethod metaMethod, Object... message) {
    Method method = metaMethod.getMethod();
    Object instance = metaMethod.getInstance();
    boolean continueExecute = true;
    Object resultObject = null;
    try {
      if (message == null) {
        resultObject = method.invoke(instance);
      } else {
        resultObject = method.invoke(instance, message);
      }
      if (listener != null) {
        // FIXME 此处需要修改
        listener.onSuccess(metaMethod, resultObject);
      }
    } catch (Exception e) {
      // 执行事件Hook
      if (listener != null) {
        continueExecute = listener.onError(e);
      }
      // 进行统一异常处理
      if (exceptionAdvice != null) {
        exceptionAdvice.handleException(method, new RuntimeException(e));
      }
    }
    return continueExecute;
  }
}
