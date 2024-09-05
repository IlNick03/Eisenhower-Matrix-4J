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
    
    private final Map<Quadrant, Collection<T>> quadrantsMapping = new HashMap<>();
    
    /**
     * Constructs an instance of {@link AbstractEisenhowerMatrix} and initializes the matrix.
     * @see #initializeMatrix() 
     */
    public AbstractEisenhowerMatrix() {
        this.initializeMatrix();
    }
    
    /**
     * Initializes the matrix: each quadrant is associated with an empty {@link Collection}
     * of tasks. 
     * Concrete subclasses must provide their own implementation.
     * 
     * @see #put(com.eisenhower.util.Quadrant, java.util.Collection) 
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
        return quadrantsMapping.put(key, tasks);
    }
    
    @Override
    public final Map<Quadrant, Collection<T>> toMap() {
        return new HashMap<>(quadrantsMapping);
    }
    
    @Override
    public final Collection<T>[][] toMatrix() {
        Collection<T>[][] matrix = new Collection[2][2];
//        matrix[0][0] = quadrantsMapping.get(Quadrant.DO_IT_NOW);
//        matrix[0][1] = quadrantsMapping.get(Quadrant.DELEGATE_OR_OPTIMIZE_IT);
//        matrix[1][0] = quadrantsMapping.get(Quadrant.SCHEDULE_IT);
//        matrix[1][1] = quadrantsMapping.get(Quadrant.ELIMINATE_IT);

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
        return Objects.equals(this.quadrantsMapping, other.quadrantsMapping);
    }
    
    @Override
    public final int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.quadrantsMapping);
        return hash;
    }
    
    // -------------------------------------------------------------------------
    
    @Override
    public final boolean putTask(T task, boolean urgent, boolean important) {
        if (task == null) {
            throw new NullPointerException("Task is null.");
        }
        
        return this.putTask(task, Quadrant.getQuadrant(urgent, important));
    }
    
    @Override
    public final boolean putAll(boolean urgent, boolean important, Collection<? extends T> tasks) {
        if (tasks == null) {
            throw new NullPointerException("Null pointer for tasks collection.");
        }
        
        return this.putAll(Quadrant.getQuadrant(urgent, important), tasks);
    }
    
    @Override
    public final void putIfAbsentInQuadrant(T task, Quadrant quadrant) {
        if ((task == null) || (quadrant == null)) {
            throw new NullPointerException("Null argument(s) for this function.");
        }
        
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
        if (task == null) {
            throw new NullPointerException("Task is null.");
        }
        
        this.putIfAbsentInQuadrant(task, Quadrant.getQuadrant(urgent, important));
    }
    
    @Override
    public final void putIfAbsentInMatrix(T task, Quadrant quadrant) {
        if ((task == null) || (quadrant == null)) {
            throw new NullPointerException("Null argument(s) for this function.");
        }
        
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
        if (task == null) {
            throw new NullPointerException("Task is null.");
        }
        
        this.putIfAbsentInMatrix(task, Quadrant.getQuadrant(urgent, important));
    }
    
    @Override
    public final boolean putAll(Quadrant quadrant, Collection<? extends T> tasks) {
        if ((tasks == null) || (quadrant == null)) {
            throw new NullPointerException("Null argument(s) for this function.");
        }
        
        Collection<T> tasksInThisQuadrant = this.getTasks(quadrant);
        if (tasksInThisQuadrant == null) {
            throw new NullPointerException("This quadrant hasn't been initialized with a proper collection of tasks.");
        }
        return tasksInThisQuadrant.addAll(tasks);
    }
    
    @Override
    public final boolean putAll(Map<Quadrant, Collection<? extends T>> eisenhowerMap) {
        if (eisenhowerMap == null) {
            throw new NullPointerException("Null pointer for Eisenhower quadrants' map.");
        }
        if (eisenhowerMap.isEmpty()) {
            return false;
        }
        
        boolean modified = true;
        for (Quadrant quadrant : quadrantsMapping.keySet()) {
            Collection<T> tasksInThisQuadrant = this.getTasks(quadrant);
            Collection<? extends T> tasksInOtherQuadrant = eisenhowerMap.get(quadrant);
            if (tasksInOtherQuadrant == null) {
                break;
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
        return quadrantsMapping.get(quadrant);
    }
    
    @Override
    public final Collection<T> getTasks(boolean urgent, boolean important) {
        return this.getTasks(Quadrant.getQuadrant(urgent, important));
    }
    
    @Override
    public final List<T> getTasksSorted(Quadrant quadrant) {
        if (quadrant == null) {
            throw new NullPointerException("Quadrant is null.");
        }
            
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
        if (quadrant == null) {
            throw new NullPointerException("Quadrant is null.");
        }
        
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
        for (Collection<T> tasksInQuadrant : quadrantsMapping.values()) {
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
        if (task == null) {
            throw new NullPointerException("Task is null.");
        }
        
        for (Quadrant quadrant : Quadrant.linearPrioritySorting()) {
            if (this.getTasks(quadrant).contains(task)) {
                return quadrant;
            }
        }
        return null;
    }

    @Override
    public final Set<Quadrant> getQuadrants(T task) {
        if (task == null) {
            throw new NullPointerException("Task is null.");
        }
        
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
        return quadrantsMapping.values().stream()
                    .anyMatch(tasks -> tasks.contains(task));
    }

    @Override
    public final boolean containsTask(T task, Quadrant quadrant) {
        if (quadrant == null) {
            throw new NullPointerException("Quadrant is null.");
        }
        
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
        if (quadrant == null) {
            throw new NullPointerException("Quadrant is null.");
        }
        
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