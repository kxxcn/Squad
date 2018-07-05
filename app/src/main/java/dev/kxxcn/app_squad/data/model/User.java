package dev.kxxcn.app_squad.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kxxcn on 2018-05-08.
 */

public class User implements Parcelable {

	private String email;
	private String uid;
	private String team;
	private String token;
	private String age;
	private String intro;
	private Notice notice;

	public User() {

	}

	public User(Parcel in) {
		this.email = in.readString();
		this.uid = in.readString();
		this.team = in.readString();
		this.token = in.readString();
		this.age = in.readString();
		this.intro = in.readString();
	}

	public User(String email, String uid, String team, String token) {
		this.email = email;
		this.uid = uid;
		this.team = team;
		this.token = token;
		this.notice = new Notice(true, true, true);
	}

	public String getEmail() {
		return email;
	}

	public String getUid() {
		return uid;
	}

	public String getTeam() {
		return team;
	}

	public String getToken() {
		return token;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public Notice getNotice() {
		return notice;
	}

	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
		@Override
		public User createFromParcel(Parcel source) {
			return new User(source);
		}

		@Override
		public User[] newArray(int size) {
			return new User[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(email);
		dest.writeString(uid);
		dest.writeString(team);
		dest.writeString(token);
		dest.writeString(age);
		dest.writeString(intro);
	}

}
