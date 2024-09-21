package com.eisenhower.matrix;

import com.eisenhower.util.EQuadrantsSorting;
import com.eisenhower.util.Quadrant;
import static com.eisenhower.util.Quadrant.ELIMINATE_IT;
import java.util.*;

/**
 * Represents an Eisenhower Matrix, a productivity tool that helps organize tasks based on their 
 * urgency and importance. The matrix categorizes tasks into 4 quadrants:
 * 
 * <ol>
 *   <li><b>Do It Now</b> (Urgent and Important)</li>
 *   <li><b>Schedule It</b> (Urgent but Not Important)</li>
 *   <li><b>Delegate or Optimize It</b> (Not Urgent but Important)</li>
 *   <li><b>Eliminate It</b> (Not Urgent and Not Important)</li>
 * </ol>
 * 
 * This interface defines the essential operations for managing tasks within the matrix, 
 * including adding, retrieving, sorting, and removing tasks.
 *
 * @param <T> the type of task which must implement {@link Comparable} to allow sorting.
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
     * Converts the matrix to a 2x2 array representation, where each element is a 
     * collection of tasks corresponding to a specific quadrant.
     *
     * @return a 2x2 array of collections of tasks.
     */
    Collection<T>[][] toMatrix();

    /**
     * Retrieves the type of collection used to store tasks within the 4 quadrants. 
     * More specifically, a {@link Class} which can be {@link List} or {@link Set}.
     * It depends on the chosen implementation for {@link EisenhowerMatrix}.
     *
     * @return the Class object representing the type of collections.
     */
    Class<?> getImplementingCollectionType();
    
    // -------------------------------------------------------------------------

    /**
     * Adds a task to the specified quadrant in the matrix.
     *
     * @param task     the task to be added.
     * @param quadrant the quadrant where the task should be added.
     * @return {@code true} if the task was added successfully, {@code false} otherwise.
     * @throws NullPointerException if {@code task} or {@code quadrant} is {@code null}.
     */
    boolean addTask(T task, Quadrant quadrant);

    /**
     * Adds a task to the specified quadrant, determined by urgency and importance.
     *
     * @param task       the task to be added.
     * @param urgent     indicates if the task is urgent.
     * @param important  indicates if the task is important.
     * @return {@code true} if the task was added successfully, {@code false} otherwise.
     * @throws NullPointerException if {@code task} is {@code null}.
     * @see Quadrant#getQuadrant(boolean, boolean)
     */
    boolean addTask(T task, boolean urgent, boolean important);

    /**
     * Adds all tasks to the specified quadrant in the matrix.
     *
     * @param quadrant the quadrant where tasks should be added.
     * @param tasks    the collection of tasks to be added.
     * @return {@code true} if the tasks were added successfully, {@code false} otherwise.
     * @throws NullPointerException if {@code quadrant} or {@code tasks} is {@code null}.
     */
    boolean addAllTasks(Quadrant quadrant, Collection<? extends T> tasks);

    /**
     * Adds all tasks to the specified quadrant, determined by urgency and importance.
     *
     * @param urgent     indicates if the tasks are urgent.
     * @param important  indicates if the tasks are important.
     * @param tasks      the collection of tasks to be added.
     * @return {@code true} if the tasks were added successfully, {@code false} otherwise.
     * @throws NullPointerException if {@code tasks} is {@code null}.
     * @see Quadrant#getQuadrant(boolean, boolean)
     */
    boolean addAllTasks(boolean urgent, boolean important, Collection<? extends T> tasks);

    /**
     * Adds all tasks from a map of quadrants to collections of tasks to the matrix.
     *
     * @param eisenhowerMap a map where keys are quadrants and values are collections of tasks.
     * @return {@code true} if the tasks were added successfully, {@code false} otherwise.
     * @throws NullPointerException if {@code eisenhowerMap} is {@code null}.
     */
    boolean addAllTasks(Map<Quadrant, Collection<? extends T>> eisenhowerMap);

    /**
     * Adds a task to the specified quadrant only if it is not already present in that quadrant.
     *
     * @param task     the task to be added.
     * @param quadrant the quadrant where the task should be added.
     * @throws NullPointerException if {@code task} or {@code quadrant} is {@code null}.
     */
    void addTaskIfAbsentInQuadrant(T task, Quadrant quadrant);

    /**
     * Adds a task to the specified quadrant (determined by urgency and importance) 
     * only if it is not already present.
     *
     * @param task       the task to be added.
     * @param urgent     indicates if the task is urgent.
     * @param important  indicates if the task is important.
     * @throws NullPointerException if {@code task} is {@code null}.
     * @see Quadrant#getQuadrant(boolean, boolean)
     */
    void addTaskIfAbsentInQuadrant(T task, boolean urgent, boolean important);

    /**
     * Adds a task to the matrix, only if it is not already present in any quadrant.
     *
     * @param task     the task to be added.
     * @param quadrant the quadrant where the task should be added if absent.
     * @throws NullPointerException if {@code task} or {@code quadrant} is {@code null}.
     */
    void addTaskIfAbsentInMatrix(T task, Quadrant quadrant);

    /**
     * Adds a task to the matrix, only if it is not already present in any quadrant, 
     * with quadrant determined by urgency and importance.
     *
     * @param task       the task to be added.
     * @param urgent     indicates if the task is urgent.
     * @param important  indicates if the task is important.
     * @throws NullPointerException if {@code task} is {@code null}.
     * @see Quadrant#getQuadrant(boolean, boolean)
     */
    void addTaskIfAbsentInMatrix(T task, boolean urgent, boolean important);

    // -------------------------------------------------------------------------

    /**
     * Retrieves all tasks from the specified quadrant.
     *
     * @param quadrant the quadrant from which to retrieve tasks.
     * @return a collection of tasks in the specified quadrant.
     * @throws NullPointerException if {@code quadrant} is {@code null}.
     */
    Collection<T> getTasks(Quadrant quadrant);

    /**
     * Retrieves all tasks from the specified quadrant determined by urgency and importance.
     *
     * @param urgent     indicates if the tasks are urgent.
     * @param important  indicates if the tasks are important.
     * @return a collection of tasks in the specified quadrant.
     * @see Quadrant#getQuadrant(boolean, boolean)
     */
    Collection<T> getTasks(boolean urgent, boolean important);

    /**
     * Retrieves and sorts all tasks from the specified quadrant using their natural ordering.
     *
     * @param quadrant the quadrant from which to retrieve and sort tasks.
     * @return a list of sorted tasks in the specified quadrant.
     * @throws NullPointerException if {@code quadrant} is {@code null}.
     * @see Collections#sort(java.util.List)
     */
    List<T> getTasksSorted(Quadrant quadrant);

    /**
     * Retrieves and sorts all tasks from the specified quadrant (determined by 
     * urgency and importance) using their natural ordering.
     *
     * @param urgent     indicates if the tasks are urgent.
     * @param important  indicates if the tasks are important.
     * @return a list of sorted tasks in the specified quadrant.
     * @see Collections#sort(java.util.List)
     * @see Quadrant#getQuadrant(boolean, boolean)
     */
    List<T> getTasksSorted(boolean urgent, boolean important);

    /**
     * Retrieves and sorts all tasks from the specified quadrant, using the given comparator.
     *
     * @param quadrant   the quadrant from which to retrieve and sort tasks.
     * @param comparator the comparator used to sort the tasks.
     * @return a list of sorted tasks in the specified quadrant.
     * @throws NullPointerException if {@code quadrant} or {@code comparator} is {@code null}.
     * @see Collections#sort(java.util.List, java.util.Comparator)
     */
    List<T> getTasksSorted(Quadrant quadrant, Comparator<T> comparator);

    /**
     * Retrieves and sorts all tasks from the specified quadrant (determined by 
     * urgency and importance) using the given comparator.
     *
     * @param urgent     indicates if the tasks are urgent.
     * @param important  indicates if the tasks are important.
     * @param comparator the comparator used to sort the tasks.
     * @return a list of sorted tasks in the specified quadrant.
     * @see Collections#sort(java.util.List, java.util.Comparator)
     * @see Quadrant#getQuadrant(boolean, boolean)
     */
    List<T> getTasksSorted(boolean urgent, boolean important, Comparator<T> comparator);

    /**
     * Retrieves all tasks from all quadrants.
     *
     * @return a set of all tasks in the matrix.
     */
    Set<T> getAllTasks();

    /**
     * Retrieves and sorts all tasks from all quadrants, using their natural ordering.
     * Converts a matrix-based tasks system into a unified linear sorting.
     * <p>
     * More specifically, it is a double-level sorting:
     * <ul>
     *     <li> Sorts tasks within each quadrant. This step creates 4 different lists. 
     *     <li> Merges each quadrant' list together, ordering them according to the specified {@code quadrantsOrdering}.
     * </ul>
     *
     * @param quadrantsOrdering the criterior used to sort quadrants, giving priority to importance over urgency, or vice-versa.
     * @return a list of all tasks in the matrix, sorted by quadrant priority and task order.
     * @see Collections#sort(java.util.List) 
     * @see EQuadrantsSorting#IMPORTANCE_OVER_URGENCY
     * @see EQuadrantsSorting#URGENCY_OVER_IMPORTANCE
     */
    List<T> getAllTasksSorted(EQuadrantsSorting quadrantsOrdering);

    /**
     * Retrieves and sorts all tasks from all quadrants, using the given comparator for tasks.
     * Converts a matrix-based tasks system into a unified linear sorting.
     * <p>
     * More specifically, it is a double-level sorting:
     * <ul>
     *     <li> Sorts tasks within each quadrant, using the given comparator. 
     *          This step creates 4 different lists. 
     *     <li> Merges each quadrant' list together, ordering them according to the specified {@code quadrantsOrdering}.
     * </ul>
     *
     * @param tasksComparator the tasksComparator used to sort the tasks.
     * @return a list of all tasks in the matrix, sorted by quadrant priority and using the given tasksComparator.
     * @throws NullPointerException if {@code tasksComparator} is {@code null}.
     * @see Collections#sort(java.util.List, java.util.Comparator) 
     * @see EQuadrantsSorting#IMPORTANCE_OVER_URGENCY
     * @see EQuadrantsSorting#URGENCY_OVER_IMPORTANCE
     */
    List<T> getAllTasksSorted(Comparator<T> tasksComparator, EQuadrantsSorting quadrantsOrdering);

    /**
     * Retrieves all tasks from all quadrants, using individual comparators for each quadrant.
     * Converts a matrix-based tasks system into a unified linear sorting.
     * <p>
     * More specifically, it is a double-level sorting:
     * <ul>
     *     <li> Sorts tasks within each quadrant, using a specific comparator for each quandrant.
     *          This step creates 4 different lists.
     *     <li> Merges each quadrant' list together, ordering them according to the specified {@code quadrantsOrdering}.
     * </ul>
     *
     * @param comparators a map of quadrants to their respective comparators.
     * @return a list of all tasks in the matrix, sorted by quadrant priority and using the provided comparators.
     * @throws UnsupportedOperationException if the map doesn't contain comparators for all four quadrants.
     * @throws NullPointerException if {@code comparators} is {@code null}.
     * @see Collections#sort(java.util.List, java.util.Comparator) 
     * @see EQuadrantsSorting#IMPORTANCE_OVER_URGENCY
     * @see EQuadrantsSorting#URGENCY_OVER_IMPORTANCE
     */
    List<T> getAllTasksSorted(Map<Quadrant, Comparator<T>> comparators, EQuadrantsSorting quadrantsOrdering);

    /**
     * Retrieves the quadrant where the given task is located.
     * More specifically, the first quadrant found according to the order specified by:
     * {@link Quadrant#linearPrioritySorting()}.
     *
     * @param task the task for which to find the quadrant.
     * @return the quadrant containing the task, or {@code null} if not found.
     * @throws NullPointerException if {@code task} is {@code null}.
     * @see Quadrant#linearPrioritySorting() 
     */
    Quadrant getQuadrant(T task);

    /**
     * Retrieves all quadrants where the given task is located.
     *
     * @param task the task for which to find the quadrants.
     * @return a set of quadrants containing the task.
     * @throws NullPointerException if {@code task} is {@code null}.
     */
    Set<Quadrant> getQuadrants(T task);

    // -------------------------------------------------------------------------

    /**
     * Checks if a task is present in any quadrant.
     *
     * @param task the task to check.
     * @return {@code true} if the task is present, {@code false} otherwise.
     * @throws NullPointerException if {@code task} is {@code null}.
     */
    boolean containsTask(T task);

    /**
     * Checks if a task is present in the specified quadrant.
     *
     * @param task     the task to check.
     * @param quadrant the quadrant to check in.
     * @return {@code true} if the task is present in the quadrant, {@code false} otherwise.
     * @throws NullPointerException if {@code task} or {@code quadrant} is {@code null}.
     */
    boolean containsTask(T task, Quadrant quadrant);

    /**
     * Checks if a task is present in the specified quadrant (determined by urgency and importance).
     *
     * @param task       the task to check.
     * @param urgent     indicates if the task is urgent.
     * @param important  indicates if the task is important.
     * @return {@code true} if the task is present in the quadrant, {@code false} otherwise.
     * @throws NullPointerException if {@code task} is {@code null}.
     * @see Quadrant#getQuadrant(boolean, boolean)
     */
    boolean containsTask(T task, boolean urgent, boolean important);

    // -------------------------------------------------------------------------
    
    /**
     * Removes the first occurrence of a task, from the specified quadrant.
     * 
     * @param task     the task to be removed.
     * @param quadrant the quadrant from which to remove the task.
     * @return {@code true} if the task was removed, {@code false} otherwise.
     * @throws NullPointerException if task or quadrant is {@code null}.
     * @see Quadrant#getQuadrant(boolean, boolean)
     */
    boolean removeTask(T task, Quadrant quadrant);
    
    /**
     * Removes the first occurrence of a task, from the specified quadrant.
     * 
     * @param task     the task to be removed.
     * @param urgent     indicates if the task is urgent.
     * @param important  indicates if the task is important.
     * @return {@code true} if the task was removed, {@code false} otherwise.
     * @throws NullPointerException if task or quadrant is {@code null}.
     * @see Quadrant#getQuadrant(boolean, boolean)
     */
    boolean removeTask(T task, boolean urgent, boolean important);

    /**
     * Removes all copies of this task, from the specified quadrant.
     * 
     * <p>More specifically, we mean <i>copies</i> all "identical" objects according to 
     * the {@link Object#equals(java.lang.Object)} function.</p>
     *
     * @param task     the task to be removed.
     * @param quadrant the quadrant from which the task should be removed.
     * @return {@code true} if the task was removed successfully, {@code false} otherwise.
     * @throws NullPointerException if {@code task} or {@code quadrant} is {@code null}.
     */
    boolean removeTaskOccurrences(T task, Quadrant quadrant);

    /**
     * Removes all copies of this task, from the specified quadrant (determined by urgency and importance).
     * 
     * <p>More specifically, we mean <i>copies</i> all "identical" objects according to 
     * the {@link Object#equals(java.lang.Object)} function.</p>
     *
     * @param task       the task to be removed.
     * @param urgent     indicates if the task is urgent.
     * @param important  indicates if the task is important.
     * @return {@code true} if the task was removed successfully, {@code false} otherwise.
     * @throws NullPointerException if {@code task} is {@code null}.
     * @see Object#equals(java.lang.Object) 
     * @see Quadrant#getQuadrant(boolean, boolean)
     */
    boolean removeTaskOccurrences(T task, boolean urgent, boolean important);

    /**
     * Removes all copies of this task, from all quadrants.
     * 
     * <p>More specifically, we mean <i>copies</i> all "identical" objects according to 
     * the {@link Object#equals(java.lang.Object)} function.</p>
     *
     * @param task the task to be removed.
     * @return {@code true} if the task was removed successfully from at least one quadrant, {@code false} otherwise.
     * @throws NullPointerException if {@code task} is {@code null}.
     */
    boolean removeTaskOccurrences(T task);

    // -------------------------------------------------------------------------

    /**
     * Clears all tasks from the specified quadrant.
     *
     * @param quadrant the quadrant to be cleared.
     * @return a copy of this Eisenhower Matrix with the specified quadrant cleared.
     * @throws NullPointerException if {@code quadrant} is {@code null}.
     */
    EisenhowerMatrix<T> clearQuadrant(Quadrant quadrant);

    /**
     * Clears all tasks from the specified quadrant determined by urgency and importance.
     *
     * @param urgent     indicates if the tasks are urgent.
     * @param important  indicates if the tasks are important.
     * @return a copy of this Eisenhower Matrix with the specified quadrant cleared.
     * @see Quadrant#getQuadrant(boolean, boolean)
     */
    EisenhowerMatrix<T> clearQuadrant(boolean urgent, boolean important);
    
    /**
     * Clears all tasks from the {@link Quadrant#ELIMINATE_IT} quadrant,
     * which are only a waste of time, energy and effort: not important, nor urgent.
     * 
     * @return a copy of this Eisenhower Matrix with the specified quadrant cleared.
     * @see #clearQuadrant(com.eisenhower.util.Quadrant) 
     * @see Quadrant#getQuadrant(boolean, boolean)
     */
    default EisenhowerMatrix<T> clearUseless() {
        return this.clearQuadrant(ELIMINATE_IT);
    }

    /**
     * Clears all tasks from the entire matrix, effectively resetting it.
     */
    EisenhowerMatrix<T> clearAllTasks();
}
