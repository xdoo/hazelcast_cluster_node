package de.muenchen.datagrid.node;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.MapEvent;
import com.hazelcast.map.listener.EntryAddedListener;
import com.hazelcast.map.listener.EntryEvictedListener;
import com.hazelcast.map.listener.EntryRemovedListener;
import com.hazelcast.map.listener.EntryUpdatedListener;
import com.hazelcast.map.listener.MapClearedListener;
import com.hazelcast.map.listener.MapEvictedListener;
import reactor.bus.Event;
import reactor.bus.EventBus;

/**
 *
 * @author claus.straube
 */
public class PersonCacheListener implements EntryAddedListener<String, Person>, 
                                          EntryRemovedListener<String, Person>, 
                                          EntryUpdatedListener<String, Person>, 
                                          EntryEvictedListener<String, Person> , 
                                          MapEvictedListener, 
                                          MapClearedListener {

    private final EventBus eventBus;
    
    public PersonCacheListener(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void mapEvicted(MapEvent me) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mapCleared(MapEvent me) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void entryAdded(EntryEvent<String, Person> ee) {
        this.eventBus.notify(EventType.ADD, Event.wrap(ee));
    }

    @Override
    public void entryRemoved(EntryEvent<String, Person> ee) {
        this.eventBus.notify(EventType.REMOVE, Event.wrap(ee));
    }

    @Override
    public void entryUpdated(EntryEvent<String, Person> ee) {
        this.eventBus.notify(EventType.UPDATE, Event.wrap(ee));
    }

    @Override
    public void entryEvicted(EntryEvent<String, Person> ee) {
        this.eventBus.notify(EventType.EVICT, Event.wrap(ee));
    }
    
}
