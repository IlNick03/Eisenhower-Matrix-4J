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
     * Puts 
     * 
     * @param key
     * @param tasks
     * @return 
     */
    protected Collection<T> put(Quadrant key, Collection<T> tasks) {
        return quadrants.put(key, tasks);
    }
    
    @Override
    public Map<Quadrant, Collection<T>> toMap() {
        return new HashMap<>(quadrants);
    }
    
    @Override
    public Collection<T>[][] toMatrix() {
        Collection<T>[][] matrix = new Collection[2][2];
        matrix[0][0] = quadrants.get(Quadrant.DO_IT_NOW);
        matrix[0][1] = quadrants.get(Quadrant.SCHEDULE_IT);
        matrix[1][0] = quadrants.get(Quadrant.DELEGATE_OR_OPTIMIZE_IT);
        matrix[1][1] = quadrants.get(Quadrant.ELIMINATE_IT);
        return matrix;
    }

    @Override
    public boolean equals(Object obj) {
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
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.quadrants);
        return hash;
    }
    
    // -------------------------------------------------------------------------
    
    @Override
    public void putIfAbsentInMatrix(T task, Quadrant quadrant) {
        if (!this.containsTask(task)) {
            Collection<T> tasksInThisQuadrant = this.getTasks(quadrant);
            if (tasksInThisQuadrant == null) {
                throw new NullPointerException("This quadrant hasn't been initialized with a proper collection of tasks.");
            }
            tasksInThisQuadrant.add(task);
        }
    }
    
    @Override
    public void putIfAbsentInQuadrant(T task, Quadrant quadrant) {
        if (!this.containsTask(task, quadrant)) {
            Collection<T> tasksInThisQuadrant = this.getTasks(quadrant);
            if (tasksInThisQuadrant == null) {
                throw new NullPointerException("This quadrant hasn't been initialized with a proper collection of tasks.");
            }
            tasksInThisQuadrant.add(task);
        }
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
            };
        }
        return modified;
    }
    
    // -------------------------------------------------------------------------
    
    @Override
    public final Collection<T> getTasks(Quadrant quadrant) {
        return quadrants.get(quadrant);
    }
    
    @Override
    public final List<T> getTasksSorted(Quadrant quadrant) {
        List<T> tasksList = new ArrayList<>(this.getTasks(quadrant));
        Collections.sort(tasksList);
        return tasksList;
    }

    @Override
    public final List<T> getTasksSorted(Quadrant quadrant, Comparator<T> comparator) {
        List<T> tasksList = new ArrayList<>(this.getTasks(quadrant));
        Collections.sort(tasksList, comparator);
        return tasksList;
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
    public Quadrant getQuadrant(T task) {
        for (Quadrant quadrant : Quadrant.linearPrioritySorting()) {
            if (this.getTasks(quadrant).contains(task)) {
                return quadrant;
            }
        }
        return null;
    }

    @Override
    public Set<Quadrant> getQuadrants(T task) {
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
    
    // -------------------------------------------------------------------------
    
    @Override
    public final boolean removeTask(T task) {
        boolean removed = true;
        for (Quadrant quadrant : Quadrant.linearPrioritySorting()) {
            if (!this.removeTask(task, quadrant)) {
                removed = false;
            };
        }
        return removed;
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
    public final void clearAllMatrix() {
        this.initializeMatrix();
    }
    
}