package com.meiqia.meiqiasdk.util;

import java.lang.reflect.Field;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

public class MQResUtils {

	private static Context context;

	public static void init(Context context) {
		MQResUtils.context = context.getApplicationContext();
	}

	public static int getResLayoutID(String resName) {
		Resources res = context.getResources();
		return res.getIdentifier(resName, "layout", context.getPackageName());
	}

	public static int getResStringID(String resName) {
		Resources res = context.getResources();
		return res.getIdentifier(resName, "string", context.getPackageName());
	}

	public static int getResIdID(String resName) {
		Resources res = context.getResources();
		return res.getIdentifier(resName, "id", context.getPackageName());
	}

	public static int getResDrawableID(String resName) {
		Resources res = context.getResources();
		return res.getIdentifier(resName, "drawable", context.getPackageName());
	}

	public static int getResRawID(String resName) {
		Resources res = context.getResources();
		return res.getIdentifier(resName, "raw", context.getPackageName());
	}

	public static int getResStyleID(String resName) {
		Resources res = context.getResources();
		return res.getIdentifier(resName, "style", context.getPackageName());
	}

	public static int getResColorID(String resName) {
		Resources res = context.getResources();
		return res.getIdentifier(resName, "color", context.getPackageName());
	}

	public static int getResDimenID(String resName) {
		Resources res = context.getResources();
		return res.getIdentifier(resName, "dimen", context.getPackageName());
	}
	
	public static int[] getStyleableIntArray(Context context, String name) {
        try {
            Field[] fields = Class.forName(context.getPackageName() + ".R$styleable").getFields();//.与$ difference,$表示R的子类
            for (Field field : fields) {
                if (field.getName().equals(name)) {
                    int ret[] = (int[]) field.get(null);
                    return ret;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

}
