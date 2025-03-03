package com.whilter.rdbms;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * Created by deepakchauhan on 23/07/17.
 */
public class EntityManagerFactoryBean implements FactoryBean<EntityManagerFactory> {

    private String[] packagesToScan;
    private DataSource dataSource;
    private RDBMS rdbms;
    private String persistenceUnit;

    public EntityManagerFactoryBean(RDBMS rdbms) {
        this.rdbms = rdbms;
    }

    @Override
    public EntityManagerFactory getObject() throws Exception {
//        final LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
//        emf.setDataSource(dataSource);
//        emf.setPackagesToScan(packagesToScan);
//        emf.setJpaDialect(new HibernateJpaDialect());
//
//        if (persistenceUnit != null && !persistenceUnit.isEmpty()) {
//            emf.setPersistenceUnitName(persistenceUnit);
//        }
//
//        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        vendorAdapter.setDatabase(Database.valueOf(rdbms.getDatabase().name()));
//        emf.setJpaVendorAdapter(vendorAdapter);
//        emf.setJpaPropertyMap(rdbms.getProperties());
//        emf.afterPropertiesSet();
//        return emf.getObject();
        return null;
    }

    // GC fix this later if used
//
//    @Override
//    public EntityManagerFactory getObject() throws Exception {
//        final LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
//        emf.setDataSource(dataSource);
//        emf.setPackagesToScan(packagesToScan);
//        emf.setJpaDialect(new HibernateJpaDialect());
//
//        if (persistenceUnit != null && !persistenceUnit.isEmpty()) {
//            emf.setPersistenceUnitName(persistenceUnit);
//        }
//
//        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        vendorAdapter.setDatabase(Database.valueOf(rdbms.getDatabase().name()));
//        emf.setJpaVendorAdapter(vendorAdapter);
//        emf.setJpaPropertyMap(rdbms.getProperties());
//        emf.afterPropertiesSet();
//        return emf.getObject();
//    }

    @Override
    public Class<?> getObjectType() {
        return EntityManagerFactory.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setPackagesToScan(String[] packagesToScan) {
        this.packagesToScan = packagesToScan;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setPersistenceUnit(String persistenceUnit) {
        this.persistenceUnit = persistenceUnit;
    }
}
