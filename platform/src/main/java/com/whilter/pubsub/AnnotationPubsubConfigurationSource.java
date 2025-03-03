package com.whilter.pubsub;

import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.annotation.Annotation;

/**
 * Created by deepakchauhan on 03/09/17.
 */
public class AnnotationPubsubConfigurationSource implements PubsubConfigurationSource {

    private final AnnotationAttributes attributes;

    public AnnotationPubsubConfigurationSource(Class<? extends Annotation> annotation, AnnotationMetadata metadata) {
        this.attributes = new AnnotationAttributes(metadata.getAnnotationAttributes(annotation.getName()));
    }

    @Override
    public Pubsub[] values() {
        return this.attributes.getAnnotationArray("value", Pubsub.class);
    }
}
