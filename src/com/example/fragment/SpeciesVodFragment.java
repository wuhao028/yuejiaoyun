package com.example.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.activity.ShowVodsInSelectedPpList;
import com.example.yuejiaoyun.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/*
 * 显示 分类Vod
 */
public class SpeciesVodFragment extends Fragment {

	//中学 课程GridView，包括语文...文史
	private GridView highSchoolGridView;
	//大学 课程GridView，包括IT类...文史
	private GridView universityGridView;
	//其他GridView，包括纪录片...电视剧
	private GridView othersGridView;
	
	private String[] highSchoolTitles = new String[] {"语文","数学","英语","物理","化学","生物","文史"};
	private String[] universityTitles = new String[] {"IT类","电子","经济","法律","文史"};
	private String[] othersTitles = new String[] {"纪录片","职业","体育","电影","电视剧"};
	
	private int[] highSchoolImages = new int[] { R.drawable.pp_list8, R.drawable.pp_list9, R.drawable.pp_list10, R.drawable.pp_list11, R.drawable.pp_list12, R.drawable.pp_list13, R.drawable.pp_list14};
	private int[] universityImages = new int[] { R.drawable.pp_list15, R.drawable.pp_list16, R.drawable.pp_list17, R.drawable.pp_list18, R.drawable.pp_list19};
	private int[] othersImages = new int[] { R.drawable.pp_list3, R.drawable.pp_list4, R.drawable.pp_list5, R.drawable.pp_list6, R.drawable.pp_list7};
	
	public SpeciesVodFragment() {
		// TODO Auto-generated constructor stub
	}

	// 该方法的返回值就是该Fragment显示的View组件
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//加载 SpeciesVodFragment 的布局文件,生成的这个rootView就是将要显示的View
		View rootView = inflater.inflate(R.layout.fragment_vod_species, container, false);
		
		//获取 布局文件中 的GridView
		highSchoolGridView = (GridView) rootView.findViewById(R.id.highSchoolGridView);
		universityGridView = (GridView) rootView.findViewById(R.id.universityGridView);
		othersGridView = (GridView) rootView.findViewById(R.id.othersGridView);
		//定义适配器 来位GridView配置资源
		PictureAdapter highSchoolAdapter = new PictureAdapter(highSchoolTitles, highSchoolImages, this.getActivity());
		PictureAdapter universityAdapter = new PictureAdapter(universityTitles, universityImages, this.getActivity());
		PictureAdapter othersAdapter = new PictureAdapter(othersTitles, othersImages, this.getActivity());
		//为GridView设置对应的适配器,以显示图片
		highSchoolGridView.setAdapter(highSchoolAdapter);
		universityGridView.setAdapter(universityAdapter);
		othersGridView.setAdapter(othersAdapter);
		//为GridView设置单击事件的监听器
		highSchoolGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parentViews, View clickView, int position,
					long id) {
				//为Intent消息对象建立 默认的Bundle数据包
				Bundle bundle = new Bundle();
				//position从0开始计起，8为“中学gridview”下的第一个Item"语文"，其List_id为8，即position和对应分类的cid的偏移量
				bundle.putInt("list_id", position+8);
				bundle.putString("list_name", highSchoolTitles[position]);
				Intent intent=new Intent(getActivity(), ShowVodsInSelectedPpList.class);
				intent.putExtras(bundle);
				getActivity().startActivity(intent);
				
			}
		});
		universityGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parentViews, View clickView, int position,
					long id) {
				//为Intent消息对象建立 默认的Bundle数据包
				Bundle bundle = new Bundle();
				//position从0开始计起，15为“大学gridview”下的第一个Item是“IT类”，其List_id为15，即position和对应分类的cid的偏移量
				bundle.putInt("list_id", position+15);
				bundle.putString("list_name", universityTitles[position]);
				Intent intent=new Intent(getActivity(), ShowVodsInSelectedPpList.class);
				intent.putExtras(bundle);
				getActivity().startActivity(intent);
				
			}
		});
		othersGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parentViews, View clickView, int position,
					long id) {
				//为Intent消息对象建立 默认的Bundle数据包
				Bundle bundle = new Bundle();
				//position从0开始计起，3为“其他gridview”下的第一个Item纪录片，其List_id为3，即position和对应分类的cid的偏移量
				bundle.putInt("list_id", position+3);
				bundle.putString("list_name", othersTitles[position]);
				Intent intent=new Intent(getActivity(), ShowVodsInSelectedPpList.class);
				intent.putExtras(bundle);
				getActivity().startActivity(intent);
				
			}
		});
		//TextView textView = (TextView)container.findViewById(R.id.);
		
		return rootView; 
	}
}
//内部类
class PictureAdapter extends BaseAdapter {// 创建基于BaseAdapter的子类

    private LayoutInflater inflater;// 创建LayoutInflater对象
    private List<Picture> pictures;// 创建List泛型集合

    // 为类创建构造函数
    public PictureAdapter(String[] titles, int[] images, Context context) {
        super();
        pictures = new ArrayList<Picture>();// 初始化泛型集合对象
        inflater = LayoutInflater.from(context);// 初始化LayoutInflater对象
        for (int i = 0; i < images.length; i++)// 遍历图像数组
        {
            Picture picture = new Picture(titles[i], images[i]);// 使用标题和图像生成Picture对象
            pictures.add(picture);// 将Picture对象添加到泛型集合中
        }
    }

    @Override
    public int getCount() {// 获取泛型集合的长度
        if (null != pictures) {// 如果泛型集合不为空
            return pictures.size();// 返回泛型长度
        } else {
            return 0;// 返回0
        }
    }

    @Override
    public Object getItem(int arg0) {
        return pictures.get(arg0);// 获取泛型集合指定索引处的项
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;// 返回泛型集合的索引
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        ViewHolder viewHolder;// 创建ViewHolder对象
        if (arg1 == null) {// 判断图像标识是否为空

            arg1 = inflater.inflate(R.layout.gridview_item, null);// 设置图像标识
            viewHolder = new ViewHolder();// 初始化ViewHolder对象
            viewHolder.title = (TextView) arg1.findViewById(R.id.ItemTitle);// 设置图像标题
            viewHolder.image = (ImageView) arg1.findViewById(R.id.ItemImage);// 设置图像的二进制值
            arg1.setTag(viewHolder);// 设置提示
        } else {
            viewHolder = (ViewHolder) arg1.getTag();// 设置提示
        }
        viewHolder.title.setText(pictures.get(arg0).getTitle());// 设置图像标题
        viewHolder.image.setImageResource(pictures.get(arg0).getImageId());// 设置图像的二进制值
        return arg1;// 返回图像标识
    }
}

class ViewHolder {// 创建ViewHolder类

    public TextView title;// 创建TextView对象
    public ImageView image;// 创建ImageView对象
}

class Picture {// 创建Picture类

    private String title;// 定义字符串，表示图像标题
    private int imageId;// 定义int变量，表示图像的二进制值

    public Picture() {// 默认构造函数

        super();
    }

    public Picture(String title, int imageId) {// 定义有参构造函数

        super();
        this.title = title;// 为图像标题赋值
        this.imageId = imageId;// 为图像的二进制值赋值
    }

    public String getTitle() {// 定义图像标题的可读属性
        return title;
    }

    public void setTitle(String title) {// 定义图像标题的可写属性
        this.title = title;
    }

    public int getImageId() {// 定义图像二进制值的可读属性
        return imageId;
    }

    public void setimageId(int imageId) {// 定义图像二进制值的可写属性
        this.imageId = imageId;
    }
}