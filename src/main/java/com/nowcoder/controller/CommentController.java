package com.nowcoder.controller;

import com.nowcoder.dao.QuestionDAO;
import com.nowcoder.model.*;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/5/13.
 */
@Controller
public class CommentController {
    private static final Logger logger= LoggerFactory.getLogger(LoginController.class);
    @Autowired
    CommentService commentService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    QuestionDAO questionDAO;

    @Autowired
    UserService userService;

    @RequestMapping(path={"/addComment"}, method={RequestMethod.POST})
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content){
        try{
            Comment comment=new Comment();
            comment.setContent(content);
            if(hostHolder!=null)
            {
                comment.setUserId(hostHolder.getUser().getId());
            }else {
                comment.setUserId(WendaUtil.ANONYMOUS_USERID);
            }
            comment.setCreatedDate(new Date());
            comment.setEntityId(questionId);
            comment.setEntityType(EntityType.ENTITY_QUESTION);
           commentService.addComment(comment);
        }catch (Exception e){
            logger.error("增加评论失败"+e.getMessage());
        }
       return "redirect:/question/"+String.valueOf(questionId);
    }


}
