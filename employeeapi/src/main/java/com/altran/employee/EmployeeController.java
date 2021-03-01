package com.altran.employee;


import com.altran.reservation.ReservationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/reservation")
public class EmployeeController {

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity sendMessageToFirstQueue(@Valid @RequestBody Reservation reservation) throws JsonProcessingException, TException {
        ObjectMapper objectMapper=new ObjectMapper();
        String messageAsString = objectMapper.writeValueAsString(reservation);
        System.out.println("@@@@@@@@@@ Writing message {} to Reservation Thrift server {}"+ messageAsString +" @@@@@@@@@@");

        TTransport transport = new TSocket("localhost", 8616);
        transport.open();

        com.altran.reservation.Reservation reservation1=new com.altran.reservation.Reservation();

       reservation1.setCheckInTime(reservation.getCheckInTime());
       reservation1.setEmailId(reservation.getEmailId());
       reservation1.setCity(reservation.getCity());
       reservation1.setCheckOutTime(reservation.getCheckOutTime());
        TProtocol protocol = new TBinaryProtocol(transport);
        ReservationService.Client client = new ReservationService.Client(protocol);

        System.out.println("default values ");
        if(client.save(reservation1)){
            transport.close();
            return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\":\"we will email you soon about your reservation\"}");
        } else {
            transport.close();
            return ResponseEntity.badRequest().body("Invalid Input");
        }

    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest().body("{\"message\":\"Invalid Input\"}");
    }
}