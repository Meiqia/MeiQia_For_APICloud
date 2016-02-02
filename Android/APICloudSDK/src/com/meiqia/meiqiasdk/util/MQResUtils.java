package com.meiqia.meiqiasdk.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

public class MQResUtils {
	
	private static Context context;
	
	public static void init(Context context){
		MQResUtils.context = context.getApplicationContext();
	}
	
	public static int getResLayoutID(String resName){
		Resources res=context.getResources();
		return res.getIdentifier(resName,"layout",context.getPackageName());
	}
	
	public static int getResStringID(String resName){
		Resources res=context.getResources();
		return res.getIdentifier(resName,"string",context.getPackageName());
	}

	public static int getResIdID(String resName){
		Resources res=context.getResources();
		return res.getIdentifier(resName,"id",context.getPackageName());
	}

	public static int getResDrawableID(String resName){
		Resources res=context.getResources();
		return res.getIdentifier(resName,"drawable",context.getPackageName());
	}
	
	public static int getResRawID(String resName){
		Resources res=context.getResources();
		return res.getIdentifier(resName,"raw",context.getPackageName());
	}
	
	public static int getResStyleID(String resName){
		Resources res=context.getResources();
		return res.getIdentifier(resName,"style",context.getPackageName());
	}
	
	public static int getResColorID(String resName){
		Resources res=context.getResources();
		return res.getIdentifier(resName,"color",context.getPackageName());
	}
	
}
