package ru.svetozarov.Home;

import org.apache.log4j.Logger;
import ru.svetozarov.ProducerConsumer.ConsumerForHome;
import ru.svetozarov.Exception.ExceptionCountFloor;
import ru.svetozarov.Lift.ILift;
import ru.svetozarov.Passenger.Passenger;
import ru.svetozarov.ProducerConsumer.ProducerForHome;
import ru.svetozarov.Other.ProjectConstants;

import java.util.ArrayList;

/**
 * Created by Evgenij on 21.06.2017.
 * Класс, описывающий жилой дом. Содержит в себе пассажиров, которые поедут
 * на пассажирском лифте (arrayPassengerForSmallLift) и на грузовом (arrayPassengerForBigLift).
 * Функция создние лифтов и пассажиров делегируется  consumerForHome.
 * Функция запуска лифтов и пассажиров делегируется producerForHome.
 */
public class Home implements ProjectConstants {
    private static Logger logger =  Logger.getLogger(Home.class);
    private ProducerForHome producerForHome = new ProducerForHome();
    private ConsumerForHome consumerForHome = new ConsumerForHome();
    private ArrayList<Passenger> arrayPassengerForSmallLift;
    private ArrayList<Passenger> arrayPassengerForBigLift;
    private ILift passengerLift;
    private ILift freightLift;
    private int numberFloor;
    private int numberPassenger;


    public ILift getPassengerLift() {
        return passengerLift;
    }

    public ILift getFreightLift() {
        return freightLift;
    }

    public int getNumberFloor() {
        return numberFloor;
    }

    public int getNumberPassenger() {
        return numberPassenger;
    }

    public ArrayList<Passenger> getArrayPassengerForSmallLift() {
        return arrayPassengerForSmallLift;
    }

    public ArrayList<Passenger> getArrayPassengerForBigLift() {
        return arrayPassengerForBigLift;
    }

    public Home(int numberFloor, int numberPassenger) throws ExceptionCountFloor {
        if(numberFloor < MIN_NUMBER_FLOOR)
            throw new ExceptionCountFloor("Количество этажей в доме не должно быть меньше "+ MIN_NUMBER_FLOOR);
        this.numberFloor = numberFloor;
        this.numberPassenger = numberPassenger;
        logger.trace("Количество этажей в доме "+ numberFloor);
        logger.trace("Количество пасссажиров в доме "+ numberPassenger);
    }

    private void createPassenger(){
        arrayPassengerForSmallLift = producerForHome.createPassenger(numberPassenger /2, numberFloor, passengerLift);
        arrayPassengerForBigLift = producerForHome.createPassenger(numberPassenger /2, numberFloor, freightLift);
    }

    private void createLift(){
        passengerLift = producerForHome.createLift(PASSENGER_LIFT, numberFloor);
        freightLift = producerForHome.createLift(FREIGHT_LIFT, numberFloor);
    }
    private void startPassenger(){
        consumerForHome.startPassenger(arrayPassengerForSmallLift);
        consumerForHome.startPassenger(arrayPassengerForBigLift);
    }
    private void startLift(){
        consumerForHome.startLift(passengerLift, freightLift);
    }
    public void startWork(){
        createLift();
        createPassenger();
        startLift();
        startPassenger();
    }
}
