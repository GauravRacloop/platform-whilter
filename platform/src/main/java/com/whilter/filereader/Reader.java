package com.whilter.filereader;

import java.lang.annotation.*;

/**
 * @author mayank on 30/07/20 1:22 PM
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Reader {
    String beanName();

    /**
     * Type of the Excel Reader
     */
    EnableFileReader.Type type();

    /**
     * Required only in case of excel Reader, should be the sheet required to read from excel file.
     */
    String sheetName() default "";

    String delimiter() default ",";
}
