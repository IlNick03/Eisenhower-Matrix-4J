package com.eisenhower.matrix;

import com.eisenhower.util.Quadrant;
import java.util.*;

/**
 * Represents an Eisenhower matrix, a productivity tool that helps organize tasks based on urgency and importance.
 * This interface defines the basic operations for managing tasks within the matrix.
 *
 * @param <T> Type of task which must implement {@link Comparable} to allow sorting.
 * @see EisenhowerMatrixSet
 * @see EisenhowerMatrixList
 */
public interface EisenhowerMatrix<T extends Comparable<T>> {

    /**
     * Converts the matrix to a map representation, where the keys are quadrants and
     * the values are collections of tasks.
     *
     * @return a map of quadrants to collections of tasks.
     */
    Map<Quadrant, Collection<T>> toMap();

    /**
     * Converts the matrix to a 2x2 array representation, where each element is a collection of tasks
     * corresponding to a specific quadrant.
     *
     * @return a 2x2 array of collections of tasks.
     */
    Collection<T>[][] toMatrix();

    @Override
    boolean equals(Object obj);

    @Override
    int hashCode();

    /**
     * Adds a task to the specified quadrant in the matrix.
     *
     * @param task     the task to be added.
     * @param quadrant the quadrant where the task should be added.
     * @return true if the task was added successfully, false otherwise.
     */
    boolean putTask(T task, Quadrant quadrant);

    /**
     * Adds all tasks to the specified quadrant in the matrix.
     *
     * @param quadrant the quadrant where tasks should be added.
     * @param tasks    the tasks to be added.
     * @return true if the tasks were added successfully, false otherwise.
     */
    boolean putAll(Quadrant quadrant, Collection<? extends T> tasks);

    /**
     * Adds all tasks from a map of quadrants to collections of tasks to the matrix.
     *
     * @param eisenhowerMap a map where keys are quadrants and values are collections of tasks.
     * @return true if the tasks were added successfully, false otherwise.
     */
    boolean putAll(Map<Quadrant, Collection<? extends T>> eisenhowerMap);

    /**
     * Adds a task to the specified quadrant only if it is not already present in the quadrant.
     *
     * @param task     the task to be added.
     * @param quadrant the quadrant where the task should be added.
     */
    void putIfAbsentInQuadrant(T task, Quadrant quadrant);

    /**
     * Adds a task to the matrix only if it is not already present in any quadrant.
     *
     * @param task     the task to be added.
     * @param quadrant the quadrant where the task should be added if absent.
     */
    void putIfAbsentInMatrix(T task, Quadrant quadrant);

    /**
     * Retrieves all tasks from the specified quadrant.
     *
     * @param quadrant the quadrant from which to retrieve tasks.
     * @return a collection of tasks in the specified quadrant.
     */
    Collection<T> getTasks(Quadrant quadrant);

    /**
     * Retrieves and sorts all tasks from the specified quadrant using the natural ordering.
     *
     * @param quadrant the quadrant from which to retrieve and sort tasks.
     * @return a list of sorted tasks in the specified quadrant.
     */
    List<T> getTasksSorted(Quadrant quadrant);

    /**
     * Retrieves and sorts all tasks from the specified quadrant using the given comparator.
     *
     * @param quadrant  the quadrant from which to retrieve and sort tasks.
     * @param comparator the comparator used to sort the tasks.
     * @return a list of sorted tasks in the specified quadrant.
     */
    List<T> getTasksSorted(Quadrant quadrant, Comparator<T> comparator);

    /**
     * Retrieves all tasks from all quadrants.
     *
     * @return a set of all tasks in the matrix.
     */
    Set<T> getAllTasks();

    /**
     * Retrieves and sorts all tasks from all quadrants using the natural ordering.
     *
     * @return a list of all tasks in the matrix sorted by quadrant priority and task order.
     */
    List<T> getAllTasksSorted();

    /**
     * Retrieves and sorts all tasks from all quadrants using the given comparator.
     *
     * @param comparator the comparator used to sort the tasks.
     * @return a list of all tasks in the matrix sorted by quadrant priority and using the given comparator.
     */
    List<T> getAllTasksSorted(Comparator<T> comparator);

    /**
     * Retrieves the quadrant where the given task is located.
     *
     * @param task the task for which to find the quadrant.
     * @return the quadrant containing the task, or null if not found.
     */
    Quadrant getQuadrant(T task);

    /**
     * Retrieves all quadrants where the given task is located.
     *
     * @param task the task for which to find the quadrants.
     * @return a set of quadrants containing the task.
     */
    Set<Quadrant> getQuadrants(T task);

    /**
     * Checks if a task is present in any quadrant.
     *
     * @param task the task to check.
     * @return true if the task is present, false otherwise.
     */
    boolean containsTask(T task);

    /**
     * Checks if a task is present in the specified quadrant.
     *
     * @param task     the task to check.
     * @param quadrant the quadrant to check in.
     * @return true if the task is present in the quadrant, false otherwise.
     */
    boolean containsTask(T task, Quadrant quadrant);

    /**
     * Removes a task from the specified quadrant.
     *
     * @param task     the task to be removed.
     * @param quadrant the quadrant from which the task should be removed.
     * @return true if the task was removed successfully, false otherwise.
     */
    boolean removeTask(T task, Quadrant quadrant);

    /**
     * Removes a task from all quadrants.
     *
     * @param task the task to be removed.
     * @return true if the task was removed successfully, false otherwise.
     */
    boolean removeTask(T task);

    /**
     * Clears all tasks from the specified quadrant.
     *
     * @param quadrant the quadrant to be cleared.
     */
    void clearQuadrant(Quadrant quadrant);

    /**
     * Clears all tasks from the entire matrix.
     */
    void clearAllMatrix();
}