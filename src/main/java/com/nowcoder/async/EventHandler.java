package com.nowcoder.async;

import java.util.List;

/**
 * Created by Administrator on 2017/6/6.
 */
public interface EventHandler {
    void doHandler(EventModel model);

    List<EventType> getSupportEventTypers();
}
