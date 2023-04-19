package com.education.paygoal.errorException;

public class ListaVaciaException extends ErrorException {
    public ListaVaciaException() {
        super("La lista esta vacia");
    }
}
