package org.yg.spring.springboot.velocity.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 小天
 * @date 2020/6/14 14:18
 */
@Controller
public class DemoController {

    @RequestMapping(path = {"/", "/index"})
    public String index() {
        return "index";
    }
}
