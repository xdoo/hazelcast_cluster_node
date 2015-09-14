package de.muenchen.datagrid.node;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.Environment;
import reactor.bus.EventBus;

/**
 *
 * @author claus.straube
 */
@Configuration
public class EventBusConfiguration {
    
    @Bean
    Environment env() {
        return Environment.initializeIfEmpty()
                          .assignErrorJournal();
    }
    
    @Bean
    public EventBus getEventBus(Environment env) {
        return EventBus.create(env);
    }
    
}
