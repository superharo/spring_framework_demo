package online.superh.validation.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2022-12-09 15:40
 */
@Data
public class BaseDTO {

    @NotNull(message = "用户编号不能为空",groups = UserUpdateGenderDTO.class)
    private Long id;

}
