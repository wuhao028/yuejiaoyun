package com.example.constants;

import java.util.Date;

/**
 * 该类包含所有Web配置信息，多为静态常量
 * HM5049B42NT0044D0DBBD
 */
public class WebInfoConfig {
	
	public static final String TEST_FLV_URL =
			"http://219.223.199.96:5080/flvseek/data/userdata/vod/transcode/201401/zKEXnF7U_R3.flv";
	public static final String TEST_SOURCE_ID = "XUIRjNGP";
	
	
	//粤教云 点播服务器 地址
	public static final String YJY_DEMAND_SERVER_API_URL = "http://202.116.39.44/";
	//北大PKUsz点播服务器   
	public static final String PKUsz_DEMAND_SERVER_API_URL = "http://219.223.199.96/";	
	//矽伟智 “粤教云点播服务器” http://202.116.39.44/ 提供的 网络接口信息
	public static final String YJY_ACCESS_ID = "6RDdRsrzAn2ciUCG";
	//北大PKUsz点播服务器 http://219.223.199.96/提供的 网络接口信息
	public static final String PKUsz_ACCESS_ID = "pUsPqQJXwj6XAnzn";
	public static final String GET_PPLIST_URL = "http://219.223.192.21/testTv/service/getlist.php";
	public static final String GET_PPVOD_URL = "http://219.223.192.21/testTv/service/getvod.php";
	/**更新点击量的接口
	 * http://219.223.192.21/testTv/service/hitsupdate.php?vodid=68
	 */
	public static final String ADD_HITS_URL = "http://219.223.192.21/testTv/service/hitsupdate.php?vodid=";
	
	
//	顶节目更新接口
	public static final String LIKE_HITS_URL="http://219.223.192.21/testTv/service/upupdate.php?vodid=";
//踩节目更新接口
	public static final String DISLIKE_HITS_URL="http://219.223.192.21/testTv/service/downupdate.php?vodid=";
//	分数更新接口
	public static final String SCORE_URL="http://219.223.192.21/testTv/service/goldupdate.php?vodid=95&vodgold=";
	
	
	//获取Unix时间戳,精确到秒
	public String UnixTimeStamp = Long.toString(new Date().getTime()/1000);
	
}
