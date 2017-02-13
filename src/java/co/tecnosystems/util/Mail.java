/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.tecnosystems.util;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 *
 */
public class Mail {

    private static Session getMailOrdenes() throws NamingException {

        javax.naming.Context c = new InitialContext();
        return (Session) c.lookup("mail/construsenales");
    }

    public static void sendMail(String email, String subject, String body) throws NamingException, MessagingException {
        Session mailorden = getMailOrdenes();
        MimeMessage message = new MimeMessage(mailorden);
        message.setSubject(subject);
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
        message.setText(body);
        Transport.send(message);
    }

}
