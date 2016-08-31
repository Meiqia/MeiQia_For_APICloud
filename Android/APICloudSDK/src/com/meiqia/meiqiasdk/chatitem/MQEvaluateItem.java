package com.meiqia.meiqiasdk.chatitem;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.meiqia.meiqiasdk.model.EvaluateMessage;
import com.meiqia.meiqiasdk.util.MQResUtils;
import com.meiqia.meiqiasdk.widget.MQBaseCustomCompositeView;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/5/23 下午4:54
 * 描述:评价消息item
 */
public class MQEvaluateItem extends MQBaseCustomCompositeView {
    private TextView mLevelTv;
    private ImageView mLevelImg;
    private View mLevelBg;
    private TextView mContentTv;
    
    public MQEvaluateItem(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return MQResUtils.getResLayoutID("mq_item_msg_evaluate");
    }

    @Override
    protected void initView() {
        mLevelTv = getViewById(MQResUtils.getResIdID("tv_msg_evaluate_level"));
        mLevelBg = getViewById(MQResUtils.getResIdID("view_msg_evaluate_level"));
        mLevelImg = getViewById(MQResUtils.getResIdID("ic_msg_evaluate_level"));
        mContentTv = getViewById(MQResUtils.getResIdID("tv_msg_evaluate_content"));
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void processLogic() {
    }
    
    public void setMessage(EvaluateMessage evaluateMessage) {
        switch (evaluateMessage.getLevel()) {
            case EvaluateMessage.EVALUATE_BAD:
                mLevelImg.setImageResource(MQResUtils.getResDrawableID("mq_ic_angry_face"));
                mLevelTv.setText(MQResUtils.getResStringID("mq_evaluate_bad"));
                mLevelBg.setBackgroundResource(MQResUtils.getResDrawableID("mq_shape_evaluate_angry"));
                break;
            case EvaluateMessage.EVALUATE_MEDIUM:
                mLevelImg.setImageResource(MQResUtils.getResDrawableID("mq_ic_neutral_face"));
                mLevelTv.setText(MQResUtils.getResStringID("mq_evaluate_medium"));
                mLevelBg.setBackgroundResource(MQResUtils.getResDrawableID("mq_shape_evaluate_neutral"));
                break;
            case EvaluateMessage.EVALUATE_GOOD:
                mLevelImg.setImageResource(MQResUtils.getResDrawableID("mq_ic_smiling_face"));
                mLevelTv.setText(MQResUtils.getResStringID("mq_evaluate_good"));
                mLevelBg.setBackgroundResource(MQResUtils.getResDrawableID("mq_shape_evaluate_smiling"));
                break;
        }
        final String context = evaluateMessage.getContent();
        if (!TextUtils.isEmpty(context)) {
            mContentTv.setVisibility(View.VISIBLE);
            mContentTv.setText(context);
        } else {
            mContentTv.setVisibility(View.GONE);
        }
    }
}
