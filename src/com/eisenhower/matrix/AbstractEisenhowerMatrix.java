package com.eisenhower.matrix;

import com.eisenhower.util.Quadrant;
import java.util.*;

/**
 *
 * @author Nicolas Scalese
 * @param <T> Any class representing a task to put in the Eisenhower matrix
 */
public abstract class AbstractEisenhowerMatrix<T extends Comparable<T>> 
        implements EisenhowerMatrix<T> {
    
    private Map<Quadrant, Collection<T>> quadrants = new HashMap<>();
    
    public AbstractEisenhowerMatrix() {
        this.initializeMatrix();
    }
    
    // -------------------------------------------------------------------------
    
    protected abstract void initializeMatrix();
     
    protected Map<Quadrant, Collection<T>> getMap() {
        return this.quadrants;
    }
    
    @Override
    public Map<Quadrant, Collection<T>> toMap() {
        return new HashMap<>(this.quadrants);
    }
    
    @Override
    public Collection<T>[][] toMatrix() {
        Collection<T>[][] matrix = new Collection[2][2];
        
        matrix[0][0] = quadrants.get(Quadrant.DO_IT);
        matrix[0][1] = quadrants.get(Quadrant.SCHEDULE_IT);
        matrix[1][0] = quadrants.get(Quadrant.DELEGATE_IT);
        matrix[1][1] = quadrants.get(Quadrant.DELETE_IT);
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
            Collection<T> tasksInThisQuadrant = quadrants.get(quadrant);
            tasksInThisQuadrant.add(task);
        }
    }
    
    @Override
    public void putIfAbsentInQuadrant(T task, Quadrant quadrant) {
        if (!this.containsTask(task, quadrant)) {
            Collection<T> tasksInThisQuadrant = quadrants.get(quadrant);
            tasksInThisQuadrant.add(task);
        }
    }
    
    @Override
    public final boolean putAll(Quadrant quadrant, Collection<? extends T> tasks) {
        Collection<T> tasksInThisQuadrant = quadrants.get(quadrant);
        return tasksInThisQuadrant.addAll(tasks);
    }
    
    @Override
    public final boolean putAll(Map<Quadrant, Collection<? extends T>> eisenhowerMap) {
        for (Quadrant quadrant : quadrants.keySet()) {
            Collection<T> tasksInThisQuadrant = quadrants.get(quadrant);
            Collection<? extends T> tasksInOtherQuadrant = eisenhowerMap.get(quadrant);
            tasksInThisQuadrant.addAll(tasksInOtherQuadrant);
        }
        return true;
    }
    
    // -------------------------------------------------------------------------
    
    @Override
    public final Collection<T> getTasks(Quadrant quadrant) {
        return quadrants.get(quadrant);
    }
    
    @Override
    public final List<T> getTasksSorted(Quadrant quadrant) {
        List<T> tasksList = new ArrayList<>(quadrants.get(quadrant));
        Collections.sort(tasksList);
        return tasksList;
    }

    @Override
    public final List<T> getTasksSorted(Quadrant quadrant, Comparator<T> comparator) {
        List<T> tasksList = new ArrayList<>(quadrants.get(quadrant));
        Collections.sort(tasksList, comparator);
        return tasksList;
    }
    
    @Override
    public final Set<T> getAllTasks(Quadrant quadrant) {
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
            List<T> tasksInQuadrant = this.getTasksSorted(quadrant);
            allTasksList.addAll(tasksInQuadrant);
        }
        return allTasksList;
    }

    @Override
    public final List<T> getAllTasksSorted(Comparator<T> comparator) {
        List<T> allTasksList = new ArrayList<>();
        for (Quadrant quadrant : Quadrant.linearPrioritySorting()) {
            List<T> tasksInQuadrant = this.getTasksSorted(quadrant, comparator);
            allTasksList.addAll(tasksInQuadrant);
        }
        return allTasksList;
    }
    
    
    @Override
    public Quadrant getQuadrant(T task) {
        for (Quadrant quadrant : Quadrant.linearPrioritySorting()) {
            Collection<T> tasksInQuadrant = quadrants.get(quadrant);
            if (tasksInQuadrant.contains(task)) {
                return quadrant;
            }
        }
        return null;
    }

    @Override
    public Set<Quadrant> getQuadrants(T task) {
        Set<Quadrant> quadrantsHavingTask = new HashSet<>();
        for (Quadrant quadrant : Quadrant.linearPrioritySorting()) {
            Collection<T> tasksInQuadrant = quadrants.get(quadrant);
            if (tasksInQuadrant.contains(task)) {
                quadrantsHavingTask.add(quadrant);
            }
        }
        return quadrantsHavingTask;
    }
    
    // -------------------------------------------------------------------------
    
    @Override
    public final boolean containsTask(T task) {
        return quadrants.containsValue(task);
    }

    @Override
    public final boolean containsTask(T task, Quadrant quadrant) {
        Collection<T> tasksInQuadrant = quadrants.get(quadrant);
        return tasksInQuadrant.contains(task);
    }
    
    // -------------------------------------------------------------------------
    
    @Override
    public final void clearQuadrant(Quadrant quadrant) {
        Collection<T> tasksInQuadrant = quadrants.get(quadrant);
        tasksInQuadrant.clear();
    }
    
    @Override
    public final void clearAllMatrix() {
        this.initializeMatrix();
    }
    
    // -------------------------------------------------------------------------

    
    
}
