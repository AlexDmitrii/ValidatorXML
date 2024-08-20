package ru.alexdmitrii.exceptions;

public class IncorrectAmountLoanException extends Exception {

    public IncorrectAmountLoanException(double minAmount, double maxAmount){
        super("Сумма кредита должна быть больше " + minAmount + " и меньше " + maxAmount + ".");
    }
}
