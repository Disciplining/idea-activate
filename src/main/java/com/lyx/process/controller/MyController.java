package com.lyx.process.controller;

import com.lyx.common.CommonResult;
import com.lyx.process.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController
{
    @Autowired
    @Qualifier("myServiceImpl")
    private MyService myService;

    @GetMapping("/get-code")
    public CommonResult getCode(@RequestParam String url)
    {
        return myService.getCode(url);
    }
}