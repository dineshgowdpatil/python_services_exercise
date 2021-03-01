package com.altran.reservation.thrift.impl;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServlet;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.apache.thrift.transport.TTransportException;

import javax.servlet.Servlet;

@SpringBootApplication
@EnableWebMvc
@EnableAutoConfiguration
public class ReservationThriftServer {

	public static void main(String[] args) throws TTransportException{
		SpringApplication.run(ReservationThriftServer.class, args);
		TServerTransport transport= null;
		try {
			transport = new TServerSocket(8616);
		} catch (TTransportException e) {
			e.printStackTrace();
		}
		TServer server=new TThreadPoolServer(new TThreadPoolServer.Args(transport)
				.processor(new ReservationService.Processor(new ReservationServiceImpl())));
		System.out.println("called");
		server.serve();
	}
}
