package org.ih.notification;

import org.ih.common.logging.Logger;
import org.ih.util.StringUtil;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

/**
 * @author Hector Plahar
 */
class Email {

    private static final String NO_REPLY_EMAIL = "no-reply@infinitihealth.org";
    private final String password = "xyp984ertpaSDODS7823de12343435wer";
    private static final String SMTP_HOST = "smtp.office365.com";

    public void send(String email, String subject, String body) {
        Logger.info("Sending email to " + email + " with subject " + subject);
//        String smtpHost = new Settings().getValue(ConfigurationValue.SMTP_HOST);

        if (StringUtil.isEmpty(SMTP_HOST)) {
            Logger.error("Smtp host has not been set. Aborting email send.");
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(NO_REPLY_EMAIL, password);
            }
        };

        Session session = Session.getDefaultInstance(props, auth);

        Message msg = new MimeMessage(session);

        try {
            msg.setFrom(new InternetAddress(NO_REPLY_EMAIL, "Infiniti Health"));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email, "Infiniti Health"));
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            msg.setText(body);
            Transport.send(msg);
        } catch (MessagingException | UnsupportedEncodingException e) {
            Logger.error(e);
        }
    }
}
