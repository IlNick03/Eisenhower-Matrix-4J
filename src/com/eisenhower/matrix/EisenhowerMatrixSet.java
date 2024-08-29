package com.eisenhower.matrix;

import com.eisenhower.util.Quadrant;
import java.util.*;

/**
 * An Eisenhower matrix having in each quadrant a {@link Set} of tasks defined by the user.
 * <p>
 * It is recommended to use this implementation instead of {@link EisenhowerMatrixList}
 * because it is impossibile to have duplicated tasks in all the matrix.
 * 
 * @author Nicolas Scalese
 * @param <T> Any class representing a task to put in the Eisenhower matrix
 */
public class EisenhowerMatrixSet<T extends Comparable<T>> extends AbstractEisenhowerMatrix<T> {

    @Override
    protected void initializeMatrix() {
        super.getMap().put(Quadrant.DO_IT, new HashSet<>());
        super.getMap().put(Quadrant.DELEGATE_IT, new HashSet<>());
        super.getMap().put(Quadrant.SCHEDULE_IT, new HashSet<>());
        super.getMap().put(Quadrant.DELETE_IT, new HashSet<>());
    }

    // -------------------------------------------------------------------------
    
    @Override
    public final boolean putTask(T task, Quadrant quadrant) {
        if (this.containsTask(task)) {
            return false;
        }
        Collection<T> tasksInQuadrant = super.getMap().get(quadrant);
        return tasksInQuadrant.add(task);
    }
    
    // -------------------------------------------------------------------------

    @Override
    public final boolean removeTask(T task, Quadrant quadrant) {
        Collection<T> tasksInQuadrant = super.getMap().get(quadrant);
        return tasksInQuadrant.remove(task);
    }

    @Override
    public final boolean removeTask(T task) {
        return (super.getMap().remove(task) != null);
    }
    
    // -------------------------------------------------------------------------
    
    public final boolean isUrgent(T task) {
        if (!super.containsTask(task)) {
            throw new NoSuchElementException("Task not present anywhere in this Eisenhower matrix.");
        }
        Quadrant quadrant = super.getQuadrant(task);
        return quadrant.isUrgent();
   }
    
    public final boolean isImportant(T task) {
        if (!super.containsTask(task)) {
            throw new NoSuchElementException("Task not present anywhere in this Eisenhower matrix.");
        }
        Quadrant quadrant = super.getQuadrant(task);
        return quadrant.isImportant();
    }
}
