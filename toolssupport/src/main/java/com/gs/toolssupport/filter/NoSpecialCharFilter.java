package com.gs.toolssupport.filter;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Toast;
import com.gs.toolssupport.GlobalInit;
import com.gs.toolssupport.R;
import com.gs.toolssupport.resources.ResourcesUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * @author husky
 * create on 2019-05-16-20:39
 */
public class NoSpecialCharFilter implements InputFilter {

    /**
     * 标点符号
     */
    @SuppressWarnings("Annotator")
    Pattern limitExPattern = compile("[`~@#$%^&*()+=|{}''\\[\\]<>/~@#￥%&*（）——+|{}【】]");
    /**
     * 去除emoji表情
     */
    Pattern emojiPattern = compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]|[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]",
            Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
    /**
     * 中文，字母和数字
     */
    Pattern numAndCharPattern = compile("[^a-zA-Z0-9\\u4E00-\\u9FA5_]");


    /**
     * 只能是汉字
     */
    private boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B;

    }

    private boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    /**
     * 数字
     */
    private boolean isNum(char c) {
        return ('0' <= c && c <= '9');
    }

    /**
     * 字母
     *
     * @param c
     * @return
     */
    private boolean isZiMu(char c) {
        if ('a' <= c && c <= 'z') {
            return true;
        }
        return 'A' <= c && c <= 'Z';

    }


    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        Matcher numAndCharMatcher = numAndCharPattern.matcher(source);
        Matcher emojiMatcher = emojiPattern.matcher(source);
        Matcher limitExMatcher = limitExPattern.matcher(source);
        if ((numAndCharMatcher.find() && limitExMatcher.find()) || emojiMatcher.find()) {
            Toast.makeText(GlobalInit.application, ResourcesUtils.getString(R.string.no_emoji), Toast.LENGTH_SHORT).show();
            return "";
        } else {

            return null;
        }


    }
}
