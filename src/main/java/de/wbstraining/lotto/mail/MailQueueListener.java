package de.wbstraining.lotto.mail;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.sun.mail.util.MailConnectException;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:jboss/exported/jms/queue/mymail"),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue") })
public class MailQueueListener implements MessageListener {

	@EJB
	private MailQueueSenderLocal mailProducer;

	private static final Logger LOG = Logger
			.getLogger(MailQueueListener.class.getName());
	private byte[] inMemoryPDF;
	private String toEmailAddress;
	private String mailSubject;
	private String mailContent;

	@Override
	public void onMessage(Message message) {
		getMailDetailsFrom(message);

		Properties props = new Properties();
		props.put("mail.smtp.host", "localhost");
		props.put("mail.smtp.socketFactory.port", "25");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "25");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication("lottouser", "test");
					}
				});

		try {

			javax.mail.Message mailMessage = new MimeMessage(session);
			mailMessage.setFrom(new InternetAddress("lottouser@lotto.test"));
			mailMessage.setRecipients(javax.mail.Message.RecipientType.TO,
					InternetAddress.parse(toEmailAddress));
			mailMessage.setSubject(mailSubject);

			MimeBodyPart content = new MimeBodyPart();
			content.setText(mailContent);

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(content);

			// add attachments
			MimeBodyPart attachment = new MimeBodyPart();
			ByteArrayDataSource source = new ByteArrayDataSource(inMemoryPDF,
					"application/pdf");
			attachment.setDataHandler(new DataHandler(source));
			attachment.setFileName("Quittung.pdf");
			multipart.addBodyPart(attachment);

			mailMessage.setContent(multipart);

			Transport.send(mailMessage);

			LOG.log(Level.INFO, "Email from queue sent.");
		} catch (MailConnectException mce) {
			LOG.log(Level.WARNING, "cannot connect to mail server");
			mailProducer.sendEmail(toEmailAddress, mailSubject, mailContent,
					inMemoryPDF, 2000L);
		} catch (MessagingException e) {
			LOG.log(Level.INFO, "email apparently not sent", e);
		}
		LOG.log(Level.INFO, "message received...");

	}

	private void getMailDetailsFrom(Message message) {

		MapMessage mapMsg = (MapMessage) message;

		try {
			inMemoryPDF = mapMsg.getBytes("inMemoryPDF");
			toEmailAddress = mapMsg.getString("toEmailAddress");
			mailSubject = mapMsg.getString("mailSubject");
			mailContent = mapMsg.getString("mailContent");

		} catch (JMSException ex) {
			Logger.getLogger(MailQueueListener.class.getName())
					.log(Level.SEVERE, null, ex);
		}
	}

}
