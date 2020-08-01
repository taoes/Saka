package com.zhoutao123.framework.saka.autoconfig;

import java.lang.reflect.InvocationTargetException;

/** Saka客户端接口 */
public interface ISakaClient<T> {

  void send() throws InvocationTargetException, IllegalAccessException;

  void send(T message) throws InvocationTargetException, IllegalAccessException;
}
