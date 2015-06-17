package com.example.model;

public class PpListItem {
	
	private String list_id;
	private String list_pid;
	private String list_name;

	public PpListItem() {
		// TODO Auto-generated constructor stub
	}

	public PpListItem(String list_id, String list_pid, String list_name) {
		super();
		this.list_id = list_id;
		this.list_pid = list_pid;
		this.list_name = list_name;
	}

	public String getList_id() {
		return list_id;
	}

	public void setList_id(String list_id) {
		this.list_id = list_id;
	}

	public String getList_pid() {
		return list_pid;
	}

	public void setList_pid(String list_pid) {
		this.list_pid = list_pid;
	}

	public String getList_name() {
		return list_name;
	}

	public void setList_name(String list_name) {
		this.list_name = list_name;
	}

	@Override
	public String toString() {
		return "PpListItem [list_id=" + list_id + ", list_pid=" + list_pid
				+ ", list_name=" + list_name + "]";
	}

	
	
}
