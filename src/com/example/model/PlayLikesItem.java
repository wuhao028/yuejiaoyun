package com.example.model;

public class PlayLikesItem {

	//play_history的表结构(his_id integer primary key AUTOINCREMENT,vod_id,playtime)
	private String likes_id;
	private String vod_id;
	
	public PlayLikesItem() {
		// TODO Auto-generated constructor stub
	}
	public PlayLikesItem(String likes_id, String vod_id) {
		super();
		this.likes_id = likes_id;
		this.vod_id = vod_id;
	}
	public String getLikes_id() {
		return likes_id;
	}
	public void setLikes_id(String likes_id) {
		this.likes_id = likes_id;
	}
	public String getVod_id() {
		return vod_id;
	}
	public void setVod_id(String vod_id) {
		this.vod_id = vod_id;
	}
	
	@Override
	public String toString() {
		return "PlayLikesItem [likes_id=" + likes_id + ", vod_id=" + vod_id
				 + "]";
	}
	
	
}
