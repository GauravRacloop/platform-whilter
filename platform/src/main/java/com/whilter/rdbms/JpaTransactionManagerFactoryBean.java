package com.whilter.rdbms;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

//import javax.persistence.EntityManagerFactory;

/**
 * Created by deepakchauhan on 23/07/17.
 */
public class JpaTransactionManagerFactoryBean implements FactoryBean<PlatformTransactionManager> {

    private EntityManagerFactory emf;

    public JpaTransactionManagerFactoryBean(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public PlatformTransactionManager getObject() {
        return new JpaTransactionManager(emf);
    }

    @Override
    public Class<?> getObjectType() {
        return PlatformTransactionManager.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
