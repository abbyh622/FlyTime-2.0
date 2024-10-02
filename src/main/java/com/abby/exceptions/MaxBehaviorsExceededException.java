package com.abby.exceptions;

// exception for trying to create an Experiment with > 9 behaviors
public class MaxBehaviorsExceededException extends Exception {
    public MaxBehaviorsExceededException(String message) {
        super(message);
    }
}