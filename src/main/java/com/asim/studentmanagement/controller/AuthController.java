package com.asim.studentmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }
   // @PostMapping("/login")
   // return "redirect:/dashboard";
    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String saveRegister() {

        System.out.println("User Registered");

        return "redirect:/login";
    }

}