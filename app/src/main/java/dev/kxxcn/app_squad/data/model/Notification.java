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
	private String flag;
	private String uncertainPlace;
	private String uncertainDate;
	private String uncertainTime;
	private String uncertainMoney;

	public Notification() {

	}

	public Notification(String message, String sender, String timestamp, boolean isCheck, String date, String type, String flag) {
		this.message = message;
		this.sender = sender;
		this.timestamp = timestamp;
		this.isCheck = isCheck;
		this.date = date;
		this.type = type;
		this.flag = flag;
	}

	public Notification(String message, String sender, String timestamp, boolean isCheck, String date, String type, String flag,
						String uncertainPlace, String uncertainDate, String uncertainTime, String uncertainMoney) {
		this.message = message;
		this.sender = sender;
		this.timestamp = timestamp;
		this.isCheck = isCheck;
		this.date = date;
		this.type = type;
		this.flag = flag;
		this.uncertainPlace = uncertainPlace;
		this.uncertainDate = uncertainDate;
		this.uncertainTime = uncertainTime;
		this.uncertainMoney = uncertainMoney;
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

	public String getFlag() {
		return flag;
	}

	public String getUncertainPlace() {
		return uncertainPlace;
	}

	public String getUncertainDate() {
		return uncertainDate;
	}

	public String getUncertainTime() {
		return uncertainTime;
	}

	public String getUncertainMoney() {
		return uncertainMoney;
	}

}
