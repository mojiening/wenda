package com.nowcoder.controller;

import com.nowcoder.model.*;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jws.WebParam;
import java.util.*;

/**
 * Created by Administrator on 2017/5/14.
 */
@Controller
public class MessageController {
    private static final Logger logger= LoggerFactory.getLogger(LoginController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @RequestMapping(path={"/msg/addMessage"}, method={RequestMethod.POST})
    @ResponseBody
    public String addComment(@RequestParam("toName") String toName,
                             @RequestParam("content") String content){
        try{
            if(hostHolder.getUser()==null){
                return WendaUtil.getJSONString(999,"未登录");
            }

            User user=userService.selectByName(toName);
            if(user==null)
            {
                return WendaUtil.getJSONString(1,"用户不存在");
            }
            Message message=new Message();
            message.setContent(content);
            message.setCreatedDate(new Date());
            message.setFromId(hostHolder.getUser().getId());
            message.setToId(user.getId());
            messageService.addMessage(message);
            return WendaUtil.getJSONString(0);
        }catch (Exception e){
            logger.error("增加评论失败"+e.getMessage());
        }
        return WendaUtil.getJSONString(1,"发信失败");
    }

    @RequestMapping(path={"/msg/list"})
    public String getConversationList(Model model){
        try{
            if(hostHolder.getUser()==null){
                return "redirect:/reglogin";
            }

            int localUserId=hostHolder.getUser().getId();
          List<Message> conversationList=  messageService.selectConversationList(hostHolder.getUser().getId(),0,10);
            List<ViewObject> conversations=new ArrayList<ViewObject>();
            for(Message message:conversationList){
                ViewObject vo=new ViewObject();
                vo.set("conversation",message);
                int targetId=message.getFromId()==localUserId?message.getToId():message.getFromId();
                vo.set("user",userService.getUser(targetId));
                int count=messageService.selectUnreadConversationCount(localUserId,message.getConversationId());
                vo.set("unread",count);
                conversations.add(vo);
            }

            model.addAttribute("conversations",conversations);
        }catch (Exception e){
            logger.error("获取所有回话列表失败"+e.getMessage());
        }
        return "letter";
    }

    @RequestMapping(path={"/msg/detail"})
    public String getConversationDetail(Model model,@RequestParam("conversationId") String conversationId){
        try{
           List<Message> messageList= messageService.selectConversationDetail(conversationId,0,10);
           List<ViewObject> messages=new ArrayList<ViewObject>();
           for(Message message:messageList){
               ViewObject vo=new ViewObject();
               vo.set("message",message);
               vo.set("user",userService.getUser(message.getFromId()));
               messages.add(vo);
           }
           model.addAttribute("messages",messages);
        }catch (Exception e){
            logger.error("获取详情失败"+e.getMessage());
        }
        return "letterDetail";
    }
}
