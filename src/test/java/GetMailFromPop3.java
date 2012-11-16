import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;


public class GetMailFromPop3 {

	//IMAP
	//http://stackoverflow.com/questions/61176/getting-mail-from-gmail-into-java-application-using-imap
	public static void main(String[] args) throws Exception{
		Properties props = new Properties();
		props.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.pop3.socketFactory.fallback", "false");
		props.setProperty("mail.pop3.port", "995");
		props.setProperty("mail.pop3.socketFactory.port", "995");
	    String host = "pop.gmail.com";
	    String username = "chanutsomton@gmail.com";
	    String password = "malilawan";
	    String provider = "pop3";
	    
	    Session session = Session.getDefaultInstance(props, null);
	    Store store = session.getStore(provider);
	    store.connect(host, username, password);
	    
	    Folder inbox = store.getFolder("Inbox");
	    if (inbox == null) {
	      System.out.println("No INBOX");
	      System.exit(1);
	    }
	    inbox.open(Folder.READ_WRITE);
	    System.out.println(inbox.getMessageCount());
	    System.out.println(inbox.getNewMessageCount());
//	    Message[] messages = inbox.getMessages();
//	    for (int i = 0; i < messages.length; i++) {
//	      System.out.println("Message " + (i + 1));
//	      messages[i].writeTo(System.out);
//	    }
	    inbox.close(false);
	    store.close();
	}
	
	/**
	 * 
Hotmail now supports pop3 (through SSL).

Thus, you need the following settings:

pop3Props.setProperty("mail.pop3.ssl.enable", "true");

For all other properties, you must add a "s" in the properties string (so it says "pop3s" instead of "pop3"):

pop3Props.setProperty("mail.pop3s.socketFactory.class", SSL_FACTORY); pop3Props.setProperty("mail.pop3s.socketFactory.fallback", "false"); pop3Props.setProperty("mail.pop3s.port", "995"); pop3Props.setProperty("mail.pop3s.socketFactory.port", "995");

For me, the following code works nicely:

String host = "pop3.live.com";
String username = "laqetqetqet@hotmail.com";
String password = "rqetqetq";

Properties pop3Props = new Properties();
pop3Props.setProperty("mail.pop3s.port",  "995");

Session session = Session.getInstance(pop3Props, null);
Store store = session.getStore("pop3s");
store.connect(host, 995, username, password);
	 */
}
