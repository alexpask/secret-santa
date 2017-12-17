package org.awsprog.secretsanta;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SantaController {

    @GetMapping("/")
    public String getIndex() {
        return "index";
    }
}
