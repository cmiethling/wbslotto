package de.wbstraining.lotto.mail;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Queue;

@Stateless
public class MailQueueSender implements MailQueueSenderLocal {

	@Resource(mappedName = "java:jboss/exported/jms/queue/mymail")
	private Queue mailQueue;

	@Inject
	@JMSConnectionFactory("java:comp/DefaultJMSConnectionFactory")
	private JMSContext context;

	private static final Logger LOG = Logger.getLogger(MailQueueSender.class.getName());

	@Override
	public void sendEmail(String toEmailAddress, String mailSubject, String mailContent, byte[] inMemoryPDF) {
		try {
			MapMessage mapMessage = createMessage(toEmailAddress, mailSubject, mailContent, inMemoryPDF);
			context.createProducer().send(mailQueue, mapMessage);
			LOG.log(Level.INFO, "Mail Message queued");
		} catch (JMSException ex) {
			Logger.getLogger(MailQueueSender.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void sendEmail(String toEmailAddress, String mailSubject, String mailContent, byte[] inMemoryPDF, Long delay) {
		try {
			MapMessage mapMessage = createMessage(toEmailAddress, mailSubject, mailContent, inMemoryPDF);
			context.createProducer().setDeliveryDelay(delay).send(mailQueue, mapMessage);
			LOG.log(Level.INFO, "Delayed Mail Message queued");
		} catch (JMSException ex) {
			Logger.getLogger(MailQueueSender.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private MapMessage createMessage(String toEmailAddress, String mailSubject, String mailContent, byte[] inMemoryPDF) throws JMSException {
		MapMessage mapMessage = context.createMapMessage();
		mapMessage.setString("toEmailAddress", toEmailAddress);
		mapMessage.setString("mailSubject", mailSubject);
		mapMessage.setString("mailContent", mailContent);
		mapMessage.setBytes("inMemoryPDF", inMemoryPDF);
		return mapMessage;
	}

}