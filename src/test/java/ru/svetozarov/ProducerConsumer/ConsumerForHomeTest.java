package ru.svetozarov.ProducerConsumer;

import org.junit.Before;
import org.junit.Test;
import ru.svetozarov.Lift.ElectricMotor.ElectricMotor;
import ru.svetozarov.Lift.Lift;
import ru.svetozarov.Passenger.Passenger;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Evgenij on 22.06.2017.
 */
public class ConsumerForHomeTest {
    ConsumerForHome consumerForHome;
    Lift passengerLift;
    Lift freightLift;
    ArrayList<Passenger> arrayPassenger;
    @Before
    public void init(){
         consumerForHome = new ConsumerForHome();
         passengerLift = new Lift(new ElectricMotor(0,0,0),
                5, "test-1");
         freightLift = new Lift(new ElectricMotor(0,0,0),
                5, "test-2");
    }
    @Test
    public void startLift() throws Exception {
        int countThread = Thread.activeCount();
        consumerForHome.startLift(passengerLift,freightLift);
        assertTrue(Thread.activeCount() == countThread+2);
    }

    @Test
    public void startPassenger() throws Exception {
        arrayPassenger = new ArrayList<>();
        arrayPassenger.add(new Passenger(5,4, passengerLift));
        arrayPassenger.add(new Passenger(1,3, freightLift));
        int countThread = Thread.activeCount();
        consumerForHome.startPassenger(arrayPassenger);
        assertTrue(Thread.activeCount() == countThread+2);
    }

}