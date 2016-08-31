package com.meiqia.meiqiasdk.dialog;

import android.app.Activity;
import android.app.Dialog;

import com.meiqia.meiqiasdk.util.MQResUtils;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/3/4 上午11:03
 * 描述:
 */
public class MQLoadingDialog extends Dialog {

    public MQLoadingDialog(Activity activity) {
        super(activity, MQResUtils.getResStyleID("MQDialog"));
        setContentView(MQResUtils.getResLayoutID("mq_dialog_loading"));
    }
}
