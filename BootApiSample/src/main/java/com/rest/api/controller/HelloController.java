package com.rest.api.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HelloController {

    /**
     * 1. 화면에 hello world 출력
     * @return
     */
    @GetMapping(value = "/helloworld/string")
    @ResponseBody
    public String helloworldString() {
        System.out.println("[/helloworld/string] helloworldString() 호출");
        return "hello world";
    }

    /**
     * 2. 화면에 {message: "hello world class message"} 출력
     * @return
     */
    @GetMapping(value = "/helloword/json")
    @ResponseBody
    public Hello helloworldJson() {
        System.out.println("[/helloworld/json] helloworldJson() 호출");
        Hello hello = new Hello();
        hello.message = "hello world class message";
        return hello;
    }

    /**
     * 3. 화면에 helloworld.ftl 내용 출력
     * @return
     */
    @GetMapping(value = "/helloworld/page")
    public String helloworld() {
        System.out.println("[/helloworld/page] helloworld() 호출");
        return "helloworld";
    }

    @Getter
    @Setter
    public static class Hello {
        private String message;
    }
}
