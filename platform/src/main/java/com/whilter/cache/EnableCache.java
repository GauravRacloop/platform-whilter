package com.whilter.cache;

import com.whilter.conf.ConfigReader;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.interceptor.*;
import org.springframework.cache.jcache.JCacheCacheManager;

import java.util.Collection;

@EnableCaching
public class EnableCache extends CachingConfigurerSupport {

    @Override
    public CacheManager cacheManager() {
        ConfigReader configReader = ConfigReader.get();
        Collection<CacheConfig> cacheConfigs = configReader.read(CacheConfig.class);

        net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
        CacheConfiguration global = new CacheConfiguration();
        global.setName(CacheConfig.GLOBAL_CACHE);
        global.setMemoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.CLOCK.toString());
        global.setMaxEntriesLocalHeap(10000);
        global.setTimeToIdleSeconds(1800);//30 minutes
        global.setTimeToLiveSeconds(1800);//30 minutes
        config.addCache(global);
        if (cacheConfigs != null) {
            for (CacheConfig conf : cacheConfigs) {
                CacheConfiguration cacheConfiguration = new CacheConfiguration();
                cacheConfiguration.setName(conf.getID() == null ? conf.getName() : conf.getID());
                cacheConfiguration.setMemoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.CLOCK.toString());
                cacheConfiguration.setMaxEntriesLocalHeap(conf.getMaxSize());
                cacheConfiguration.setTimeToIdleSeconds(conf.getIdle());
                cacheConfiguration.setTimeToLiveSeconds(conf.getTtl());
                config.addCache(cacheConfiguration);
            }
        }
        return new JCacheCacheManager();
    }

    @Override
    public KeyGenerator keyGenerator() {
        return new KeyGenerator();
    }

    @Override
    public CacheResolver cacheResolver() {
        return new SimpleCacheResolver(cacheManager());
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new SimpleCacheErrorHandler();
    }
}
