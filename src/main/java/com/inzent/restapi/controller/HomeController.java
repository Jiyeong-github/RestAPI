package com.inzent.restapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HomeController {

    final static Logger logger = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping(value = "/", method = { RequestMethod.GET, RequestMethod.POST})
    public String home(HttpServletRequest request) throws Exception{
        JSONObject json = new JSONObject();

        json.put("SUCCESS", true);
        json.put("ADD",10);
        json.put(null,10);

        return json.toString(4);
    }
}
