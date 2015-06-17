package com.example.fragment;

import java.util.List;

import com.example.activity.VideoPlayActivity;
import com.example.constants.DBInfoConfig;
import com.example.dao.OperateDAO;
import com.example.model.PpVodItem;
import com.example.utils.FragmentVodAdapter;
import com.example.yuejiaoyun.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/*
 * 显示精选的Vod
 */
public class ChosenVodFragment extends Fragment {

	//显示精选Vods的个数
	private final int NUMBER = DBInfoConfig.NUM_OF_GET_CHOSEN_VOD;
	public ChosenVodFragment() {
		// TODO Auto-generated constructor stub
	}

	// 该方法的返回值就是该Fragment显示的View组件
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_vod_chosen, container, false);
		OperateDAO dao = new OperateDAO(getActivity());
		List<PpVodItem> vodItems = dao.getChosenVods(NUMBER);
		if(!vodItems.isEmpty()){//如果结果集非空
			ListView vodsList = (ListView)rootView.findViewById(R.id.chosenVodsList);
			FragmentVodAdapter vodAdapter = new FragmentVodAdapter(inflater,vodItems);
			vodsList.setAdapter(vodAdapter);
			vodsList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View view,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					System.out.println("data from tag--->"+view.getTag());
					Intent intent = new Intent(view.getContext(), VideoPlayActivity.class);
					intent.putExtra("wantShowVod", (PpVodItem)view.getTag());
					
					startActivity(intent);
				}
			});
		}else{
			System.out.println("从数据库中查询出的结果集为空");
		}
		/*TextView textView = new TextView(getActivity());
		textView.setText("ChosenVodFragment");*/
		return rootView;
	}
}
