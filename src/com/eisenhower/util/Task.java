package com.eisenhower.util;

import static com.eisenhower.util.TaskProperties.*;
import java.time.*;
import java.util.*;

/**
 * Represents a task with customizable properties and optional subtasks. 
 * <p>
 * This class allows the creation of atomic (non-divisible) or compound tasks, 
 * where each task can have subtasks, and properties such as name, date, and time.
 * Concrete subclasses can define their own required properties by overriding 
 * {@link #getRequiredProperties()}. 
 * </p>
 * 
 * <p>
 * Tasks can be compared based on their associated date and time, and can be 
 * customized by adding properties dynamically.
 * </p>
 */
public class Task implements Comparable<Task> {

    // Map containing the properties of the task
    private final Map<String, Object> properties = new HashMap<>();
    // Collection of subtasks (empty if the task is atomic)
    private final Collection<Task> subtasks = new ArrayList<>();
    private boolean atomicTask;
    
    /**
     * Default constructor, used for creating a task without predefined properties.
     * <p>
     * Subclasses can call this constructor and then use the {@code putProperty} 
     * method to set additional properties.
     * </p>
     */
    protected Task() {
    }

    /**
     * Constructs a task and specifies whether it is atomic (i.e., it cannot have subtasks).
     * 
     * @param atomicTask {@code true} if the task is atomic, {@code false} otherwise.
     */
    protected Task(boolean atomicTask) {
        this.atomicTask = atomicTask;
    }

    /**
     * Constructs a task with a name and a date.
     * 
     * @param taskName The name of the task.
     * @param date The date associated with the task.
     */
    public Task(String taskName, LocalDate date) {
        properties.put(TASK_NAME, taskName);
        properties.put(DATE, date);
    }

    /**
     * Constructs a task with a name, a date, and specifies whether it is atomic.
     * 
     * @param taskName The name of the task.
     * @param date The date associated with the task.
     * @param atomicTask {@code true} if the task is atomic, {@code false} otherwise.
     */
    public Task(String taskName, LocalDate date, boolean atomicTask) {
        properties.put(TASK_NAME, taskName);
        properties.put(DATE, date);
        this.atomicTask = atomicTask;
    }

    /**
     * Constructs a task with a name, a date, and a time.
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
     * Constructs a task with a name, a date, a time, and specifies whether it is atomic.
     * 
     * @param taskName The name of the task.
     * @param date The date associated with the task.
     * @param time The time associated with the task.
     * @param atomicTask {@code true} if the task is atomic, {@code false} otherwise.
     */
    public Task(String taskName, LocalDate date, LocalTime time, boolean atomicTask) {
        properties.put(TASK_NAME, taskName);
        properties.put(DATE, date);
        properties.put(TIME, time);
        this.atomicTask = atomicTask;
    }

    /**
     * Validates that all required properties are set. If any required property is missing, 
     * an {@link IllegalStateException} is thrown.
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
     * Returns the set of properties that are required for this task.
     * Subclasses can override this method to specify custom required properties.
     * 
     * @return A set of required property keys.
     */
    public Set<String> getRequiredProperties() {
        return new HashSet<>(Arrays.asList(TASK_NAME, DATE));
    }

    /**
     * Returns the number of properties currently held by the task.
     * 
     * @return The number of properties.
     */
    public final int propertiesCount() {
        return properties.size();
    }

    /**
     * Adds or updates a property of the task.
     * 
     * @param key The property key.
     * @param value The value to be associated with the key.
     * @return The previous value associated with the key, or {@code null} if none existed.
     */
    public final Object putProperty(String key, Object value) {
        return properties.put(key, value);
    }

    /**
     * Adds a property to the task if it is not already present.
     * 
     * @param key The property key.
     * @param value The value to be associated with the key.
     * @return The previous value associated with the key, or {@code null} if none existed.
     */
    public final Object putPropertyIfAbsent(String key, Object value) {
        return properties.putIfAbsent(key, value);
    }

    /**
     * Retrieves the value of a property.
     * 
     * @param key The property key.
     * @return The value associated with the key, or {@code null} if no such key exists.
     */
    public final Object getProperty(String key) {
        return properties.get(key);
    }

    /**
     * Retrieves the value of a property, or returns a default value if the property is not set.
     * 
     * @param key The property key.
     * @param defaultValue The value to return if the property is not present.
     * @return The value associated with the key, or {@code defaultValue} if no such key exists.
     */
    public final Object getPropertyOrDefault(String key, Object defaultValue) {
        return properties.getOrDefault(key, defaultValue);
    }

    /**
     * Removes a property from the task.
     * <p>
     * Required properties cannot be removed, and attempting to do so will throw 
     * an {@link UnsupportedOperationException}.
     * </p>
     * 
     * @param key The property key.
     * @return The removed value, or {@code null} if no such key existed.
     * @throws UnsupportedOperationException If the property is required and cannot be removed.
     */
    public final Object removeProperty(String key) {
        if (getRequiredProperties().contains(key)) {
            throw new UnsupportedOperationException("This property is required and cannot be removed.");
        }
        return properties.remove(key);
    }

    /**
     * Replaces the value of a property.
     * 
     * @param key The property key.
     * @param value The new value.
     * @return The previous value associated with the key, or {@code null} if none existed.
     */
    public final Object replaceProperty(String key, Object value) {
        return properties.replace(key, value);
    }

    /**
     * Returns the subtasks of this task.
     * <p>
     * If the task is atomic, the list will be empty.
     * </p>
     * 
     * @return A collection of subtasks, or an empty list if the task is atomic.
     */
    public Collection<Task> getSubtasks() {
        return atomicTask ? List.of() : subtasks;
    }

    /**
     * Adds a subtask to this task.
     * 
     * @param subtask The subtask to be added.
     * @return {@code true} if the subtask was added, {@code false} otherwise.
     */
    public final boolean addSubtask(Task subtask) {
        if (subtask == null || subtask.equals(this) || atomicTask) {
            return false;
        }
        return this.getSubtasks().add(subtask);
    }

    /**
     * Adds a collection of subtasks to this task.
     * 
     * @param tasks The collection of subtasks to be added.
     * @return {@code true} if the subtasks were added, {@code false} otherwise.
     */
    public final boolean addAllSubtasks(Collection<Task> tasks) {
        if (tasks == null || tasks.isEmpty() || atomicTask) {
            return false;
        }
        return this.getSubtasks().addAll(tasks);
    }

    /**
     * Removes a subtask from this task.
     * 
     * @param subtask The subtask to be removed.
     * @return {@code true} if the subtask was removed, {@code false} otherwise.
     */
    public final boolean removeSubtask(Task subtask) {
        if (subtask == null) {
            return false;
        }
        return this.getSubtasks().remove(subtask);
    }

    /**
     * Removes a collection of subtasks from this task.
     * 
     * @param tasks The collection of subtasks to be removed.
     * @return {@code true} if the subtasks were removed, {@code false} otherwise.
     */
    public final boolean removeAllSubtasks(Collection<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            return false;
        }
        return this.getSubtasks().removeAll(tasks);
    }

    /**
     * Clears all subtasks of this task.
     * <p>
     * If the task is atomic, this method does nothing.
     * </p>
     */
    public final void clearSubtasks() {
        if (!atomicTask) {
            this.getSubtasks().clear();
        }
    }

    // -------------------------------------------------------------------------
    // Override methods for equality and comparison
    // -------------------------------------------------------------------------
    
    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Task other = (Task) obj;
        return Objects.equals(this.properties, other.properties) 
                && Objects.equals(this.getSubtasks(), other.getSubtasks());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(this.properties, getSubtasks());
    }

    /**
     * Compares this task with another task based on their date and time.
     * 
     * @param other The other task to compare to.
     * @return A negative integer, zero, or a positive integer as this task is earlier, 
     *         equal to, or later than the other task.
     */
    @Override
    public int compareTo(Task other) {
        LocalDate thisDate = (LocalDate) this.getProperty(DATE);
        LocalDate otherDate = (LocalDate) other.getProperty(DATE);
        
        LocalTime thisTime = (LocalTime) this.getPropertyOrDefault(TIME, LocalTime.of(0, 0));
        LocalTime otherTime = (LocalTime) other.getPropertyOrDefault(TIME, LocalTime.of(0, 0));

        return LocalDateTime.of(thisDate, thisTime)
                .compareTo(LocalDateTime.of(otherDate, otherTime));
    }
}
