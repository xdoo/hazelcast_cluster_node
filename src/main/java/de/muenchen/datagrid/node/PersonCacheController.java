package de.muenchen.datagrid.node;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author claus.straube
 */
@RestController
@RequestMapping("/cache")
public class PersonCacheController {

    private final IMap<String, Person> map;
    
    @Autowired
    public PersonCacheController(HazelcastInstance hz) {
        this.map = hz.getMap(CacheConfiguration.PERSON_CACHE);
    }
    
    @RequestMapping(value = "/put", method = {RequestMethod.POST})
    public void putPerson(@RequestBody Person person) {
        this.map.put(person.oid, person);
    }
    
    @RequestMapping(value = "/evict/{oid}", method = {RequestMethod.GET})
    public void evictPerson(@PathVariable String oid) {
        this.map.remove(oid);
    }
    
    @RequestMapping(value = "/new", method = {RequestMethod.GET})
    public Person getStructure() {
        return new Person("1", "Hans", "Test");
    }
    
}
