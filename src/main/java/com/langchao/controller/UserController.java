package com.langchao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/user")
public class UserController {
    /*@RequestMapping(value = "/quick",method = RequestMethod.GET,params = {"username"})*/
    @RequestMapping(value = "/quick",method = RequestMethod.GET,params = {"username"})
    public String save(){

        System.out.println("Controller save is running..........");

        return "/success.jsp";
    }
}
