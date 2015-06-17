package splashadapter;


import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import com.example.activity.WelcomeActivity;
import com.example.yuejiaoyun.R;


public class ViewPagerAdapter extends PagerAdapter {

	// 閻ｅ矂娼伴崚妤勩�
	private List<View> views;
	private Activity activity;

	private static final String SHAREDPREFERENCES_NAME = "first_pref";

	public ViewPagerAdapter(List<View> views, Activity activity) {
		this.views = views;
		this.activity = activity;
	}

	// 闁匡拷鐦塧rg1娴ｅ秶鐤嗛惃鍕櫕闂堬拷
	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView(views.get(arg1));
	}

	@Override
	public void finishUpdate(View arg0) {
	}

	// 閼惧嘲绶辫ぐ鎾冲閻ｅ矂娼伴弫锟�
	@Override
	public int getCount() {
		if (views != null) {
			return views.size();
		}
		return 0;
	}

	// 閸掓繂顫愰崠鏈紃g1娴ｅ秶鐤嗛惃鍕櫕闂堬拷
	@Override
	public Object instantiateItem(View arg0, int arg1) {
		((ViewPager) arg0).addView(views.get(arg1), 0);
		if (arg1 == views.size() - 1) {
			ImageView mStartWeiboImageButton = (ImageView) arg0
					.findViewById(R.id.iv_start_yuejiaoyun);
			mStartWeiboImageButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 鐠佸墽鐤嗗鑼病瀵洖顕�
					setGuided();
					goHome();

				}

			});
		}
		return views.get(arg1);
	}

	private void goHome() {
		// 鐠哄疇娴�
		Intent intent = new Intent(activity,WelcomeActivity.class);
		activity.startActivity(intent);
		activity.finish();
	}

	/**
	 * 
	 * method desc閿涙俺顔曠純顔煎嚒缂佸繐绱╃�鑹扮箖娴滃棴绱濇稉瀣偧閸氼垰濮╂稉宥囨暏閸愬秵顐煎鏇烆嚤
	 */
	private void setGuided() {
		SharedPreferences preferences = activity.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		// 鐎涙ê鍙嗛弫鐗堝祦
		editor.putBoolean("isFirstIn", false);
		// 閹绘劒姘︽穱顔芥暭
		editor.commit();
	}

	// 閸掋倖鏌囬弰顖氭儊閻㈠崬顕挒锛勬晸閹存劗鏅棃锟�
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == arg1);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
	}

}
