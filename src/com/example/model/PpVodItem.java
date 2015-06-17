package com.example.model;

import java.io.Serializable;

import android.util.Log;

public class PpVodItem implements Serializable{

	private static final long serialVersionUID = -7060210544600464481L; 
	//vod_id integer primary key,vod_cid,vod_name,vod_sourceid,vod_pic
	private String vod_id; //视频id
	private String vod_cid; //组id
	private String vod_name;
	private String vod_sourceid;
	private String vod_pic;//存储内容举例http://219.223.199.96:80/data/userdata/vod/images/201311/1806032472.jpg
	//2014-4-21接口 扩展 ,新添 以下三项
	private String vod_addtime;//视频添加时间的时间戳
	private String vod_hits;//点击数
	private String vod_content;//视频内容概要
	
	private String vod_up;  //点赞的人数
	private String vod_down; //点踩的人数
	private String vod_gold; //平均分
	
	
	public PpVodItem() {
		// TODO Auto-generated constructor stub
	}
	public PpVodItem(String vod_id, String vod_cid, String vod_name,
			String vod_sourceid, String vod_pic,String vod_addtime,String vod_hits,String vod_content,
			String vod_up,String vod_down,String vod_gold) {
		super();
		this.vod_id = vod_id;
		this.vod_cid = vod_cid;
		this.vod_name = vod_name;
		this.vod_sourceid = vod_sourceid;
		this.vod_pic = vod_pic;
		this.vod_addtime = vod_addtime;
		this.vod_hits = vod_hits;
		this.vod_content = vod_content; 
		this.vod_up= vod_up;
		this.vod_down= vod_down;
		this.vod_gold= vod_gold;
	}
	public String getVod_id() {
		return vod_id;
	}
	public void setVod_id(String vod_id) {
		this.vod_id = vod_id;
	}
	public String getVod_cid() {
		return vod_cid;
	}
	public void setVod_cid(String vod_cid) {
		this.vod_cid = vod_cid;
	}
	public String getVod_name() {
		return vod_name;
	}
	public void setVod_name(String vod_name) {
		this.vod_name = vod_name;
	}
	public String getVod_sourceid() {
		return vod_sourceid;
	}
	public void setVod_sourceid(String vod_sourceid) {
		this.vod_sourceid = vod_sourceid;
	}
	public String getVod_pic() {
		return vod_pic;
	}
	public void setVod_pic(String vod_pic) {
		this.vod_pic = vod_pic;
	}
	public String getVod_addtime() {
		return vod_addtime;
	}
	public void setVod_addtime(String vod_addtime) {
		this.vod_addtime = vod_addtime;
	}
	public String getVod_hits() {
		return vod_hits;
	}
	public void setVod_hits(String vod_hits) {
		this.vod_hits = vod_hits;
	}
	
	public String getVod_content() {
		return vod_content;
	}
	public void setVod_content(String vod_content) {
		this.vod_content = vod_content;
	}
	
	public String getVod_up() {
		return vod_up;
	}
	public void setVod_up(String vod_up){
		this.vod_up= vod_up;
	}
	
	public String getVod_down() {
		return vod_down;
	}
	public void setVod_down(String vod_down){
		this.vod_down= vod_down;
	}
	
	public String getVod_gold() {
		return vod_gold;
	}
	public void setVod_gold(String vod_gold){
		this.vod_gold= vod_gold;
	}
	 
	@Override
	public String toString() {
		return "PpVodItem [vod_id=" + vod_id + ", vod_cid=" + vod_cid
				+ ", vod_name=" + vod_name + ", vod_sourceid=" + vod_sourceid
				+ ", vod_pic=" + vod_pic + ", vod_addtime=" + vod_addtime
				+ ", vod_hits=" + vod_hits + ", vod_content=" + vod_content
				+", vod_up=" + vod_up +", vod_down=" + vod_down +", vod_gold=" + vod_gold
				+ "]";
	}


	
}
