package emailClient.emailClient;

public class ServerDetails {

  private String userName;
  private String password;
  private String server;
  private String port;
  private String protocol;
  private int noEmails;
  
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public int getNoEmails() {
		return noEmails;
	}
	public void setNoEmails(int noEmails) {
		this.noEmails = noEmails;
	}
	
}