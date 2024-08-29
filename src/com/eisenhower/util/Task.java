package com.eisenhower.util;

import static com.eisenhower.util.TaskProperties.*;
import java.time.*;
import java.util.*;

/**
 *
 * @author Nicolas Scalese
 */
public class Task {
    
    private Map<String, Object> properties = new HashMap<>();
    
    
    public Task() {
    }
    
    public Task(String taskName) {
        properties.put(TASK_NAME, taskName);
    }
    
    public Task(String taskName, LocalDate date) {
        properties.put(TASK_NAME, taskName);
        properties.put(DATE, date);
    }
    
    public Task(String taskName, String moreInfo, LocalDateTime dateTime) {
        properties.put(TASK_NAME, taskName);
        properties.put(MORE_INFO, moreInfo);
        properties.put(DATE_AND_TIME, dateTime);
    }
    
    // -------------------------------------------------------------------------
    
    public final boolean containsInfo(String key) {
        return properties.containsKey(key);
    }
    
    public final Object putInfo(String key, Object value) {
        return properties.put(key, value);
    }
    
    public final Object putInfoIfAbsent(String key, Object value) {
        return properties.putIfAbsent(key, value);
    }
    
    public final Object getInfo(String key) {
        return properties.get(key);
    }
    
    public final Object removeInfo(String key) {
        return properties.remove(key);
    }
    
    public final Object replaceInfo(String key, Object value) {
        return properties.replace(key, value);
    }
    
    // -------------------------------------------------------------------------

    @Override
    public final int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.properties);
        return hash;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Task other = (Task) obj;
        return Objects.equals(this.properties, other.properties);
    }
    
}
