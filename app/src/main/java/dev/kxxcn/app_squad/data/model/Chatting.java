package dev.kxxcn.app_squad.data.model;

/**
 * Created by kxxcn on 2018-07-10.
 */

public class Chatting {

	private int key;
	private String message;
	private String from;
	private String uid;
	private String time;

	public Chatting() {
	}

	public Chatting(String message, String from, String uid, String time) {
		this.message = message;
		this.from = from;
		this.uid = uid;
		this.time = time;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getMessage() {
		return message;
	}

	public String getFrom() {
		return from;
	}

	public String getUid() {
		return uid;
	}

	public String getTime() {
		return time;
	}

}
