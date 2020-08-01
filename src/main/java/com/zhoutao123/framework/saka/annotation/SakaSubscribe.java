package com.zhoutao123.framework.saka.annotation;

import com.zhoutao123.framework.saka.constance.OrderConstance;

import java.lang.annotation.*;

/** 订阅时间处理注解 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SakaSubscribe {

  /** 调试模式 */
  boolean debug() default false;

  /** 定义执行顺序 */
  int order() default OrderConstance.ORDER_NORMAL;
}
