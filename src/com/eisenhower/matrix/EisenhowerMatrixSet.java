package com.eisenhower.matrix;

import com.eisenhower.util.Quadrant;
import java.util.*;

/**
 * An Eisenhower matrix where each quadrant contains a {@link Set} of tasks.
 * <p>
 * This implementation does not allow duplicate tasks in the matrix and is preferred over {@link EisenhowerMatrixList}.
 *
 * @param <T> Any class representing a task to be added to the Eisenhower matrix.
 */
public class EisenhowerMatrixSet<T extends Comparable<T>> extends AbstractEisenhowerMatrix<T> {

    /**
     * Initializes the Eisenhower matrix with empty sets for each quadrant.
     * This method is called during the construction of the matrix.
     */
    @Override
    protected void initializeMatrix() {
        super.put(Quadrant.DO_IT_NOW, new HashSet<>());
        super.put(Quadrant.DELEGATE_OR_OPTIMIZE_IT, new HashSet<>());
        super.put(Quadrant.SCHEDULE_IT, new HashSet<>());
        super.put(Quadrant.ELIMINATE_IT, new HashSet<>());
    }

    /**
     * Returns the type of collections used in this matrix, which is {@link Set}.
     * 
     * @return {@link Set} class type.
     */
    @Override
    public final Class<?> getTypeOfCollections() {
        return Set.class;
    }
    
    // -------------------------------------------------------------------------

    /**
     * Adds a task to the specified quadrant, if not already present.
     * 
     * @param task     the task to be added.
     * @param quadrant the quadrant in which to add the task.
     * @return {@code true} if the task was added, {@code false} otherwise.
     * @throws NullPointerException if task or quadrant is {@code null}.
     */
    @Override
    public final boolean addTask(T task, Quadrant quadrant) {
        Objects.requireNonNull(task, "Task cannot be null.");
        Objects.requireNonNull(quadrant, "Quadrant cannot be null.");
        
        if (this.containsTask(task)) {
            return false;
        }
        Collection<T> tasksInQuadrant = this.getTasks(quadrant);
        return tasksInQuadrant.add(task);
    }
    
    // -------------------------------------------------------------------------

    /**
     * Removes a task from the specified quadrant.
     * 
     * @param task     the task to be removed.
     * @param quadrant the quadrant from which to remove the task.
     * @return {@code true} if the task was removed, {@code false} otherwise.
     * @throws NullPointerException if task or quadrant is {@code null}.
     */
    @Override
    public final boolean removeTask(T task, Quadrant quadrant) {
        Objects.requireNonNull(task, "Task cannot be null.");
        Objects.requireNonNull(quadrant, "Quadrant cannot be null.");
        
        Collection<T> tasksInQuadrant = this.getTasks(quadrant);
        return tasksInQuadrant.remove(task);
    }
    
    // -------------------------------------------------------------------------

    /**
     * Determines if the specified task is in an urgent quadrant.
     * 
     * @param task the task to check.
     * @return {@code true} if the task is in an urgent quadrant, {@code false} otherwise.
     * @throws NullPointerException if task is {@code null}.
     * @throws NoSuchElementException if the task is not present in the matrix.
     */
    public final boolean isTaskUrgent(T task) {
        Objects.requireNonNull(task, "Task cannot be null.");
        
        if (!super.containsTask(task)) {
            throw new NoSuchElementException("Task not present anywhere in this Eisenhower matrix.");
        }
        Quadrant quadrant = super.getQuadrant(task);
        return quadrant.isUrgent();
    }

    /**
     * Determines if the specified task is in an important quadrant.
     * 
     * @param task the task to check.
     * @return {@code true} if the task is in an important quadrant, {@code false} otherwise.
     * @throws NullPointerException if task is {@code null}.
     * @throws NoSuchElementException if the task is not present in the matrix.
     */
    public final boolean isTaskImportant(T task) {
        Objects.requireNonNull(task, "Task cannot be null.");
        
        if (!super.containsTask(task)) {
            throw new NoSuchElementException("Task not present anywhere in this Eisenhower matrix.");
        }
        Quadrant quadrant = super.getQuadrant(task);
        return quadrant.isImportant();
    }
}
