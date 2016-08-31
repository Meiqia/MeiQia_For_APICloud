package com.meiqia.meiqiasdk.chatitem;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meiqia.meiqiasdk.imageloader.MQImage;
import com.meiqia.meiqiasdk.model.RichTextMessage;
import com.meiqia.meiqiasdk.model.RobotMessage;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.meiqia.meiqiasdk.util.MQResUtils;
import com.meiqia.meiqiasdk.util.MQUtils;
import com.meiqia.meiqiasdk.widget.MQBaseCustomCompositeView;
import com.meiqia.meiqiasdk.widget.MQImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/4/1 下午3:55
 * 描述:机器人消息item
 */
public class MQRobotItem extends MQBaseCustomCompositeView {
    private MQImageView mAvatarIv;
    private LinearLayout mContainerLl;
    private FrameLayout mRobotRichTextFl;
    private LinearLayout mContentLl;
    private TextView mMenuTipTv;
    private LinearLayout mEvaluateLl;
    private TextView mUsefulTv;
    private TextView mUselessTv;
    private TextView mAlreadyFeedbackTv;

    private Callback mCallback;

    private int mPadding;
    private int mTextSize;
    private int mTextTipSize;

    private RobotMessage mRobotMessage;

    public MQRobotItem(Context context, Callback callback) {
        super(context);
        mCallback = callback;
    }

    @Override
    protected int getLayoutId() {
        return MQResUtils.getResLayoutID("mq_item_robot");
    }

    @Override
    protected void initView() {
        mAvatarIv = getViewById(MQResUtils.getResIdID("iv_robot_avatar"));
        mContainerLl = getViewById(MQResUtils.getResIdID("ll_robot_container"));
        mRobotRichTextFl = getViewById(MQResUtils.getResIdID("mq_robot_rich_text_container"));
        mContentLl = getViewById(MQResUtils.getResIdID("ll_robot_content"));
        mEvaluateLl = getViewById(MQResUtils.getResIdID("ll_robot_evaluate"));
        mUsefulTv = getViewById(MQResUtils.getResIdID("tv_robot_useful"));
        mUselessTv = getViewById(MQResUtils.getResIdID("tv_robot_useless"));
        mMenuTipTv = getViewById(MQResUtils.getResIdID("tv_robot_menu_tip"));
        mAlreadyFeedbackTv = getViewById(MQResUtils.getResIdID("tv_robot_already_feedback"));
    }

    @Override
    protected void setListener() {
        mUsefulTv.setOnClickListener(this);
        mUselessTv.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        MQUtils.applyCustomUITintDrawable(mContainerLl, MQResUtils.getResColorID("mq_chat_left_bubble_final"), MQResUtils.getResColorID("mq_chat_left_bubble"), MQConfig.ui.leftChatBubbleColorResId);
        MQUtils.applyCustomUITextAndImageColor(MQResUtils.getResColorID("mq_chat_robot_menu_tip_textColor"), MQConfig.ui.robotMenuTipTextColorResId, null, mMenuTipTv);
        MQUtils.applyCustomUITextAndImageColor(MQResUtils.getResColorID("mq_chat_robot_evaluate_textColor"), MQConfig.ui.robotEvaluateTextColorResId, null, mUsefulTv, mUselessTv);

        mPadding = getResources().getDimensionPixelSize(MQResUtils.getResDimenID("mq_size_level2"));
        mTextSize = getResources().getDimensionPixelSize(MQResUtils.getResDimenID("mq_textSize_level2"));
        mTextTipSize = getResources().getDimensionPixelSize(MQResUtils.getResDimenID("mq_textSize_level1"));
    }

    @Override
    public void onClick(View view) {
        if (mCallback != null) {
            if (view.getId() == MQResUtils.getResIdID("tv_robot_useful")) {
                mCallback.onEvaluateRobotAnswer(mRobotMessage, RobotMessage.EVALUATE_USEFUL);
            } else if (view.getId() == MQResUtils.getResIdID("tv_robot_useless")) {
                mCallback.onEvaluateRobotAnswer(mRobotMessage, RobotMessage.EVALUATE_USELESS);
            }
        }
    }

    public void setMessage(RobotMessage robotMessage, Activity activity) {
        reset();

        mRobotMessage = robotMessage;
        MQImage.displayImage(activity, mAvatarIv, mRobotMessage.getAvatar(), MQResUtils.getResDrawableID("mq_ic_holder_avatar"), MQResUtils.getResDrawableID("mq_ic_holder_avatar"), 100, 100, null);
        handleEvaluateStatus();
        fillContentLl();
    }

    private void reset() {
        mContentLl.removeAllViews();
        mRobotRichTextFl.removeAllViews();
        mContainerLl.setVisibility(View.GONE);
        mContentLl.setVisibility(View.GONE);
        mEvaluateLl.setVisibility(View.GONE);
        mMenuTipTv.setVisibility(View.GONE);
        mRobotRichTextFl.setVisibility(View.GONE);
        mAvatarIv.setVisibility(GONE);
    }

    private void handleEvaluateStatus() {
        if (TextUtils.equals(RobotMessage.SUB_TYPE_EVALUATE, mRobotMessage.getSubType())) {
            mEvaluateLl.setVisibility(View.VISIBLE);
            if (mRobotMessage.isAlreadyFeedback()) {
                mUselessTv.setVisibility(View.GONE);
                mUsefulTv.setVisibility(View.GONE);
                mAlreadyFeedbackTv.setVisibility(View.VISIBLE);
            } else {
                mUselessTv.setVisibility(View.VISIBLE);
                mUsefulTv.setVisibility(View.VISIBLE);
                mAlreadyFeedbackTv.setVisibility(View.GONE);
            }
        }
    }

    private void fillContentLl() {
        try {
            if (RobotMessage.SUB_TYPE_UNKNOWN.equals(mRobotMessage.getSubType())) {
                addNormalTextView(getResources().getString(MQResUtils.getResStringID("mq_unknown_msg_tip")));
                return;
            }

            JSONArray contentJsonArray = new JSONArray(mRobotMessage.getContentRobot());
            boolean isRelated = isRelated(contentJsonArray);
            for (int i = 0; i < contentJsonArray.length(); i++) {
                JSONObject itemJsonObject = contentJsonArray.optJSONObject(i);
                String rich_text = itemJsonObject.optString("rich_text");
                if (isRelated) {
                    if (TextUtils.equals("text", itemJsonObject.optString("type"))) {
                        addNormalTextView(itemJsonObject.optString("text"));
                    } else if (TextUtils.equals("related", itemJsonObject.optString("type"))) {
                        String text_before = itemJsonObject.optString("text_before");
                        addMenuList(itemJsonObject.optJSONArray("items"), text_before);
                    }
                }
                // rich_text 放在后面,不然会先解析成 rich_text
                else if (isRichText(itemJsonObject, rich_text)) {
                    addRichText(rich_text);
                } else if (TextUtils.equals("text", itemJsonObject.optString("type"))) {
                    addNormalTextView(itemJsonObject.optString("text"));
                } else if (TextUtils.equals("menu", itemJsonObject.optString("type"))) {
                    addMenuList(itemJsonObject.optJSONArray("items"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isRichText(JSONObject itemJsonObject, String rich_text) {
        return TextUtils.equals("text", itemJsonObject.optString("type"))
                && !TextUtils.isEmpty(rich_text)
                && TextUtils.equals("evaluate", mRobotMessage.getSubType());
    }

    private boolean isRelated(JSONArray contentJsonArray) {
        boolean isRelated = false;
        try {
            for (int i = 0; i < contentJsonArray.length(); i++) {
                JSONObject jsonObject = contentJsonArray.getJSONObject(i);
                String type = jsonObject.optString("type");
                if (TextUtils.equals("related", type)) {
                    isRelated = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isRelated;
    }

    private void addRichText(String rich_text) {
        try {
            mRobotRichTextFl.setVisibility(VISIBLE);
            MQRichTextItem richTextItem = new MQRichTextItem(getContext());
            RichTextMessage richTextMessage = new RichTextMessage();
            JSONObject extra = new JSONObject(mRobotMessage.getExtra());
            extra.put("content", rich_text);
            richTextMessage.setExtra(extra.toString());
            richTextItem.setMessage(richTextMessage, (Activity) getContext());
            richTextItem.setRobotMessage(mRobotMessage);
            mRobotRichTextFl.addView(richTextItem);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加普通的文本内容
     *
     * @param text
     */
    private void addNormalTextView(String text) {
        mContainerLl.setVisibility(VISIBLE);
        mAvatarIv.setVisibility(VISIBLE);
        mContentLl.setVisibility(VISIBLE);
        if (!TextUtils.isEmpty(text)) {
            TextView textView = new TextView(getContext());
            textView.setText(text);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            textView.setTextColor(getResources().getColor(MQResUtils.getResColorID("mq_chat_left_textColor")));
            textView.setPadding(mPadding, mPadding, mPadding, mPadding);
            MQUtils.applyCustomUITextAndImageColor(MQResUtils.getResColorID("mq_chat_left_textColor"), MQConfig.ui.leftChatTextColorResId, null, textView);
            mContentLl.addView(textView);
        }
    }

    /**
     * 添加菜单列表
     *
     * @param jsonArray
     */
    private void addMenuList(JSONArray jsonArray) {
        mContainerLl.setVisibility(VISIBLE);
        mAvatarIv.setVisibility(VISIBLE);
        mContentLl.setVisibility(VISIBLE);
        mMenuTipTv.setVisibility(View.VISIBLE);
        if (jsonArray != null && jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                addMenuItem(jsonArray.optJSONObject(i));
            }
        }
    }

    private void addMenuList(JSONArray items, String text_before) {
        if (!TextUtils.isEmpty(text_before)) {
            TextView textView = new TextView(getContext());
            textView.setText(text_before);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextTipSize);
            textView.setTextColor(getResources().getColor(MQResUtils.getResColorID("mq_chat_robot_menu_tip_textColor")));
            textView.setPadding(mPadding, mPadding, mPadding, mPadding);
            MQUtils.applyCustomUITextAndImageColor(MQResUtils.getResColorID("mq_chat_robot_menu_tip_textColor"), MQConfig.ui.robotMenuTipTextColorResId, null, textView);
            mContentLl.addView(textView);
        }
        addMenuList(items);
    }

    /**
     * 添加菜单列表item
     *
     * @param jsonObject
     */
    private void addMenuItem(JSONObject jsonObject) {
        final String text = jsonObject.optString("text");
        if (!TextUtils.isEmpty(text)) {
            TextView itemTv = (TextView) View.inflate(getContext(), MQResUtils.getResLayoutID("mq_item_robot_menu"), null);
            MQUtils.applyCustomUITextAndImageColor(MQResUtils.getResColorID("mq_chat_robot_menu_item_textColor"), MQConfig.ui.robotMenuItemTextColorResId, null, itemTv);
            itemTv.setText(text);
            itemTv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallback != null) {
                        if (text.indexOf(".") == 1 && text.length() > 2) {
                            mCallback.onClickRobotMenuItem(text.substring(2));
                        } else {
                            mCallback.onClickRobotMenuItem(text);
                        }
                    }
                }
            });
            mContentLl.addView(itemTv);
        }
    }

    public interface Callback {
        void onEvaluateRobotAnswer(RobotMessage robotMessage, int useful);

        void onClickRobotMenuItem(String text);
    }
}
