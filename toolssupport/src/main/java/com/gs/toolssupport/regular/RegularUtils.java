package com.gs.toolssupport.regular;

import android.text.TextUtils;

/**
 * @author husky
 * create on 2019-05-16-17:30
 */
public class RegularUtils {

/**
 * 手机号码正则
 */
   public static final String phoneRex = "^1[0-9]\\d{9}";
    /**
     * 邮箱的校验规则
     */
   public static final String emailRex = "^[A-Za-z0-9_\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    /**
     * 密码正则
     */
   public static final String passRex =
            "^(?!^\\d+$)(?!^[a-zA-Z]+$)(?!^[\\-\\/:;()$&@\"\\.,\\?\\!'\\[\\]#%\\^\\*\\+=_\\\\\\|~<>€£¥•：；（）¥@“”。，、？！【】｛｝—《》\\·]+$)[\\da-zA-Z\\-\\/:;()$&@\"\\.,\\?\\!'\\[\\]#%\\^\\*\\+=_\\\\\\|~<>€£¥•：；（）¥@“”。，、？！【】｛｝—《》\\·]{6,20}$";
    /**
     * 身份证正则
     */
   public static final String idRex = "(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])";

    /**
     * 汉字
     */
   public static final String chn_characters = "([\\u4e00-\\u9fa5]{2,10})";

    /**
     * 新的密码校验规则
     */
    public static final String newPassword = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$";
    /**
     * @param source 需要校验的字符串
     * @param ruler 校验的规则
     */
    public static boolean checkRex(String source, String ruler) {
        if (TextUtils.isEmpty(source)) {
            return false;
        }
        return source.matches(ruler);
    }
}
