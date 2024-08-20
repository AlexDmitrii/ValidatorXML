package ru.alexdmitrii.exceptions;

public class IncorrectAgeError extends Exception {

    public IncorrectAgeError(){
        super("Возраст заемщика не должен превышать 85 лет.");
    }

}
