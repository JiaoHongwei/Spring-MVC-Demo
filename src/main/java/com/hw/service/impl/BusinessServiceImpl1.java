package com.hw.service.impl;

import com.hw.annotation.Autowired;
import com.hw.annotation.Service;
import com.hw.dao.BusinessRepository;
import com.hw.service.BusinessService;

/**
 * @Description TODO
 * @Author hw
 * @Date 2018/12/3 16:44
 * @Version 1.0
 */
@Service
public class BusinessServiceImpl1 implements BusinessService {

    @Autowired
    private BusinessRepository businessRepository;

    @Override
    public String getPerson(String id) {
        return businessRepository.getPersonService1(id);
    }
}
