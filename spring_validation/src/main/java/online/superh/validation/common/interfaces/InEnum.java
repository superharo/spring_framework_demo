package online.superh.validation.common.interfaces;

import online.superh.validation.common.enums.IntArrayValuable;
import online.superh.validation.common.validation.InEnumValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2022-12-09 9:47
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
// 自定义约束的校验器
@Constraint(validatedBy = InEnumValidator.class)
public @interface InEnum {


    // value() 属性，设置实现 IntArrayValuable 接口的类。这样，我们就能获得参数需要校验的值数组。
    Class<? extends IntArrayValuable> value();


    String message() default "必须在指定范围 {value}";

    /**
     * @return 分组
     */
    Class<?>[] groups() default {};

    /**
     * @return Payload 数组
     */
    Class<? extends Payload>[] payload() default {};

    /**
     *  Defines several {@code @InEnum} constraints on the same element.
     */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        InEnum[] value();
    }


}
