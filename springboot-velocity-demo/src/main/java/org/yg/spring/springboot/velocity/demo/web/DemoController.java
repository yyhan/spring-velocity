package org.yg.spring.springboot.velocity.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 小天
 * @date 2020/6/14 14:18
 */
@Controller
public class DemoController {

    @RequestMapping(path = {"/index"})
    public String index(@RequestParam(name = "useLayout", defaultValue = "true") boolean useLayout,
                        Model model) {
        model.addAttribute("useLayout", useLayout);
        return "index";
    }
}
