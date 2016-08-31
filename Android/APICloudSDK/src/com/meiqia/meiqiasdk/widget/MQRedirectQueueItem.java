package com.meiqia.meiqiasdk.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.meiqia.meiqiasdk.callback.LeaveMessageCallback;
import com.meiqia.meiqiasdk.model.RedirectQueueMessage;
import com.meiqia.meiqiasdk.util.MQResUtils;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/4/22 上午10:37
 * 描述:
 */
public class MQRedirectQueueItem extends MQBaseCustomCompositeView {
    private ImageView mQueueAnimIv;
    private TextView mTipTv;

    private LeaveMessageCallback mCallback;

    public MQRedirectQueueItem(Context context, LeaveMessageCallback leaveMessageCallback) {
        super(context);
        mCallback = leaveMessageCallback;
    }

    @Override
    protected int getLayoutId() {
        return MQResUtils.getResLayoutID("mq_item_redirect_queue");
    }

    @Override
    protected void initView() {
        mQueueAnimIv = getViewById(MQResUtils.getResIdID("iv_redirect_queue_anim"));
        mTipTv = getViewById(MQResUtils.getResIdID("tv_redirect_queue_tip"));
    }

    @Override
    protected void setListener() {
        getViewById(MQResUtils.getResIdID("tv_redirect_queue_leave_msg")).setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
    }

    @Override
    public void onClick(View view) {
        if (mCallback != null) {
            mCallback.onClickLeaveMessage();
        }
    }

    public void setMessage(RedirectQueueMessage redirectQueueMessage) {
        mTipTv.setText(getResources().getString(MQResUtils.getResStringID("mq_queue_leave_msg"), redirectQueueMessage.getQueueSize()));
        ((AnimationDrawable) mQueueAnimIv.getDrawable()).start();
    }
}
