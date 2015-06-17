package com.example.utils;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.example.model.PpListItem;

/**
 * 解析PpListXML的助手类
 *
 */
public class PpListXMLContentHandler extends DefaultHandler {

	private PpListItem ppListItem = null;
	private List<PpListItem> ppListItems = null;
	private String tagName = null; //当前要解析的元素标签
	
	public PpListXMLContentHandler() {
		// TODO Auto-generated constructor stub
		this.ppListItems = new ArrayList<PpListItem>();
	}
	public List<PpListItem> getPpListItems() {
		return ppListItems;
	}

	//接收文档开始的通知。当遇到文档的开头的时候，调用这个方法，可以在其中做一些预处理的工作。
	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 接收元素开始的通知。当读到一个开始标签的时候，会触发这个方法。其中namespaceURI表示元素的命名空间；
     * localName表示元素的本地名称（不带前缀）；qName表示元素的限定名（带前缀）；atts 表示元素的属性集合
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		this.tagName = localName;
		if(tagName.equals("pp_list")){
			this.ppListItem = new PpListItem();
		}
		
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		
		String dataStr = new String(ch,start,length);
		if(tagName.equals("list_id")){
			this.ppListItem.setList_id(dataStr);
		}else if(tagName.equals("list_pid")){
			this.ppListItem.setList_pid(dataStr);
		}else if(tagName.equals("list_name")){
			this.ppListItem.setList_name(dataStr);
		}
		
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		if(localName.equals("pp_list")){
			this.ppListItems.add(ppListItem);
			ppListItem = null;
		}
		
		this.tagName = "";
	}
	
	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
	}
	
}
