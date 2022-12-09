package online.superh.validation.common.validation;

import online.superh.validation.common.enums.IntArrayValuable;
import online.superh.validation.common.interfaces.InEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2022-12-09 9:53
 */
public class InEnumValidator implements ConstraintValidator<InEnum, Integer> {

    private Set<Integer> values;

    /**
     * 初始化
     * 获得 @InEnum 注解的 values() 属性
     * @param annotation
     */
    @Override
    public void initialize(InEnum annotation) {
        IntArrayValuable[] values = annotation.value().getEnumConstants();
        if (values.length == 0) {
            this.values = Collections.emptySet();
        } else {
            this.values = Arrays.stream(values[0].array()).boxed().collect(Collectors.toSet());
        }
    }

    /**
     * 校验参数信息
     * @param value
     * @param context
     * @return
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (values.contains(value)) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate()
                .replaceAll("\\{value}", values.toString())).addConstraintViolation();
        return false;
    }
}
