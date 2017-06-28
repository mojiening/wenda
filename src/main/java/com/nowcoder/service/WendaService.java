package com.nowcoder.service;

import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/5/4.
 */
@Service
public class WendaService {
    public String getMessage(int id){
        return "hello message"+id;
    }
}
