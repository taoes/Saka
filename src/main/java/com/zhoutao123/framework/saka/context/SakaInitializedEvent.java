package com.zhoutao123.framework.saka.context;

import com.zhoutao123.framework.saka.entity.MetaMethod;
import org.springframework.context.ApplicationEvent;

public class SakaInitializedEvent extends ApplicationEvent {
  /** 创建一个Saka 事件 */
  public SakaInitializedEvent(MetaMethod source) {
    super(source);
  }

  public MetaMethod getServer() {
    return (MetaMethod) getSource();
  }
}
