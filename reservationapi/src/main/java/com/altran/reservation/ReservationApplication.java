package com.altran.reservation;

import com.altran.reservation.thrift.impl.ReservationService;
import com.altran.reservation.thrift.impl.ReservationServiceImpl;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableWebMvc
@EnableAutoConfiguration
public class ReservationApplication {

	private static ApplicationContext applicationContext;


	public static void main(String[] args) {
		applicationContext=SpringApplication.run(ReservationApplication.class, args);
		TServerTransport transport= null;
		try {
			transport = new TServerSocket(8616);
		} catch (TTransportException e) {
			e.printStackTrace();
		}
		TServer server=new TThreadPoolServer(new TThreadPoolServer.Args(transport)
				.processor(new ReservationService.Processor(new ReservationServiceImpl(applicationContext.getBean(ReservationAvailabilityService.class)))));
		System.out.println("Reservation Thrift server");
		server.serve();
	}
}
