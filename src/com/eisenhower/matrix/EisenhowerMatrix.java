package com.eisenhower.matrix;

import com.eisenhower.util.Quadrant;
import java.util.*;

/**
 *
 * @author Nicolas Scalese
 * @param <T>
 */
public interface EisenhowerMatrix<T extends Comparable<T>> {
    
    Map<Quadrant, Collection<T>> toMap();
    
    Collection<T>[][] toMatrix();
    
    @Override
    boolean equals(Object obj);
    
    @Override
    int hashCode();
    
    // -------------------------------------------------------------------------
    
    boolean putTask(T task, Quadrant quadrant);
    
    boolean putAll(Quadrant quadrant, Collection<? extends T> tasks);
    
    boolean putAll(Map<Quadrant, Collection<? extends T>> eisenhowerMap);
    
    void putIfAbsentInQuadrant(T task, Quadrant quadrant);
    
    void putIfAbsentInMatrix(T task, Quadrant quadrant);
    
    // -------------------------------------------------------------------------
    
    Collection<T> getTasks(Quadrant quadrant);

    List<T> getTasksSorted(Quadrant quadrant);

    List<T> getTasksSorted(Quadrant quadrant, Comparator<T> comparator);
    
    Set<T> getAllTasks(Quadrant quadrant);

    List<T> getAllTasksSorted();

    List<T> getAllTasksSorted(Comparator<T> comparator);
    
    Quadrant getQuadrant(T task);
    
    Set<Quadrant> getQuadrants(T task);
    
    // -------------------------------------------------------------------------
    
    boolean containsTask(T task);

    boolean containsTask(T task, Quadrant quadrant);
    
    // -------------------------------------------------------------------------
    
    boolean removeTask(T task, Quadrant quadrant);

    boolean removeTask(T task);

    void clearQuadrant(Quadrant quadrant);
    
    void clearAllMatrix();
    
}
