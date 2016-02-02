package com.meiqia.meiqiasdk.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import com.meiqia.meiqiasdk.activity.MQConversationActivity;
import com.meiqia.meiqiasdk.util.MQResUtils;
import com.meiqia.meiqiasdk.util.MQUtils;

import java.io.File;

public class MQChoosePictureDialog extends Dialog implements View.OnClickListener {
    private MQConversationActivity mConversationActivity;
    private String mCameraPicPath;

    public MQChoosePictureDialog(MQConversationActivity conversationActivity) {
        super(conversationActivity, MQResUtils.getResStyleID( "MQDialog"));
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        setContentView(MQResUtils.getResLayoutID("mq_dialog_choose_pic"));
        findViewById(MQResUtils.getResIdID( "tv_choose_pic_camera")).setOnClickListener(this);
        findViewById(MQResUtils.getResIdID( "tv_choose_pic_gallery")).setOnClickListener(this);

        setCanceledOnTouchOutside(true);
        setCancelable(true);
        mConversationActivity = conversationActivity;
    }

    @Override
    public void onClick(View v) {
     if (v.getId() == MQResUtils.getResIdID( "tv_choose_pic_camera")) {
            Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = new File(MQUtils.getPicStorePath(mConversationActivity));
            file.mkdirs();
            String path = MQUtils.getPicStorePath(mConversationActivity) + "/" + System.currentTimeMillis() + ".jpg";
            File imageFile = new File(path);
            camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
            mCameraPicPath = path;
            try {
                mConversationActivity.startActivityForResult(camera, MQConversationActivity.REQUEST_CODE_CAMERA);
            } catch (Exception e) {
                MQUtils.show(mConversationActivity, MQResUtils.getResStringID( "mq_photo_not_support"));
            }
        } else if (v.getId() == MQResUtils.getResIdID( "tv_choose_pic_gallery")) {
            try {
                mConversationActivity.startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), MQConversationActivity.REQUEST_CODE_PHOTO);
            } catch (Exception e) {
                MQUtils.show(mConversationActivity, MQResUtils.getResIdID("mq_photo_not_support"));
            }
        }
    }

    public File getCameraPicFile() {
        String sdState = Environment.getExternalStorageState();
        if (!sdState.equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }
        File imageFile = new File(mCameraPicPath);
        if (imageFile.exists()) {
            return imageFile;
        } else {
            return null;
        }
    }
}