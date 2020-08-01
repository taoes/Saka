package com.zhoutao123.framework.saka.entity;

import com.zhoutao123.framework.saka.annotation.SakaSubscribe;
import lombok.Data;

import java.lang.reflect.Method;

/** 元数据 */
@Data
public class MetaMethod {

  // 方法所在的实例
  private Object instance;

  // 方法
  private Method method;

  // 方法的参数数量
  private int paramCount;

  // 方法参数类型
  private Class<?>[] paramType;

  private int order;

  public MetaMethod(Object instance, Method method) {
    this.instance = instance;
    this.method = method;
    this.paramCount = method.getParameterCount();
    this.paramType = method.getParameterTypes();
    this.order = method.getAnnotation(SakaSubscribe.class).order();
  }

  /** 方法是否允许访问 */
  public boolean allowAccess() {
    return method.isAccessible();
  }

  /** 是否打印日志 */
  public boolean printLog() {
    SakaSubscribe subscribe = method.getAnnotation(SakaSubscribe.class);
    return subscribe != null && subscribe.debug();
  }
}
