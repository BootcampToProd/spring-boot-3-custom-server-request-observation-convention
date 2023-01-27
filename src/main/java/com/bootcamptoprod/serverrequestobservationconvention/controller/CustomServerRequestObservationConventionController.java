package com.bootcamptoprod.serverrequestobservationconvention.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomServerRequestObservationConventionController {

    @GetMapping("/hello")
    public String hello(@RequestParam(required = false) String user) {
        String message = "hello";

        if (user != null && !user.isBlank()) {
            message = message + " " + user;
        }
        return message;
    }
}
