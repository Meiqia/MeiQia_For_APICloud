package com.meiqia.meiqiasdk.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MQEmotionUtil {
    public static final String REGEX_EMOJI = ":[\u4e00-\u9fa5\\w]+:";
    public static final String REGEX_WEBSITE = "([a-zA-Z]+://)?[a-zA-Z\\d-]+(\\.[a-zA-Z\\d-]+)*(\\.[a-zA-Z]+)+(:\\d+)?(/[\\w\\d\\.\\-~!@#$%^&*+?:_/=<>]*)?";
    public static final String REGEX_GROUP = "(" + REGEX_EMOJI + ")|(" + REGEX_WEBSITE + ")";

    public static  Map<String, Integer> sEmotionMap;
    public static   String[] sEmotionKeyArr;
    public static  int[] sEmotionValueArr ;
    
    private MQEmotionUtil() {
    }
    
    public  static void init(){
    	sEmotionKeyArr   = new String[]{
                ":smile:",
                ":smiley:",
                ":grinning:",
                ":blush:",
                ":relaxed:",
                ":wink:",
                ":heart_eyes:",
                ":kissing_heart:",
                ":kissing_closed_eyes:",
                ":kissing:",
                ":kissing_smiling_eyes:",
                ":stuck_out_tongue_winking_eye:",
                ":stuck_out_tongue_closed_eyes:",
                ":stuck_out_tongue:",
                ":flushed:",
                ":grin:",
                ":pensive:",
                ":relieved:",
                ":unamused:",
                ":disappointed:",
                ":persevere:",
                ":cry:",
                ":joy:",
                ":sob:",
                ":sleepy:",
                ":disappointed_relieved:",
                ":cold_sweat:",
                ":sweat_smile:",
                ":sweat:",
                ":weary:",
                ":tired_face:",
                ":fearful:",
                ":scream:",
                ":angry:",
                ":rage:",
                ":dog:",
        };
    	sEmotionValueArr = new int[]{
                MQResUtils.getResDrawableID("mq_emoji_1"),
                MQResUtils.getResDrawableID("mq_emoji_2"),
                MQResUtils.getResDrawableID("mq_emoji_3"),
                MQResUtils.getResDrawableID("mq_emoji_4"),
                MQResUtils.getResDrawableID("mq_emoji_5"),
                MQResUtils.getResDrawableID("mq_emoji_6"),
                MQResUtils.getResDrawableID("mq_emoji_7"),
                MQResUtils.getResDrawableID("mq_emoji_8"),
                MQResUtils.getResDrawableID("mq_emoji_9"),
                MQResUtils.getResDrawableID("mq_emoji_10"),
                MQResUtils.getResDrawableID("mq_emoji_11"),
                MQResUtils.getResDrawableID("mq_emoji_12"),
                MQResUtils.getResDrawableID("mq_emoji_13"),
                MQResUtils.getResDrawableID("mq_emoji_14"),
                MQResUtils.getResDrawableID("mq_emoji_15"),
                MQResUtils.getResDrawableID("mq_emoji_16"),
                MQResUtils.getResDrawableID("mq_emoji_17"),
                MQResUtils.getResDrawableID("mq_emoji_18"),
                MQResUtils.getResDrawableID("mq_emoji_19"),
                MQResUtils.getResDrawableID("mq_emoji_20"),
                MQResUtils.getResDrawableID("mq_emoji_21"),
                MQResUtils.getResDrawableID("mq_emoji_22"),
                MQResUtils.getResDrawableID("mq_emoji_23"),
                MQResUtils.getResDrawableID("mq_emoji_24"),
                MQResUtils.getResDrawableID("mq_emoji_25"),
                MQResUtils.getResDrawableID("mq_emoji_26"),
                MQResUtils.getResDrawableID("mq_emoji_27"),
                MQResUtils.getResDrawableID("mq_emoji_28"),
                MQResUtils.getResDrawableID("mq_emoji_29"),
                MQResUtils.getResDrawableID("mq_emoji_30"),
                MQResUtils.getResDrawableID("mq_emoji_31"),
                MQResUtils.getResDrawableID("mq_emoji_32"),
                MQResUtils.getResDrawableID("mq_emoji_33"),
                MQResUtils.getResDrawableID("mq_emoji_34"),
                MQResUtils.getResDrawableID("mq_emoji_35"),
                MQResUtils.getResDrawableID("mq_emoji_36")
        };
    	
    	sEmotionMap = new HashMap<>();
        int count = sEmotionKeyArr.length;
        for (int i = 0; i < count; i++) {
            sEmotionMap.put(sEmotionKeyArr[i], sEmotionValueArr[i]);
        }

    }

    public static int getImgByName(String imgName) {
        Integer integer = sEmotionMap.get(imgName);
        return integer == null ? -1 : integer;
    }

    public static SpannableString getEmotionText(Context context, String source) {
        SpannableString spannableString = new SpannableString(source);
        Pattern pattern = Pattern.compile(REGEX_GROUP);
        Matcher matcher = pattern.matcher(spannableString);
        if (matcher.find()) {
            matcher.reset();
        }

        while (matcher.find()) {
            String emojiStr = matcher.group(1);
            String websiteStr = matcher.group(2);
            // 处理emoji表情
            if (emojiStr != null) {
                ImageSpan imageSpan = null;
                int imgRes = getImgByName(emojiStr);
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imgRes);
                if (bitmap != null) {
                    imageSpan = new ImageSpan(context, bitmap);
                }
                if (imageSpan != null) {
                    int start = matcher.start(1);
                    spannableString.setSpan(imageSpan, start, start + emojiStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            // 处理网址
            if (websiteStr != null) {
                int start = matcher.start(2);
                spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(MQResUtils.getResColorID("mq_url_foreground"))), start, start + websiteStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableString;
    }
}