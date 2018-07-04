package dev.kxxcn.app_squad.data.model;

/**
 * Created by kxxcn on 2018-06-12.
 */

public class Notification {

	private int key;
	private String message;
	private String sender;
	private String timestamp;
	private boolean isCheck;
	private String date;
	private String type;

	public Notification() {

	}

	public Notification(String message, String sender, String timestamp, boolean isCheck, String date, String type) {
		this.message = message;
		this.sender = sender;
		this.timestamp = timestamp;
		this.isCheck = isCheck;
		this.date = date;
		this.type = type;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int getKey() {
		return key;
	}

	public String getMessage() {
		return message;
	}

	public String getSender() {
		return sender;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setCheck(boolean check) {
		isCheck = check;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public String getDate() {
		return date;
	}

	public String getType() {
		return type;
	}

}
