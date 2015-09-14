package de.muenchen.datagrid.node;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.bus.EventBus;

/**
 *
 * @author claus.straube
 */
@Configuration
@EnableCaching
public class CacheConfiguration {
    
    public final static String PERSON_CACHE = "person_cache";
    
    @Bean
    public CacheManager cacheManager(HazelcastInstance instance) {
        return new HazelcastCacheManager(instance);
        
    }
    
    @Bean
    @Autowired
    public HazelcastInstance instance(EventBus eventBus) {
        HazelcastInstance instance = Hazelcast.newHazelcastInstance();
        IMap<String, String> map = instance.getMap( PERSON_CACHE );
        map.addEntryListener(new PersonCacheListener(eventBus), true);
        return instance;
    }
    
}
