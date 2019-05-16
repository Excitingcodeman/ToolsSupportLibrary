package com.gs.toolssupport.filter;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * @author husky
 * create on 2019-05-16-20:32
 */
public class EmojiFilter implements InputFilter {
    /**
     * 去除emoji表情
     */
    Pattern emojiPattern = compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]|[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]",
            Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        Matcher emojiMatcher = emojiPattern.matcher(source);
        if (emojiMatcher.find()) {
            return "";
        }
        return null;
    }
}
