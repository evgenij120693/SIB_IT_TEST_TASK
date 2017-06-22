package ru.svetozarov.Other;

/**
 * Created by Evgenij on 20.06.2017.
 */
public interface ProjectConstants {
    int MIN_NUMBER_FLOOR = 2;
    boolean CALL_OUTER = false;
    boolean CALL_INNER = true;
    boolean ROUTE_UP = true;
    boolean ROUTE_DOWN = false;
    int STATE_MOVE_UP_EMPTY = 4;
    int STATE_MOVE_DOWN_EMPTY = 3;
    int STATE_MOVE_UP = 2;//движение ввверх с пассажиром(ами)
    int STATE_MOVE_DOWN = 1;//движение вниз с пассажиром(ами)
    int STATE_STOP = 0;// конечная остановка, после которой лифт свободен
    int STATE_IN_OUT_PASSENGER = 5;// промежуточные остановки
    int TIME_DOWN_FOR_PASSENGER_LIFT = 1500;
    int TIME_UP_FOR_PASSENGER_LIFT = 2000;
    int TIME_STOP_FOR_PASSENGER_LIFT = 2000;
    int TIME_DOWN_FOR_FREIGHT_LIFT = 2000;
    int TIME_UP_FOR_FREIGHT_LIFT = 2500;
    int TIME_STOP_FOR_FREIGHT_LIFT = 2500;
    String PASSENGER_LIFT = "Пассажирский";
    String FREIGHT_LIFT = "Грузовой";

}
