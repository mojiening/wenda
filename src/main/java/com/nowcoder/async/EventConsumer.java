package com.nowcoder.async;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nowcoder.controller.QuestionController;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/6.
 */
@Service
public class EventConsumer implements InitializingBean,ApplicationContextAware{
    private static final Logger logger= LoggerFactory.getLogger(EventConsumer.class);
    //存放EventType和EventHandler之间的关联关系
    private Map<EventType,List<EventHandler>> config=new HashMap<EventType,List<EventHandler>>();
    private ApplicationContext applicationContext;

    @Autowired
    JedisAdapter jedisAdapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        //找到所有的EventHandler的实现类，放在beans容器中
       Map<String,EventHandler> beans=applicationContext.getBeansOfType(EventHandler.class);
       if(beans!=null){
           for(Map.Entry<String,EventHandler> entry:beans.entrySet()){
               //得到实现类关心的EventType
               List<EventType> eventTypes=entry.getValue().getSupportEventTypers();
               //将eventType和EventHandler关联起来
               for(EventType type:eventTypes){
                   if(!config.containsKey(type)){
                       config.put(type,new ArrayList<EventHandler>());
                   }
                   config.get(type).add(entry.getValue());
               }
           }
       }
       System.out.println("keyset:"+config.keySet());
       Thread thread=new Thread(new Runnable() {
           @Override
           public void run() {
               while (true){
                   String key=RedisKeyUtil.getEventQueueKey();
                   List<String> events=jedisAdapter.brpop(0,key);
                   for(String message:events){
                       if(message.equals(key))
                       {
                           continue;
                       }

                       //反序列化
                       EventModel eventModel= JSON.parseObject(message,EventModel.class);
                       if(!config.containsKey(eventModel.getType())){
                           logger.error("不能识别的事件类型");
                           continue;
                       }
                        //Handler依次处理事件
                       for(EventHandler handler:config.get(eventModel.getType())){
                           handler.doHandler(eventModel);
                       }
                   }
               }
           }
       });
      thread.start();
    }

    @Override
    public void setApplicationContext(org.springframework.context.ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
}
