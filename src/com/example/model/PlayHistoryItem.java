package com.example.model;

public class PlayHistoryItem {

	//play_history的表结构(his_id integer primary key AUTOINCREMENT,vod_id,playtime)
	private String his_id;
	private String vod_id;
	private String playtime;
	
	public PlayHistoryItem() {
		// TODO Auto-generated constructor stub
	}
	public PlayHistoryItem(String his_id, String vod_id, String playtime) {
		super();
		this.his_id = his_id;
		this.vod_id = vod_id;
		this.playtime = playtime;
	}
	public String getHis_id() {
		return his_id;
	}
	public void setHis_id(String his_id) {
		this.his_id = his_id;
	}
	public String getVod_id() {
		return vod_id;
	}
	public void setVod_id(String vod_id) {
		this.vod_id = vod_id;
	}
	public String getPlaytime() {
		return playtime;
	}
	public void setPlaytime(String playtime) {
		this.playtime = playtime;
	}
	@Override
	public String toString() {
		return "PlayHistoryItem [his_id=" + his_id + ", vod_id=" + vod_id
				+ ", playtime=" + playtime + "]";
	}
	
	
}
