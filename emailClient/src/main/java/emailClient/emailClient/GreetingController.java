package emailClient.emailClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;
import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GreetingController {

  @GetMapping("/serverDetails")
  public String greetingForm(Model model) {
    model.addAttribute("serverDetails", new ServerDetails());
    return "serverDetails";
  }

  @PostMapping("/emailDetails")
  public ResponseEntity<ArrayList<Response>> greetingSubmit(@Valid @RequestBody ServerDetails serverDetails) throws IOException{
	  ArrayList<Response> response = null;
	  if(serverDetails != null) {
		  String protocol = serverDetails.getProtocol();
	      String host = serverDetails.getServer();
	      String port = serverDetails.getPort();

	      String userName = serverDetails.getUserName();
	      String password = serverDetails.getPassword();
	      int noEmails = serverDetails.getNoEmails();
	      response = downloadEmails(protocol, host, port, userName, password, noEmails);
	      ModelAndView modelAndView = new ModelAndView();    
	      modelAndView.setViewName("result");        
	      modelAndView.addObject("response", response);
	      return ResponseEntity.ok(response);
	    //return modelAndView;
	  }else {
		  response = new ArrayList<Response>();
		  Response resp = new Response();
		  resp.setStatusCode(3);
		  resp.setStatusMessage("Parameters cannot be empty!!");
		  response.add(resp);
		  return ResponseEntity.ok(response);
	  }
	 
  }
  
  private Properties getServerProperties(String protocol, String host,
          String port) {
      Properties properties = new Properties();

      // server setting
      properties.put(String.format("mail.%s.host", protocol), host);
      properties.put(String.format("mail.%s.port", protocol), port);

      // SSL setting
      properties.setProperty(
              String.format("mail.%s.socketFactory.class", protocol),
              "javax.net.ssl.SSLSocketFactory");
      properties.setProperty(
              String.format("mail.%s.socketFactory.fallback", protocol),
              "false");
      properties.setProperty(
              String.format("mail.%s.socketFactory.port", protocol),
              String.valueOf(port));

      return properties;
  }
  
  private String parseAddresses(Address[] address) {
      String listAddress = "";

      if (address != null) {
          for (int i = 0; i < address.length; i++) {
              listAddress += address[i].toString() + ", ";
          }
      }
      if (listAddress.length() > 1) {
          listAddress = listAddress.substring(0, listAddress.length() - 2);
      }

      return listAddress;
  }

  
  public ArrayList<Response> downloadEmails(String protocol, String host, String port,
          String userName, String password, int noEmails) throws IOException {
      Properties properties = getServerProperties(protocol, host, port);
      Session session = Session.getDefaultInstance(properties);
      Response response = null;
      int limit = 0;
      ArrayList<Response> mailList = new ArrayList<Response>();
      try {
          // connects to the message store
          Store store = session.getStore(protocol);
          store.connect(userName, password);

          // opens the inbox folder
          Folder folderInbox = store.getFolder("INBOX");
          folderInbox.open(Folder.READ_ONLY);

          // fetches new messages from server
          Message[] messages = folderInbox.getMessages();
          if(messages.length > 0) {
        	  if(messages.length-1 > noEmails) {
        		  limit = noEmails;
        	  }else {
        		  limit = messages.length-1;
        	  }
        	  for (int i = messages.length-1; i > (messages.length - 1)-limit; i--) {
            	  response = new Response();
                  Message msg = messages[i];
                  Address[] fromAddress = msg.getFrom();
                  String from = fromAddress[0].toString();
                  String subject = msg.getSubject();
                  String toList = parseAddresses(msg
                          .getRecipients(RecipientType.TO));
                  String ccList = parseAddresses(msg
                          .getRecipients(RecipientType.CC));
                  String sentDate = msg.getSentDate().toString();

                  String contentType = msg.getContentType();
                  String messageContent = "";

                  if (contentType.contains("text/plain")
                          || contentType.contains("text/html")) {
                      try {
                          Object content = msg.getContent();
                          if (content != null) {
                              messageContent = content.toString();
                          }
                      } catch (Exception ex) {
                          messageContent = "[Error downloading content]";
                          ex.printStackTrace();
                      }
                  }else if(msg.isMimeType("multipart/*")) {
                	  MimeMultipart mimeMultipart = (MimeMultipart) msg.getContent();
                	  messageContent = getTextFromMimeMultipart(mimeMultipart);
                  }
                  response.setFrom(from);
                  response.setTo(toList);
                  response.setSubject(subject);
                  response.setMessage(messageContent);
                  response.setStatusCode(0);
                  response.setStatusMessage("Success");
                  mailList.add(response);
              }

          }else {
        	  response = new Response();
              response.setStatusCode(4);
              response.setStatusMessage("No emails found!");
              mailList.add(response);
          }
          // disconnect
          folderInbox.close(false);
          store.close();
      } catch (NoSuchProviderException ex) {
          System.out.println("No provider for protocol: " + protocol);
          response = new Response();
          response.setStatusCode(2);
          response.setStatusMessage("Protocol Error");
          mailList.add(response);
          ex.printStackTrace();
      } catch (MessagingException ex) {
          System.out.println("Could not connect to the message store");
          response = new Response();
          response.setStatusCode(1);
          response.setStatusMessage("Authentication Error-Incorrect Username/Password or Port");
          mailList.add(response);
          ex.printStackTrace();
      }
      return mailList;
  }
  
  private String getTextFromMimeMultipart(
	        MimeMultipart mimeMultipart)  throws MessagingException, IOException{
	    String result = "";
	    int count = mimeMultipart.getCount();
	    for (int i = 0; i < count; i++) {
	        BodyPart bodyPart = mimeMultipart.getBodyPart(i);
	        if (bodyPart.isMimeType("text/plain")) {
	            result = result + "\n" + bodyPart.getContent();
	            break; // without break same text appears twice in my tests
	        } else if (bodyPart.isMimeType("text/html")) {
	            String html = (String) bodyPart.getContent();
	            result = result + "\n";
	        } else if (bodyPart.getContent() instanceof MimeMultipart){
	            result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
	        }
	    }
	    return result;
	}
}