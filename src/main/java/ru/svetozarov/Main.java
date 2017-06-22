package ru.svetozarov;

import ru.svetozarov.Exception.ExceptionCountFloor;
import ru.svetozarov.Home.Home;


/**
 * Created by Evgenij on 19.06.2017.
 */
public class Main {
    public static void main(String[] args) {
        try {
            Home home = new Home(9, 100);
            home.startWork();
        } catch (ExceptionCountFloor exceptionCountFloor) {
            exceptionCountFloor.printStackTrace();
        }
    }
}
