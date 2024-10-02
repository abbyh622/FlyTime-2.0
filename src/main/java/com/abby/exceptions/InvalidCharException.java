package com.abby.exceptions;

// exception for trying to set unaccepted key binding
public class InvalidCharException extends Exception {
    public InvalidCharException() {
        // limited to alphanumeric because i want it to be
        super("Accepted keys are digits 0-9 and characters A-Z");
    }
}