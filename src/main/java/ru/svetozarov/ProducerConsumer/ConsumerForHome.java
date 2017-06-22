package ru.svetozarov.ProducerConsumer;

import org.apache.log4j.Logger;
import ru.svetozarov.Lift.ILift;
import ru.svetozarov.Passenger.Passenger;

import java.util.ArrayList;

/**
 * Created by Evgenij on 21.06.2017.
 */
public class ConsumerForHome {
    private static Logger logger = Logger.getLogger(ConsumerForHome.class);
    public void startLift(ILift firstLift, ILift secondLift){
        Thread thread1 = new Thread(firstLift);
        Thread thread2 = new Thread(secondLift);
        thread1.start();
        thread2.start();
    }

    public void startPassenger(ArrayList<Passenger> arrayPassenger){
        for (Passenger passenger :
                arrayPassenger ) {
            passenger.start();
        }
    }
}
