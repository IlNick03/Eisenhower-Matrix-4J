package com.eisenhower.matrix;

import com.eisenhower.util.EQuadrantsSorting;
import static com.eisenhower.util.EQuadrantsSorting.*;
import com.eisenhower.util.Quadrant;
import java.util.*;

/**
 * Abstract implementation of the {@link EisenhowerMatrix} interface.
 * Provides a common foundation for different types of matrix implementations,
 * such as list-based ({@link EisenhowerMatrixList}) and set-based ({@link EisenhowerMatrixSet}) matrices.
 *
 * <p>This class manages the internal mapping of quadrants to their respective collections of tasks.
 * Concrete subclasses must define how the quadrants are initialized by specifying the type of 
 * {@link Collection} used for storing tasks in each quadrant.</p>
 *
 * @param <T> the type of task stored in the matrix.
 */
public abstract class AbstractEisenhowerMatrix<T extends Comparable<T>> implements EisenhowerMatrix<T>, Cloneable {
    
    private final Map<Quadrant, Collection<T>> quadrantTaskMap = new HashMap<>();
    
    /**
     * Constructs an instance of {@link AbstractEisenhowerMatrix} and initializes the matrix.
     *
     * @see #initializeMatrix()
     */
    public AbstractEisenhowerMatrix() {
        this.initializeMatrix();
    }
    
    /**
     * Initializes the matrix by associating each quadrant with an empty {@link Collection} of tasks.
     * Concrete subclasses must provide their own implementation specifying the type of collection.
     *
     * @see #put(Quadrant, Collection)
     */
    protected abstract void initializeMatrix();
    
    /**
     * Associates a {@link Collection} of tasks with the given quadrant.
     * This method should be used by concrete classes during the initialization of each matrix quadrant.
     *
     * <p><b>Note:</b> All quadrants must use the same type of {@link Collection} implementation to ensure
     * consistency in operations and performance.</p>
     *
     * @param quadrant the quadrant to associate with the tasks.
     * @param tasks    the collection of tasks to associate with the quadrant.
     * @return the previous collection of tasks associated with the quadrant, or {@code null} if none existed.
     * @throws NullPointerException if {@code quadrant} or {@code tasks} is {@code null}.
     */
    protected final Collection<T> put(Quadrant quadrant, Collection<T> tasks) {
        Objects.requireNonNull(quadrant, "Quadrant cannot be null.");
        Objects.requireNonNull(tasks, "Tasks collection cannot be null.");
        return quadrantTaskMap.put(quadrant, tasks);
    }
    
    @Override
    public final Map<Quadrant, Collection<T>> toMap() {
        return new HashMap<>(quadrantTaskMap);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public final Collection<T>[][] toMatrix() {
        Collection<T>[][] matrix = new Collection[2][2];
        
        for (Quadrant quadrant : Quadrant.values()) {
            int row = quadrant.isUrgent() ? 0 : 1;
            int col = quadrant.isImportant() ? 0 : 1;
            matrix[row][col] = this.getTasks(quadrant);
        }
        return matrix;
    }
    
    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof AbstractEisenhowerMatrix)) {
            return false;
        }
        final AbstractEisenhowerMatrix<?> other = (AbstractEisenhowerMatrix<?>) obj;
        return Objects.equals(this.quadrantTaskMap, other.quadrantTaskMap);
    }
    
    @Override
    public final int hashCode() {
        return Objects.hash(quadrantTaskMap);
    }
    
    // -------------------------------------------------------------------------
    
    @Override
    public final boolean addTask(T task, boolean urgent, boolean important) {
        Objects.requireNonNull(task, "Task cannot be null.");
        Quadrant quadrant = Quadrant.getQuadrant(urgent, important);
        return this.addTask(task, quadrant);
    }
    
    @Override
    public final boolean addAllTasks(boolean urgent, boolean important, Collection<? extends T> tasks) {
        Objects.requireNonNull(tasks, "Tasks collection cannot be null.");
        Quadrant quadrant = Quadrant.getQuadrant(urgent, important);
        return this.addAllTasks(quadrant, tasks);
    }
    
    @Override
    public final void addTaskIfAbsentInQuadrant(T task, Quadrant quadrant) {
        Objects.requireNonNull(task, "Task cannot be null.");
        Objects.requireNonNull(quadrant, "Quadrant cannot be null.");
        
        if (!this.containsTask(task, quadrant)) {
            Collection<T> tasksInQuadrant = this.getTasks(quadrant);
            if (tasksInQuadrant == null) {
                throw new IllegalStateException("Quadrant " + quadrant + " has not been initialized with a proper collection.");
            }
            tasksInQuadrant.add(task);
        }
    }
    
    @Override
    public final void addTaskIfAbsentInQuadrant(T task, boolean urgent, boolean important) {
        Objects.requireNonNull(task, "Task cannot be null.");
        Quadrant quadrant = Quadrant.getQuadrant(urgent, important);
        this.addTaskIfAbsentInQuadrant(task, quadrant);
    }
    
    @Override
    public final void addTaskIfAbsentInMatrix(T task, Quadrant quadrant) {
        Objects.requireNonNull(task, "Task cannot be null.");
        Objects.requireNonNull(quadrant, "Quadrant cannot be null.");
        
        if (!this.containsTask(task)) {
            Collection<T> tasksInQuadrant = this.getTasks(quadrant);
            if (tasksInQuadrant == null) {
                throw new IllegalStateException("Quadrant " + quadrant + " has not been initialized with a proper collection.");
            }
            tasksInQuadrant.add(task);
        }
    }
    
    @Override
    public final void addTaskIfAbsentInMatrix(T task, boolean urgent, boolean important) {
        Objects.requireNonNull(task, "Task cannot be null.");
        Quadrant quadrant = Quadrant.getQuadrant(urgent, important);
        this.addTaskIfAbsentInMatrix(task, quadrant);
    }
    
    @Override
    public final boolean addAllTasks(Quadrant quadrant, Collection<? extends T> tasks) {
        Objects.requireNonNull(quadrant, "Quadrant cannot be null.");
        Objects.requireNonNull(tasks, "Tasks collection cannot be null.");
        
        Collection<T> tasksInQuadrant = this.getTasks(quadrant);
        if (tasksInQuadrant == null) {
            throw new IllegalStateException("Quadrant " + quadrant + " has not been initialized with a proper collection.");
        }
        return tasksInQuadrant.addAll(tasks);
    }
    
    @Override
    public final boolean addAllTasks(Map<Quadrant, Collection<? extends T>> eisenhowerMap) {
        Objects.requireNonNull(eisenhowerMap, "Eisenhower quadrants' map cannot be null.");
        if (eisenhowerMap.isEmpty()) {
            return false;
        }
        
        boolean modified = false;
        for (Quadrant quadrant : quadrantTaskMap.keySet()) {
            Collection<? extends T> tasksToAdd = eisenhowerMap.get(quadrant);
            Objects.requireNonNull(tasksToAdd, "Quadrant " + quadrant + " is missing in the provided map.");
            Collection<T> tasksInQuadrant = this.getTasks(quadrant);
            if (tasksInQuadrant.addAll(tasksToAdd)) {
                modified = true;
            }
        }
        return modified;
    }
    
    // -------------------------------------------------------------------------
    
    @Override
    public final Collection<T> getTasks(Quadrant quadrant) {
        Objects.requireNonNull(quadrant, "Quadrant cannot be null.");
        return quadrantTaskMap.get(quadrant);
    }
    
    @Override
    public final Collection<T> getTasks(boolean urgent, boolean important) {
        Quadrant quadrant = Quadrant.getQuadrant(urgent, important);
        return this.getTasks(quadrant);
    }
    
    @Override
    public final List<T> getTasksSorted(Quadrant quadrant) {
        Objects.requireNonNull(quadrant, "Quadrant cannot be null.");
        List<T> sortedTasks = new ArrayList<>(this.getTasks(quadrant));
        Collections.sort(sortedTasks);
        return sortedTasks;
    }
    
    @Override
    public final List<T> getTasksSorted(boolean urgent, boolean important) {
        Quadrant quadrant = Quadrant.getQuadrant(urgent, important);
        return this.getTasksSorted(quadrant);
    }

    @Override
    public final List<T> getTasksSorted(Quadrant quadrant, Comparator<T> comparator) {
        Objects.requireNonNull(quadrant, "Quadrant cannot be null.");
        Objects.requireNonNull(comparator, "Comparator cannot be null.");
        List<T> sortedTasks = new ArrayList<>(this.getTasks(quadrant));
        sortedTasks.sort(comparator);
        return sortedTasks;
    }
    
    @Override
    public final List<T> getTasksSorted(boolean urgent, boolean important, Comparator<T> comparator) {
        Quadrant quadrant = Quadrant.getQuadrant(urgent, important);
        return this.getTasksSorted(quadrant, comparator);
    }
    
    @Override
    public final Set<T> getAllTasks() {
        Set<T> allTasks = new HashSet<>();
        for (Collection<T> tasksInQuadrant : quadrantTaskMap.values()) {
            allTasks.addAll(tasksInQuadrant);
        }
        return allTasks;
    }
    
    @Override
    public final List<T> getAllTasksSorted(EQuadrantsSorting quadrantsOrdering) {
        List<T> allTasksList = new ArrayList<>();
        for (Quadrant quadrant : Quadrant.quadrantsSorted(quadrantsOrdering)) {
            allTasksList.addAll(this.getTasksSorted(quadrant));
        }
        return allTasksList;
    }

    @Override
    public final List<T> getAllTasksSorted(Comparator<T> taskComparator, EQuadrantsSorting quadrantsOrdering) {
        Objects.requireNonNull(taskComparator, "Comparator cannot be null.");
        List<T> allTasksList = new ArrayList<>();
        for (Quadrant quadrant : Quadrant.quadrantsSorted(quadrantsOrdering)) {
            allTasksList.addAll(this.getTasksSorted(quadrant, taskComparator));
        }
        return allTasksList;
    }
    
    @Override
    public final List<T> getAllTasksSorted(Map<Quadrant, Comparator<T>> comparators, EQuadrantsSorting quadrantsOrdering) {
        Objects.requireNonNull(comparators, "Quadrant comparators map cannot be null.");
        if (comparators.size() != Quadrant.values().length) {
            throw new UnsupportedOperationException("Comparator(s) missing for 1 or more Eisenhower quadrants.");
        }
        
        List<T> allTasksList = new ArrayList<>();
        for (Quadrant quadrant : Quadrant.quadrantsSorted(quadrantsOrdering)) {
            Comparator<T> comparator = comparators.get(quadrant);
            if (comparator == null) {
                throw new UnsupportedOperationException("Comparator missing for quadrant: " + quadrant);
            }
            allTasksList.addAll(this.getTasksSorted(quadrant, comparator));
        }
        return allTasksList;
    }
    
    @Override
    public final Quadrant getQuadrant(T task) {
        Objects.requireNonNull(task, "Task cannot be null.");
        
        for (Quadrant quadrant : Quadrant.quadrantsSorted(IMPORTANCE_OVER_URGENCY)) {
            if (this.getTasks(quadrant).contains(task)) {
                return quadrant;
            }
        }
        return null;
    }

    @Override
    public final Set<Quadrant> getQuadrants(T task) {
        Objects.requireNonNull(task, "Task cannot be null.");
        
        Set<Quadrant> quadrantsWithTask = new HashSet<>();
        for (Quadrant quadrant : Quadrant.quadrantsSorted(IMPORTANCE_OVER_URGENCY)) {
            if (this.getTasks(quadrant).contains(task)) {
                quadrantsWithTask.add(quadrant);
            }
        }
        return quadrantsWithTask;
    }
    
    // -------------------------------------------------------------------------
    
    @Override
    public final boolean containsTask(T task) {
        Objects.requireNonNull(task, "Task cannot be null.");
        return quadrantTaskMap.values().stream()
                .anyMatch(tasks -> tasks.contains(task));
    }

    @Override
    public final boolean containsTask(T task, Quadrant quadrant) {
        Objects.requireNonNull(task, "Task cannot be null.");
        Objects.requireNonNull(quadrant, "Quadrant cannot be null.");
        Collection<T> tasksInQuadrant = this.getTasks(quadrant);
        return (tasksInQuadrant != null) && (tasksInQuadrant.contains(task));
    }
    
    @Override
    public final boolean containsTask(T task, boolean urgent, boolean important) {
        Quadrant quadrant = Quadrant.getQuadrant(urgent, important);
        return this.containsTask(task, quadrant);
    }
    
    // -------------------------------------------------------------------------
    
    
    @Override
    public final boolean removeTask(T task, Quadrant quadrant) {
        Objects.requireNonNull(task, "Task cannot be null.");
        Objects.requireNonNull(quadrant, "Quadrant cannot be null.");
        
        Collection<T> tasksInQuadrant = this.getTasks(quadrant);
        return tasksInQuadrant.remove(task);
    }
    
    @Override
    public boolean removeTask(T task, boolean urgent, boolean important) {
        Quadrant quadrant = Quadrant.getQuadrant(urgent, important);
        return this.removeTask(task, quadrant);
    }
    
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
    public boolean removeTaskOccurrences(T task, Quadrant quadrant) {
        Objects.requireNonNull(task, "Task cannot be null.");
        Objects.requireNonNull(quadrant, "Quadrant cannot be null.");

        Collection<T> tasksInQuadrant = this.getTasks(quadrant);
        return tasksInQuadrant.removeIf(t -> t.equals(task));
    }
    
    @Override
    public final boolean removeTaskOccurrences(T task, boolean urgent, boolean important) {
        Quadrant quadrant = Quadrant.getQuadrant(urgent, important);
        return this.removeTaskOccurrences(task, quadrant);
    }
    
    @Override
    public final boolean removeTaskOccurrences(T task) {
        Objects.requireNonNull(task, "Task cannot be null.");
        boolean removed = true;
        
        for (Quadrant quadrant : Quadrant.values()) {
            if (!this.removeTaskOccurrences(task, quadrant)) {
                removed = false;
            }
        }
        return removed;
    }
    
    // -------------------------------------------------------------------------
    
    @Override
    public final EisenhowerMatrix<T> clearQuadrant(Quadrant quadrant) {
        Objects.requireNonNull(quadrant, "Quadrant cannot be null.");
        
        EisenhowerMatrix modified = (EisenhowerMatrix) this.clone();
        Collection<T> tasksInQuadrant = modified.getTasks(quadrant);
        if (tasksInQuadrant != null) {
            tasksInQuadrant.clear();
        }
        return modified;
    }
    
    @Override
    public final EisenhowerMatrix<T> clearQuadrant(boolean urgent, boolean important) {
        Quadrant quadrant = Quadrant.getQuadrant(urgent, important);
        return this.clearQuadrant(quadrant);
    }
    
    @Override
    public EisenhowerMatrix<T> clearAllTasks() {
        AbstractEisenhowerMatrix modified = (AbstractEisenhowerMatrix) this.clone();
        modified.initializeMatrix();
        return modified;
    }
    
    // -------------------------------------------------------------------------

    @Override
    protected Object clone() {
        try {
            AbstractEisenhowerMatrix matrixClone = (AbstractEisenhowerMatrix) super.clone();
            matrixClone.quadrantTaskMap.clear();
            matrixClone.quadrantTaskMap.putAll(this.quadrantTaskMap);
            return matrixClone;
        } catch (ClassCastException | CloneNotSupportedException ex) {
            return null;
        }
    }
    
    
}
