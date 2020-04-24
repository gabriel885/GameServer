package com.web.gameserver.api.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * on invalid route redirect to vue.js application staring point (index.html or  /)
 */
@Controller
public class DefaultRoutesController implements ErrorController {
    // Forwards all routes to FrontEnd except: '/', '/index.html', '/api', '/api/**'
    // Required because of 'mode: history' usage in frontend routing, see README for further details
    @RequestMapping("/error")
    public String error() {
        return "forward:/"; // forward to '/' route (vue-router will redirect accordingly
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
