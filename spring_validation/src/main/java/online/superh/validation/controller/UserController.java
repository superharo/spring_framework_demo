package online.superh.validation.controller;

import lombok.extern.slf4j.Slf4j;
import online.superh.validation.dto.UserAddDTO;
import online.superh.validation.dto.UserUpdateGenderDTO;
import org.springframework.validation.BindingResult;
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

    /**
     *  @Valid和BindingResult配套使用，@Valid用在参数前，BindingResult作为校验结果绑定返回
     *  bindingResult.hasErrors()判断是否校验通过，校验未通过，
     *  bindingResult.getFieldError().getDefaultMessage()获取在TestEntity的属性设置的自定义message，
     *  如果没有设置，则返回默认值"javax.validation.constraints.XXX.message"。
     *
     * @param id
     * @param bindingResult
     */
    @GetMapping("/get")
    public void get(@RequestParam("id") @Min(value = 1L, message = "编号必须大于 0") Integer id, BindingResult bindingResult) {
        log.info("[get][id: {}]", id);
    }

    @PostMapping("/add")
    public void add(@Valid UserAddDTO addDTO) {
        log.info("[add][addDTO: {}]", addDTO);
    }

    /**
     * 分组校验 Group02
     * @param updateDTO
     */
    @PutMapping("/update")
    public void update(@Validated(UserUpdateGenderDTO.Group02.class) UserUpdateGenderDTO updateDTO) {
        log.info("[update][updateDTO: {}]", updateDTO);
    }

}