package com.whilter.filereader;

import com.whilter.core.ComponentResolver;
import com.whilter.exception.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author mayank on 29/07/20 7:25 PM
 */
public class FileReaderBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    public static final Logger LOGGER = LoggerFactory.getLogger(FileReaderBeanDefinitionRegistrar.class);

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        ComponentResolver resolver = ((DefaultListableBeanFactory) registry).getBean(ComponentResolver.class);

        AnnotationAttributes enableFileReader = new AnnotationAttributes(metadata.getAnnotationAttributes(EnableFileReader.class.getName()));
        AnnotationAttributes[] values = enableFileReader.getAnnotationArray("value");

        for (AnnotationAttributes reader : values) {
            EnableFileReader.Type type = reader.getEnum("type");
            String delimiter = reader.getString("delimiter");
            String sheetName = reader.getString("sheetName");
            String beanName = reader.getString("beanName");

            if (beanName.trim().isEmpty()) {
                LOGGER.error("beanName is mandatory");
                throw new ApplicationException("beanName is mandatory");
            }

            FileReaderEndpoint fileReaderEndpoint = null;
            FileReaderComponent fileReaderComponent = null;
            switch (type) {
                case CSV:
                    if (delimiter.trim().isEmpty()) {
                        LOGGER.error("delimiter is mandatory");
                        throw new ApplicationException("delimiter is mandatory");
                    }
                    fileReaderEndpoint = new DelimitedFileReaderEndpoint(delimiter);
                    fileReaderComponent = resolver.resolve("filereader-delimited", FileReaderComponent.class);
                    break;
                case EXCEL:
                    if (sheetName.trim().isEmpty()) {
                        LOGGER.error("sheetName is mandatory");
                        throw new ApplicationException("sheetName is mandatory");
                    }
                    fileReaderEndpoint = new ExcelFileReaderEndpoint(sheetName);
                    fileReaderComponent = resolver.resolve("filereader-excel", FileReaderComponent.class);
                    break;
                default:
                    break;
            }

            BeanDefinitionBuilder readerBuilder = BeanDefinitionBuilder
                    .rootBeanDefinition(FileReaderFactoryBean.class)
                    .addConstructorArgValue(fileReaderEndpoint)
                    .addConstructorArgValue(fileReaderComponent);
            registry.registerBeanDefinition(beanName, readerBuilder.getBeanDefinition());
        }
    }
}
