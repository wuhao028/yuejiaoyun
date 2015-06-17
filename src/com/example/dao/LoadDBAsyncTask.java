package com.example.dao;


import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.example.constants.WebInfoConfig;
import com.example.utils.HttpUtil;
import com.example.utils.PpListXMLContentHandler;

import android.os.AsyncTask;

public class LoadDBAsyncTask extends AsyncTask<Void, Void, String> {

	
	public LoadDBAsyncTask() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}
	
	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub
		System.out.println("后台doInBackground 完成对播放列表的下载 和 对数据库的更新");
		/**
		 * 后台doInBackground 完成对播放列表的下载 和 对数据库的更新，步骤：
		 * 1、从指定Url获取结果
		 * 2、SAX解析Xml结果
		 * 3、数据持久化存入Sqlite数据库
		 */
		String getPpListUrlStr = WebInfoConfig.GET_PPLIST_URL;
		String ppListResult = null;
		 try {
			ppListResult = new HttpUtil().getRequest(getPpListUrlStr);
			//System.out.println("ppListResult-->"+ ppListResult);
			//System.out.println("ppListResult-->"+ppListResult);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         try {
			//创建解析器
			 SAXParserFactory spf = SAXParserFactory.newInstance();
			 SAXParser saxParser = spf.newSAXParser();
			 //创建助手类
			 /*PpListXMLContentHandler ppListXMLContentHandler = new PpListXMLContentHandler();
			 saxParser.parse(ppListResult, ppListXMLContentHandler);
			 System.out.println("xml解析结果1 :"+ ppListXMLContentHandler.getPpListItems().toString());
			 System.out.println("xml解析结果2 :"+ ppListXMLContentHandler.getPpListItems().get(1).getList_name());
			 */
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		/**
		 * 用Unix时间戳  1396090240（精确到秒） ，经过网上的MD5编码：
		 * 32位[小写]：4094a3f81cc5f26980974d10d292d91c
		 * 在查看报错的 用到 Unicode和汉字的互转  ，在线工具网站http://www.guabu.com/zhuanma/
		 * 
		 * 1396165673
		 * 6RDdRsrzAn2ciUCG1396165673video
		 * f644ae2ed8b1ee77d4fdf3262a768343
		 * 
		 * String unixTimeStamp = WebInfoConfig.UnixTimeStamp;
		 * System.out.println("Unix时间戳："+unixTimeStamp);
		 * System.out.println("拼接起来的字符串为："+ WebInfoConfig.SEWISE_ACCESS_ID + unixTimeStamp + "video" +"zKEXnF7U");
		 */
		//HttpGet get = new HttpGet(demandServerUrl + "service/api/?time=1396142496"+"&code=30acf57b7a1071c870409c4219115279"+"&do=video&op=geturl&sourceid=XUIRjNGP&type=http");
		
		//String finalUrlString = demandServerUrl + "service/api/?time=1396165673&code=f644ae2ed8b1ee77d4fdf3262a768343&do=video";
		//System.out.println("最终Http访问地址  "+finalUrlString);
		//HttpGet get = new HttpGet(finalUrlString);//列出所有视频文件列表
		String result = "";
		
		return null;
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}


}
