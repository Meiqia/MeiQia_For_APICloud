package com.meiqia.meiqiasdk.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.meiqia.meiqiasdk.model.EvaluateMessage;
import com.meiqia.meiqiasdk.util.MQResUtils;
import com.meiqia.meiqiasdk.util.MQUtils;

public class MQEvaluateDialog extends Dialog implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private RadioGroup mContentRg;
    private EditText mContentEt;
    private TextView mConfirmTv;
    private Callback mCallback;

    public MQEvaluateDialog(Activity activity) {
        super(activity, MQResUtils.getResStyleID("MQDialog"));
        setContentView(MQResUtils.getResLayoutID("mq_dialog_evaluate"));
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        setCanceledOnTouchOutside(true);
        setCancelable(true);

        mContentEt = (EditText) findViewById(MQResUtils.getResIdID("et_evaluate_content"));
        mContentRg = (RadioGroup) findViewById(MQResUtils.getResIdID("rg_evaluate_content"));
        mContentRg.setOnCheckedChangeListener(this);

        findViewById(MQResUtils.getResIdID("tv_evaluate_cancel")).setOnClickListener(this);
        mConfirmTv = (TextView) findViewById(MQResUtils.getResIdID("tv_evaluate_confirm"));
        mConfirmTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        MQUtils.closeKeyboard(this);
        dismiss();

        if (v.getId() == MQResUtils.getResIdID("tv_evaluate_confirm") && mCallback != null) {
            int level = EvaluateMessage.EVALUATE_GOOD;
            int checkedId = mContentRg.getCheckedRadioButtonId();
            if (checkedId == MQResUtils.getResIdID("rb_evaluate_medium")) {
                level = EvaluateMessage.EVALUATE_MEDIUM;
            } else if (checkedId == MQResUtils.getResIdID("rb_evaluate_bad")) {
                level = EvaluateMessage.EVALUATE_BAD;
            }
            String content = mContentEt.getText().toString().trim();
            mCallback.executeEvaluate(level, content);
        }

        // 重置状态
        mContentEt.setText("");
        mContentEt.clearFocus();
        mContentRg.check(MQResUtils.getResIdID("rb_evaluate_good"));
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // 点击RadioButton后关闭键盘，让Editext失去焦点
        mContentEt.clearFocus();
        MQUtils.closeKeyboard(this);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public interface Callback {
        /**
         * @param level   评价等级
         * @param content 评价内容
         */
        void executeEvaluate(int level, String content);
    }
}