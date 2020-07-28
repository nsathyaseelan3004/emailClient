package emailClient.emailClient;

import java.util.ArrayList;

public class Response {
	private String from;
	private String to;
	private String subject;
	private String message;
	private ArrayList<Response> mailList;
	private int statusCode;
	private String statusMessage;

	public String getFrom() {
		return from;
	}

	public ArrayList<Response> getMailList() {
		return mailList;
	}

	public void setMailList(ArrayList<Response> mailList) {
		this.mailList = mailList;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
}