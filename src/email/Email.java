package email;

import java.util.*;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.JOptionPane;

import util.Zip;

import java.io.UnsupportedEncodingException;

public class Email {
	
	static Usuario usuario;
	static Zip zip = new Zip();

        public static void main(String[] args) throws MessagingException, UnsupportedEncodingException {
        	usuario = Usuario.getInstance();
	      	 Properties props = new Properties();
	      	 /** Parâmetros de conexão com servidor Gmail */
	      	props.put("mail.smtp.ssl.trust", usuario.getHost());
	      	props.setProperty("mail.transport.protocol", "smtp");   
	      	props.setProperty("mail.smtp.localhost", "JJE");
	        props.setProperty("mail.smtp.host", usuario.getHost());   
	        props.put("mail.smtp.auth", "true");   
	        props.put("mail.smtp.port", "587");   
	        props.put("mail.smtp.starttls.enable", "true");
	        
            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                         protected PasswordAuthentication getPasswordAuthentication() 
                         {
                               return new PasswordAuthentication(usuario.getRemetente(), usuario.getSenha());
                         }
                    });

        /** Ativa Debug para sessão */
        session.setDebug(true);

        try {

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(usuario.getRemetente(), usuario.getNomeEmpresa()));
            
            message.setHeader("X-Priority", "1");
            message.setHeader("Priority", "Urgent");
            message.setHeader("Importance", "high");	
			
            message.setRecipients(Message.RecipientType.TO, 
                              InternetAddress.parse(usuario.getDestinatario())); //Destinatário(s)
         
            message.setSubject(usuario.getAssunto());//Assunto
            
            //System.out.println(principal.getDestino()+"\\ano_"+usuario.getAno()+"\\mes_"+usuario.getMes());
            /**Método para enviar a mensagem criada*/
              
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            
            Multipart multipart = new MimeMultipart();

            messageBodyPart = new MimeBodyPart();
            String file = "C:\\Temp\\XML.zip";
            String fileName = "XML_"+usuario.getAno()+"_"+usuario.getMes()+".zip";
            DataSource source = new FileDataSource(file);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setDisposition(Part.ATTACHMENT);
            messageBodyPart.setFileName(fileName);
            
            MimeBodyPart messageBodyPartMessage = new MimeBodyPart();
            
            messageBodyPartMessage.setText("Você está recebendo um arquivo referente aos XMLs de vendas.");

            multipart.addBodyPart(messageBodyPartMessage);
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);
            
            Transport.send(message);

            JOptionPane.showMessageDialog(null, "Enviado com sucesso!");

       } catch (MessagingException e) {

    	   JOptionPane.showMessageDialog(null, "Email não Enviado, Verifique seus dados!", "JJE", 1);
            throw new RuntimeException(e);
      }
    }
}