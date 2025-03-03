package com.whilter.pubsub;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created by deepakchauhan on 03/09/17.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(PubsubBeanDefinitionRegister.class)
public @interface EnablePubSub {

    Pubsub[] value();

}
