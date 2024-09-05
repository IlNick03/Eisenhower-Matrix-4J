package com.eisenhower.matrix;

import com.eisenhower.util.Quadrant;
import java.util.*;

/**
 * Abstract implementation of the {@link EisenhowerMatrix} interface.
 * Provides a common base for list-based ({@link EisenhowerMatrixList}) and 
 * set-based ({@link EisenhowerMatrixSet}) matrix implementations.
 *
 * @param <T> The type of task stored in the matrix.
 */
public abstract class AbstractEisenhowerMatrix<T extends Comparable<T>> implements EisenhowerMatrix<T> {
    
    private Map<Quadrant, Collection<T>> quadrants = new HashMap<>();
    
    /**
     * Constructs an instance of {@link AbstractEisenhowerMatrix} and initializes the matrix.
     * @see #initializeMatrix() 
     */
    public AbstractEisenhowerMatrix() {
        initializeMatrix();
    }
    
    /**
     * Initializes the matrix: each quadrant is associated with an empty {@link Collection}
     * of tasks. 
     * Concrete subclasses must provide their own implementation.
     */
    protected abstract void initializeMatrix();
    
    /**
     * Puts a {@link Collection} of tasks in the given Eisenhower' quadrant.
     * This method should be used by concrete classes for initialize each matrix' quadrant.
     * 
     * @apiNote For containing tasks within, concrete classes <b>must</b> choose the same 
     *          implementation of {@link Collection} for all quadrants, for guarantee 
     *          consistency in operations and equality in performance.
     * 
     * @param key
     * @param tasks
     * @return 
     */
    protected final Collection<T> put(Quadrant key, Collection<T> tasks) {
        return quadrants.put(key, tasks);
    }
    
    @Override
    public final Map<Quadrant, Collection<T>> toMap() {
        return new HashMap<>(quadrants);
    }
    
    @Override
    public final Collection<T>[][] toMatrix() {
        Collection<T>[][] matrix = new Collection[2][2];
//        matrix[0][0] = quadrants.get(Quadrant.DO_IT_NOW);
//        matrix[0][1] = quadrants.get(Quadrant.DELEGATE_OR_OPTIMIZE_IT);
//        matrix[1][0] = quadrants.get(Quadrant.SCHEDULE_IT);
//        matrix[1][1] = quadrants.get(Quadrant.ELIMINATE_IT);

        for (Quadrant quadrant : Quadrant.linearPrioritySorting()) {
            int invertedUrgentNumber = quadrant.isUrgent() ? 0 : 1;
            int invertedImportantNumber = quadrant.isImportant() ? 0 : 1;
            
            matrix[invertedUrgentNumber][invertedImportantNumber] = this.getTasks(quadrant);
        }
        return matrix;
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
        final AbstractEisenhowerMatrix<T> other = (AbstractEisenhowerMatrix<T>) obj;
        return Objects.equals(this.quadrants, other.quadrants);
    }
    
    @Override
    public final int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.quadrants);
        return hash;
    }
    
    // -------------------------------------------------------------------------
    
    @Override
    public final boolean putTask(T task, boolean urgent, boolean important) {
        return this.putTask(task, Quadrant.getQuadrant(urgent, important));
    }
    
    @Override
    public final boolean putAll(boolean urgent, boolean important, Collection<? extends T> tasks) {
        return this.putAll(Quadrant.getQuadrant(urgent, important), tasks);
    }
    
    @Override
    public final void putIfAbsentInQuadrant(T task, Quadrant quadrant) {
        if (!this.containsTask(task, quadrant)) {
            Collection<T> tasksInThisQuadrant = this.getTasks(quadrant);
            if (tasksInThisQuadrant == null) {
                throw new NullPointerException("This quadrant hasn't been initialized with a proper collection of tasks.");
            }
            tasksInThisQuadrant.add(task);
        }
    }
    
    @Override
    public final void putIfAbsentInQuadrant(T task, boolean urgent, boolean important) {
        this.putIfAbsentInQuadrant(task, Quadrant.getQuadrant(urgent, important));
    }
    
    @Override
    public final void putIfAbsentInMatrix(T task, Quadrant quadrant) {
        if (!this.containsTask(task)) {
            Collection<T> tasksInThisQuadrant = this.getTasks(quadrant);
            if (tasksInThisQuadrant == null) {
                throw new NullPointerException("This quadrant hasn't been initialized with a proper collection of tasks.");
            }
            tasksInThisQuadrant.add(task);
        }
    }
    
    @Override
    public final void putIfAbsentInMatrix(T task, boolean urgent, boolean important) {
        this.putIfAbsentInMatrix(task, Quadrant.getQuadrant(urgent, important));
    }
    
    @Override
    public final boolean putAll(Quadrant quadrant, Collection<? extends T> tasks) {
        Collection<T> tasksInThisQuadrant = this.getTasks(quadrant);
        if (tasksInThisQuadrant == null) {
            throw new NullPointerException("This quadrant hasn't been initialized with a proper collection of tasks.");
        }
        return tasksInThisQuadrant.addAll(tasks);
    }
    
    @Override
    public final boolean putAll(Map<Quadrant, Collection<? extends T>> eisenhowerMap) {
        boolean modified = true;
        for (Quadrant quadrant : quadrants.keySet()) {
            Collection<T> tasksInThisQuadrant = this.getTasks(quadrant);
            Collection<? extends T> tasksInOtherQuadrant = eisenhowerMap.get(quadrant);
            if (tasksInThisQuadrant == null || tasksInOtherQuadrant == null) {
                throw new NullPointerException("One of these quadrants hasn't been initialized with a proper collection of tasks.");
            }
            if (!tasksInThisQuadrant.addAll(tasksInOtherQuadrant)) {
                modified = false;
            }
        }
        return modified;
    }
    
    // -------------------------------------------------------------------------
    
    @Override
    public final Collection<T> getTasks(Quadrant quadrant) {
        return quadrants.get(quadrant);
    }
    
    @Override
    public final Collection<T> getTasks(boolean urgent, boolean important) {
        return this.getTasks(Quadrant.getQuadrant(urgent, important));
    }
    
    @Override
    public final List<T> getTasksSorted(Quadrant quadrant) {
        List<T> tasksList = new ArrayList<>(this.getTasks(quadrant));
        Collections.sort(tasksList);
        return tasksList;
    }
    
    @Override
    public final List<T> getTasksSorted(boolean urgent, boolean important) {
        return this.getTasksSorted(Quadrant.getQuadrant(urgent, important));
    }

    @Override
    public final List<T> getTasksSorted(Quadrant quadrant, Comparator<T> comparator) {
        List<T> tasksList = new ArrayList<>(this.getTasks(quadrant));
        Collections.sort(tasksList, comparator);
        return tasksList;
    }
    
    @Override
    public final List<T> getTasksSorted(boolean urgent, boolean important, Comparator<T> comparator) {
        return this.getTasksSorted(Quadrant.getQuadrant(urgent, important), comparator);
    }
    
    @Override
    public final Set<T> getAllTasks() {
        Set<T> allTasks = new HashSet<>();
        for (Collection<T> tasksInQuadrant : quadrants.values()) {
            allTasks.addAll(tasksInQuadrant);
        }
        return allTasks;
    }
    
    @Override
    public final List<T> getAllTasksSorted() {
        List<T> allTasksList = new ArrayList<>();
        for (Quadrant quadrant : Quadrant.linearPrioritySorting()) {
            allTasksList.addAll(this.getTasksSorted(quadrant));
        }
        return allTasksList;
    }

    @Override
    public final List<T> getAllTasksSorted(Comparator<T> comparator) {
        List<T> allTasksList = new ArrayList<>();
        for (Quadrant quadrant : Quadrant.linearPrioritySorting()) {
            allTasksList.addAll(this.getTasksSorted(quadrant, comparator));
        }
        return allTasksList;
    }
    
    @Override
    public final List<T> getAllTasksSorted(Map<Quadrant, Comparator<T>> quadrantComparators) {
        if (quadrantComparators.size() != 4) {
            throw new UnsupportedOperationException("Comparator(s) missing for 1 or more Eisenhower quadrants.");
        }
        
        List<T> allTasksList = new ArrayList<>();
        for (Quadrant quadrant : Quadrant.linearPrioritySorting()) {
            Comparator<T> specificComparator = quadrantComparators.get(quadrant);
            allTasksList.addAll(this.getTasksSorted(quadrant, specificComparator));
        }
        return allTasksList;
    }
    
    
    @Override
    public final Quadrant getQuadrant(T task) {
        for (Quadrant quadrant : Quadrant.linearPrioritySorting()) {
            if (this.getTasks(quadrant).contains(task)) {
                return quadrant;
            }
        }
        return null;
    }

    @Override
    public final Set<Quadrant> getQuadrants(T task) {
        Set<Quadrant> quadrantsHavingTask = new HashSet<>();
        for (Quadrant quadrant : Quadrant.linearPrioritySorting()) {
            if (this.getTasks(quadrant).contains(task)) {
                quadrantsHavingTask.add(quadrant);
            }
        }
        return quadrantsHavingTask;
    }
    
    // -------------------------------------------------------------------------
    
    @Override
    public final boolean containsTask(T task) {
        return quadrants.values().stream()
                    .anyMatch(tasks -> tasks.contains(task));
    }

    @Override
    public final boolean containsTask(T task, Quadrant quadrant) {
        return this.getTasks(quadrant).contains(task);
    }
    
    @Override
    public final boolean containsTask(T task, boolean urgent, boolean important) {
        return this.containsTask(task, Quadrant.getQuadrant(urgent, important));
    }
    
    // -------------------------------------------------------------------------
    
    @Override
    public final boolean removeTask(T task) {
        boolean removed = true;
        for (Quadrant quadrant : Quadrant.linearPrioritySorting()) {
            if (!this.removeTask(task, quadrant)) {
                removed = false;
            }
        }
        return removed;
    }
    
    @Override
    public final boolean removeTask(T task, boolean urgent, boolean important) {
        return this.removeTask(task, Quadrant.getQuadrant(urgent, important));
    }
    
    // -------------------------------------------------------------------------
    
    @Override
    public final void clearQuadrant(Quadrant quadrant) {
        Collection<T> tasksInQuadrant = this.getTasks(quadrant);
        if (tasksInQuadrant != null) {
            tasksInQuadrant.clear();
        }
    }
    
    @Override
    public final void clearQuadrant(boolean urgent, boolean important) {
        this.clearQuadrant(Quadrant.getQuadrant(urgent, important));
    }
    
    @Override
    public final void clearAllMatrix() {
        this.initializeMatrix();
    }
    
}