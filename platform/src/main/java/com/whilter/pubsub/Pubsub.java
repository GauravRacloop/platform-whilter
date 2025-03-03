package com.whilter.pubsub;


import java.io.Serializable;
import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Pubsub {

    String confId();
    String producerBean() default "";
    String c2dProducerBean() default "";
    String consumerBean() default "";
    Class<? extends SerializerFactory> serializerFactoryCls() default StringSerializerFactory.class;
    Class<? extends Serializable> serializableClass() default String.class;
}
