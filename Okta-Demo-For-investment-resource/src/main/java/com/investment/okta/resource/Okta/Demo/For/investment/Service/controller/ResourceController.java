package com.investment.okta.resource.Okta.Demo.For.investment.Service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/resources")
public class ResourceController {


    @GetMapping("/hello")
    public String Hello(){
        return "Hello from resource Server";
    }
}
