package beam.vortrag;

import java.io.Serializable;

import org.joda.time.DateTime;

public class AdvertisingBean implements Serializable{

	private static final long serialVersionUID = 4584150031296867712L;

	private String username;
	private DateTime eventtime;
	private String advertising;

	public AdvertisingBean(String username, String advertising, DateTime eventtime) {
		this.username = username;
		this.advertising = advertising;
		this.eventtime = eventtime;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public DateTime getEventtime() {
		return eventtime;
	}

	public void setEventtime(DateTime eventtime) {
		this.eventtime = eventtime;
	}

	public String getAdvertising() {
		return advertising;
	}

	public void setAdvertising(String advertising) {
		this.advertising = advertising;
	}
}
