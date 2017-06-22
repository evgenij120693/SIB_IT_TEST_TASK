package ru.svetozarov.ProducerConsumer;

import org.junit.Before;
import org.junit.Test;
import ru.svetozarov.Lift.ElectricMotor.ElectricMotor;
import ru.svetozarov.Lift.Lift;
import ru.svetozarov.Other.ProjectConstants;
import ru.svetozarov.Passenger.Passenger;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Evgenij on 22.06.2017.
 */
public class ProducerForHomeTest implements ProjectConstants{
    ProducerForHome producerForHome;
    Lift lift;
    int numberFloor, numberPassenger;
    @Before
    public void init(){
        producerForHome = new ProducerForHome();
        lift = new Lift(new ElectricMotor(0,0,0),
                5, "test-1");
        numberFloor = 9;
        numberPassenger = 100;
    }

    @Test
    public void createPassenger() throws Exception {
        ArrayList<Passenger> arrayList = producerForHome.createPassenger(numberPassenger, numberFloor,lift);
        assertNotNull(arrayList);
        assertTrue(arrayList.size() == numberPassenger);
    }

    @Test
    public void createLift() throws Exception {
        assertNotNull(producerForHome.createLift(PASSENGER_LIFT, numberFloor));
        assertNotNull(producerForHome.createLift(FREIGHT_LIFT, numberFloor));
    }

}