package ru.svetozarov.Passenger;

import ru.svetozarov.Lift.ILift;
import ru.svetozarov.Other.ProjectConstants;

import java.util.Random;

/**
 * Created by Evgenij on 19.06.2017.
 * Класс описывает пассажира
 */
public class Passenger extends Thread implements ProjectConstants {

    private int startFloor;//этаж, с которого происходит вызов
    private int finalFloor;//конечный этаж
    private int id;//id пассажира
    private ILift lift;//Лифт, на котором поедет пассажир

    public Passenger(int startFloor, int finalFloor, ILift lift) {
        this.startFloor = startFloor;
        this.finalFloor = finalFloor;
        this.lift = lift;
    }

    public int getStartFloor() {
        return startFloor;
    }

    public int getFinalFloor() {
        return finalFloor;
    }

    /**
     * Функция описывает процесс поездки на лифте.
     * Сначала пассажир вызывает лифт  и ждет его брибытия,
     * затем заходит и указывает конечный этаж
     */
    public void callLift(){
        boolean route;
        if(startFloor>finalFloor){
            route = ROUTE_DOWN;
        }else{
            route = ROUTE_UP;
        }
        if(lift.callOuter(startFloor,route, id)) {
            while (lift.getFloor() != startFloor || lift.getState() != STATE_IN_OUT_PASSENGER
                    || lift.getRouteCallLift() != route) {
                Thread.yield();
            }
            lift.callInner(finalFloor, route, id);
        }
    }

    private void delay(){
        try {
            Thread.sleep(new Random().nextInt(500));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        this.id =(int) Thread.currentThread().getId();
        delay();
        callLift();
    }
}
