package ru.alexdmitrii.exceptions;

public class IncorrectTermLoanException extends Exception{

    public IncorrectTermLoanException(int minTerm, int maxTerm){
        super("Срок кредита должен быть больше " + minTerm + " и меньше " + maxTerm + " месяцев.");

    }
}
