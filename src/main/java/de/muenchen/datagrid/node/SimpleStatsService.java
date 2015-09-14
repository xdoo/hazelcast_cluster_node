package de.muenchen.datagrid.node;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import reactor.bus.Event;
import reactor.bus.EventBus;
import static reactor.bus.selector.Selectors.$;

/**
 *
 * @author claus.straube
 */
@Service
public class SimpleStatsService {
    
    private static final Logger LOG = LoggerFactory.getLogger(SimpleStatsService.class);
    
    private final EventBus eventBus;
    private final MetricRegistry metrics;
    
    private final Meter metricsMeterAdd;
    private final Meter metricsMeterRemove;
    private final Meter metricsMeterUpdate;
    private final Meter metricsMeterEvict;
    
    private final Counter metricsCounterAdd;
    private final Counter metricsCounterEvict;
    
    public static final String METRIC_METER_ADD = "meter.cache.add";
    public static final String METRIC_METER_REMOVE = "meter.cache.remove";
    public static final String METRIC_METER_UPDATE = "meter.cache.update";
    public static final String METRIC_METER_EVICT = "meter.cache.evict";
    
    public static final String METRIC_COUNTER_ADD = "counter.cache.add";
    public static final String METRIC_COUNTER_EVICT = "counter.cache.evict";
    
    public static final String METRIC_GAUGE_CACHE_SIZE = "gauge.cache.size";
    
    @Autowired
    public SimpleStatsService(
            EventBus eventBus, 
            MetricRegistry metricRegistry,
            HazelcastInstance cache) {
        
        this.eventBus = eventBus;
        this.metrics = metricRegistry;
        
        // meters
        this.metricsMeterAdd = this.metrics.meter(METRIC_METER_ADD);
        this.metricsMeterRemove = this.metrics.meter(METRIC_METER_REMOVE);
        this.metricsMeterUpdate = this.metrics.meter(METRIC_METER_UPDATE);
        this.metricsMeterEvict = this.metrics.meter(METRIC_METER_EVICT);
        
        // counters
        this.metricsCounterAdd = this.metrics.counter(METRIC_COUNTER_ADD);
        this.metricsCounterEvict = this.metrics.counter(METRIC_COUNTER_EVICT);
        
        // gauges
        this.metrics.register(METRIC_GAUGE_CACHE_SIZE, 
                (Gauge<Integer>) () -> cache.getMap(CacheConfiguration.PERSON_CACHE).size());
        
        // register to eventbus
        this.eventBus.on($(EventType.ADD), (Event<EntryEvent<String, Person>> t) -> {
            this.add(t.getData());
        });
        
        this.eventBus.on($(EventType.REMOVE), (Event<EntryEvent<String, Person>> t) -> {
            this.remove(t.getData());
        });
        
        this.eventBus.on($(EventType.UPDATE), (Event<EntryEvent<String, Person>> t) -> {
            this.update(t.getData());
        });
        
        this.eventBus.on($(EventType.EVICT), (Event<EntryEvent<String, Person>> t) -> {
            this.evict(t.getData());
        });

    }
    
    public final void add(EntryEvent<String, Person> e) {
        this.metricsMeterAdd.mark();
        this.metricsCounterAdd.inc();
    }
    
    public final void remove(EntryEvent<String, Person> e) {
        this.metricsMeterRemove.mark();
        this.metricsCounterEvict.inc();
    }
    
    public final void update(EntryEvent<String, Person> e) {
        this.metricsMeterUpdate.mark();
    }
    
    public final void evict(EntryEvent<String, Person> e) {
        this.metricsMeterEvict.mark();
        this.metricsCounterEvict.inc();
    }
    
}
