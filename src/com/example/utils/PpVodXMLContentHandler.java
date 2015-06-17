package com.example.utils;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.example.model.PpVodItem;

/**
 * 解析PpVodXML的助手类
 */
public class PpVodXMLContentHandler extends DefaultHandler {

	private PpVodItem ppVodItem = null;
	private List<PpVodItem> ppVodItems = null;
	private String tagName = null; //当前要解析的元素标签
	
	public PpVodXMLContentHandler() {
		// TODO Auto-generated constructor stub
		this.ppVodItems = new ArrayList<PpVodItem>();
	}

	public List<PpVodItem> getPpVodItems() {
		return ppVodItems;
	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
	}
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		this.tagName = localName;
		if(tagName.equals("pp_vod")){
			this.ppVodItem = new PpVodItem();
		}
	}
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		
		String dataStr = new String(ch,start,length);
		if(tagName.equals("vod_id")){
			this.ppVodItem.setVod_id(dataStr);
		}else if(tagName.equals("vod_cid")){
			this.ppVodItem.setVod_cid(dataStr);
		}else if(tagName.equals("vod_name")){
			this.ppVodItem.setVod_name(dataStr);
		}else if(tagName.equals("vod_sourceid")){
			this.ppVodItem.setVod_sourceid(dataStr);
		}else if(tagName.equals("vod_pic")){
			this.ppVodItem.setVod_pic(dataStr);
		}else if(tagName.equals("vod_addtime")){
			this.ppVodItem.setVod_addtime(dataStr);
		}else if(tagName.equals("vod_hits")){
			this.ppVodItem.setVod_hits(dataStr);
		}else if(tagName.equals("vod_content")){
			this.ppVodItem.setVod_content(dataStr);
		}else if(tagName.equals("vod_up")){
		    Log.d("点赞人数点赞人数点赞人数点赞人数", dataStr);
			this.ppVodItem.setVod_up(dataStr);
		}else if(tagName.equals("vod_down")){
			this.ppVodItem.setVod_down(dataStr);
		}else if(tagName.equals("vod_gold")){
			this.ppVodItem.setVod_gold(dataStr);}
	}
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub

		if(localName.equals("pp_vod")){
			this.ppVodItems.add(ppVodItem);
			ppVodItem = null;
		}
		
		this.tagName = "";
	}
	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
	}
}
