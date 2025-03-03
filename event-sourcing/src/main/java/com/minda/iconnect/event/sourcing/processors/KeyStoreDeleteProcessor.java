package com.minda.iconnect.event.sourcing.processors;

import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;

/**
 * @author jaspreet on 09/01/19
 */
public class KeyStoreDeleteProcessor implements Processor<String, String> {
    private final String stateStore;
    private ProcessorContext context;
    private KeyValueStore<String, String> kvStore;

    public KeyStoreDeleteProcessor(String stateStore) {
        this.stateStore = stateStore;
    }

    @Override
    public void init(ProcessorContext processorContext) {
        this.context = processorContext;
        this.kvStore = (KeyValueStore<String, String>) context.getStateStore(stateStore);
    }

    @Override
    public void process(String imei, String command) {
       kvStore.delete(imei);
    }

    @Override
    public void close() {

    }
}
