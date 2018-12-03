package com.hw.controller;

import com.hw.annotation.Autowired;
import com.hw.annotation.Controller;
import com.hw.annotation.Qualifier;
import com.hw.annotation.RequestMapping;
import com.hw.annotation.RequestParam;
import com.hw.service.BusinessService;

/**
 * @Description TODO
 * @Author hw
 * @Date 2018/12/3 16:35
 * @Version 1.0
 */
@Controller
@RequestMapping("/business")
public class BusinessController {


    @Qualifier
    @Autowired
    private BusinessService businessServiceImpl1;

    @Qualifier("businessServiceImpl2")
    @Autowired
    private BusinessService businessService;


    @RequestMapping("/home")
    public String home() {
        System.out.println("访问主页成功！");
        return "主页";
    }

    @RequestMapping("/service1/person")
    public String personS1(@RequestParam String id) {
        String name = businessServiceImpl1.getPerson(id);
        System.out.println(name);
        return name;
    }

    @RequestMapping("/service2/person")
    public String personS2(@RequestParam String id) {
        String name = businessService.getPerson(id);
        System.out.println(name);
        return name;
    }


}
