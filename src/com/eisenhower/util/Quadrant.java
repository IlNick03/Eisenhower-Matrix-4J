package com.eisenhower.util;

import java.util.*;

/**
 * Enum representing the four quadrants of the Eisenhower matrix.
 * Each quadrant is defined by its urgency and importance.
 */
public enum Quadrant implements Comparable<Quadrant> {
    
    DO_IT_NOW(true, true),
    DELEGATE_OR_OPTIMIZE_IT(true, false),
    SCHEDULE_IT(false, true),
    ELIMINATE_IT(false, false);
    
    private boolean urgent;
    private boolean important;
    
    private Quadrant(boolean urgent, boolean important) {
        this.urgent = urgent;
        this.important = important;
    }
    
    /**
     * Returns a list of quadrants ordered by their priority in the Eisenhower matrix.
     * 
     * @return A list of quadrants ordered by priority.
     */
    public static List<Quadrant> linearPrioritySorting() {
        return Arrays.asList(DO_IT_NOW, DELEGATE_OR_OPTIMIZE_IT, SCHEDULE_IT, ELIMINATE_IT);
    }
    
    /**
     * Checks if the quadrant is urgent.
     * 
     * @return {@code true} if the quadrant is urgent, {@code false} otherwise.
     */
    public boolean isUrgent() {
        return urgent;
    }
    
    /**
     * Checks if the quadrant is important.
     * 
     * @return {@code true} if the quadrant is important, {@code false} otherwise.
     */
    public boolean isImportant() {
        return important;
    }
}
