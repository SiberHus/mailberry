import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.siberhus.mailberry.email.SpamAssassinInvoker;

class StreamGobbler extends Thread {
	InputStream is;
	String type;
	OutputStream os;

	StreamGobbler(InputStream is, String type) {
		this(is, type, null);
	}

	StreamGobbler(InputStream is, String type, OutputStream redirect) {
		this.is = is;
		this.type = type;
		this.os = redirect;
	}

	public void run() {
		try {
			PrintWriter pw = null;
			if (os != null)
				pw = new PrintWriter(os);

			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				if (pw != null)
					pw.println(line);
				System.out.println(type + ">" + line);
			}
			if (pw != null)
				pw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}

public class SpamAssassin {

	public static void main(String[] args) throws Exception {
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom("ceo@siberhus.com");
		helper.setTo("developer@siberhus.com");
		helper.setSubject("Test222222222");
//		helper.setText("If your spam filter supports it, the GTUBE provides a test by which you " +
//				"can verify that the filter is installed correctly and is detecting incoming spam. " +
//				"You can send yourself a test mail containing the following string of characters " +
//				"(in upper case and with no white spaces and line breaks \n" +
//				"XJS*C4JDBQADN1.NSBN3*2IDNEN*GTUBE-STANDARD-ANTI-UBE-TEST-EMAIL*C.34X");
		helper.setText("Dear\n${firstName} ${lastName}\nYour party is ${party}\nwebsite\n$[copyright]");
		
//		Properties props = System.getProperties();
//		props.setProperty("mail.smtp.host", "localhost");
//		props.put("mail.transport.protocol", "smtp");
//		Session mailSession = Session.getDefaultInstance(props, null);
//		File emlFile = new File("src/main/webapp/WEB-INF/spamassassin/sample-nonspam.txt");
//		InputStream source = new FileInputStream(emlFile);
//		MimeMessage message = new MimeMessage(Session.getInstance(new Properties()), source);
//		System.out.println(message.getFrom()[0]);
//		System.out.println(message.getContent());
		SpamAssassinInvoker i = new SpamAssassinInvoker("localhost", 783);
		i.scanMail(message);
		System.out.println(i.getScore());
		System.out.println(i.getRequiredScore());
		System.out.println(i.getAnalysisDetails());
		
	}
}
