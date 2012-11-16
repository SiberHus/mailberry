package com.siberhus.mailberry.email;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpamAssassinInvoker {
	
	private final Logger log = LoggerFactory.getLogger(SpamAssassinInvoker.class);
	
	private String spamdHost;
	
	private int spamdPort;

	private String score = "?";

	private String required = "?";

	private List<String> details = new ArrayList<String>();
	
	/**
	 * Init the spamassassin invoker
	 * 
	 * @param spamdHost
	 *            The host on which spamd runs
	 * @param spamdPort
	 *            The port on which spamd listen
	 */
	public SpamAssassinInvoker(String spamdHost, int spamdPort) {
		this.spamdHost = spamdHost;
		this.spamdPort = spamdPort;
	}

	/**
	 * Scan a MimeMessage for spam by passing it to spamd.
	 * 
	 * @param message
	 *            The MimeMessage to scan
	 * @return true if spam otherwise false
	 * @throws MessagingException
	 *             if an error on scanning is detected
	 */
	public boolean scanMail(MimeMessage message) throws MessagingException {
		Socket socket = null;
		OutputStream out = null;
		BufferedReader in = null;
		
		try {
			log.debug("Connecting spamd server at {}:{}", 
					new Object[]{spamdHost, spamdPort});
			socket = new Socket(spamdHost, spamdPort);
			log.debug("Connected successfully.");
			out = socket.getOutputStream();
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			out.write("REPORT SPAMC/1.2\r\n\r\n".getBytes());
			
			// pass the message to spamd
			message.writeTo(out);
			out.flush();
			socket.shutdownOutput();
			boolean analysisPart = false;
			boolean spam = false;
			String s = null;
			while ((s = in.readLine()) != null) {
				log.debug("Server Response: {}", s);
				if (s.startsWith("Spam:")) {
					StringTokenizer t = new StringTokenizer(s, " ");
					try {
						t.nextToken();
						spam = Boolean.valueOf(t.nextToken()).booleanValue();
					} catch (Exception e) {
						// On exception return flase
						spam = false;
					}
					t.nextToken();
					score = t.nextToken();
					t.nextToken();
					required = t.nextToken();
				}else if(s.startsWith("Content analysis details:")){
					analysisPart = true;
					continue;
				}
				if(analysisPart){
					if(StringUtils.isBlank(s)) continue;
					details.add(s);
				}
			}
			return spam;
		} catch (UnknownHostException e1) {
			throw new MessagingException(
					"Error communicating with spamd. Unknown host: "
							+ spamdHost, e1);
		} catch (IOException e1) {
			throw new MessagingException("Error communicating with spamd on "
					+ spamdHost + ":" + spamdPort, e1);
		} catch (MessagingException e1) {
			throw new MessagingException("Error communicating with spamd on "
					+ spamdHost + ":" + spamdPort, e1);
		} finally {
			log.debug("Closing IO resources");
			try {
				in.close();
				out.close();
				socket.close();
			} catch (Exception e) {
				// Should never happin
			}

		}
	}

	/**
	 * Return the score which was returned by spamd
	 * 
	 * @return hits The score which was detected
	 */
	public String getScore() {
		return score;
	}

	/**
	 * Return the required score
	 * 
	 * @return required The required score before a message is handled as spam
	 */
	public String getRequiredScore() {
		return required;
	}

	
	public List<String> getAnalysisDetails() {
		return details;
	}
	
	public static void main(String[] args)throws Exception {
		Socket socket = new Socket("localhost", 54321);
		socket.close();
	}
}