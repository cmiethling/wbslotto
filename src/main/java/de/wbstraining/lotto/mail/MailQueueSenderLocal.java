package de.wbstraining.lotto.mail;

import javax.ejb.Local;

@Local
public interface MailQueueSenderLocal {

	void sendEmail(String toEmailAddress, String mailSubject, String mailContent, byte[] inMemoryPDF);
	void sendEmail(String toEmailAddress, String mailSubject, String mailContent, byte[] inMemoryPDF, Long delay);
	
}