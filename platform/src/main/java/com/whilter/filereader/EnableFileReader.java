package com.whilter.filereader;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author mayank on 29/07/20 7:20 PM
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(FileReaderBeanDefinitionRegistrar.class)
public @interface EnableFileReader {

    Reader[] value();

    enum Type {
        EXCEL, CSV
    }
}
