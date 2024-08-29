/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.eisenhower.util;

import java.util.*;

/**
 *
 * @author Nicolas
 */
public enum Quadrant implements Comparable<Quadrant> {
    
    DO_IT(true, true),
    DELEGATE_IT(true, false),
    SCHEDULE_IT(false, true),
    DELETE_IT(false, false);
    
    private boolean urgent;
    private boolean important;
    
    private Quadrant(boolean urgent, boolean important) {
        this.urgent = urgent;
        this.important = important;
    }
    
    public static List<Quadrant> linearPrioritySorting() {
        return Arrays.asList(DO_IT, DELEGATE_IT, SCHEDULE_IT, DELETE_IT);
    }
    
    public boolean isUrgent() {
        return urgent;
    }
    
    public boolean isImportant() {
        return important;
    }
}
