package com.web.gameserver.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ApiController {

    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    /**
     * health check - return status code 200 on success, otherwise return 401
     *
     * @return
     */
    @GetMapping("/health")
    public ResponseEntity healthcheck() {
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/endpoints")
    public String getEndpoints() {
        StringBuilder endpoints = new StringBuilder();
        List<RequestMappingInfo> mappingsInfo = requestMappingHandlerMapping.getHandlerMethods().keySet().stream().collect(Collectors.toList());
        for (RequestMappingInfo info : mappingsInfo) {
            // remove curly brackets
            String line = info.toString().substring(1, info.toString().length() - 1);
            endpoints.append(line + "<br />");
        }
        return endpoints.toString();
    }
}
