package com.prueba.service.mail;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service("mailService")
public class MailServiceImpl implements MailService {
	private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);
	@Autowired
	private JavaMailSenderImpl mailSender;

	public void send(String to, String subject, String text) throws MessagingException {
		send(to, subject, text, null);
	}

	public void send(String to, String subject, String text,
			List<File> attachments) throws MessagingException {

		// La propiedades se recuperan de la session que se configura con Spring
		// que recupera los datos de JNDI
		Properties properties = mailSender.getSession().getProperties();
		
		// asegurando la trazabilidad
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Mail Property size:"+properties.size());
			Iterator<Map.Entry<Object, Object>> iter = properties.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<Object, Object> entry = (Map.Entry<Object, Object>) iter.next();
				LOGGER.debug("Mail Property:"+entry);
				LOGGER.debug("Mail Property key:"+entry.getKey());
				LOGGER.debug("Mail Property value:"+entry.getValue());
			}
		}
		
		String from = properties.getProperty("mail.from");
		mailSender.setHost(properties.getProperty("mail.smtp.host").trim());
		mailSender.setPort(Integer.parseInt(properties.getProperty("mail.smtp.port").trim()));
		mailSender.setUsername(properties.getProperty("mail.smtp.user"));
		mailSender.setPassword(properties.getProperty("mail.smtp.password"));
		mailSender.setProtocol(properties.getProperty("mail.transport.protocol"));
		mailSender.setJavaMailProperties(properties);
		
		final boolean usingPassword = !"".equals(mailSender.getPassword());
		
		final MimeMessage message = mailSender.createMimeMessage();

		// el flag a true indica que va a ser multipart
		final MimeMessageHelper helper = new MimeMessageHelper(message, true);

		// settings de los parametros del envio
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(text);
		helper.setFrom(from);
		
//		message.setFrom(new InternetAddress(properties.getProperty("mail.from")));
		// asegurando la trazabilidad
		if (LOGGER.isDebugEnabled()) {	
			LOGGER.debug("Sending email to: '" + to + "' [through host: '"
					+ mailSender.getHost() + ":" + mailSender.getPort()
					+ "', username: '" + mailSender.getUsername()
					+ "', from: '" + from
					+ "' usingPassword:" + usingPassword + "].");
		}
		// plantilla para el envio de email
		

		// adjuntando los ficheros
		if (attachments != null && attachments.size() > 0) {
			for (int i = 0; i < attachments.size(); i++) {
				FileSystemResource file = new FileSystemResource(attachments
						.get(i));
				helper.addAttachment(attachments.get(i).getName(), file);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("File '" + file + "' attached.");
				}
			}
		}

		try {
			this.mailSender.send(message);
			LOGGER.info("Enviado mail a: '" + to + "' [ host: '"
						+ mailSender.getHost() + ":" + mailSender.getPort()
						+ "', usuario: '" + mailSender.getUsername()
						+ "', de: '" + from
						+ "' usandoPass:" + usingPassword + "].");

		} catch (org.springframework.mail.MailException e) {
			LOGGER.warn(e.getMessage());
			throw new MessagingException("Fallo al enviar mail:"+e.getMessage());
		}

	}
}