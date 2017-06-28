package com.nowcoder.Aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by Administrator on 2017/5/4.
 */
@Aspect
@Component
public class LogAspect {

    private  static  final org.slf4j.Logger logger= LoggerFactory.getLogger(LogAspect.class);
    @Before("execution(* com.nowcoder.controller.IndexController.*(..))")
    public void beforeMethod(){
     logger.info("before method"+new Date());
    }

    @After("execution(* com.nowcoder.controller.IndexController.*(..))")
    public  void afterMethod(){
     logger.info("after method"+new Date());
    }
}
