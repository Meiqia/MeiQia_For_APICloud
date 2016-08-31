package com.meiqia.meiqiasdk.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meiqia.meiqiasdk.util.MQConfig;
import com.meiqia.meiqiasdk.util.MQResUtils;
import com.meiqia.meiqiasdk.util.MQUtils;

/**
 * OnePiece
 * Created by xukq on 6/27/16.
 */
public abstract class MQBaseActivity extends Activity {

    private RelativeLayout mTitleRl;
    private RelativeLayout mBackRl;
    private TextView mBackTv;
    private ImageView mBackIv;
    private TextView mTitleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());

        mTitleRl = (RelativeLayout) findViewById(MQResUtils.getResIdID("title_rl"));
        mBackRl = (RelativeLayout) findViewById(MQResUtils.getResIdID("back_rl"));
        mBackTv = (TextView) findViewById(MQResUtils.getResIdID("back_tv"));
        mBackIv = (ImageView) findViewById(MQResUtils.getResIdID("back_iv"));
        mTitleTv = (TextView) findViewById(MQResUtils.getResIdID("title_tv"));
        applyCustomUIConfig();
        mBackRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        initView(savedInstanceState);
        setListener();
        processLogic(savedInstanceState);
    }

    private void applyCustomUIConfig() {
        if (MQConfig.DEFAULT != MQConfig.ui.backArrowIconResId) {
            mBackIv.setImageResource(MQConfig.ui.backArrowIconResId);
        }

        // 处理标题栏背景色
        MQUtils.applyCustomUITintDrawable(mTitleRl, MQResUtils.getResColorID("mq_white"), MQResUtils.getResColorID("mq_activity_title_bg"), MQConfig.ui.titleBackgroundResId);

        // 处理标题、返回、返回箭头颜色
        MQUtils.applyCustomUITextAndImageColor(MQResUtils.getResColorID("mq_activity_title_textColor"), MQConfig.ui.titleTextColorResId, mBackIv, mBackTv, mTitleTv);

        // 处理标题文本的对其方式
        MQUtils.applyCustomUITitleGravity(mBackTv, mTitleTv);
    }

    protected void setTitle(String title) {
        mTitleTv.setText(title);
    }

    protected abstract int getLayoutRes();

    /**
     * 初始化布局以及View控件
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 给View控件添加事件监听器
     */
    protected abstract void setListener();

    /**
     * 处理业务逻辑，状态恢复等操作
     *
     * @param savedInstanceState
     */
    protected abstract void processLogic(Bundle savedInstanceState);

}

