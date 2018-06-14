package dev.kxxcn.app_squad.data.model.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kxxcn on 2018-06-12.
 */

public class Data {
	@SerializedName("title")
	@Expose
	private String title;
	@SerializedName("message")
	@Expose
	private String message;
	@SerializedName("sender")
	@Expose
	private String sender;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}
}
