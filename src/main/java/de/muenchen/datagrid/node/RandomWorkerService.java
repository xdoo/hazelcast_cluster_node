package de.muenchen.datagrid.node;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 *
 * @author claus.straube
 */
@Service
public class RandomWorkerService {
    
    private Cache cache;
    private final BlockingQueue<String> queue = new ArrayBlockingQueue<>(1024);

    @Autowired
    public RandomWorkerService(CacheManager cache) {
        this.cache = cache.getCache(CacheConfiguration.PERSON_CACHE);
    }
    
    @PostConstruct
    public void run() {
        ScheduledExecutorService ex = Executors.newScheduledThreadPool(10);
        ex.scheduleWithFixedDelay(() -> {
            int nextInt = ThreadLocalRandom.current().nextInt(20, 500);
            Person person = new Person(IdService.next(), "Hans", "Test");
            cache.put(person.oid, person);
            try {
                Thread.sleep(nextInt);
            } catch (InterruptedException ex1) {
                Logger.getLogger(RandomWorkerService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }, 5000, 50, TimeUnit.MILLISECONDS);
    }

}
