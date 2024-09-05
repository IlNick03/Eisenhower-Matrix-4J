package com.eisenhower.matrix;

import com.eisenhower.util.Quadrant;
import java.util.*;

/**
 * An Eisenhower matrix where each quadrant contains a {@link Set} of tasks.
 * <p>
 * This implementation does not allow duplicate tasks in the matrix and is preferred over {@link EisenhowerMatrixList}.
 *
 * @param <T> Any class representing a task to be added to the Eisenhower matrix.
 */
public class EisenhowerMatrixSet<T extends Comparable<T>> extends AbstractEisenhowerMatrix<T> {

    @Override
    protected void initializeMatrix() {
        super.put(Quadrant.DO_IT_NOW, new HashSet<>());
        super.put(Quadrant.DELEGATE_OR_OPTIMIZE_IT, new HashSet<>());
        super.put(Quadrant.SCHEDULE_IT, new HashSet<>());
        super.put(Quadrant.ELIMINATE_IT, new HashSet<>());
    }
    
    @Override
    public final Class getTypeOfCollections() {
        return Set.class;
    }

    // -------------------------------------------------------------------------
    
    @Override
    public final boolean putTask(T task, Quadrant quadrant) {
        if ((task == null) || (quadrant == null)) {
            throw new NullPointerException("Null argument(s) for this function.");
        }
        
        if (this.containsTask(task)) {
            return false;
        }
        Collection<T> tasksInQuadrant = this.getTasks(quadrant);
        return tasksInQuadrant.add(task);
    }

    // -------------------------------------------------------------------------

    @Override
    public final boolean removeTask(T task, Quadrant quadrant) {
        if ((task == null) || (quadrant == null)) {
            throw new NullPointerException("Null argument(s) for this function.");
        }
        
        Collection<T> tasksInQuadrant = this.getTasks(quadrant);
        return tasksInQuadrant.remove(task);
    }

    // -------------------------------------------------------------------------
    
    public final boolean isUrgent(T task) {
        if (task == null) {
            throw new NullPointerException("Task is null.");
        }
        
        if (!super.containsTask(task)) {
            throw new NoSuchElementException("Task not present anywhere in this Eisenhower matrix.");
        }
        Quadrant quadrant = super.getQuadrant(task);
        return quadrant.isUrgent();
    }

    public final boolean isImportant(T task) {
        if (task == null) {
            throw new NullPointerException("Task is null.");
        }
        
        if (!super.containsTask(task)) {
            throw new NoSuchElementException("Task not present anywhere in this Eisenhower matrix.");
        }
        Quadrant quadrant = super.getQuadrant(task);
        return quadrant.isImportant();
    }
}
