package com.catchmind.pro.util;

import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JOptionPane;

public class MailExam {

	public String mailPasswordFind(String address, String password) {
		try {
			Properties props = new Properties();
			props.setProperty("mail.transport.protocol", "smtp");
			props.setProperty("mail.host", "smtp.gmail.com");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.socketFactory.fallback", "false");
			props.setProperty("mail.smtp.quitwait", "false");

			Authenticator auth = new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("wkdwhdrb1212@gmail.com", "jik55166");
				}
			};

			Session session = Session.getDefaultInstance(props, auth);

			MimeMessage message = new MimeMessage(session);
			message.setSender(new InternetAddress("wkdwhdrb1212@gmail.com"));
			message.setSubject("[ ĳġ�� �ӽú�й�ȣ ]");

			message.setRecipient(Message.RecipientType.TO, new InternetAddress(address));

			Multipart mp = new MimeMultipart();
			MimeBodyPart mbpl = new MimeBodyPart();
			mbpl.setText("������ ĳġ�� �ӽú�й�ȣ�� [ " + password + " ] �Դϴ�.");
			mp.addBodyPart(mbpl);

			MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
			mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
			mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
			mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
			mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
			mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
			CommandMap.setDefaultCommandMap(mc);

			message.setContent(mp);

			Transport.send(message);

			System.out.println("[���ۿϷ�!]");
			JOptionPane.showMessageDialog(null, "�ӽ� ��й�ȣ�� ������ ��ϵ� �̸��Ϸ� �����߽��ϴ�.!");
			return "@pwFindOK";
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "�̸������ۿ� �����߽��ϴ�.");
		}
		return "@pwFindFail";
	}

	public boolean mailJoinConfirm(String address, String confirm) {
		try {
			Properties props = new Properties();
			props.setProperty("mail.transport.protocol", "smtp");
			props.setProperty("mail.host", "smtp.gmail.com");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.socketFactory.fallback", "false");
			props.setProperty("mail.smtp.quitwait", "false");

			Authenticator auth = new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("wkdwhdrb1212@gmail.com", "jik55166");
				}
			};

			Session session = Session.getDefaultInstance(props, auth);

			MimeMessage message = new MimeMessage(session);
			message.setSender(new InternetAddress("wkdwhdrb1212@gmail.com"));
			message.setSubject("[ĳġ�� ��������]");

			message.setRecipient(Message.RecipientType.TO, new InternetAddress(address));

			Multipart mp = new MimeMultipart();
			MimeBodyPart mbpl = new MimeBodyPart();
			mbpl.setText("ĳġ�� �̸��� ������ȣ��  [ " + confirm + " ] �Դϴ�.");
			mp.addBodyPart(mbpl);

			MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
			mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
			mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
			mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
			mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
			mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
			CommandMap.setDefaultCommandMap(mc);

			message.setContent(mp);

			Transport.send(message);

			System.out.println("[���ۿϷ�!]");
			JOptionPane.showMessageDialog(null, "������ȣ ���ۿϷ� !");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "������ȣ�� �����µ� �����߽��ϴ�. �´������� �̸������� Ȯ���غ�����.");
			return false;
		}
	}
}
