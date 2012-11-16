package com.siberhus.mailberry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.siberhus.fakesmtp.FakeSmtpServer;

public class FakeSmtpTest {
	
	@Test
	public void test() throws MessagingException{
//		Properties props = new Properties();
//		props.put("mail.smtp.host", "localhost");
//		Session session = Session.getDefaultInstance(props, null);
//		
//		System.out.println(session.getTransport("smtp").getClass());
//		SMTPTransport
		FakeSmtpServer server = new FakeSmtpServer();
		server.start();
		
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		sender.setHost("localhost");
		sender.setPort(25);
		
		List<MimeMessage> messages = new ArrayList<MimeMessage>();
		for(int i=0;i<10;i++){
			MimeMessage message = sender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, false);
			helper.setTo(RandomStringUtils.randomAlphanumeric(8)+"@siberhus.com");
			helper.setFrom("admin@siberhus.com");
			helper.setSubject("Test subject");
			helper.setText("Hello Everybody");
			messages.add(message);
		}
		try{
			sender.send(messages.toArray(new MimeMessage[0]));
		}catch(MailSendException e){
			System.out.println(e.getFailedMessages());
		}finally{
			server.stop();
		}
		System.out.println(server.getReceivedEmails());
		
	}
	
	
	
	public static void main(String[] args) {
		
		for(int i=0;i<10;i++){
			Random rand = new Random();
			if(rand.nextInt(10)>5){
				System.out.println("Hello");
			}
		}
	}
}
