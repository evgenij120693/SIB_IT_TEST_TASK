package ru.svetozarov.Lift;

/**
 * Created by Evgenij on 19.06.2017.
 */
public interface ILift extends Runnable{
    public boolean callOuter(int floor, boolean route, int idPassenger);
    public boolean callInner(int floor, boolean route, int idPassenger);
    public int getFloor();
    public int getState();
    public boolean getRouteCallLift();
}
