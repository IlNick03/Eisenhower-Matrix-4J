package com.eisenhower.util;


/**
 * An enum representing the criterion used to sort the Eisenhower's 4 quadrants:
 * <ol>
 *   <li><b>Do It Now </b> (Urgent and Important)</li>
 *   <li><b>Schedule It </b> (Urgent but Not Important)</li>
 *   <li><b>Delegate or Optimize It </b> (Not Urgent but Important)</li>
 *   <li><b>Eliminate It </b> (Not Urgent and Not Important)</li>
 * </ol>
 * It can be useful to convert a matrix-based tasks system into a unified linear sorting.
 * You may prefer to give more importance to the "urgent" ones at the expense of "important" ones,
 * or vice-versa.
 * 
 * @author Nicolas Scalese
 * @see EisenhowerMatrix
 * @see Quadrant
 */
public enum EQuadrantsSorting {
    
    /** 
     * 4-Quadrants sorting which gives more priority to "important" than "urgent".
     * Sort all tasks contained
     * The order which corresponds to their classical numerical representation (1-4):
     * <ol>
     *     <li>{@link Quadrant#DO_IT_NOW} (Quadrant 1)</li>
     *     <li>{@link Quadrant#SCHEDULE_IT} (Quadrant 2)</li>
     *     <li>{@link Quadrant#DELEGATE_OR_OPTIMIZE_IT} (Quadrant 3)</li>
     *     <li>{@link Quadrant#ELIMINATE_IT} (Quadrant 4)</li>
     * </ol>
     */
    IMPORTANCE_OVER_URGENCY,
    
    /** 
     * 4-Quadrants sorting which gives more priority to "urgent" than "important".
     * The order is as follows:
     * <ol>
     *     <li>{@link Quadrant#DO_IT_NOW} (Quadrant 1)</li>
     *     <li>{@link Quadrant#DELEGATE_OR_OPTIMIZE_IT} (Quadrant 3)</li>
     *     <li>{@link Quadrant#SCHEDULE_IT} (Quadrant 2)</li>
     *     <li>{@link Quadrant#ELIMINATE_IT} (Quadrant 4)</li>
     * </ol>
     */
    URGENCY_OVER_IMPORTANCE;
}
