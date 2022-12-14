package online.superh.springsecurity.auth.resource.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2022-12-14 11:23
 */

@RestController
@RequestMapping("/api/resource")
public class ResourceController {

    @RequestMapping("/hello")
    public String hello() {
        return "world";
    }


}
