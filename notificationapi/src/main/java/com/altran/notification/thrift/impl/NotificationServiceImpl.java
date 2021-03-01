package com.altran.notification.thrift.impl;

import com.altran.notification.NotificationController;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService.Iface {

    private NotificationController notificationController;

    public NotificationServiceImpl(final NotificationController notificationController) {
        this.notificationController = notificationController;
    }

    @Override
    public ReservationNotification get() throws TException {
        System.out.println("Dummy method!!");
        return null;
    }

    @Override
    public boolean save(ReservationNotification reservationNotification) throws TException {
       return notificationController.sendEmail(reservationNotification);

    }


    @Override
    public boolean status() throws TException {
        System.out.println("Check whether notification thrift server is up!!");
        return true;
    }
}