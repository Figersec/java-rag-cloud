package com.kailin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author chengpuhui
 * @Date 2021/12/27
 */
@Controller
public class CheckController {

    @RequestMapping("/klcheck")
    public void klcheck(HttpServletResponse response){
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
