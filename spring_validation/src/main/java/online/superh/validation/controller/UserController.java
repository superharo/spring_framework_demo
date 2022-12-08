package online.superh.validation.controller;

import lombok.extern.slf4j.Slf4j;
import online.superh.validation.dto.UserAddDTO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2022-12-08 13:43
 */
@RestController
@RequestMapping("/users")
@Validated
@Slf4j
public class UserController {

    @GetMapping("/get")
    public void get(@RequestParam("id") @Min(value = 1L, message = "编号必须大于 0") Integer id) {
        log.info("[get][id: {}]", id);
    }

    @PostMapping("/add")
    public void add(@Valid UserAddDTO addDTO) {
        log.info("[add][addDTO: {}]", addDTO);
    }

}