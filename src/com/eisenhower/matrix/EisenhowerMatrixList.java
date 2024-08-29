package com.eisenhower.matrix;

import com.eisenhower.util.Quadrant;
import java.util.*;

/**
 *
 * @author Nicolas Scalese
 * @param <T> Any class representing a task to put in the Eisenhower matrix
 */
public class EisenhowerMatrixList<T extends Comparable<T>> extends AbstractEisenhowerMatrix<T> {
    
    @Override
    protected void initializeMatrix() {
        super.getMap().put(Quadrant.DO_IT, new ArrayList<>());
        super.getMap().put(Quadrant.DELEGATE_IT, new ArrayList<>());
        super.getMap().put(Quadrant.SCHEDULE_IT, new ArrayList<>());
        super.getMap().put(Quadrant.DELETE_IT, new ArrayList<>());
    }

    // -------------------------------------------------------------------------
    
    @Override
    public boolean putTask(T task, Quadrant quadrant) {
        Collection<T> tasksInQuadrant = super.getMap().get(quadrant);
        return tasksInQuadrant.add(task);
    }
    
    // -------------------------------------------------------------------------

    @Override
    public boolean removeTask(T task, Quadrant quadrant) {
        Collection<T> tasksInQuadrant = super.getMap().get(quadrant);
        int count = this.countEqualInQuadrant(task, quadrant);
        if (count == 0) {
            return false;
        }
        for (int i = 0; i < count; i++) {
            return tasksInQuadrant.remove(task);
        }
        return true;
    }

    @Override
    public boolean removeTask(T task) {
        int count = this.countEqualInMatrix(task);
        if (count == 0) {
            return false;
        }
        for (int i = 0; i < count; i++) {
            super.getMap().remove(task);
        }
        return true;
    }
    
    private int countEqualInMatrix(T task) {
        int count = 0;
        for (Quadrant quadrant : super.getMap().keySet()) {
            Collection<T> tasksInQuadrant = super.getMap().get(quadrant);
            List<T> duplicatedTasks = tasksInQuadrant.stream()
                            .filter(t -> Objects.equals(task, t))
                            .toList();
            count += duplicatedTasks.size();
        }
        return count;
    }
    
    private int countEqualInQuadrant(T task, Quadrant quadrant) {
        Collection<T> tasksInQuadrant = super.getMap().get(quadrant);
        List<T> duplicatedTasks = tasksInQuadrant.stream()
                        .filter(t -> Objects.equals(task, t))
                        .toList();
        return duplicatedTasks.size();
    }
    
}
