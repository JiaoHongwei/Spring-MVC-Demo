package com.hw.controller;

import com.hw.annotation.*;
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


    @Quatifier("businessServiceImpl1")
    @Autowired
    private BusinessService businessService1;

    @Quatifier("businessServiceImpl2")
    @Autowired
    private BusinessService businessService2;


    @RequestMapping("/home")
    public String home() {
        System.out.println("访问主页成功！");
        return "主页";
    }

    @RequestMapping("/service1/person")
    public String personS1(@RequestParam String id) {
        return businessService1.getPerson(id);
    }

    @RequestMapping("/service2/person")
    public String personS2(@RequestParam String id) {
        return businessService2.getPerson(id);
    }


}
