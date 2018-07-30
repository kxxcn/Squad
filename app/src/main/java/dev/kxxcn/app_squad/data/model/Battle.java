package dev.kxxcn.app_squad.data.model;

/**
 * Created by kxxcn on 2018-06-25.
 */

public class Battle {

	private String enemy;
	private String date;
	private String place;
	private boolean home;
	private String flag;

	public Battle() {
	}

	public Battle(String enemy, String date, String place, boolean home, String flag) {
		this.enemy = enemy;
		this.date = date;
		this.place = place;
		this.home = home;
		this.flag = flag;
	}

	public String getEnemy() {
		return enemy;
	}

	public String getDate() {
		return date;
	}

	public String getPlace() {
		return place;
	}

	public boolean isHome() {
		return home;
	}

	public String getFlag() {
		return flag;
	}

}
