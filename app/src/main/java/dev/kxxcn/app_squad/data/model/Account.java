package dev.kxxcn.app_squad.data.model;

/**
 * Created by kxxcn on 2018-06-11.
 */

public class Account {

	private static User user;

	public Account(User user) {
		this.user = user;
	}

	public static User getInstance() {
		if (user != null) {
			return user;
		}

		return null;
	}
}
