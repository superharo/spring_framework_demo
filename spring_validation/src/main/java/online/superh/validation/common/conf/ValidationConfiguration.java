// package online.superh.validation.common.conf;
//
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.context.support.ResourceBundleMessageSource;
// import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
//
// /**
//  * @version: 1.0
//  * @author: haro
//  * @description: 支持 i18 国际化的 Validator Bean 对象
//  * @date: 2022-12-09 13:10
//  */
// @Slf4j
// @Configuration
// public class ValidationConfiguration  {
//
//     @Bean
//     public javax.validation.Validator validator(ResourceBundleMessageSource messageSource)  {
//         log.info("Configuration---validator--i18n");
//         LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
//         validator.setValidationMessageSource(messageSource);
//         return validator;
//     }
//
//
// }
