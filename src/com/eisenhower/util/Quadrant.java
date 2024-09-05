package com.eisenhower.util;

import java.util.*;

/**
 * Enum representing the four quadrants of the Eisenhower matrix.
 * Each quadrant is defined by its urgency and importance.
 */
public enum Quadrant implements Comparable<Quadrant> {
    
    DO_IT_NOW(true, true),
    SCHEDULE_IT(false, true),
    DELEGATE_OR_OPTIMIZE_IT(true, false),
    ELIMINATE_IT(false, false);
    
    private final boolean urgent;
    private final boolean important;
    
    private Quadrant(boolean urgent, boolean important) {
        this.urgent = urgent;
        this.important = important;
    }
    
    // -------------------------------------------------------------------------
    
    /**
     * Returns a list of quadrants ordered by their priority in the Eisenhower matrix.
     * 
     * @return A list of quadrants ordered by priority.
     */
    public static List<Quadrant> linearPrioritySorting() {
        return Arrays.asList(DO_IT_NOW, DELEGATE_OR_OPTIMIZE_IT, SCHEDULE_IT, ELIMINATE_IT);
    }
    
    public static List<Quadrant> classicalSortingByNumber() {
        return Arrays.asList(DO_IT_NOW, SCHEDULE_IT, DELEGATE_OR_OPTIMIZE_IT, ELIMINATE_IT);
    }
    
    public static Quadrant getQuadrant(boolean urgent, boolean important) {
        if (urgent && important) {
            return DO_IT_NOW;
        } 
        if (!urgent && important) {
            return SCHEDULE_IT;
        }
        if (urgent && !important) {
            return DELEGATE_OR_OPTIMIZE_IT;
        }
        // not urgent, nor important
        return ELIMINATE_IT;
    }
    
    /**
     * 
     * @param quadrantNumber
     * @return 
     * @throws IndexOutOfBoundsException if the index is out of the range: 1-4. 
     */
    public static Quadrant getQuadrant(int quadrantNumber) {
        return switch (quadrantNumber) {
            case 1 -> DO_IT_NOW;
            case 2 -> DO_IT_NOW;
            case 3 -> DO_IT_NOW;
            case 4 -> DO_IT_NOW;
            default -> {
                throw new IndexOutOfBoundsException("No Eisenhower quadrant for such index.");
            }
        };
    }
    
    // -------------------------------------------------------------------------
    
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
    
    public int getQuadrantNumber() {
        return super.ordinal() + 1;
    }
}
