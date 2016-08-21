package com.serfarmed.controllers.util;


import java.io.File;
import java.util.List;
import java.util.Properties;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Mail {

  public static void sendEmail(String to, String subject, String htmlContent, List<File> files) {
    
    final String username = "ultracolorucayali@gmail.com"; //ur email
    final String password = "ultracolor07";
//    final String To = "romanixd@gmail.com";

    Properties props = new Properties();
    props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
    props.put("mail.smtp.auth", "true");
    props.put("mail.debug", "false");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");

    Session session = Session.getInstance(props, new javax.mail.Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
      }
    });

    try {

      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(username));//ur email
      message.setRecipients(Message.RecipientType.TO,
              InternetAddress.parse(to));//u will send to
      message.setSubject(subject);
      
      MimeBodyPart messageBodyPart = new MimeBodyPart();
      Multipart multipart = new MimeMultipart();
      
////      messageBodyPart.setContent("<h2>¿Para qué se usa Spark?</h2>"
////              + "<p>Aunque Spark está basado en Sinatra, no se presenta como una competencia. Su finalidad es que haya una alternativa para los desarrolladores Java, que les permita <strong>desarrollar rápidamente aplicaciones web</strong> con el lenguaje que ya conocen.</p>"
////              + "<p>Según una <a target=\"_blank\" href=\"http://sparkjava.com/news.html#sparksurvey\">encuesta que realizó el equipo de Spark</a> este año, se dio a conocer que alrededor del <strong>50% de los usuarios de Spark lo usan para crear APIs REST</strong> y un <strong>25% lo usa para crear páginas web</strong>. También mencionan que el 15% de las aplicaciones Spark desplegadas sirven a más de 10.000 usuarios a diario. Con estos datos podemos concluir que es un framework adecuado si la finalidad es construir un API con Java de forma ágil.</p>",  "text/html; charset=utf-8");
      
      messageBodyPart.setContent(htmlContent, "text/html; charset=utf-8");
      
      multipart.addBodyPart(messageBodyPart); 
      
      
      //Attachhed files
      for (File file : files) {
        messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(file.getPath());
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(file.getName());
        multipart.addBodyPart(messageBodyPart); 
      }

             
      message.setContent(multipart);

      System.out.println("sending");
      Transport.send(message);
      System.out.println("Done");

    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }
}
