package dev.kxxcn.app_squad.data.model;

/**
 * Created by kxxcn on 2018-05-18.
 */

public class Information {

	private String region;
	private String place;
	private String date;
	private String time;
	private String money;
	private String rule;
	private String inquiry;

	public Information(String region, String place, String date, String time, String money, String rule, String inquiry) {
		this.region = region;
		this.place = place;
		this.date = date;
		this.time = time;
		this.money = money;
		this.rule = rule;
		this.inquiry = inquiry;
	}

	public String getRegion() {
		return region;
	}

	public String getPlace() {
		return place;
	}

	public String getDate() {
		return date;
	}

	public String getTime() {
		return time;
	}

	public String getMoney() {
		return money;
	}

	public String getRule() {
		return rule;
	}

	public String getInquiry() {
		return inquiry;
	}

}
