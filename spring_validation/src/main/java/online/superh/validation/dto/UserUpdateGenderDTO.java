package online.superh.validation.dto;

import online.superh.validation.common.enums.GenderEnum;
import online.superh.validation.common.interfaces.InEnum;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2022-12-09 10:30
 */
public class UserUpdateGenderDTO {

    public interface Group01 {}

    public interface Group02 {}


    @NotNull(message = "用户编号不能为空")
    //国际化
    // @NotNull(message = "{UserUpdateGenderDTO.id.NotNull}")
    private Integer id;

    @NotNull(message = "性别不能为空")
    @InEnum(value = GenderEnum.class, message = "性别必须是 {value}")
    private Integer gender;

    @AssertTrue(message = "状态必须为 true", groups = Group01.class)
    @AssertFalse(message = "状态必须为 false", groups = Group02.class)
    private Boolean status;

}
