package com.example.dao;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.constants.DBInfoConfig;
import com.example.model.PlayHistoryItem;
import com.example.model.PlayLikesItem;
import com.example.model.PpListItem;
import com.example.model.PpVodItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class OperateDAO {

    public DBOpenHelper helper;// 创建DBOpenHelper对象
    public SQLiteDatabase db;// 创建SQLiteDatabase对象
    
	public OperateDAO(Context context) {
		helper = new DBOpenHelper(context, DBInfoConfig.DB_NAME);// 初始化DBOpenHelper对象
	}
	
	//这个getReadableDB()函数 是为了 迎合 CustomSuggestionProvider中getSuggestions() 而定义的临时函数
	public SQLiteDatabase getReadableDB(){
		return helper.getReadableDatabase();
	}
	/**
	 * 得到 指定list_id下的所有vods，包括其子分类下的所有vods
	 * @param list_id
	 * @return
	 */
	public List<PpVodItem> getVodsByPpList(String list_id){
		List<PpVodItem> vodItems = new ArrayList<PpVodItem>();
		
		System.out.println("sql之前的list_id--" + list_id);
		//先做个判断，看所查询的List_id是否是根列表，如果是根列表，则需要遍历其底下的所有分类
		//由于目前数据结构直观上采取记录的分类级别为二级，故简化遍历为一次联表查询
		PpListItem testItem = getPpListItemByList_ID(list_id);
		db = helper.getReadableDatabase();
		Cursor cursor = null;
		if(testItem.getList_pid().equals("0")){
			//恰好为根分类【如：中学、大学】,则需要将两个表联合起来查询
			String sql = DBInfoConfig.SELECT_VOD_BY_LIST_PID;
			cursor = db.rawQuery(sql, new String[]{list_id});
			while(cursor.moveToNext()){
				PpVodItem vodItem = new PpVodItem();
//vod表结构 vod_id integer primary key,vod_cid,vod_name,vod_sourceid,vod_pic,vod_addtime,vod_hits,vod_content
				vodItem.setVod_id(cursor.getInt(cursor.getColumnIndex("vod_id")) +"");
				vodItem.setVod_cid(cursor.getString(cursor.getColumnIndex("vod_cid")));
				vodItem.setVod_name(cursor.getString(cursor.getColumnIndex("vod_name")));
				vodItem.setVod_sourceid(cursor.getString(cursor.getColumnIndex("vod_sourceid")));
				vodItem.setVod_pic(cursor.getString(cursor.getColumnIndex("vod_pic")));
				vodItem.setVod_addtime(cursor.getString(cursor.getColumnIndex("vod_addtime")));
				vodItem.setVod_hits(cursor.getString(cursor.getColumnIndex("vod_hits")));
				vodItem.setVod_content(cursor.getString(cursor.getColumnIndex("vod_content")));
				vodItem.setVod_up(cursor.getString(cursor.getColumnIndex("vod_up")) +"");
				vodItem.setVod_down(cursor.getString(cursor.getColumnIndex("vod_down")) +"");
				vodItem.setVod_gold(cursor.getString(cursor.getColumnIndex("vod_gold")) +"");
				vodItems.add(vodItem);
			}
		}else{
			String sql = DBInfoConfig.SELECT_VOD_BY_PPLIST;
			cursor = db.rawQuery(sql, new String[]{list_id});
			while(cursor.moveToNext()){
				PpVodItem vodItem = new PpVodItem();
				//vod表结构 vod_id integer primary key,vod_cid,vod_name,vod_sourceid,vod_pic,vod_addtime,vod_hits,vod_content
				vodItem.setVod_id(cursor.getInt(cursor.getColumnIndex("vod_id")) +"");
				vodItem.setVod_cid(cursor.getString(cursor.getColumnIndex("vod_cid")));
				vodItem.setVod_name(cursor.getString(cursor.getColumnIndex("vod_name")));
				vodItem.setVod_sourceid(cursor.getString(cursor.getColumnIndex("vod_sourceid")));
				vodItem.setVod_pic(cursor.getString(cursor.getColumnIndex("vod_pic")));
				vodItem.setVod_addtime(cursor.getString(cursor.getColumnIndex("vod_addtime")));
				vodItem.setVod_hits(cursor.getString(cursor.getColumnIndex("vod_hits")));
				vodItem.setVod_content(cursor.getString(cursor.getColumnIndex("vod_content")));
				vodItem.setVod_up(cursor.getString(cursor.getColumnIndex("vod_up")) +"");
				vodItem.setVod_down(cursor.getString(cursor.getColumnIndex("vod_down")) +"");
				vodItem.setVod_gold(cursor.getString(cursor.getColumnIndex("vod_gold")) +"");
				vodItems.add(vodItem);
			}
		}
		if(cursor != null){
			cursor.close();
		}
		if(db !=null && db.isOpen()){
			db.close();
		}
		return vodItems;
	}
	
	/**
	 * 得到count个最新视频，按vod_addtime  DESC 排序
	 * @param count
	 * @return
	 */
	public List<PpVodItem> getLatestVods(int count){
		List<PpVodItem> vodItems = new ArrayList<PpVodItem>();
		db = helper.getReadableDatabase();
		String sql = DBInfoConfig.SELECT_LATEST_VOD;
		Cursor cursor = db.rawQuery(sql, null);
		//cursor指针和count双重约束条件
		while(cursor.moveToNext() && count>0){
			PpVodItem vodItem = new PpVodItem();
			//vod表结构 vod_id integer primary key,vod_cid,vod_name,vod_sourceid,vod_pic,vod_addtime,vod_hits,vod_content
			vodItem.setVod_id(cursor.getInt(cursor.getColumnIndex("vod_id")) +"");
			vodItem.setVod_cid(cursor.getString(cursor.getColumnIndex("vod_cid")));
			vodItem.setVod_name(cursor.getString(cursor.getColumnIndex("vod_name")));
			vodItem.setVod_sourceid(cursor.getString(cursor.getColumnIndex("vod_sourceid")));
			vodItem.setVod_pic(cursor.getString(cursor.getColumnIndex("vod_pic")));
			vodItem.setVod_addtime(cursor.getString(cursor.getColumnIndex("vod_addtime")));
			vodItem.setVod_hits(cursor.getString(cursor.getColumnIndex("vod_hits")));
			vodItem.setVod_content(cursor.getString(cursor.getColumnIndex("vod_content")));
			vodItem.setVod_up(cursor.getString(cursor.getColumnIndex("vod_up")) +"");
			vodItem.setVod_down(cursor.getString(cursor.getColumnIndex("vod_down")) +"");
			vodItem.setVod_gold(cursor.getString(cursor.getColumnIndex("vod_gold")) +"");
			vodItems.add(vodItem);			
			count = count-1;
		}
		if(cursor != null){
			cursor.close();
		}
		if(db !=null && db.isOpen()){
			db.close();
		}
		return vodItems;
	}
	/**
	 * 得到count个精选视频 ,按vod_hits DESC 排序
	 * @param count
	 * @return
	 */
	public List<PpVodItem> getChosenVods(int count){
		List<PpVodItem> vodItems = new ArrayList<PpVodItem>();
		db = helper.getReadableDatabase();
		String sql = DBInfoConfig.SELECT_CHOSEN_VOD;
		Cursor cursor = db.rawQuery(sql, null);
		//cursor指针和count双重约束条件
		while(cursor.moveToNext() && count>0){
			PpVodItem vodItem = new PpVodItem();
			//vod表结构 vod_id integer primary key,vod_cid,vod_name,vod_sourceid,vod_pic,vod_addtime,vod_hits,vod_content
			vodItem.setVod_id(cursor.getInt(cursor.getColumnIndex("vod_id")) +"");
			vodItem.setVod_cid(cursor.getString(cursor.getColumnIndex("vod_cid")));
			vodItem.setVod_name(cursor.getString(cursor.getColumnIndex("vod_name")));
			vodItem.setVod_sourceid(cursor.getString(cursor.getColumnIndex("vod_sourceid")));
			vodItem.setVod_pic(cursor.getString(cursor.getColumnIndex("vod_pic")));
			vodItem.setVod_addtime(cursor.getString(cursor.getColumnIndex("vod_addtime")));
			vodItem.setVod_hits(cursor.getString(cursor.getColumnIndex("vod_hits")));
			vodItem.setVod_content(cursor.getString(cursor.getColumnIndex("vod_content")));
			vodItem.setVod_up(cursor.getString(cursor.getColumnIndex("vod_up")) +"");
			vodItem.setVod_down(cursor.getString(cursor.getColumnIndex("vod_down")) +"");
			vodItem.setVod_gold(cursor.getString(cursor.getColumnIndex("vod_gold")) +"");
			vodItems.add(vodItem);			
			count = count-1;
			//应该是select top X * from table_name
		}
		if(cursor != null){
			cursor.close();
		}
		if(db !=null && db.isOpen()){
			db.close();
		}
		return vodItems;
	}
	
	/**
	 * list_id 即  vod_cid，按照list_id查询得到一个 PpListItem
	 * @param vod_cid
	 * @return
	 */
	public PpListItem getPpListItemByList_ID(String list_id){
		PpListItem item = new PpListItem();
		db = helper.getReadableDatabase();
		String sql = DBInfoConfig.SELECT_PPLIST_ITEM_BY_LISTID;
		String bindArgs[] = new String[]{list_id};
		Cursor cursor = db.rawQuery(sql, bindArgs);
		if(cursor.moveToNext()){
			item.setList_id(cursor.getInt(cursor.getColumnIndex("list_id"))+"");
			item.setList_name(cursor.getString(cursor.getColumnIndex("list_name")));
			item.setList_pid(cursor.getString(cursor.getColumnIndex("list_pid")));
		}
		if(cursor != null){
			cursor.close();
		}
		if(db !=null && db.isOpen()){
			db.close();
		}
		return item;
	}
	
	/**
	 * 更新pp_vod表中的vod_hits字段，使其 “自加一”
	 * @param 欲修改的原数据vodItem
	 * @return 数据库中被该更新操作影响了的记录行数
	 */
	public int addVodHits(PpVodItem vodItem){
		int result = 0;
		db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("vod_hits", vodItem.getVod_hits()+1);
		result = db.update("pp_vod", values, "vod_id=?", new String[]{vodItem.getVod_id()});
		if(db !=null && db.isOpen()){
			db.close();
		}
		return result;
	}
	
	/**
	 * 在pp_vod表中 like 搜索跟queryStr字符串相关的记录，包括匹配vod_name和vod_content
	 * @param queryStr
	 * @return
	 */
	public List<PpVodItem> searchVodLikeStr(String queryStr){
		List<PpVodItem> vodItems = new ArrayList<PpVodItem>();
		db = helper.getReadableDatabase();
		String sql = DBInfoConfig.SELECT_VODS_LIKE_STRING;
		String likeStr = "%"+ queryStr +"%";
		String bindArgs[] = new String[]{likeStr,likeStr};
		Cursor cursor = db.rawQuery(sql, bindArgs);
		while(cursor.moveToNext()){
			PpVodItem vodItem = new PpVodItem();
			//vod表结构 vod_id integer primary key,vod_cid,vod_name,vod_sourceid,vod_pic,vod_addtime,vod_hits,vod_content
			vodItem.setVod_id(cursor.getInt(cursor.getColumnIndex("vod_id")) +"");
			vodItem.setVod_cid(cursor.getString(cursor.getColumnIndex("vod_cid")));
			vodItem.setVod_name(cursor.getString(cursor.getColumnIndex("vod_name")));
			vodItem.setVod_sourceid(cursor.getString(cursor.getColumnIndex("vod_sourceid")));
			vodItem.setVod_pic(cursor.getString(cursor.getColumnIndex("vod_pic")));
			vodItem.setVod_addtime(cursor.getString(cursor.getColumnIndex("vod_addtime")));
			vodItem.setVod_hits(cursor.getString(cursor.getColumnIndex("vod_hits")));
			vodItem.setVod_content(cursor.getString(cursor.getColumnIndex("vod_content")));
			vodItem.setVod_up(cursor.getString(cursor.getColumnIndex("vod_up")) +"");
			vodItem.setVod_down(cursor.getString(cursor.getColumnIndex("vod_down")) +"");
			vodItem.setVod_gold(cursor.getString(cursor.getColumnIndex("vod_gold")) +"");
			vodItems.add(vodItem);
		}
		if(cursor != null){
			cursor.close();
		}
		if(db !=null && db.isOpen()){
			db.close();
		}
		return vodItems;
	}
	
	/**
	 * 按 指定vod_id 返回 List<PpVodItem>对象的函数
	 * 用于SearchableActivity中的ACTION_VIEW.doPointSearch()
	 * @param vod_id
	 * @return 包含指定PpVodItem的List，size为1
	 */
	public List<PpVodItem> searchVodByVod_id(String vod_id){
		List<PpVodItem> vodItems = new ArrayList<PpVodItem>();
		PpVodItem vodItem = getVodByVod_id(vod_id);
		if(vodItem!=null){
			vodItems.add(vodItem);
		}else{
			System.out.println("OperateDAO中searchVodByVod_id() 方法异常");
		}
		return vodItems;
	}
	/**
	 * 执行较底层的数据库select方法
	 * @param vod_id
	 * @return
	 */
	public PpVodItem getVodByVod_id(String vod_id){
		PpVodItem vodItem = null;
		db = helper.getReadableDatabase();
		String sql = DBInfoConfig.SELECT_VODS_BY_VOD_ID;
		String bindArgs[] = new String[]{vod_id};
		Cursor cursor = db.rawQuery(sql, bindArgs);
		if(cursor.moveToNext()){
			vodItem = new PpVodItem();
			//vod表结构 vod_id integer primary key,vod_cid,vod_name,vod_sourceid,vod_pic,vod_addtime,vod_hits,vod_content
			vodItem.setVod_id(cursor.getInt(cursor.getColumnIndex("vod_id")) +"");
			vodItem.setVod_cid(cursor.getString(cursor.getColumnIndex("vod_cid")));
			vodItem.setVod_name(cursor.getString(cursor.getColumnIndex("vod_name")));
			vodItem.setVod_sourceid(cursor.getString(cursor.getColumnIndex("vod_sourceid")));
			vodItem.setVod_pic(cursor.getString(cursor.getColumnIndex("vod_pic")));
			vodItem.setVod_addtime(cursor.getString(cursor.getColumnIndex("vod_addtime")));
			vodItem.setVod_hits(cursor.getString(cursor.getColumnIndex("vod_hits")));
			vodItem.setVod_content(cursor.getString(cursor.getColumnIndex("vod_content")));
			vodItem.setVod_up(cursor.getString(cursor.getColumnIndex("vod_up")) +"");
			vodItem.setVod_down(cursor.getString(cursor.getColumnIndex("vod_down")) +"");
			vodItem.setVod_gold(cursor.getString(cursor.getColumnIndex("vod_gold")) +"");
		}
		if(cursor != null){
			cursor.close();
		}
		if(db !=null && db.isOpen()){
			db.close();
		}
		return vodItem;
	}
	/**
	 * 将播放过的Vod记录添加到play_history表中，play_history表只保存vod_id字段
	 * @param vodItem
	 * @return newlyInsertRowID 新近插入的RowID，如果插入失败，则返回-1,没有反应则返回0
	 */
	public int insertVodIntoPlayHistory(PpVodItem vodItem){
		int newlyInsertRowID = 0;
		//操作play_history表，(his_id AUTOINCREMENT,vod_id,playtime)
		db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("vod_id", vodItem.getVod_id());
		values.put("playtime", Long.toString(new Date().getTime()/1000));
		newlyInsertRowID = (int)db.insert("play_history","nullColumnHack",values);
		if(db !=null && db.isOpen()){
			db.close();
		}
		//return结果：the row ID of the newly inserted row, or -1 if an error occurred 
		return newlyInsertRowID;
	}
	
	
//	添加收藏栏目
	public int insertVodIntoPlayLikes(PpVodItem vodItem){
		int newlyInsertRowID = 0;
		int likesid=0;
		//操作play_history表，(his_id AUTOINCREMENT,vod_id,playtime)
		db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("vod_id", vodItem.getVod_id());
//		values.put("likes_id", ++likesid);
		newlyInsertRowID = (int)db.insert("play_likes","nullColumnHack",values);
		if(db !=null && db.isOpen()){
			db.close();
		}
		//return结果：the row ID of the newly inserted row, or -1 if an error occurred 
		return newlyInsertRowID;
	}
	
	
	/**
	 * 返回指定个数个 历史播放记录，按照播放事件playtime DESC 排序
	 * @param count
	 * @return
	 */
	public List<PlayHistoryItem> getPlayHistory(int count){
		List<PlayHistoryItem> historyItems = new ArrayList<PlayHistoryItem>();
		db = helper.getReadableDatabase();
		String sql = DBInfoConfig.SELECT_HISTORY_VOD;
		Cursor cursor = db.rawQuery(sql, null);
		//cursor指针和count双重约束条件
		while(cursor.moveToNext() && count>0){
			PlayHistoryItem historyItem = new PlayHistoryItem();
			//play_history的表结构(his_id integer primary key AUTOINCREMENT,vod_id,playtime)
			historyItem.setHis_id(cursor.getInt(cursor.getColumnIndex("his_id"))+"");
			historyItem.setVod_id(cursor.getString(cursor.getColumnIndex("vod_id")));
			historyItem.setPlaytime(cursor.getString(cursor.getColumnIndex("playtime")));
			historyItems.add(historyItem);
			count = count-1;
		}
		if(cursor != null){
			cursor.close();
		}
		if(db !=null && db.isOpen()){
			db.close();
		}
		return historyItems;
	}
	
	
	
	public List<PlayLikesItem> getPlayLikes(int count){
		List<PlayLikesItem> likesItems = new ArrayList<PlayLikesItem>();
		db = helper.getReadableDatabase();
		String sql = DBInfoConfig.SELECT_LIKES_VOD;
		Cursor cursor = db.rawQuery(sql, null);
		//cursor指针和count双重约束条件
		while(cursor.moveToNext() && count>0){
			PlayLikesItem likesItem = new PlayLikesItem();
			//play_history的表结构(his_id integer primary key AUTOINCREMENT,vod_id,playtime)
			likesItem.setLikes_id(cursor.getInt(cursor.getColumnIndex("likes_id"))+"");
			likesItem.setVod_id(cursor.getString(cursor.getColumnIndex("vod_id")));
			likesItems.add(likesItem);
			count = count-1;
		}
		if(cursor != null){
			cursor.close();
		}
		if(db !=null && db.isOpen()){
			db.close();
		}
		return likesItems;
	}
	
}

