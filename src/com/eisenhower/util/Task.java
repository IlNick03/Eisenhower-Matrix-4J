package com.eisenhower.util;

import static com.eisenhower.util.TaskProperties.*;
import java.time.*;
import java.util.*;

/**
 * Represents an abstract task with various customizable properties.
 * Concrete subclasses can define required properties by overriding {@link #getRequiredProperties()}.
 */
public class Task implements Comparable<Task> {

    private final Map<String, Object> properties = new HashMap<>();
    private final Collection<Task> subtasks = new ArrayList<>();
    private boolean atomicTask;
    
    protected Task() {
    }
    
    protected Task(boolean atomicTask) {
        this.atomicTask = atomicTask;
    }

    /**
     * Constructs a task with a name, additional information, and a date.
     *
     * @param taskName The name of the task.
     * @param date The date associated with the task.
     */
    public Task(String taskName, LocalDate date) {
        properties.put(TASK_NAME, taskName);
        properties.put(DATE, date);
    }
    
    /**
     * Constructs a task with a name, additional information, and a date.
     *
     * @param taskName The name of the task.
     * @param date The date associated with the task.
     * @param atomicTask If the task is atomic (i.e: mustn't contain subtasks)
     */
    public Task(String taskName, LocalDate date, boolean atomicTask) {
        properties.put(TASK_NAME, taskName);
        properties.put(DATE, date);
        this.atomicTask = atomicTask;
    }
    
    /**
     * Constructs a task with a name, additional information, a date and a time.
     *
     * @param taskName The name of the task.
     * @param date The date associated with the task.
     * @param time The time associated with the task.
     */
    public Task(String taskName, LocalDate date, LocalTime time) {
        properties.put(TASK_NAME, taskName);
        properties.put(DATE, date);
        properties.put(TIME, time);
    }
    
    /**
     * Constructs a task with a name, additional information, a date and a time.
     *
     * @param taskName The name of the task.
     * @param date The date associated with the task.
     * @param time The time associated with the task.
     * @param atomicTask If the task is atomic (i.e: mustn't contain subtasks)
     */
    public Task(String taskName, LocalDate date, LocalTime time, boolean atomicTask) {
        properties.put(TASK_NAME, taskName);
        properties.put(DATE, date);
        properties.put(TIME, time);
        this.atomicTask = atomicTask;
    }

    /**
     * Validates that all required properties are set.
     * Useful for concrete subclasses' constructors.
     */
    protected final void validateRequiredProperties() {
        Set<String> requiredProperties = this.getRequiredProperties();
        for (String property : requiredProperties) {
            if (!properties.containsKey(property)) {
                throw new IllegalStateException("Missing required property: " + property);
            }
        }
    }

    /**
     * Returns the set of required informationsHold for this task.
     *
     * @return A set of property keys that are required for this task.
     */
    public Set<String> getRequiredProperties() {
        return new HashSet<>(Arrays.asList(TASK_NAME, DATE));
    }
    
    public final int informationsHold() {
        return properties.size();
    }

    /**
     * Adds or updates a property of the task.
     *
     * @param key The property key.
     * @param value The property value.
     * @return The previous value associated with the key, or {@code null} if there was no previous value.
     */
    public final Object putInfo(String key, Object value) {
        return properties.put(key, value);
    }
    
    public final Object putInfoIfAbsent(String key, Object value) {
        return properties.putIfAbsent(key, value);
    }

    /**
     * Retrieves the value of a specific property.
     *
     * @param key The property key.
     * @return The value associated with the key, or {@code null} if the key does not exist.
     */
    public final Object getInfo(String key) {
        return properties.get(key);
    }
    
    public final Object getInfoOrDefault(String key, Object defaultValue) {
        return properties.getOrDefault(key, defaultValue);
    }
    
    public final Object removeInfo(String key) {
        if (!this.getRequiredProperties().contains(key)) {
            throw new UnsupportedOperationException("This info is strictly necessary and can't be removed.");
        }
        return properties.remove(key);
    }
    
    public final Object replaceInfo(String key, Object value) {
        return properties.replace(key, value);
    }
    
    // -------------------------------------------------------------------------
    
    public Collection<Task> getSubtasks() {
        return atomicTask ? List.of() : subtasks;
    }
    
    public final boolean addSubtask(Task subtask) {
        if (subtask == null || subtask.equals(this)) {
            return false;
        }
        return this.getSubtasks().add(subtask);
    }
    
    public final boolean addAllSubtasks(Collection<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            return false;
        }
        return this.getSubtasks().addAll(tasks);
    }
    
    public final boolean removeSubtask(Task subtask) {
        if (subtask == null) {
            return false;
        }
        return this.getSubtasks().remove(subtask);
    }
    
    public final boolean removeAllSubtasks(Collection<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            return false;
        }
        return this.getSubtasks().removeAll(tasks);
    }
    
    public final void clearSubtasks() {
        if (atomicTask) {
            return;
        }
        this.getSubtasks().clear();
    }
    
    // -------------------------------------------------------------------------
    
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
        return Objects.equals(this.properties, other.properties) 
                && Objects.equals(this.getSubtasks(), other.getSubtasks());
    }
    
    @Override
    public final int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.properties);
        hash = 17 * hash + Objects.hashCode(this.getSubtasks());
        return hash;
    }

    @Override
    public int compareTo(Task o) {
        // required property
        LocalDate thisDate = (LocalDate) this.getInfo(DATE);
        LocalDate otherDate = (LocalDate) o.getInfo(DATE);
        // additional property
        LocalTime thisTime = (LocalTime) this.getInfoOrDefault(TIME, LocalTime.of(0, 0));
        LocalTime otherTime = (LocalTime) o.getInfoOrDefault(TIME, LocalTime.of(0, 0));
        
        // date and time together
        LocalDateTime thisDateTime = LocalDateTime.of(thisDate, thisTime);
        LocalDateTime otherDateTime = LocalDateTime.of(otherDate, otherTime);
        
        return thisDateTime.compareTo(otherDateTime);
    }
}
