package ru.svetozarov.Home;

import org.junit.Test;
import ru.svetozarov.Exception.ExceptionCountFloor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * Created by Evgenij on 22.06.2017.
 */
public class HomeTest {
    public Home initHome() {
        try {
            return new Home(10, 4000);
        } catch (ExceptionCountFloor exceptionCountFloor) {
            exceptionCountFloor.printStackTrace();
        }
        return null;
    }

    @Test
    public void createLift() {
        Home home = initHome();
        Class homeClass = Home.class;
        try {
            Method createLift = homeClass.getDeclaredMethod("createLift");
            createLift.setAccessible(true);
            assertNull(home.getPassengerLift());
            assertNull(home.getFreightLift());
            createLift.invoke(home);
            assertNotNull(home.getPassengerLift());
            assertNotNull(home.getFreightLift());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createPassenger() {
        Home home = initHome();
        Class homeClass = Home.class;
        try {
            Method createPassenger = homeClass.getDeclaredMethod("createPassenger");
            createPassenger.setAccessible(true);
            assertNull(home.getArrayPassengerForBigLift());
            assertNull(home.getArrayPassengerForSmallLift());
            createPassenger.invoke(home);
            assertNotNull(home.getArrayPassengerForBigLift());
            assertNotNull(home.getArrayPassengerForSmallLift());
            assertTrue(home.getArrayPassengerForBigLift().size() == home.getNumberPassenger()/2);
            assertTrue(home.getArrayPassengerForSmallLift().size() == home.getNumberPassenger()/2);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createHomeFailed(){
        try {
            new Home(-10, 22);
            fail("При инициализация дома должна была произойти ошибка");
        } catch (ExceptionCountFloor exceptionCountFloor) {
            assertTrue(true);
        }
    }

}