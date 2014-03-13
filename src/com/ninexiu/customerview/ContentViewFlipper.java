package com.ninexiu.customerview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ViewFlipper;

public class ContentViewFlipper extends ViewFlipper {

	public ContentViewFlipper(Context paramContext)
	  {
	    super(paramContext);
	  }

	  public ContentViewFlipper(Context paramContext, AttributeSet paramAttributeSet)
	  {
	    super(paramContext, paramAttributeSet);
	  }
	  
	  @Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}
	}
