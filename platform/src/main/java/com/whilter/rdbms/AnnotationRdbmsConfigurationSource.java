package com.whilter.rdbms;

import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.annotation.Annotation;

/**
 * Created by deepakchauhan on 03/09/17.
 */
public class AnnotationRdbmsConfigurationSource implements RdbmsConfigurationSource {

    private final AnnotationAttributes attributes;

    public AnnotationRdbmsConfigurationSource(Class<? extends Annotation> annotation, AnnotationMetadata metadata) {
        this.attributes = new AnnotationAttributes(metadata.getAnnotationAttributes(annotation.getName()));
    }

    @Override
    public String confId() {
        return attributes.getString("confId");
    }

    @Override
    public String persistenceUnit() {
        String persistenceUnit = attributes.getString("persistenceUnit");
        return persistenceUnit.equalsIgnoreCase(EnableRDBMS.NONE) ? null : persistenceUnit;
    }

    @Override
    public String[] packagesToScan() {
        return attributes.getStringArray("packagesToScan");
    }

    @Override
    public boolean enableJpa() {
        return attributes.getBoolean("enableJpa");
    }
}
