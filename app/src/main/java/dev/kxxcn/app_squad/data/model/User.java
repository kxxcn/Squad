package dev.kxxcn.app_squad.data.model;

/**
 * Created by kxxcn on 2018-05-08.
 */

public class User {

	private String email;
	private String uid;
	private String team;

	public User() {

	}

	public User(String email, String uid, String team) {
		this.email = email;
		this.uid = uid;
		this.team = team;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

}
