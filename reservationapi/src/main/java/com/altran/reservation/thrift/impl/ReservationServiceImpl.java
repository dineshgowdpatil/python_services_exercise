package com.altran.reservation.thrift.impl;

import com.altran.reservation.ReservationAvailabilityService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService.Iface {

    ReservationAvailabilityService reservationAvailabilityService;

    public ReservationServiceImpl(final ReservationAvailabilityService reservationAvailabilityService) {
        this.reservationAvailabilityService = reservationAvailabilityService;
    }

    @Override
    public Reservation get() throws TException {
        System.out.println("return next reservation object");
        return new Reservation();
    }

    @Override
    public boolean save(Reservation reservation) throws TException {
        System.out.println("save reservation object to reservation server");
        ReservationNotification reservationNotification=new ReservationNotification();
        reservationNotification.setConfirmed(reservationAvailabilityService.checkAvailability(reservation));
        if(!reservationNotification.isConfirmed())
            reservationNotification.setSlotList(slotAdapter(reservationAvailabilityService.availableSlots(reservation)));
        TTransport transport = new TSocket("localhost", 8617);
        transport.open();
        reservationNotification.setCheckInTime(reservation.getCheckInTime());
        reservationNotification.setEmailId(reservation.getEmailId());
        reservationNotification.setCity(reservation.getCity());
        reservationNotification.setCheckOutTime(reservation.getCheckOutTime());
        TProtocol protocol = new TBinaryProtocol(transport);
        NotificationService.Client client = new NotificationService.Client(protocol);
       if (client.save(reservationNotification)){
           System.out.println("Posted Successfully to notification server");
        transport.close();
        return true;
       } else {
           System.out.println("Failed to Post to notification server ");
           transport.close();
           return false;
       }

    }

    private static List<Slot> slotAdapter(List<com.altran.reservation.Slot> slotList){
        return slotList.stream().map(slot -> { return new Slot(slot.getSid(),slot.getStartTime(),slot.getEndTime());}).collect(Collectors.toList());
    }

    @Override
    public boolean status() throws TException {
        System.out.println("Check whether thrift server is up!!");
        return true;
    }
}

/*
  employeeapi:
    image: employeeapi:0.0.1-SNAPSHOT
    ports:
      - 8613:8613
    depends_on:
      localstack:
        condition: service_healthy
      db:
        condition: service_healthy
  reservationapi:
    image: reservationapi:0.0.1-SNAPSHOT
    ports:
      - 8614:8614
    depends_on:
      localstack:
        condition: service_healthy
      db:
        condition: service_healthy
  notificationapi:
    image: notificationapi:0.0.1-SNAPSHOT
    ports:
      - 8615:8615
    depends_on:
      localstack:
        condition: service_healthy
      db:
        condition: service_healthy
 */