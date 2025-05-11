package utils;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailSender {
    private static final String EMAIL_HOST = "travelagencygui@gmail.com";
    private static final String APP_PASSWORD = "";


    public static String sendEmail( String to) throws MessagingException {

        String code=CodeGenerator.generateCode();
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host","smtp.gmail.com");
        props.put("mail.smtp.port", "587");



        Session session=Session.getInstance(props,new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_HOST, APP_PASSWORD);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(EMAIL_HOST));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject("Password Reset Code");
        message.setText("Your password reset code is: " + code);
        Transport.send(message);
        System.out.println("Sent message successfully to : "+to);
        return code;

    }
}
