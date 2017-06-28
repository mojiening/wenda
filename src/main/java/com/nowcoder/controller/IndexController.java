package com.nowcoder.controller;


import com.nowcoder.model.User;
import com.nowcoder.service.WendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.*;

/**
 * Created by Administrator on 2017/5/4.
 */
//@Controller
public class IndexController {
    @Autowired
    WendaService wendaService;
    @RequestMapping(path = {"/","/index"})
    @ResponseBody
    public String index(HttpSession httpSession){
        return "Hello NowCoder!" +httpSession.getAttribute("msg")+wendaService.getMessage(1);
    }

    @RequestMapping(path = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("userId") int userId,
                          @PathVariable("groupId") String groupId,
                          @RequestParam("type") int type,
                          @RequestParam(value = "key",defaultValue ="22",required = false) int key ){
        return String.format("profile page of %s / %d,t: %d k: %d",groupId,userId,type,key);
    }

    @RequestMapping(path = {"/vm"})
    public String template(Model model){
        model.addAttribute("value1","vvvv1");
        String array[]={"red","grren","blue"};
        List<String> colors= Arrays.asList(array);
        model.addAttribute("colors",colors);
        model.addAttribute("user",new User("Lee"));
        return "home";
    }

    @RequestMapping(path = {"/request"})
    @ResponseBody
    public String request(Model model, HttpServletRequest request,
                           HttpServletResponse response,
                           HttpSession session) {
        StringBuilder sb=new StringBuilder();
        Enumeration<String> headerNames=request.getHeaderNames();
        while(headerNames.hasMoreElements())
        {
            String name=headerNames.nextElement();
            sb.append(name+":"+request.getHeader(name)+"<br>");
        }
        sb.append(request.getCookies()+"<br>");
        sb.append(request.changeSessionId()+"<br>");
        sb.append(request.getPathInfo()+"<br>");
        sb.append(request.getRequestURI()+"<br>");
        return sb.toString();
    }

    @RequestMapping(path = {"/redirect"})
    public String redirect(HttpSession httpSession){
        httpSession.setAttribute("msg","httpseesion");
        return "redirect:/";
    }

}
