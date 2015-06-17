package com.example.dao;

import com.example.constants.DBInfoConfig;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * DBOpenHelper 是操作数据库的助手类，功能：
 * 1、通过getReadableDatabase(),getWritableDatabase()可以获得SQLiteDatabse对象，通过该对象可以对数据库进行操作
 * 2、提供了onCreate()和onUpgrade()两个回调函数，允许我们在创建和升级数据库时，进行自己的操作
 *
 * 
 *
 */
public class DBOpenHelper extends SQLiteOpenHelper {

	private static final int VERSION = DBInfoConfig.DB_VERSION;
	
	public DBOpenHelper(Context context, String name) {
		super(context, name, null, VERSION);
		// TODO Auto-generated constructor stub
	}
	public DBOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public DBOpenHelper(Context context, String name, CursorFactory factory,
			int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		//创建pp_list，pp_vod 表，用以缓存从WebService得到的基本数据  create or replace table
		System.out.println("初次 建立数据库 内各表");
		//db.execSQL("create table pp_list(list_id integer primary key,list_pid,list_oid,list_sid,list_name,list_skin,list_dir,list_keywords,list_title,list_description);");
		//db.execSQL("create table pp_vod(vod_id integer primary key,vod_cid,vod_name,vod_title,vod_keywords,vod_color,vod_actor,vod_director,vod_content,vod_pic,vod_area,vod_language,vod_year,vod_continu,vod_addtime,vod_hits,vod_stars,vod_del,vod_up,vod_down,vod_play,vod_server,vod_url,vod_inputer,vod_reurl,vod_letter,vod_skin,vod_gold,vod_golder)");
		
		db.execSQL(DBInfoConfig.CREATE_TABLE_PLAY_LIKES);
		db.execSQL(DBInfoConfig.CREATE_TABLE_PPLIST);
		db.execSQL(DBInfoConfig.CREATE_TABLE_PPVOD);
		db.execSQL(DBInfoConfig.CREATE_TABLE_PLAY_HISTORY);
		
		
		System.out.println("建表结束");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
