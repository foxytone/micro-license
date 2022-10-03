package org.neat0n.licensingservice;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class HomePageController {
    @GetMapping("/")
    String homepage(){
        return "homepage";
    }
}
