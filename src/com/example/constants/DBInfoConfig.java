package com.example.constants;

public class DBInfoConfig {

	public static final int DB_VERSION = 1;
	public static final String DB_NAME = "yjy.db";
	//初始化数据库
	public static final String CREATE_TABLE_PPLIST = "create table if not exists pp_list(list_id integer primary key,list_pid,list_name)";
	public static final String CREATE_TABLE_PPVOD = "create table if not exists pp_vod(vod_id integer primary key,vod_cid,vod_name,vod_sourceid,vod_pic,vod_addtime,vod_hits,vod_content,vod_up,vod_down,vod_gold)";
	public static final String CREATE_TABLE_PLAY_HISTORY = "create table if not exists play_history(his_id integer primary key AUTOINCREMENT,vod_id,playtime)";
	public static final String CREATE_TABLE_PLAY_LIKES = "create table if not exists play_likes(likes_id integer primary key AUTOINCREMENT,vod_id)";
	public static final String DELETE_TABLE_PPLIST_RECORDS ="delete from pp_list";
	public static final String DELETE_TABLE_PPVOD_RECORDS ="delete from pp_vod";
	//Service 中使用：
	public static final String SELECT_ROOT_LIST_ID = " select list_id from pp_list where list_pid='0'";
	public static final String INSERT_INTO_PPLIST = "insert into pp_list values(?,?,?)";
	public static final String INSERT_INTO_PPVOD = "insert into pp_vod values(?,?,?,?,?,?,?,?,?,?,?)";
//	public static final String INSERT_INTO_PLAY_LIKES = "insert into play_likes values(?,?)";
//OperateDAO中使用：
	public static final String SELECT_VOD_BY_PPLIST = "select * from pp_vod where vod_cid=?";
	public static final String SELECT_VOD_BY_LIST_PID="select * from pp_vod,pp_list where vod_cid=list_id and list_pid=?";
	public static final String SELECT_LATEST_VOD = "select * from pp_vod order by vod_addtime desc";
	public static final String SELECT_CHOSEN_VOD = "select * from pp_vod order by vod_hits desc";
	public static final String SELECT_HISTORY_VOD = "select * from play_history order by playtime desc";
	public static final String SELECT_LIKES_VOD = "select * from play_likes order by likes_id desc";
	public static final int NUM_OF_GET_LATEST_VOD = 10;
	public static final int NUM_OF_GET_CHOSEN_VOD = 10;
	public static final int NUM_OF_GET_HISTORY_VOD = 10;
	public static final int NUM_OF_GET_LIKES_VOD = 10;
	
	//VideoPlayActivity中使用:
	public static final String SELECT_PPLIST_ITEM_BY_LISTID = "select * from pp_list where list_id=?";
	
	//SearchView相关使用：
	public static final String SELECT_VODS_LIKE_STRING = "select * from pp_vod where vod_name like ? or vod_content like ?";
	public static final String SELECT_VODS_BY_VOD_ID = "select * from pp_vod where vod_id=?";
	public static final String SELECT_VODS_BY_LIKES_ID = "select * from play_likes where likes_id=?";
	
}
