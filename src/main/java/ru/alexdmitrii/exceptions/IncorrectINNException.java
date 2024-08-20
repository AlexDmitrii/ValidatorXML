package ru.alexdmitrii.exceptions;

public class IncorrectINNException extends Exception{

    public IncorrectINNException(){
        super("Некорректный ИНН.");
    }

}