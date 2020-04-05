package com.muggle.poseidon.listener;

import org.springframework.context.ApplicationEvent;

/**
 * @Description:
 * @Author: muggle
 * @Date: 2020/4/2$
 **/
public class ExceptionEvent extends ApplicationEvent {
    private String message;
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public ExceptionEvent(String message,Object source) {
        super(source);
        this.message=message;
    }

    public String getMessage(){
        return this.message;
    }
}
