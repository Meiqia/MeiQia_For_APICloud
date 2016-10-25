package com.meiqia.meiqiasdk.apicloud;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.meiqia.core.MQScheduleRule;
import com.meiqia.core.bean.MQMessage;
import com.meiqia.core.MQManager;
import com.meiqia.core.callback.OnClientInfoCallback;
import com.meiqia.core.callback.OnGetMessageListCallback;
import com.meiqia.core.callback.OnInitCallback;
import com.meiqia.meiqiasdk.activity.MQConversationActivity;
import com.meiqia.meiqiasdk.imageloader.MQUILImageLoader;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.uzmap.pkg.uzcore.UZWebView;
import com.uzmap.pkg.uzcore.uzmodule.UZModule;
import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;

public class UZMeiQia extends UZModule {

	private String customizedId;
	private String clientId;
	private int titleColor = MQConfig.DEFAULT;
	private int titleBarColor = MQConfig.DEFAULT;
	private String preSendMessage;
	private HashMap<String, String> clientInfoMap;

	public UZMeiQia(UZWebView webView) {
		super(webView);
	}

	public void jsmethod_initMeiQia(final UZModuleContext moduleContext) {
		String appkey = moduleContext.optString("appkey");
		String appKey = moduleContext.optString("appKey");
		String realKey = TextUtils.isEmpty(appkey) ? appKey : appkey;
		MQConfig.init(moduleContext.getContext(), realKey, new OnInitCallback() {
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
		clientInfoMap = parseJsonToMap(clientInfo);
	}

	public void jsmethod_setScheduledAgentOrAgentGroup(final UZModuleContext moduleContext) {
		String agentId = moduleContext.optString("agentId");
		String agentGroup = moduleContext.optString("agentGroup");
		String scheduleRuleStr = moduleContext.optString("scheduleRule");
		MQScheduleRule scheduleRule = MQScheduleRule.REDIRECT_ENTERPRISE;
		if ("none".equals(scheduleRuleStr)) {
			scheduleRule = MQScheduleRule.REDIRECT_NONE;
		} else if ("group".equals(scheduleRuleStr)) {
			scheduleRule = MQScheduleRule.REDIRECT_GROUP;
		}
		MQManager.getInstance(moduleContext.getContext()).setScheduledAgentOrGroupWithId(agentId, agentGroup,
				scheduleRule);
	}

	public void jsmethod_setLoginCustomizedId(final UZModuleContext moduleContext) {
		customizedId = moduleContext.optString("id");
		clientId = null;
	}

	public void jsmethod_setLoginMQClientId(final UZModuleContext moduleContext) {
		clientId = moduleContext.optString("id");
		customizedId = null;
	}

	public void jsmethod_show(final UZModuleContext moduleContext) {
		Intent intent = new Intent(moduleContext.getContext(), MQConversationActivity.class);
		if (!TextUtils.isEmpty(customizedId)) {
			intent.putExtra(MQConversationActivity.CUSTOMIZED_ID, customizedId);
		}
		if (!TextUtils.isEmpty(clientId)) {
			intent.putExtra(MQConversationActivity.CLIENT_ID, clientId);
		}
		intent.putExtra(MQConversationActivity.PRE_SEND_TEXT, preSendMessage);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("titleColor", titleColor);
		intent.putExtra("titleBarColor", titleBarColor);
		if (clientInfoMap != null) {
			intent.putExtra(MQConversationActivity.CLIENT_INFO, clientInfoMap);
		}
		startActivity(intent);
	}

	public void jsmethod_setTitleColor(final UZModuleContext moduleContext) {
		String color = moduleContext.optString("color");
		titleColor = Color.parseColor(color);
	}

	public void jsmethod_setTitleBarColor(final UZModuleContext moduleContext) {
		String color = moduleContext.optString("color");
		titleBarColor = Color.parseColor(color);
	}

	public void jsmethod_setPreSendTextMessage(final UZModuleContext moduleContext) {
		preSendMessage = moduleContext.optString("message");
	}

	public void jsmethod_setNavRightButton(final UZModuleContext moduleContext) {
		String title = moduleContext.optString("title");
		String image = moduleContext.optString("image");
		MQConversationActivity.rightButtonText = title;
		MQConversationActivity.rightButtonImageUrl = image;
		MQConversationActivity.rightIconOnClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				JSONObject result = new JSONObject();
				try {
					result.put("info", "success");
					result.put("clientId", clientId);
					moduleContext.success(result, true);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
	}

	public void jsmethod_getUnreadMessageCount(final UZModuleContext moduleContext) {
		MQManager.getInstance(moduleContext.getContext()).getUnreadMessages(new OnGetMessageListCallback() {
			@Override
			public void onFailure(int arg0, String arg1) {
				callbackFail(moduleContext, arg0, arg1);
			}

			@Override
			public void onSuccess(List<MQMessage> arg0) {
				JSONObject result = new JSONObject();
				Iterator<MQMessage> messageIterator = arg0.iterator();
				while (messageIterator.hasNext()) {
					MQMessage message = messageIterator.next();
					if (MQMessage.TYPE_FROM_CLIENT.equals(message.getFrom_type())) {
						messageIterator.remove();
					}
				}
				try {
					result.put("info", "success");
					result.put("count", arg0.size());
					moduleContext.success(result, true);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void callbackFail(final UZModuleContext moduleContext, int code, String response) {
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

	private HashMap<String, String> parseJsonToMap(JSONObject clientInfo) {
		HashMap<String, String> map = new HashMap<>();
		Iterator<String> keysItr = clientInfo.keys();
		while (keysItr.hasNext()) {
			String key = keysItr.next();
			String value = String.valueOf(clientInfo.opt(key));
			map.put(key, value);
		}
		return map;
	}

}
