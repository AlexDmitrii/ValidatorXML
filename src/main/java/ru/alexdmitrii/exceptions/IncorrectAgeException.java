package ru.alexdmitrii.exceptions;

public class IncorrectAgeException extends Exception {

    public IncorrectAgeException(){
        super("Возраст заемщика не должен превышать 85 лет.");
    }

}
