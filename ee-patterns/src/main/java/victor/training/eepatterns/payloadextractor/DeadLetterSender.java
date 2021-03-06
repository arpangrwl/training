package victor.training.eepatterns.payloadextractor;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSDestinationDefinitions;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;

@Stateless
@JMSDestinationDefinitions({
	@JMSDestinationDefinition(name = "java:global/jms/my_error_queue", interfaceName = "javax.jms.Queue", destinationName = "error_out", description = "My Error Queue")
})
public class DeadLetterSender {

	@Resource(mappedName = "java:/ConnectionFactory")
	private ConnectionFactory factory;

	@Resource(mappedName = "java:global/jms/my_error_queue")
	private Queue deadLetterQueue;

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void sendDeadLetter(Message message, String errorString, Throwable throwable) {
		try {
			System.out.println("return message as dead letter");

			Connection conn = factory.createConnection();
			Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			session.createProducer(deadLetterQueue).send(message);
			conn.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// other escallation
		}
	}
}
