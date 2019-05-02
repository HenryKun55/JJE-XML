package email;

import java.util.*;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JRException;
import util.PrintReport;
import util.Zip;

import java.io.UnsupportedEncodingException;

public class Email {
	
	static Usuario usuario;
	static Zip zip = new Zip();

        public void send() throws MessagingException, UnsupportedEncodingException {
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

            String file = "C:\\Temp\\XML.zip";
            String fileName = "XML_"+usuario.getAno()+"_"+usuario.getMes()+".zip";
            DataSource source = new FileDataSource(file);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setDisposition(Part.ATTACHMENT);
            messageBodyPart.setFileName(fileName);

            PrintReport printReport = new PrintReport();
            try {
                printReport.showReport(usuario.getHostControl());
            } catch (JRException e) {
                e.printStackTrace();
            }

            String file2 = "C:\\Temp\\report.pdf";
            DataSource source2 = new FileDataSource(file2);
            BodyPart messageBodyPart2 = new MimeBodyPart();
            messageBodyPart2.setDataHandler(new DataHandler(source2));
            messageBodyPart2.setFileName(file2);

            String body = "<HTML><HEAD></HEAD><BODY>Você está recebendo um relatório de vendas juntamente com os arquivos XMLs de NFC-e do cliente <b>" + usuario.getNomeEmpresa() + "</b> . <br>" +
                    "Este e-mail é enviado automaticamente, caso queira resolver alguma pendência favor entrar em contato com nosso suporte técnico nos canais abaixo: <br>" +
                    "E-mail: suporte@jjeautomacao.com.br <br>" +
                    "Telefones: 81 3719-3557/81 3136-0155 <br>" +
                    "WhatsApp: 81 99859-4479 <br>" +
                    "<br> Atenciosamente,<br>" +
                    "<br>" +
                    "Equipe JJE Automação<BODY></HTML>";

            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent( body, "text/html; charset=utf-8" );

            multipart.addBodyPart(htmlPart);
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(messageBodyPart2);

            message.setContent(multipart);
            
            Transport.send(message);

            JOptionPane.showMessageDialog(null, "Enviado com sucesso!");

       } catch (MessagingException e) {
    	   JOptionPane.showMessageDialog(null, "Email não Enviado, Verifique seus dados!", "JJE", 1);
    	   throw new RuntimeException(e);
      }
    }
}