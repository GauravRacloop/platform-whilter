package com.whilter.rdbms;

import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.lang.annotation.*;

/**
 * Created by deepakchauhan on 03/09/17.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableTransactionManagement
@Import(RDBMSBeanDefinitionRegister.class)
public @interface EnableRDBMS {

    String NONE = "NONE";

    String confId();
    boolean enableJpa() default false;
    String persistenceUnit() default NONE;
    String[] packagesToScan() default {};
}
