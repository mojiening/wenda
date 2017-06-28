package com.nowcoder.service;

import com.nowcoder.dao.CommentDAO;
import com.nowcoder.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/5/13.
 */
@Service
public class CommentService {
    @Autowired
    CommentDAO commentDAO;

    @Autowired
    SensitiveService sensitiveService;

    public  int addComment(Comment comment){
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveService.filter(comment.getContent()));
        return commentDAO.addcomment(comment);
    }
    public List<Comment> getCommentByEntity(int entityId, int entityType)
    {
        return commentDAO.selectCommentByEntity(entityId,entityType);
    }

    int getCommentCount(int entityId,int entityType){
        return commentDAO.getCommentCount(entityId,entityType);
    }

    public boolean deleteComment(int commentId){
       return commentDAO.updateStatus(commentId,1);
    }

    public  Comment getCommentById(int id){
        return commentDAO.selectCommentById(id);
    }
}
