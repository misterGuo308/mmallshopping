package com.example.mmallshopping.controller;

import com.example.mmallshopping.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author guoyou
 * @date 2019/10/22 14:47
 */
@RestController
public class Test {

    @Resource
    private TestService testService;

    @GetMapping("/test")
    public String test(){
        String test = testService.test();
        System.out.println("进来了"+test);
      return   test;
    }
}
