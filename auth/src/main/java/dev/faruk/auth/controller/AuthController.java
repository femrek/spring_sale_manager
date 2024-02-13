package dev.faruk.auth;

import org.springframework.web.bind.annotation.*;

@org.springframework.web.bind.annotation.RestController
public class AuthController {
    @GetMapping("/")
    public String index() {
        return "auth index";
    }

//    @PostMapping('/login')
//    public String login(@RequestParam ) {
//
//    }
}
