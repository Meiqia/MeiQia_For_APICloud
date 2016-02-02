package com.meiqia.meiqiasdk.apicloud;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;

import com.meiqia.core.MQScheduleRule;
import com.meiqia.core.MQManager;
import com.meiqia.core.callback.OnClientInfoCallback;
import com.meiqia.core.callback.OnInitCallback;
import com.meiqia.meiqiasdk.activity.MQConversationActivity;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.uzmap.pkg.uzcore.UZWebView;
import com.uzmap.pkg.uzcore.uzmodule.UZModule;
import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;

public class UZMeiQia extends UZModule {

	private String customizedId;
	private String clientId;

	public UZMeiQia(UZWebView webView) {
		super(webView);
	}

	public void jsmethod_initMeiQia(final UZModuleContext moduleContext) {
		String appkey = moduleContext.optString("appkey");
		String appKey = moduleContext.optString("appKey");
		String realKey = TextUtils.isEmpty(appkey) ? appKey : appkey;
		MQManager.init(moduleContext.getContext(), realKey,
				new OnInitCallback() {
					@Override
					public void onFailure(int arg0, String arg1) {
						callbackFail(moduleContext, arg0, arg1);
					}

					@Override
					public void onSuccess(String clientId) {
						JSONObject result = new JSONObject();
						try {
							result.put("info", "success");
							result.put("clientId", clientId);
							moduleContext.success(result, true);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	public void jsmethod_setClientInfo(final UZModuleContext moduleContext) {
		OnClientInfoCallback callback = new OnClientInfoCallback() {

			@Override
			public void onFailure(int arg0, String arg1) {
				callbackFail(moduleContext, arg0, arg1);
			}

			@Override
			public void onSuccess() {
				callbackSuc(moduleContext);
			}
		};
		JSONObject clientInfo = moduleContext.get();
		if (clientInfo == null) {
			callback.onFailure(0, "param error");
			return;
		}
		Map<String, String> clientInfoMap = parseJsonToMap(clientInfo);
		MQManager.getInstance(moduleContext.getContext()).setClientInfo(
				clientInfoMap, callback);
	}

	public void jsmethod_setScheduledAgentOrAgentGroup(
			final UZModuleContext moduleContext) {
		String agentId = moduleContext.optString("agentId");
		String agentGroup = moduleContext.optString("agentGroup");
		String scheduleRuleStr = moduleContext.optString("scheduleRule");
		MQScheduleRule scheduleRule = MQScheduleRule.REDIRECT_ENTERPRISE;
		if ("none".equals(scheduleRuleStr)) {
			scheduleRule = MQScheduleRule.REDIRECT_NONE;
		} else if ("group".equals(scheduleRuleStr)) {
			scheduleRule = MQScheduleRule.REDIRECT_GROUP;
		}
		MQManager.getInstance(moduleContext.getContext())
				.setScheduledAgentOrGroupWithId(agentId, agentGroup,
						scheduleRule);
	}

	public void jsmethod_setLoginCustomizedId(
			final UZModuleContext moduleContext) {
		customizedId = moduleContext.optString("id");
		clientId = null;
	}

	public void jsmethod_setLoginMQClientId(final UZModuleContext moduleContext) {
		clientId = moduleContext.optString("id");
		customizedId = null;
	}

	public void jsmethod_show(final UZModuleContext moduleContext) {
		Intent intent = new Intent(moduleContext.getContext(),
				MQConversationActivity.class);
		if (!TextUtils.isEmpty(customizedId)) {
			intent.putExtra(MQConversationActivity.CUSTOMIZED_ID, customizedId);
		}
		if (!TextUtils.isEmpty(clientId)) {
			intent.putExtra(MQConversationActivity.CLIENT_ID, clientId);
		}
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	public void jsmethod_setTitleColor(final UZModuleContext moduleContext) {
		MQConfig config = new MQConfig(mContext);
		String color = moduleContext.optString("color");
		config.setTitleTextColor(Color.parseColor(color));
	}

	public void jsmethod_setTitleBarColor(final UZModuleContext moduleContext) {
		MQConfig config = new MQConfig(mContext);
		String color = moduleContext.optString("color");
		config.setTitleBackgroundColor(Color.parseColor(color));
	}

	private void callbackFail(final UZModuleContext moduleContext, int code,
			String response) {
		JSONObject result = new JSONObject();
		try {
			result.put("code", code);
			result.put("info", response);
			moduleContext.error(result, result, true);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.e("meiqia", "code = " + code + "  info = " + response);
	}

	private void callbackSuc(final UZModuleContext moduleContext) {
		JSONObject result = new JSONObject();
		try {
			result.put("info", "success");
			moduleContext.success(result, true);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.e("meiqia", "suc");
	}

	private Map<String, String> parseJsonToMap(JSONObject clientInfo) {
		Map<String, String> map = new HashMap<>();
		Iterator<String> keysItr = clientInfo.keys();
		while (keysItr.hasNext()) {
			String key = keysItr.next();
			String value = String.valueOf(clientInfo.opt(key));
			map.put(key, value);
		}
		return map;
	}

}
