package com.nowcoder.service;

import com.nowcoder.dao.MessageDAO;
import com.nowcoder.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/5/14.
 */
@Service
public class MessageService {
    @Autowired
    MessageDAO messageDAO;

    @Autowired
    SensitiveService sensitiveService;

    public int addMessage(Message message)
    {
        message.setContent(sensitiveService.filter(message.getContent()));
       return messageDAO.addMessage(message)>0?message.getId():0;
    }

   public List<Message> selectConversationDetail(String conversationId,int offset,int limit){
       return messageDAO.selectConversationDetail(conversationId,offset,limit);
   }

    public List<Message> selectConversationList(int userId,int offset,int limit){
        return messageDAO.selectConversationList(userId,offset,limit);
    }

    public int selectUnreadConversationCount(int userId,String conversationId){
        return messageDAO.selectCoversationUnreadCount(userId,conversationId);
    }
}
