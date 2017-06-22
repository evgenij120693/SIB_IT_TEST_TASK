package ru.svetozarov.ProducerConsumer;

import org.apache.log4j.Logger;
import ru.svetozarov.Lift.ElectricMotor.ElectricMotor;
import ru.svetozarov.Lift.ILift;
import ru.svetozarov.Lift.Lift;
import ru.svetozarov.Passenger.Passenger;
import ru.svetozarov.Other.ProjectConstants;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Evgenij on 21.06.2017.
 * Продюсер, создает лифты и пассажиров
 */
public class ProducerForHome implements ProjectConstants {
    private static Logger logger =  Logger.getLogger(ProducerForHome.class);
    public ArrayList<Passenger> createPassenger(int numberPassenger, int numberFloor, ILift lift) {
        ArrayList<Passenger> arrayPassenger = new ArrayList<>(numberPassenger);
        Random random = new Random();
        int startFloor;
        int finalFloor;
        while (numberPassenger != 0) {
            startFloor = random.nextInt(numberFloor) + 1;
            do {
                finalFloor = random.nextInt(numberFloor) + 1;
            } while (finalFloor == startFloor);
            arrayPassenger.add(new Passenger(startFloor, finalFloor, lift));
            numberPassenger--;
        }
        return arrayPassenger;
    }

    public ILift createLift(String typeLift, int numberFloor) {
        if (typeLift == PASSENGER_LIFT)
            return new Lift(new ElectricMotor(TIME_UP_FOR_PASSENGER_LIFT, TIME_DOWN_FOR_PASSENGER_LIFT,
                    TIME_STOP_FOR_PASSENGER_LIFT), numberFloor, typeLift);
        else
            return new Lift(new ElectricMotor(TIME_UP_FOR_FREIGHT_LIFT, TIME_DOWN_FOR_FREIGHT_LIFT,
                    TIME_STOP_FOR_FREIGHT_LIFT), numberFloor, typeLift);
    }
}
