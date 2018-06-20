package com.tao.framework.saka.context;


import com.tao.framework.saka.entity.MetaMethod;
import org.springframework.context.ApplicationEvent;

public class SakaInitializedEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public SakaInitializedEvent(MetaMethod source) {
        super(source);
    }
    public MetaMethod getServer(){
        return (MetaMethod) getSource();
    }
}
