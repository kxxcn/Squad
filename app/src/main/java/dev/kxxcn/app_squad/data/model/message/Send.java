package dev.kxxcn.app_squad.data.model.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kxxcn on 2018-06-12.
 */

public class Send {
	@SerializedName("to")
	@Expose
	private String to;
	@SerializedName("data")
	@Expose
	private Data data;

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
}
