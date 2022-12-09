package online.superh.validation.common.enums;

import java.util.Arrays;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2022-12-09 9:41
 */
public enum GenderEnum implements  IntArrayValuable{

    MALE(1,"男"),

    FEMALE(2,"女");

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(GenderEnum::getCode).toArray();

    private final Integer code;


    private final String name;


    GenderEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public int[] array() {
        return ARRAYS;
    }
}
