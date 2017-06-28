package com.nowcoder.controller;

import com.nowcoder.service.WendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2017/5/4.
 */
@Controller
public class SettingController {
    @Autowired
    WendaService wendaService;
    @RequestMapping("/setting")
    @ResponseBody
    public String setting(){
        return "setting "+ wendaService.getMessage(2);
    }
}
