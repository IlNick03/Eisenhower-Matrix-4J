package com.eisenhower.util;

import static com.eisenhower.util.EQuadrantsSorting.*;

/**
 * This enumeration represents the 4 quadrants of the Eisenhower Matrix, a productivity tool used to 
 * prioritize tasks based on their urgency and importance. 
 * 
 * The Eisenhower Matrix, also known as the Urgent-Important Matrix, is a decision-making framework that helps 
 * individuals and organizations manage time and tasks effectively by dividing tasks into 4 categories, or quadrants:
 * 
 * <ol>
 *     <li><b>Do it now:</b> Urgent and important tasks that require immediate attention.</li>
 *     <li><b>Schedule it:</b> Important but not urgent tasks that can be scheduled for later.</li>
 *     <li><b>Delegate or optimize it:</b> Urgent but not important tasks that should be delegated or optimized.</li>
 *     <li><b>Eliminate it:</b> Neither urgent nor important tasks that should be eliminated to save time.</li>
 * </ol>
 * 
 * Each quadrant is defined by two properties: urgency and importance. 
 * The combination of these properties defines the quadrant's priority.
 */
public enum Quadrant implements Comparable<Quadrant> {
    
    /** Represents tasks that are both urgent and important (Quadrant 1). */
    DO_IT_NOW(true, true),
    
    /** Represents tasks that are important but not urgent (Quadrant 2). */
    SCHEDULE_IT(false, true),
    
    /** Represents tasks that are urgent but not important (Quadrant 3). */
    DELEGATE_OR_OPTIMIZE_IT(true, false),
    
    /** Represents tasks that are neither urgent nor important (Quadrant 4). */
    ELIMINATE_IT(false, false);
    
    private final boolean urgent;
    private final boolean important;
    
    /**
     * Private constructor for initializing the quadrant with urgency and importance.
     * 
     * @param urgent    whether the task is urgent.
     * @param important whether the task is important.
     */
    private Quadrant(boolean urgent, boolean important) {
        this.urgent = urgent;
        this.important = important;
    }
    
    // -------------------------------------------------------------------------
    
    /**
     * Returns all 4 quadrants of an Eisenhower Matrix, sorted by a specific criterion.
     * It can be useful to convert a matrix-based tasks system into a unified linear sorting.
     * More specifically, you may:
     * <ul>
     *     <li>Prefer to give priority and resources to the <b>"urgent"</b> ones 
     *     <b>at the expense of "important"</b> ones -> {@code (Q1, Q3, Q2, Q4)}.<p>
     *     Allows you to manage better and get much things done in the short term if you 
     *     are very busy and overwhelmed. Needed if you really can't delegate commitments 
     *     to other people, nor delete less important ones.</li>
     *     <li>Prefer to give priority and resources to the <b>"important"</b> ones and 
     *     <b>worry less about "urgent"</b> ones -> {@code (Q1, Q2, Q3, Q4)}.<p>
     *     Allows you to invest time and energies for your long term goals, 
     *     while meeting deadlines over the short term. </li>
     * </ul>
     * 
     * @param ordering the ordering criterion to be used
     * @return a ordering of the 4 Eisenhower Matrix' quadrants, based on the given criterion.
     * @see EQuadrantsSorting#IMPORTANCE_OVER_URGENCY
     * @see EQuadrantsSorting#URGENCY_OVER_IMPORTANCE
     */
    public static Quadrant[] quadrantsSorted(EQuadrantsSorting ordering) {
        switch (ordering) {
            case IMPORTANCE_OVER_URGENCY -> {
                return new Quadrant[] {DO_IT_NOW, SCHEDULE_IT, DELEGATE_OR_OPTIMIZE_IT, ELIMINATE_IT};
            }
            case URGENCY_OVER_IMPORTANCE -> {
                return new Quadrant[] {DO_IT_NOW, DELEGATE_OR_OPTIMIZE_IT, SCHEDULE_IT, ELIMINATE_IT};
            }
        }
        return null;
    }
    
    /**
     * Returns the quadrant based on the given urgency and importance.
     * 
     * @param urgent    whether the task is urgent.
     * @param important whether the task is important.
     * @return the corresponding quadrant based on urgency and importance.
     */
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
        // Not urgent and not important.
        return ELIMINATE_IT;
    }
    
    /**
     * Returns the quadrant associated with the specific quadrant number.
     * 
     * @param quadrantNumber the number representing the quadrant (1-4).
     * @return the quadrant corresponding to the given number.
     * @throws IndexOutOfBoundsException if the quadrant number is outside the range 1-4.
     */
    public static Quadrant getQuadrant(int quadrantNumber) {
        return switch (quadrantNumber) {
            case 1 -> DO_IT_NOW;
            case 2 -> SCHEDULE_IT;
            case 3 -> DELEGATE_OR_OPTIMIZE_IT;
            case 4 -> ELIMINATE_IT;
            default -> {
                throw new IndexOutOfBoundsException("No Eisenhower quadrant exists for the given index. Valid values are 1-4.");
            }
        };
    }
    
    // -------------------------------------------------------------------------
    
    /**
     * Checks whether this quadrant is considered urgent.
     * 
     * @return {@code true} if the quadrant is urgent, {@code false} otherwise.
     */
    public boolean isUrgent() {
        return urgent;
    }
    
    /**
     * Checks whether this quadrant is considered important.
     * 
     * @return {@code true} if the quadrant is important, {@code false} otherwise.
     */
    public boolean isImportant() {
        return important;
    }
    
    /**
     * Returns the numerical representation of the quadrant.
     * The quadrants are numbered from 1 to 4 based on their priority in the Eisenhower Matrix:
     * <ol>
     *     <li>{@link DO_IT_NOW}</li>
     *     <li>{@link SCHEDULE_IT}</li>
     *     <li>{@link DELEGATE_OR_OPTIMIZE_IT}</li>
     *     <li>{@link ELIMINATE_IT}</li>
     * </ol>
     * 
     * @return the quadrant number, from 1 to 4.
     */
    public int getQuadrantNumber() {
        return super.ordinal() + 1;
    }
}
