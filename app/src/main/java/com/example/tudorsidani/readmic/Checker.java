package com.example.tudorsidani.readmic;

/**
 * Created by tudor on 13.04.2017.
 */

public interface Checker {
    boolean isStatusOk();
    String getErrorMessage();
    void clearObject();
}
