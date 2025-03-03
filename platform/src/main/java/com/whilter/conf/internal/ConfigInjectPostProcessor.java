package com.whilter.conf.internal;

import com.whilter.conf.ConfigReader;
import com.whilter.conf.Configuration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.util.Collection;
import java.util.Map;

/**
 * Created by deepakchauhan on 14/07/17.
 */
public class ConfigInjectPostProcessor implements BeanFactoryPostProcessor {

    private ConfigReader configReader;

    public ConfigInjectPostProcessor() {
        this.configReader = ConfigReader.get();
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        try {
            DefaultListableBeanFactory bf = (DefaultListableBeanFactory) beanFactory;

            Map<String, ConfigRegistryHolder.ConfRegistry> registry = ConfigRegistryHolder.getInstance().getRegistry();
            for (ConfigRegistryHolder.ConfRegistry confRegistry : registry.values()) {
                Collection<? extends Configuration> configurations = configReader.read((Class<? extends Configuration>) confRegistry.getCls());
                for (Configuration conf : configurations) {
                    BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(ConfigFactoryBean.class).addConstructorArgValue(conf);
                    String id = conf.getID();
                    if (id == null || id.isEmpty()) {
                        id = conf.getClass().getName() + '#' + System.currentTimeMillis();
                    }
                    bf.registerBeanDefinition(id, builder.getBeanDefinition());
                    if (conf.getAliases() != null) {
                        for (String alias : conf.getAliases()) {
                            bf.registerAlias(id, alias);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
