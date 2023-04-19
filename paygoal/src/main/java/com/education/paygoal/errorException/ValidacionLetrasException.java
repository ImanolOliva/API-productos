package com.education.paygoal.errorException;

public class ValidacionLetrasException extends ErrorException{

    public ValidacionLetrasException() {
        super("Solo se deben ingresar letras");
    }
}
