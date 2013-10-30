package org.nuxeo.mule.poll;

import java.util.List;

import org.mule.api.callback.SourceCallback;

public class ListenerConfig {

    protected final SourceCallback callback;
    protected List<String> eventNames;

    public ListenerConfig(List<String> eventNames,SourceCallback callback) {
        this.callback = callback;
        this.eventNames = eventNames;
    }

    public SourceCallback getCallback() {
        return callback;
    }

    public List<String> getEventNames() {
        return eventNames;
    }

}
