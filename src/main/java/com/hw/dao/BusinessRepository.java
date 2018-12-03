package com.hw.dao;

import com.hw.annotation.Repository;

/**
 * @Description TODO
 * @Author hw
 * @Date 2018/12/3 16:45
 * @Version 1.0
 */
@Repository
public class BusinessRepository {

    public String getPersonService1(String id) {
        return "张三，ID =" + id;
    }

    public String getPersonService2(String id) {
        return "李四，ID =" + id;
    }
}
