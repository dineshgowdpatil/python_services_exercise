package com.altran.notification;

import com.altran.notification.thrift.impl.NotificationService;
import com.altran.notification.thrift.impl.NotificationServiceImpl;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@SpringBootApplication(exclude = {ContextStackAutoConfiguration.class, DataSourceAutoConfiguration.class})
@EnableWebMvc
public class NotificationApplication {

	private static ApplicationContext applicationContext;

	public static void main(String[] args) {
			applicationContext=SpringApplication.run(NotificationApplication.class, args);
			TServerTransport transport= null;
			try {
				transport = new TServerSocket(8617);
			} catch (TTransportException e) {
				e.printStackTrace();
			}
			TServer server=new TThreadPoolServer(new TThreadPoolServer.Args(transport)
					.processor(new NotificationService.Processor(new NotificationServiceImpl(applicationContext.getBean(NotificationController.class)))));
			System.out.println("Notification Thrift server");
			server.serve();
		}
	}

