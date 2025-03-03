package com.whilter.rdbms;

import com.whilter.conf.ConfigReader;
import com.whilter.rdbms.jooq.JOOQConfigFactoryBean;
import com.whilter.rdbms.jooq.JOOQFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Created by deepakchauhan on 03/09/17.
 */
public class RDBMSBeanDefinitionRegister implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        ConfigReader configReader = ConfigReader.get();
        AnnotationRdbmsConfigurationSource source = new AnnotationRdbmsConfigurationSource(EnableRDBMS.class, metadata);

        //setDataSource
        RDBMS rdbms = configReader.read(source.confId(), RDBMS.class);

        BeanDefinitionBuilder dsBuilder = BeanDefinitionBuilder
                .rootBeanDefinition(DataSourceFactoryBean.class)
                .addConstructorArgValue(rdbms);
        registry.registerBeanDefinition(rdbms.getID() + RDBMS.DS, dsBuilder.getBeanDefinition());

        for (String alias : rdbms.getAliases()) {
            registry.registerAlias(rdbms.getID() + RDBMS.DS, alias + RDBMS.DS);
        }

        String beanPrefix = source.persistenceUnit() == null ? rdbms.getID() : source.persistenceUnit();

        if (source.enableJpa()) {
            //setEMF
            BeanDefinitionBuilder emfBuilder = BeanDefinitionBuilder
                    .rootBeanDefinition(EntityManagerFactoryBean.class)
                    .addConstructorArgValue(rdbms)
                    .addPropertyReference("dataSource", rdbms.getID() + RDBMS.DS)
                    .addPropertyValue("persistenceUnit", beanPrefix)
                    .addPropertyValue("packagesToScan", source.packagesToScan());
            registry.registerBeanDefinition(beanPrefix + RDBMS.EMF, emfBuilder.getBeanDefinition());
            for (String alias : rdbms.getAliases()) {
                registry.registerAlias(beanPrefix + RDBMS.EMF, alias + RDBMS.EMF);
            }

            //setTX
            BeanDefinitionBuilder txBuilder = BeanDefinitionBuilder
                    .rootBeanDefinition(JpaTransactionManagerFactoryBean.class)
                    .addConstructorArgReference(beanPrefix + RDBMS.EMF);
            registry.registerBeanDefinition(beanPrefix + RDBMS.TX, txBuilder.getBeanDefinition());
            for (String alias : rdbms.getAliases()) {
                registry.registerAlias(beanPrefix + RDBMS.TX, alias + RDBMS.TX);
            }

        } else {
            //setTX
            BeanDefinitionBuilder txBuilder = BeanDefinitionBuilder
                    .rootBeanDefinition(DSTransactionManagerFactoryBean.class)
                    .addConstructorArgReference(rdbms.getID() + RDBMS.DS);
            registry.registerBeanDefinition(beanPrefix + RDBMS.TX, txBuilder.getBeanDefinition());
            for (String alias : rdbms.getAliases()) {
                registry.registerAlias(beanPrefix + RDBMS.TX, alias + RDBMS.TX);
            }
        }

        //setJooQ
        BeanDefinitionBuilder jooqConfBuilder = BeanDefinitionBuilder
                .rootBeanDefinition(JOOQConfigFactoryBean.class)
                .addConstructorArgValue(rdbms)
                .addPropertyReference("dataSource", rdbms.getID() + RDBMS.DS)
                .addPropertyReference("transactionManager", beanPrefix + RDBMS.TX);
        registry.registerBeanDefinition(beanPrefix + RDBMS.jOOQ, jooqConfBuilder.getBeanDefinition());
        for (String alias : rdbms.getAliases()) {
            registry.registerAlias(beanPrefix + RDBMS.jOOQ, alias + RDBMS.jOOQ);
        }


        BeanDefinitionBuilder jooqBuilder = BeanDefinitionBuilder
                .rootBeanDefinition(JOOQFactoryBean.class)
                .addConstructorArgReference(beanPrefix + RDBMS.jOOQ);
        registry.registerBeanDefinition(beanPrefix + RDBMS.jOOQDSL, jooqBuilder.getBeanDefinition());
        for (String alias : rdbms.getAliases()) {
            registry.registerAlias(beanPrefix + RDBMS.jOOQDSL, alias + RDBMS.jOOQDSL);
        }
    }
}
