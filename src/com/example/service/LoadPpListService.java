package com.example.service;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.example.constants.DBInfoConfig;
import com.example.constants.WebInfoConfig;
import com.example.dao.DBOpenHelper;
import com.example.model.PpListItem;
import com.example.model.PpVodItem;
import com.example.utils.HttpUtil;
import com.example.utils.PpListXMLContentHandler;
import com.example.utils.PpVodXMLContentHandler;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;


public class LoadPpListService extends Service {

	DBOpenHelper dbOpenHelper = null;
	SQLiteDatabase db = null;
	
	public LoadPpListService() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		//获取数据库操作帮助类
		dbOpenHelper = new DBOpenHelper(this, DBInfoConfig.DB_NAME);
		//打开数据库
		openDB();
		
		System.out.println("service begin-->"+new WebInfoConfig().UnixTimeStamp);
		
		String getPpListUrlStr = WebInfoConfig.GET_PPLIST_URL;
		String ppListResult = null;
		//请求WebService, 得到PpList
		try {
			ppListResult = HttpUtil.getRequest(getPpListUrlStr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		//解析PpListXML,得到所有ppList
		List<PpListItem> ppList = parsePpListXML(ppListResult);
		//清理掉pp_List数据表中的原有数据
		cleanTablePpList();
		//将ppList放入数据库
		putPpList2DB(ppList);

		
		//从数据库中读取“根类别”的pp_list_ids
		List<String> list_ids = null;
		list_ids = getRootPpListsFromDB();
		//清理掉pp_Vod数据表中的原有数据
		cleanTablePpVod();
		//遍历每一个根类别，去获取该类别下的所有 vods
		for(String list_id : list_ids) {
			//System.out.println(list_id);
			//打开数据库
			//openDB();
			String getPpVodUrlStr = WebInfoConfig.GET_PPVOD_URL + "?cid=" + list_id;
			//获取指定分类cid下的所有视频，xml结果
			String ppVodResult = loadPpVodFromWeb(getPpVodUrlStr);
			//解析xml结果 得到视频Item列表
			List<PpVodItem> vodItems = parsePpVodXML(ppVodResult);
			//System.out.println("本次解析出来总共有这么多的VodItem-->"+vodItems.size());

			//将视频Item列表 持久化 存入数据库
			putPpVod2DB(vodItems);

		}
		//试着将DB中的vod信息取出来打印
		//printAllVodFromDB();
		
		//关闭数据库连接db
		closeDB();
		System.out.println("service end-->"+new WebInfoConfig().UnixTimeStamp);
		//完成操作后，结束当前的Service
        stopSelf();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	private List<PpListItem> parsePpListXML(String ppListResult){
		List<PpListItem> ppList = null;
		//创建助手类
		PpListXMLContentHandler ppListXMLContentHandler;
		try {
			//创建解析器
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			XMLReader xmlReader = saxParserFactory.newSAXParser().getXMLReader();
			
			ppListXMLContentHandler = new PpListXMLContentHandler();
			xmlReader.setContentHandler(ppListXMLContentHandler);
			xmlReader.parse(new InputSource(new StringReader(ppListResult)));
			
			ppList = ppListXMLContentHandler.getPpListItems();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 		
		return ppList;
	}
	//将ppList放入数据库
	private void putPpList2DB(List<PpListItem> ppList){
		
		//循环插入数据
		for (PpListItem ppListItem : ppList) {
			 //System.out.println(ppListItem.toString());
			String sql = DBInfoConfig.INSERT_INTO_PPLIST;
			String bindArgs[] = new String[]{ppListItem.getList_id(),ppListItem.getList_pid(),ppListItem.getList_name()};
			db.execSQL(sql, bindArgs);
		}

	}
	
	/**
	 * 从数据库中读出所存储的pp_list
	 * @return
	 */
	private List<String> getRootPpListsFromDB(){
		List<String> list_ids = new ArrayList<String>();
		String sql = DBInfoConfig.SELECT_ROOT_LIST_ID;
		Cursor cursor = db.rawQuery(sql, null);
		while(cursor.moveToNext()){
			list_ids.add(cursor.getString(0));
			
		}
		return list_ids;
	}
	
	/**
	 * 从指定URL上load下来pp_vod
	 * @param urlStr
	 */
	private String loadPpVodFromWeb(String urlStr){
		
		String ppVodResult = null;
		//请求WebService, 得到PpList
		try {
			ppVodResult = HttpUtil.getRequest(urlStr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return ppVodResult;
	}
	
	/**
	 * 解析所获得的的PpVodXML
	 * @param ppVodResult
	 * @return List<PpVodItem>
	 */
	private List<PpVodItem> parsePpVodXML(String ppVodResult){
	
		List<PpVodItem> vodList = null;
	
		try {
			//创建解析器
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			XMLReader xmlReader = saxParserFactory.newSAXParser().getXMLReader();
			
			PpVodXMLContentHandler ppVodXMLContentHandler = new PpVodXMLContentHandler();
			xmlReader.setContentHandler(ppVodXMLContentHandler);
			xmlReader.parse(new InputSource(new StringReader(ppVodResult)));
			
			vodList = ppVodXMLContentHandler.getPpVodItems();
			
			
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return vodList;
	}
	
	//将ppVod放入数据库
	private void putPpVod2DB(List<PpVodItem> vodList){
		//如果参数为空，不做数据插入操作
		if(vodList.size() == 0) return;
		String sql = DBInfoConfig.INSERT_INTO_PPVOD;
		//循环插入数据
		for (PpVodItem ppVodItem : vodList) {
			
//		新加参数在这里存入数据库
			String bindArgs[] = new String[]{ppVodItem.getVod_id(),ppVodItem.getVod_cid(),ppVodItem.getVod_name(),ppVodItem.getVod_sourceid(),ppVodItem.getVod_pic(),ppVodItem.getVod_addtime(),ppVodItem.getVod_hits(),ppVodItem.getVod_content(),ppVodItem.getVod_up(),ppVodItem.getVod_down(),ppVodItem.getVod_gold()};
			//System.out.println("bindArgs-->"+ppVodItem.getVod_id());
			db.execSQL(sql, bindArgs);
			//System.out.println("完成本次insert");
		}

	}
	
	private void openDB(){

		db = dbOpenHelper.getWritableDatabase();
	}
	
	private void closeDB(){
		//关闭数据库
		if(db != null && db.isOpen()){
			db.close();
		}
	}
	private void cleanTablePpList(){
		//先将表中数据清空掉
		db.execSQL(DBInfoConfig.DELETE_TABLE_PPLIST_RECORDS);
	}
	private void cleanTablePpVod(){
		//先将表中数据清空掉
		db.execSQL(DBInfoConfig.DELETE_TABLE_PPVOD_RECORDS);
		
	}
	/*
	private void printAllVodFromDB(){
		Cursor cursor = db.rawQuery("select * from pp_vod", null);
		while(cursor.moveToNext()){
			System.out.println(cursor.getInt(0)+"  "+cursor.getString(1)+"  "+cursor.getString(2)+"  "+cursor.getString(3)+"  "+cursor.getString(4));
		}
	}*/
	
	
}
