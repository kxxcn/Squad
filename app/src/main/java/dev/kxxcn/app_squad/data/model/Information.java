package dev.kxxcn.app_squad.data.model;

/**
 * Created by kxxcn on 2018-05-18.
 */

public class Information {

	private String email;
	private String region;
	private String place;
	private String date;
	private String time;
	private String money;
	private String rule;
	private String age;
	private String inquiry;
	private boolean isConnect;
	private String team;
	private String battle;

	public Information() {

	}

	public Information(String email, String region, String place, String date, String time, String money, String rule, String age, String inquiry, boolean isConnect) {
		this.email = email;
		this.region = region;
		this.place = place;
		this.date = date;
		this.time = time;
		this.money = money;
		this.rule = rule;
		this.age = age;
		this.inquiry = inquiry;
		this.isConnect = isConnect;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getInquiry() {
		return inquiry;
	}

	public void setInquiry(String inquiry) {
		this.inquiry = inquiry;
	}

	public boolean isConnect() {
		return isConnect;
	}

	public void setConnect(boolean connect) {
		isConnect = connect;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public String getBattle() {
		return battle;
	}

	public void setBattle(String battle) {
		this.battle = battle;
	}

}
