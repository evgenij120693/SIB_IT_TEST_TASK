package ru.svetozarov.Lift;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.junit.Test;
import ru.svetozarov.Lift.Call.Call;
import ru.svetozarov.Lift.ElectricMotor.ElectricMotor;
import ru.svetozarov.Lift.ElectricMotor.IElectricMotor;
import ru.svetozarov.Other.ProjectConstants;

import javax.swing.text.StyledEditorKit;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.Assert.*;

/**
 * Created by Evgenij on 22.06.2017.
 */
public class LiftTest implements ProjectConstants {

    public Lift initLift(int numberFloor, String typeLift,
                         int timeUp, int timeDown, int timeStop) {
        Lift lift = new Lift(new ElectricMotor(timeUp, timeDown, timeStop), numberFloor, typeLift);
        return lift;
    }

    public int numberWaiting(int floor, boolean route, Lift lift) {
        Class classField = Lift.class;
        ConcurrentHashMap<Integer, Integer> numberWaitingOnTheFloor;
        String nameField = "numberWaitingOnTheFloorRU";
        if (route == ROUTE_DOWN)
            nameField = "numberWaitingOnTheFloorRD";
        try {
            Field numberWaitingOnTheFloorField = classField.getDeclaredField(nameField);
            numberWaitingOnTheFloorField.setAccessible(true);
            numberWaitingOnTheFloor = (ConcurrentHashMap<Integer, Integer>) numberWaitingOnTheFloorField.get(lift);
            return numberWaitingOnTheFloor.get(floor);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public CopyOnWriteArrayList<Call> getQueueCalls(Lift lift) {
        Class classField = Lift.class;
        try {
            Field queueCalls = classField.getDeclaredField("queueCalls");
            queueCalls.setAccessible(true);
            return (CopyOnWriteArrayList<Call>) queueCalls.get(lift);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test
    public void getRouteCallLift() {
        Lift lift = initLift(5, "test", 0, 0, 0);
        assertTrue(lift.getRouteCallLift() == ROUTE_UP);
    }


    @Test
    public void getFloor() {
        Lift lift = initLift(5, "test", 0, 0, 0);
        assertTrue(lift.getFloor() == 1);
    }

    @Test
    public void getState() {
        Lift lift = initLift(5, "test", 0, 0, 0);
        assertTrue(lift.getState() == STATE_STOP);
    }

    @Test
    public void callOuterConditionFirst() {
        Lift lift = initLift(5, "test", 0, 0, 0);
        int floor = 1;
        boolean route = ROUTE_UP;
        int idPassenger = 999;
        Class classLift = Lift.class;
        try {
            Field floorLift = classLift.getDeclaredField("floor");
            floorLift.setAccessible(true);
            floorLift.set(lift, 2);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        assertTrue(numberWaiting(floor, route, lift) == 0);
        assertTrue(lift.getFloor() == 2);
        assertTrue(lift.getFloor() > floor);
        assertTrue(lift.getState() == STATE_STOP);
        lift.callOuter(floor, route, idPassenger);
        assertTrue(numberWaiting(floor, route, lift) == 1);
        assertTrue(lift.getReady() == true);
        assertTrue(lift.getIdFirstPassenger() == idPassenger);
        assertTrue(lift.getState() == STATE_MOVE_DOWN_EMPTY);
        assertTrue(lift.getFinalFloor() == floor);
        assertTrue(lift.getRouteCallLift() == route);
    }

    @Test
    public void callOuterConditionSecond() {
        Lift lift = initLift(5, "test", 0, 0, 0);
        int floor = 3;
        boolean route = ROUTE_UP;
        int idPassenger = 999;
        assertTrue(numberWaiting(floor, route, lift) == 0);
        assertTrue(lift.getFloor() == 1);
        assertTrue(lift.getFloor() < floor);
        assertTrue(lift.getState() == STATE_STOP);
        boolean result = lift.callOuter(floor, route, idPassenger);
        assertTrue(result == true);
        assertTrue(numberWaiting(floor, route, lift) == 1);
        assertTrue(lift.getReady() == true);
        assertTrue(lift.getIdFirstPassenger() == idPassenger);
        assertTrue(lift.getState() == STATE_MOVE_UP_EMPTY);
        assertTrue(lift.getFinalFloor() == floor);
        assertTrue(lift.getRouteCallLift() == route);
    }

    @Test
    public void callOuterConditionThird() {
        Lift lift = initLift(5, "test", 0, 0, 0);
        int floor = 3;
        boolean route = ROUTE_UP;
        int idPassenger = 999;
        Class classLift = Lift.class;
        try {
            Field floorLift = classLift.getDeclaredField("floor");
            floorLift.setAccessible(true);
            floorLift.set(lift, floor);
            Field stateLift = classLift.getDeclaredField("state");
            stateLift.setAccessible(true);
            stateLift.set(lift, STATE_IN_OUT_PASSENGER);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        assertTrue(numberWaiting(floor, route, lift) == 0);
        assertTrue(lift.getFloor() == floor);
        assertTrue(lift.getRouteCallLift() == route);
        assertTrue(lift.getState() == STATE_IN_OUT_PASSENGER);
        boolean result = lift.callOuter(floor, route, idPassenger);
        assertTrue(result == true);
        assertTrue(numberWaiting(floor, route, lift) == 1);
        assertTrue(lift.getFloor() == floor);
        assertTrue(lift.getRouteCallLift() == route);
        assertTrue(lift.getState() == STATE_IN_OUT_PASSENGER);
    }

    @Test
    public void callOuterConditionFourth() {
        Lift lift = initLift(5, "test", 0, 0, 0);
        int floor = 1;
        boolean route = ROUTE_UP;
        int idPassenger = 999;
        assertTrue(numberWaiting(floor, route, lift) == 0);
        assertTrue(lift.getFloor() == floor);
        assertTrue(lift.getState() == STATE_STOP);
        boolean result = lift.callOuter(floor, route, idPassenger);
        assertTrue(result == true);
        assertTrue(numberWaiting(floor, route, lift) == 1);
        assertTrue(lift.getRouteCallLift() == route);
        assertTrue(lift.getState() == STATE_IN_OUT_PASSENGER);
        assertTrue(lift.getReady() == false);
    }

    @Test
    public void callOuterConditionFifth() {
        Lift lift = initLift(5, "test", 0, 0, 0);
        int floor = 2;
        boolean route = ROUTE_DOWN;
        int idPassenger = 999;
        Class classLift = Lift.class;
        try {
            Field floorLift = classLift.getDeclaredField("floor");
            floorLift.setAccessible(true);
            floorLift.set(lift, 3);
            Field stateLift = classLift.getDeclaredField("state");
            stateLift.setAccessible(true);
            stateLift.set(lift, STATE_MOVE_UP);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        CopyOnWriteArrayList<Call> queueCalls = getQueueCalls(lift);
        assertTrue(queueCalls.size() == 0);
        assertTrue(numberWaiting(floor, route, lift) == 0);
        boolean result = lift.callOuter(floor, route, idPassenger);
        assertTrue(result == true);
        assertTrue(numberWaiting(floor, route, lift) == 1);
        assertTrue(queueCalls.size() == 1);

    }

    @Test
    public void callOuterFailed() {
        Lift lift = initLift(5, "test", 0, 0, 0);
        int floor = 999;
        boolean route = ROUTE_DOWN;
        int idPassenger = 999;
        assertTrue(lift.callOuter(floor, route, idPassenger) == false);
    }

    @Test
    public void callInnerConditionFirst() throws Exception {
        Lift lift = initLift(5, "test", 0, 0, 0);
        int floor = 1;
        boolean route = ROUTE_DOWN;
        int idPassenger = 999;
        Class classLift = Lift.class;
        try {
            Field floorLift = classLift.getDeclaredField("floor");
            floorLift.setAccessible(true);
            floorLift.set(lift, 5);
            Field finalFloorLift = classLift.getDeclaredField("finalFloor");
            finalFloorLift.setAccessible(true);
            finalFloorLift.set(lift, 2);
            Field stateLift = classLift.getDeclaredField("state");
            stateLift.setAccessible(true);
            stateLift.set(lift, STATE_IN_OUT_PASSENGER);
            Field routeCallLift = classLift.getDeclaredField("routeCallLift");
            routeCallLift.setAccessible(true);
            routeCallLift.set(lift, ROUTE_DOWN);
            Method increment = classLift.getDeclaredMethod("incrementWaitingOnTheFloor",
                    int.class, boolean.class);
            increment.setAccessible(true);
            increment.invoke(lift, lift.getFloor(), route);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        CopyOnWriteArrayList<Call> queueCalls = getQueueCalls(lift);
        assertTrue(queueCalls.size() == 0);
        assertTrue(lift.getRouteCallLift() == ROUTE_DOWN);
        assertTrue(lift.getFinalFloor() < lift.getFloor());
        assertTrue(lift.getFinalFloor() > floor);
        assertTrue(numberWaiting(lift.getFloor(), route, lift) == 1);

        assertTrue(lift.callInner(floor, route, idPassenger));

        assertTrue(numberWaiting(lift.getFloor(), route, lift) == 0);
        assertTrue(lift.getFinalFloor() == floor);
        assertTrue(lift.getIdFirstPassenger() == idPassenger);
        assertTrue(queueCalls.size() == 1);
        assertTrue(lift.getReady());
        assertTrue(lift.getState() != STATE_IN_OUT_PASSENGER);
    }

    @Test
    public void callInnerConditionSecond() {
        Lift lift = initLift(5, "test", 0, 0, 0);
        int floor = 4;
        boolean route = ROUTE_UP;
        int idPassenger = 999;
        Class classLift = Lift.class;
        try {
            Field floorLift = classLift.getDeclaredField("floor");
            floorLift.setAccessible(true);
            floorLift.set(lift, 1);
            Field finalFloorLift = classLift.getDeclaredField("finalFloor");
            finalFloorLift.setAccessible(true);
            finalFloorLift.set(lift, 3);
            Field stateLift = classLift.getDeclaredField("state");
            stateLift.setAccessible(true);
            stateLift.set(lift, STATE_IN_OUT_PASSENGER);
            Field routeCallLift = classLift.getDeclaredField("routeCallLift");
            routeCallLift.setAccessible(true);
            routeCallLift.set(lift, ROUTE_UP);
            Method increment = null;
            increment = classLift.getDeclaredMethod("incrementWaitingOnTheFloor",
                    int.class, boolean.class);
            increment.setAccessible(true);
            increment.invoke(lift, lift.getFloor(), route);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        CopyOnWriteArrayList<Call> queueCalls = getQueueCalls(lift);
        assertTrue(queueCalls.size() == 0);
        assertTrue(lift.getRouteCallLift() == ROUTE_UP);
        assertTrue(lift.getFinalFloor() > lift.getFloor());
        assertTrue(lift.getFinalFloor() < floor);
        assertTrue(numberWaiting(lift.getFloor(), route, lift) == 1);

        assertTrue(lift.callInner(floor, route, idPassenger));

        assertTrue(numberWaiting(lift.getFloor(), route, lift) == 0);
        assertTrue(lift.getFinalFloor() == floor);
        assertTrue(lift.getIdFirstPassenger() == idPassenger);
        assertTrue(queueCalls.size() == 1);
        assertTrue(lift.getReady());
        assertTrue(lift.getState() != STATE_IN_OUT_PASSENGER);
    }

    @Test
    public void callInnerFailed() {
        Lift lift = initLift(5, "test", 0, 0, 0);
        int floor = 434;
        boolean route = ROUTE_UP;
        int idPassenger = 999;
        assertTrue(!lift.callInner(floor, route, idPassenger));
    }

    @Test
    public void checkCorrectFloorAndRoute() {
        Lift lift = initLift(5, "test", 0, 0, 0);
        Class classLift = Lift.class;
        try {
            Method checkCorrectFloorAndRoute = classLift.getDeclaredMethod("checkCorrectFloorAndRoute",
                    int.class, boolean.class, boolean.class);
            checkCorrectFloorAndRoute.setAccessible(true);
            int floor = 1;
            boolean route = ROUTE_DOWN;
            boolean modeCall = CALL_OUTER;
            assertTrue((boolean) checkCorrectFloorAndRoute.invoke(lift, floor, route, modeCall) == false);
            floor = 5;
            route = ROUTE_UP;
            assertTrue((boolean) checkCorrectFloorAndRoute.invoke(lift, floor, route, modeCall) == false);
            floor = 555555;
            assertTrue((boolean) checkCorrectFloorAndRoute.invoke(lift, floor, route, modeCall) == false);
            floor = 2;
            assertTrue((boolean) checkCorrectFloorAndRoute.invoke(lift, floor, route, modeCall) == true);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void runStateMoveUpEmpty() {
        Lift lift = initLift(5, "test", 0, 0, 0);
        int floor = 3;
        boolean route = ROUTE_UP;
        Class classLift = Lift.class;
        try {
            Field finalFloorLift = classLift.getDeclaredField("finalFloor");
            finalFloorLift.setAccessible(true);
            finalFloorLift.set(lift, floor);
            Field stateLift = classLift.getDeclaredField("state");
            stateLift.setAccessible(true);
            stateLift.set(lift, STATE_MOVE_UP_EMPTY);
            Field routeCallLift = classLift.getDeclaredField("routeCallLift");
            routeCallLift.setAccessible(true);
            routeCallLift.set(lift, route);
            Field ready = classLift.getDeclaredField("ready");
            ready.setAccessible(true);
            ready.set(lift, true);

            Thread threadLift = new Thread(lift);
            threadLift.start();
            Thread inputPassengerThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (lift.getState() != STATE_IN_OUT_PASSENGER)
                            Thread.yield();
                        Field ready = classLift.getDeclaredField("ready");
                        ready.setAccessible(true);
                        ready.set(lift, true);

                        Field stateLift = classLift.getDeclaredField("state");
                        stateLift.setAccessible(true);
                        stateLift.set(lift, STATE_STOP);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }) {{
                this.start();
            }};
            inputPassengerThread.join();
            threadLift.join();
            assertTrue(lift.getState() == STATE_STOP);
            assertTrue(lift.getFloor() == floor);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void runStateMoveDownEmpty() {
        Lift lift = initLift(5, "test", 0, 0, 0);
        int floor = 2;
        boolean route = ROUTE_UP;
        Class classLift = Lift.class;
        try {
            Field floorLift = classLift.getDeclaredField("floor");
            floorLift.setAccessible(true);
            floorLift.set(lift, 5);
            Field finalFloorLift = classLift.getDeclaredField("finalFloor");
            finalFloorLift.setAccessible(true);
            finalFloorLift.set(lift, floor);
            Field stateLift = classLift.getDeclaredField("state");
            stateLift.setAccessible(true);
            stateLift.set(lift, STATE_MOVE_DOWN_EMPTY);
            Field routeCallLift = classLift.getDeclaredField("routeCallLift");
            routeCallLift.setAccessible(true);
            routeCallLift.set(lift, route);
            Field ready = classLift.getDeclaredField("ready");
            ready.setAccessible(true);
            ready.set(lift, true);

            Thread threadLift = new Thread(lift);
            threadLift.start();
            Thread inputPassengerThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (lift.getState() != STATE_IN_OUT_PASSENGER)
                            Thread.yield();
                        Field ready = classLift.getDeclaredField("ready");
                        ready.setAccessible(true);
                        ready.set(lift, true);
                        Field stateLift = classLift.getDeclaredField("state");
                        stateLift.setAccessible(true);
                        stateLift.set(lift, STATE_STOP);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }) {{
                this.start();
            }};
            inputPassengerThread.join();
            threadLift.join();
            assertTrue(lift.getState() == STATE_STOP);
            assertTrue(lift.getFloor() == floor);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void runStateMoveUp() {
        Lift lift = initLift(5, "test", 0, 0, 0);
        int floor = 4;
        boolean route = ROUTE_UP;
        int idPassenger = 9999;
        Class classLift = Lift.class;
        try {

            Field finalFloorLift = classLift.getDeclaredField("finalFloor");
            finalFloorLift.setAccessible(true);
            finalFloorLift.set(lift, floor);

            Field stateLift = classLift.getDeclaredField("state");
            stateLift.setAccessible(true);
            stateLift.set(lift, STATE_MOVE_UP);

            Field routeCallLift = classLift.getDeclaredField("routeCallLift");
            routeCallLift.setAccessible(true);
            routeCallLift.set(lift, route);

            Field ready = classLift.getDeclaredField("ready");
            ready.setAccessible(true);
            ready.set(lift, true);

            CopyOnWriteArrayList<Call> queuCall = getQueueCalls(lift);
            queuCall.add(new Call(floor, route, CALL_INNER, idPassenger));
            assertTrue(queuCall.size() == 1);

            lift.run();

            assertTrue(lift.getState() == STATE_STOP);
            assertTrue(lift.getFloor() == floor);
            assertTrue(queuCall.size() == 0);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void runStateMoveDown() {
        Lift lift = initLift(5, "test", 0, 0, 0);
        int floor = 1;
        boolean route = ROUTE_DOWN;
        int idPassenger = 9999;
        Class classLift = Lift.class;
        try {

            Field finalFloorLift = classLift.getDeclaredField("finalFloor");
            finalFloorLift.setAccessible(true);
            finalFloorLift.set(lift, floor);

            Field floorLift = classLift.getDeclaredField("floor");
            floorLift.setAccessible(true);
            floorLift.set(lift, 4);

            Field stateLift = classLift.getDeclaredField("state");
            stateLift.setAccessible(true);
            stateLift.set(lift, STATE_MOVE_DOWN);

            Field routeCallLift = classLift.getDeclaredField("routeCallLift");
            routeCallLift.setAccessible(true);
            routeCallLift.set(lift, route);

            Field ready = classLift.getDeclaredField("ready");
            ready.setAccessible(true);
            ready.set(lift, true);

            CopyOnWriteArrayList<Call> queuCall = getQueueCalls(lift);
            queuCall.add(new Call(floor, route, CALL_INNER, idPassenger));
            assertTrue(queuCall.size() == 1);

            lift.run();

            assertTrue(lift.getState() == STATE_STOP);
            assertTrue(lift.getFloor() == floor);
            assertTrue(queuCall.size() == 0);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void runStateStop() {
        Lift lift = initLift(5, "test", 0, 0, 0);
        int floor = 4;
        boolean route = ROUTE_UP;
        int idPassenger = 9999;
        Class classLift = Lift.class;
        try {
            Field ready = classLift.getDeclaredField("ready");
            ready.setAccessible(true);
            ready.set(lift, true);

            CopyOnWriteArrayList<Call> queuCall = getQueueCalls(lift);
            queuCall.add(new Call(floor, route, CALL_OUTER, idPassenger));
            assertTrue(queuCall.size() == 1);
            Thread threadLift = new Thread(lift){{start();}};


            Thread inputPassengerThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (lift.getState() != STATE_IN_OUT_PASSENGER)
                            Thread.yield();
                        Field ready = classLift.getDeclaredField("ready");
                        ready.setAccessible(true);
                        ready.set(lift, true);
                        Field stateLift = classLift.getDeclaredField("state");
                        stateLift.setAccessible(true);
                        stateLift.set(lift, STATE_STOP);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }) {{
                this.start();
            }};
            inputPassengerThread.join();
            assertTrue(queuCall.size() == 0);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}