package com.vou.api.dto.stream;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Data
public class StreamInfo {
    String eventID;
    String roomID;
    String[] videoUrl;
    List<Question> questions;
    Script script;
    long estimatedDuration;

    String eventName;
    String gameName;
    String eventBanner;
    // -1: start stream; -2: end stream
    int order;
    StreamEvent event;
    final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void raiseEvent(String streamKey) {
        support.firePropertyChange(streamKey, null, this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}
