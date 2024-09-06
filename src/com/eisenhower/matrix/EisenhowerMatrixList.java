package com.eisenhower.matrix;

import com.eisenhower.util.Quadrant;
import java.util.*;

/**
 * An Eisenhower matrix where each quadrant contains a {@link List} of tasks.
 * <p>
 * This implementation allows duplicated tasks both across quadrants and within a single quadrant.
 *
 * @param <T> Any class representing a task to be added to the Eisenhower matrix.
 */
public class EisenhowerMatrixList<T extends Comparable<T>> extends AbstractEisenhowerMatrix<T> implements EisenhowerMatrix<T> {

    /**
     * Initializes the matrix by setting up each quadrant with an empty {@link List}.
     * This method is called during the construction of the matrix.
     */
    @Override
    protected void initializeMatrix() {
        super.put(Quadrant.DO_IT_NOW, new ArrayList<>());
        super.put(Quadrant.DELEGATE_OR_OPTIMIZE_IT, new ArrayList<>());
        super.put(Quadrant.SCHEDULE_IT, new ArrayList<>());
        super.put(Quadrant.ELIMINATE_IT, new ArrayList<>());
    }

    /**
     * Returns the type of collection used in this implementation, which is {@link List}.
     *
     * @return {@link List} class object.
     */
    @Override
    public final Class<?> getTypeOfCollections() {
        return List.class;
    }

    // -------------------------------------------------------------------------

    /**
     * Adds a task to the specified quadrant. Duplicates are allowed in this implementation.
     *
     * @param task     The task to be added.
     * @param quadrant The quadrant where the task will be added.
     * @return {@code true} if the task was added successfully.
     * @throws NullPointerException if the task or quadrant is {@code null}.
     */
    @Override
    public final boolean addTask(T task, Quadrant quadrant) {
        Objects.requireNonNull(task, "Task cannot be null.");
        Objects.requireNonNull(quadrant, "Quadrant cannot be null.");

        Collection<T> tasksInQuadrant = this.getTasks(quadrant);
        return tasksInQuadrant.add(task);
    }

    // -------------------------------------------------------------------------

    /**
     * Retrieves a task from the specified quadrant by its index.
     *
     * @param quadrant The quadrant to retrieve the task from.
     * @param index    The index of the task.
     * @return The task at the specified index.
     * @throws NullPointerException     if the quadrant is {@code null}.
     * @throws IndexOutOfBoundsException if the index is out of range.
     */
    public final T getTask(Quadrant quadrant, int index) {
        Objects.requireNonNull(quadrant, "Quadrant cannot be null.");

        List<T> tasksInQuadrant = (List<T>) this.getTasks(quadrant);
        try {
            return tasksInQuadrant.get(index);
        } catch (IndexOutOfBoundsException ex) {
            throw ex;
        }
    }

    /**
     * Retrieves a task from the quadrant determined by its urgency and importance.
     *
     * @param urgent    Indicates if the task is urgent.
     * @param important Indicates if the task is important.
     * @param index     The index of the task.
     * @return The task at the specified index.
     * @throws IndexOutOfBoundsException if the index is out of range.
     */
    public final T getTask(boolean urgent, boolean important, int index) {
        try {
            return this.getTask(Quadrant.getQuadrant(urgent, important), index);
        } catch (IndexOutOfBoundsException ex) {
            throw ex;
        }
    }

    
    public final List<T> sublist(Quadrant quadrant, int fromIndex, int toIndex) {
        if (quadrant == null) {
            throw new NullPointerException("Quadrant is null.");
        }
        
        List<T> tasksInQuadrant = (List<T>) this.getTasks(quadrant);
        try {
            return tasksInQuadrant.subList(fromIndex, toIndex);
        } catch (IndexOutOfBoundsException ex) {
            throw ex;
        }
    }
    
    public final List<T> sublist(boolean urgent, boolean important, int fromIndex, int toIndex) {
        try {
            return this.sublist(Quadrant.getQuadrant(urgent, important), fromIndex, toIndex);
        } catch (IndexOutOfBoundsException ex) {
            throw ex;
        }
    }
    
    // -------------------------------------------------------------------------

    /**
     * Replaces a task at the specified index in the given quadrant.
     *
     * @param task     The new task to be set.
     * @param quadrant The quadrant where the task will be replaced.
     * @param index    The index of the task to replace.
     * @return The task previously at the specified position.
     * @throws NullPointerException if the task or quadrant is {@code null}.
     * @throws IndexOutOfBoundsException if the index is out of range.
     */
    public final T setTask(T task, Quadrant quadrant, int index) {
        Objects.requireNonNull(task, "Task cannot be null.");
        Objects.requireNonNull(quadrant, "Quadrant cannot be null.");

        List<T> tasksInQuadrant = (List<T>) this.getTasks(quadrant);
        try {
            return tasksInQuadrant.set(index, task);
        } catch (RuntimeException ex) {
            throw ex;
        }
    }

    /**
     * Replaces a task at the specified index, determined by urgency and importance.
     *
     * @param task      The new task to be set.
     * @param urgent    Indicates if the task is urgent.
     * @param important Indicates if the task is important.
     * @param index     The index of the task to replace.
     * @return The task previously at the specified position.
     * @throws NullPointerException if the task is {@code null}.
     */
    public final T setTask(T task, boolean urgent, boolean important, int index) {
        try {
            return this.setTask(task, Quadrant.getQuadrant(urgent, important), index);
        } catch (RuntimeException ex) {
            throw ex;
        }
    }

    // -------------------------------------------------------------------------

    /**
     * Removes all instances of the specified task from the given quadrant.
     * This implementation allows duplicate tasks, so it removes all occurrences.
     *
     * @param task     The task to be removed.
     * @param quadrant The quadrant from which the task will be removed.
     * @return {@code true} if at least one instance of the task was removed, otherwise {@code false}.
     * @throws NullPointerException if the task or quadrant is {@code null}.
     * @see Object#equals(java.lang.Object) 
     */
    @Override
    public final boolean removeTask(T task, Quadrant quadrant) {
        Objects.requireNonNull(task, "Task cannot be null.");
        Objects.requireNonNull(quadrant, "Quadrant cannot be null.");

        Collection<T> tasksInQuadrant = this.getTasks(quadrant);
        int count = this.countEqualInQuadrant(task, quadrant);
        if (count == 0) {
            return false;
        }
        boolean removed = true;
        for (int i = 0; i < count; i++) {
            if (!tasksInQuadrant.remove(task)) {
                removed = false;
            }
        }
        return removed;
    }

    /**
     * Counts the number of occurrences of the given task in the specified quadrant.
     *
     * @param task     The task to count.
     * @param quadrant The quadrant to search in.
     * @return The number of occurrences of the task in the quadrant.
     */
    private int countEqualInQuadrant(T task, Quadrant quadrant) {
        Collection<T> tasksInQuadrant = this.getTasks(quadrant);
        int count = 0;
        for (T t : tasksInQuadrant) {
            if (Objects.equals(task, t)) {
                count++;
            }
        }
        return count;
    }
}
