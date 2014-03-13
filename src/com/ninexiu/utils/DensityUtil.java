package com.ninexiu.utils;


import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

public class DensityUtil
{
  private static final String TAG = DensityUtil.class.getSimpleName();
  private static DisplayMetrics dm;
  private static float dmDensityDpi = 0.0F;
  private static float scale = 0.0F;

  public DensityUtil(Context paramContext)
  {
    dm = new DisplayMetrics();
    dm = paramContext.getApplicationContext().getResources().getDisplayMetrics();
    setDmDensityDpi(dm.densityDpi);
    scale = getDmDensityDpi() / 160.0F;
    Log.i(TAG, toString());
  }

  public static int dip2px(float paramFloat)
  {
    return (int)(0.5F + paramFloat * scale);
  }

  public static int dip2px(Context paramContext, float paramFloat)
  {
    return (int)(0.5F + paramFloat * paramContext.getResources().getDisplayMetrics().density);
  }

  public static float getDmDensityDpi()
  {
    return dmDensityDpi;
  }

  public static int px2dip(Context paramContext, float paramFloat)
  {
    return (int)(0.5F + paramFloat / paramContext.getResources().getDisplayMetrics().density);
  }

  public static void setDmDensityDpi(float paramFloat)
  {
    dmDensityDpi = paramFloat;
  }

  public int px2dip(float paramFloat)
  {
    return (int)(0.5F + paramFloat / scale);
  }

  public String toString()
  {
    return " dmDensityDpi:" + dmDensityDpi;
  }
}

/* Location:           C:\Users\Administrator\Desktop\SixRooms_6cn_2.0.0.2\classes_dex2jar.jar
 * Qualified Name:     cn.v6.sixrooms.utils.DensityUtil
 * JD-Core Version:    0.6.0
 */