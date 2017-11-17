package com.beijing.chengxin.utils;

import android.content.Context;

public class PinyinUtils {

    private static String[] BUFFERS;

    public static void create(Context context, int resId) {
        BUFFERS = context.getResources().getStringArray(resId);
    }

    public static void release() {
        BUFFERS = null;
    }

    public static String convert(String value) {
        if (value == null) {
            return null;
        }

        if (BUFFERS == null) {
            return value;
        }

        char[] array = value.toCharArray();

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            try {
                int buff = array[i] & 0xFFFF;
                if (buff == 0x3007) {
                    builder.append(BUFFERS[0]);
                } else if ((buff >= 0x4E00) && (buff <= 0x9FA5)) {
                    builder.append(BUFFERS[buff - 0x4E00 + 1]);
                } else {
                    builder.append(array[i]);
                }
            } catch (Exception e) {
                builder.append(array[i]);
            }
        }

        return builder.toString();
    }
}
