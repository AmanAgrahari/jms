import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;


public class TextMessageListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		
		if (message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) message;
			String text;
			System.out.println("aman");
			try {
				text = textMessage.getText();
				System.out.println(text);
				System.out.println("aman_try");
			} catch (JMSException e) {
				throw new RuntimeException(e);
			}
			
		}
		else {
			System.out.println("Invalid message received.");
		}
	}

}
