package com.example.activity;

import info.info;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import yuejiaoyun.xml.VideoListContentHandle;
import com.example.yuejiaoyun.R;
import download.HttpDownloader;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;


public class getsearchresult extends ListActivity{
	
	List<info> Vlist=null;
	

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
      //  requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.searchresult);
        
    	
        //获取传来的地址
      //  Intent intent1=getIntent();
      //	String s1=intent1.getStringExtra("searchurl");
      	
        
        
    //    updateListview();
        
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()   
        .detectDiskReads()   
        .detectDiskWrites()   
        .detectNetwork()   // or .detectAll() for all detectable problems   
        .penaltyLog()   
        .build());   
		//设置虚拟机的策略
		  StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()   
		         .detectLeakedSqlLiteObjects()   
		         //.detectLeakedClosableObjects()   
		         .penaltyLog()   
		         .penaltyDeath()   
		         .build());
//		 String s2="http://219.223.192.21/testTv/service/searchvod.php?keyword=%E7%94%B2%E5%8D%88";
		  
		  Bundle bundle=getIntent().getExtras();
		  String s1=bundle.getString("searchurl");
		  
		  String xml=downloadXML(s1);
		//   Log.d("结果中第一个结果中搜索的完整关键字",s1);
		 //  Log.d("结果中第二个结果中搜索的完整关键字",s2);
		   
			Vlist=parse(xml);
			SimpleAdapter simpleAdapter=getAdapter(Vlist);
			setListAdapter(simpleAdapter);
	
    }
    
 

	
    
	private String downloadXML(String str){
    	HttpDownloader hd=new HttpDownloader();
    	String result=hd.download(str);
    	return result;
    }
	
	
	
	
    @SuppressWarnings("unused")
	private List<info> parse(String xmlstr){
    	SAXParserFactory saxParserFactory=SAXParserFactory.newInstance();
    	List<info> list=null;   	
    	try {   		   		
			XMLReader xmlReader=saxParserFactory.newSAXParser().getXMLReader();
			list=new ArrayList<info>();
			VideoListContentHandle videohandler=new VideoListContentHandle(list);
			xmlReader.setContentHandler(videohandler);
			xmlReader.parse(new InputSource(new StringReader(xmlstr)));
			for(Iterator<info> iterator=list.iterator();iterator.hasNext();){
				info info=iterator.next();
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return list;
    	
    }
	
	
	
    private SimpleAdapter getAdapter(List<info> Vlist){   	
		List<HashMap<String, String>> list=new ArrayList<HashMap<String,String>>();
		for(Iterator<info> iterator=Vlist.iterator();iterator.hasNext();){
			info videoinfos=iterator.next();
			HashMap<String, String> map=new HashMap<String, String>();
			map.put("vod_name", videoinfos.getVideoName());
			map.put("vod_http",videoinfos.getVideoURL());
			list.add(map);
		}
		SimpleAdapter simpleAdapter=new SimpleAdapter(this, list, R.layout.resultlist,
				new String[]{"vod_name","vod_http"},
				new int[]{R.id.vod_name,R.id.vod_http});
		return simpleAdapter;
    }
    
    
  
	/*   
	    private void updateListview() {
	    	
	    	String xml=downloadXML("http://219.223.192.21/testTv/service/searchvod.php?keyword=%E7%94%B2%E5%8D%88");
			Vlist=parse(xml);
			SimpleAdapter simpleAdapter=getAdapter(Vlist);
			setListAdapter(simpleAdapter);
		
			
	    }

 */
 

    
    @Override
   	protected void onListItemClick(ListView l, View v, int position, long id) {
   		Intent intentlist=new Intent();
   		info infos=Vlist.get(position);
   		intentlist.putExtra("path", infos.getVideoURL().toString());
   		intentlist.setClass(getsearchresult.this, VideoPlayer.class);
   		startActivity(intentlist);
   		super.onListItemClick(l, v, position, id);
   	}
	
}
