package com.example.utils;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class VodPicCallbackImpl implements AsyncImageLoader.ImageCallback{
	private ImageView imageView ;
	
	public VodPicCallbackImpl(ImageView imageView) {
		super();
		this.imageView = imageView;
	}

	@Override
	public void imageLoaded(Drawable imageDrawable) {
		imageView.setImageDrawable(imageDrawable);
	}

}
