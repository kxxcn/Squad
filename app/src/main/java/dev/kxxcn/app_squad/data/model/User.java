package dev.kxxcn.app_squad.data.model;

/**
 * Created by kxxcn on 2018-05-08.
 */

public class User {

	private String email;
	private String uid;
	private String team;
	private String token;

	public User() {

	}

	public User(String email, String uid, String team, String token) {
		this.email = email;
		this.uid = uid;
		this.team = team;
		this.token = token;
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

}
