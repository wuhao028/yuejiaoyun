package com.example.utils;

import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import com.example.yuejiaoyun.R;

import android.R.drawable;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

//该类的主要作用是实现图片的异步加载
public class AsyncImageLoader {
	//图片缓存对象
	//键是图片的URL，值是一个SoftReference对象，该对象指向一个Drawable对象
	private Map<String, SoftReference<Drawable>> imageCache = 
		new HashMap<String, SoftReference<Drawable>>();
	
	//实现图片的异步加载
	public Drawable loadDrawable(final String imageUrl,final ImageCallback callback){
		/* 本打算 在此 做对应的 空参数 处理，但是后来发现 尽管在这儿返回了Drawable图片，但是对应的Key值为空，同样会出错，
		 * 所以改为直接在较高级的adapter里面对参数wantShowVod.getVod_pic()作判断并处理，于是这段代码无用了
		 //如果imageUrl参数为空的话，返回程序内置的“默认图片”
		if(imageUrl.equals("") || imageUrl == null){
			return Resources.getSystem().getDrawable(R.drawable.img404);
		}
		 */
		
		//查询缓存，查看当前需要下载的图片是否已经存在于缓存当中
		if(imageCache.containsKey(imageUrl)){
			SoftReference<Drawable> softReference=imageCache.get(imageUrl);
			if(softReference.get() != null){//软引用还未被清除出缓存
				return softReference.get();
			}
		}
		
		final Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				callback.imageLoaded((Drawable) msg.obj);
			}
		};
		//新开辟一个线程，该线程用于进行图片的下载
		new Thread(){
			public void run() {
				Drawable drawable=loadImageFromUrl(imageUrl);
				imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
			};
		}.start();
		return null;
	}
	//该方法用于根据图片的URL，从网络上下载图片
	protected Drawable loadImageFromUrl(String imageUrl) {
		try {
			//根据图片的URL，下载图片，并生成一个Drawable对象		
				return Drawable.createFromStream(new URL(imageUrl).openStream(), "src");
			
		} catch (Exception e) {
//		throw new RuntimeException(e);
		
//      	return Resources.getSystem().getDrawable(R.drawable.push);

//	不能从网络获取对应图片时，加载粤教云官网上的图标		
			final String yuejiaoyun =
		       "http://ecloud.edugd.cn/images/global/logo.png";		
//			return Drawable.createFromStream(yuejiaoyun.openStream(), "src");
			Drawable yuejiaoyundraw=loadImageFromUrl(yuejiaoyun);
			return yuejiaoyundraw;
		}
	}
	
	//回调接口
	public interface ImageCallback{
		public void imageLoaded(Drawable imageDrawable);
	}
}
