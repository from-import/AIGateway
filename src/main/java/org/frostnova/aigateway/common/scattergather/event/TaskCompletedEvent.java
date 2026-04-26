package org.frostnova.aigateway.common.scattergather.event;

import lombok.Getter;
import org.frostnova.aigateway.common.scattergather.enums.CallbackActionEnum;
import org.springframework.context.ApplicationEvent;

@Getter
public class TaskCompletedEvent extends ApplicationEvent {

    private final String parentId;
    private final CallbackActionEnum action;
    private final String target;

    /**
     * 故意不在这里放置 List<String> results (大模型的所有返回结果)
     */

    public TaskCompletedEvent(Object source, String parentId, CallbackActionEnum action, String target) {
        super(source); // source 通常是触发该事件的类实例 (this)
        this.parentId = parentId;
        this.action = action;
        this.target = target;
    }

}
