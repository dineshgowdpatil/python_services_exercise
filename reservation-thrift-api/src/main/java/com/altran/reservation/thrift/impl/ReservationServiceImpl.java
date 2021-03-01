package com.altran.reservation.thrift.impl;


import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl implements ReservationService.Iface {


    @Override
    public Reservation get() throws TException {
        System.out.println("return next reservation object");
        return new Reservation();
    }

    @Override
    public boolean save(Reservation reservation) throws TException {
        System.out.println("Send reservation object to reservation api");
        return true;

    }

    @Override
    public boolean status() throws TException {
        System.out.println("Check whether thrift server is up!!");
        return true;
    }
}