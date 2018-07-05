package dev.kxxcn.app_squad.data.model;

/**
 * Created by kxxcn on 2018-07-05.
 */

public class Notice {

	private boolean squad;
	private boolean notice;
	private boolean event;

	public Notice() {
	}

	public Notice(boolean squad, boolean notice, boolean event) {
		this.squad = squad;
		this.notice = notice;
		this.event = event;
	}

	public boolean isSquad() {
		return squad;
	}

	public void setSquad(boolean squad) {
		this.squad = squad;
	}

	public boolean isNotice() {
		return notice;
	}

	public void setNotice(boolean notice) {
		this.notice = notice;
	}

	public boolean isEvent() {
		return event;
	}

	public void setEvent(boolean event) {
		this.event = event;
	}

}
