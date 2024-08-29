package com.eisenhower.matrix;

import com.eisenhower.util.Quadrant;
import java.util.*;

/**
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
    public boolean putTask(T task, Quadrant quadrant) {
        if (this.containsTask(task)) {
            return false;
        }
        Collection<T> tasksInQuadrant = super.getMap().get(quadrant);
        return tasksInQuadrant.add(task);
    }
    
    // -------------------------------------------------------------------------

    @Override
    public boolean removeTask(T task, Quadrant quadrant) {
        Collection<T> tasksInQuadrant = super.getMap().get(quadrant);
        return tasksInQuadrant.remove(task);
    }

    @Override
    public boolean removeTask(T task) {
        return (super.getMap().remove(task) != null);
    }
    
    // -------------------------------------------------------------------------
    
    public boolean isUrgent(T task) {
        if (!super.containsTask(task)) {
            throw new NoSuchElementException("Task not present anywhere in this Eisenhower matrix.");
        }
        Quadrant quadrant = super.getQuadrant(task);
        return quadrant.isUrgent();
   }
    
    public boolean isImportant(T task) {
        if (!super.containsTask(task)) {
            throw new NoSuchElementException("Task not present anywhere in this Eisenhower matrix.");
        }
        Quadrant quadrant = super.getQuadrant(task);
        return quadrant.isImportant();
    }
}
