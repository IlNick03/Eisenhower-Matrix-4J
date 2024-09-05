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
public class EisenhowerMatrixList<T extends Comparable<T>> extends AbstractEisenhowerMatrix<T> {

    @Override
    protected void initializeMatrix() {
        super.put(Quadrant.DO_IT_NOW, new ArrayList<>());
        super.put(Quadrant.DELEGATE_OR_OPTIMIZE_IT, new ArrayList<>());
        super.put(Quadrant.SCHEDULE_IT, new ArrayList<>());
        super.put(Quadrant.ELIMINATE_IT, new ArrayList<>());
    }
    
    @Override
    public final Class getTypeOfCollections() {
        return List.class;
    }

    // -------------------------------------------------------------------------
    
    @Override
    public final boolean putTask(T task, Quadrant quadrant) {
        if ((task == null) || (quadrant == null)) {
            throw new NullPointerException("Null argument(s) for this function.");
        }
        
        Collection<T> tasksInQuadrant = this.getTasks(quadrant);
        return tasksInQuadrant.add(task);
    }
    
    // -------------------------------------------------------------------------
    
    public final T getTask(Quadrant quadrant, int index) {
        if (quadrant == null) {
            throw new NullPointerException("Quadrant is null.");
        }
        
        List<T> tasksInQuadrant = (List<T>) this.getTasks(quadrant);
        try {
            return tasksInQuadrant.get(index);
        } catch (IndexOutOfBoundsException ex) {
            throw ex;
        }
    }
    
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
    
    public final T setTask(T task, Quadrant quadrant, int index) {
        if ((task == null) || (quadrant == null)) {
            throw new NullPointerException("Null argument(s) for this function.");
        }
        
        List<T> tasksInQuadrant = (List<T>) this.getTasks(quadrant);
        try {
            return tasksInQuadrant.set(index, task);
        } catch (RuntimeException ex) {
            throw ex;
        }
    }
    
    public final T setTask(T task, boolean urgent, boolean important, int index) {
        if (task == null) {
            throw new NullPointerException("Task is null.");
        }
        
        try {
            return this.setTask(task,Quadrant.getQuadrant(urgent, important), index);
        } catch (RuntimeException ex) {
            throw ex;
        }
    }
            
    // -------------------------------------------------------------------------

    @Override
    public final boolean removeTask(T task, Quadrant quadrant) {
        if ((task == null) || (quadrant == null)) {
            throw new NullPointerException("Null argument(s) for this function.");
        }
        
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
