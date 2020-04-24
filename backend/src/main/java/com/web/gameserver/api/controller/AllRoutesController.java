package com.web.gameserver.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * redirect all unknown routes to main route '/' (start point of vue.js application
 **/
@RestController
public class AllRoutesController {
    @RequestMapping(value = "{_:^(?!index\\.html|api).$}")
    public String redirectApi() {
        return "forward:/";
    }
}
