package dev.kxxcn.app_squad.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by kxxcn on 2018-05-18.
 */

public class Information implements Parcelable {

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
	private String enemy;
	private List<String> join;
	private String people;

	public Information() {

	}

	public Information(Parcel in) {
		this.email = in.readString();
		this.region = in.readString();
		this.place = in.readString();
		this.date = in.readString();
		this.time = in.readString();
		this.money = in.readString();
		this.rule = in.readString();
		this.age = in.readString();
		this.inquiry = in.readString();
		this.team = in.readString();
		this.enemy = in.readString();
		this.people = in.readString();
	}

	public Information(String team, String email, String region, String place, String date, String time, String money, String rule, String age, String inquiry, boolean isConnect, String people) {
		this.team = team;
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
		this.people = people;
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

	public String getEnemy() {
		return enemy;
	}

	public void setEnemy(String enemy) {
		this.enemy = enemy;
	}

	public List<String> getJoin() {
		return join;
	}

	public void setJoin(List<String> join) {
		this.join = join;
	}

	public String getPeople() {
		return people;
	}

	public void setPeople(String people) {
		this.people = people;
	}

	public static final Parcelable.Creator<Information> CREATOR = new Parcelable.Creator<Information>() {
		@Override
		public Information createFromParcel(Parcel source) {
			return new Information(source);
		}

		@Override
		public Information[] newArray(int size) {
			return new Information[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(email);
		dest.writeString(region);
		dest.writeString(place);
		dest.writeString(date);
		dest.writeString(time);
		dest.writeString(money);
		dest.writeString(rule);
		dest.writeString(age);
		dest.writeString(inquiry);
		dest.writeString(team);
		dest.writeString(enemy);
		dest.writeString(people);
	}

}
