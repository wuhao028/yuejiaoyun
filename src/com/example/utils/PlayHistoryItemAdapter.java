package com.example.utils;

import java.util.List;

import com.example.dao.OperateDAO;
import com.example.model.PlayHistoryItem;
import com.example.model.PpVodItem;
import com.example.yuejiaoyun.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayHistoryItemAdapter extends BaseAdapter {

	public Context context;
	public List<PlayHistoryItem> historyItems;
	private OperateDAO dao;
	
	public PlayHistoryItemAdapter(Context context,
			List<PlayHistoryItem> historyItems) {
		super();
		this.context = context;
		this.historyItems = historyItems;
		dao = new OperateDAO(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return historyItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return historyItems.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return Integer.parseInt(historyItems.get(arg0).getHis_id());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		PlayHistoryItem wantShowHistoryItem = historyItems.get(position);
		PpVodItem wantShowVod = dao.getVodByVod_id(wantShowHistoryItem.getVod_id());
		convertView = LayoutInflater.from(context).inflate(R.layout.adapter_show_playhistory_info_item, null);
		//获取playtime
		TextView playtimeView = (TextView)convertView.findViewById(R.id.playtime);
		playtimeView.setText("播放时间："+TransformDateAndString.TimeStamp2Date(wantShowHistoryItem.getPlaytime(), "yyyy-MM-dd HH:mm:ss"));
		
		//获取各个控件 以 显示 vod,与ActivityVodAdapter中的功能类似
		TextView vod_idView=(TextView)convertView.findViewById(R.id.vod_id);
		TextView vod_cidView=(TextView)convertView.findViewById(R.id.vod_cid);
		TextView vod_nameView=(TextView)convertView.findViewById(R.id.vod_name);
		TextView vod_sourceidView=(TextView)convertView.findViewById(R.id.vod_sourceid);
		ImageView vod_picView = (ImageView)convertView.findViewById(R.id.vod_pic);
		TextView vod_addtimeView=(TextView)convertView.findViewById(R.id.vod_addtime);
		TextView vod_hitsView=(TextView)convertView.findViewById(R.id.vod_hits);
		TextView vod_contentView=(TextView)convertView.findViewById(R.id.vod_content);
		//为各个控件设置值
		vod_idView.setText("id:"+wantShowVod.getVod_id());
		vod_cidView.setText("cid:"+wantShowVod.getVod_cid());
		vod_nameView.setText(wantShowVod.getVod_name());
		vod_sourceidView.setText("sourceid:"+wantShowVod.getVod_sourceid());
		/*
		 * 这儿需要处理一种异常情况，即当pic属性值（picUrlString）为空的时候，是不能将图片放入软引用来使用的，因为key为空额
		 *处理策略：1、可以在这儿做判断是否为空  2、在较底层的AsyncImageLoader中处理
		 * 可能 在这儿先做判断 会比较好
		 */
		if(wantShowVod.getVod_pic() == null ||wantShowVod.getVod_pic().equals("")){
			vod_picView.setImageResource(R.drawable.img404);
		}else{
			AsyncImageLoader loader = new AsyncImageLoader();
			VodPicCallbackImpl callbackImpl = new VodPicCallbackImpl(vod_picView);
	    	Drawable cacheImage = 
	    		loader.loadDrawable(wantShowVod.getVod_pic(), callbackImpl);
			if (cacheImage != null) {
				vod_picView.setImageDrawable(cacheImage);
			}
		}
		vod_addtimeView.setText("时间："+TransformDateAndString.TimeStamp2Date(wantShowVod.getVod_addtime(), "yyyy-MM-dd HH:mm:ss"));
		vod_hitsView.setText("点击数："+wantShowVod.getVod_hits());
		
		/**
		 * 同样的，对于vod_content也得做 内容为空 的特殊情况处理
		 */
		if(wantShowVod.getVod_content() == null || wantShowVod.getVod_content().equals("")){
			vod_contentView.setText("剧情梗概：暂无");
		}else{
			vod_contentView.setText("剧情梗概："+wantShowVod.getVod_content());
		}
		//为当前Item的View设定Tag标签为可序列化的PpVodItem对象，以供随后为其设定OnClick事件监听时 能更方便的提供SourceId数据
		convertView.setTag(wantShowVod);
		return convertView;
	}

}
