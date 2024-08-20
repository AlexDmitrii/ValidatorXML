package ru.alexdmitrii;

public class Main {

    public static void main(String[] args) {
        LoanValidator validator = new LoanValidator("loan_application.xml");
        boolean isValid = validator.validate();

        if (isValid){
            System.out.println("Success!");
        }
    }

}
