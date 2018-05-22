package com.bon.util;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.bon.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java8.util.function.Consumer;

public class StringUtils {
    private static final String TAG = StringUtils.class.getSimpleName();

    /**
     * use to display list to string, for example, address, city, state
     *
     * @param strings
     * @return
     */
    public static String joinText(String... strings) {
        return joinText(", ", strings);
    }

    /**
     * use to display list to string, for example, address, city, state
     *
     * @param strings
     * @return
     */
    public static String joinText(String joinCharacter, String... strings) {
        try {
            if (strings == null || strings.length <= 0) return "";

            List<String> stringList = new ArrayList<>();
            for (String s : strings) if (!StringUtils.isEmpty(s)) stringList.add(s);
            if (stringList == null || stringList.size() <= 0) return "";

            return TextUtils.join(joinCharacter, stringList);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return "";
    }

    /**
     * @param strings
     * @return
     */
    public static String joinText(List<String> strings) {
        return joinText(", ", strings);
    }

    /**
     * @param joinCharacter
     * @param strings
     * @return
     */
    public static String joinText(String joinCharacter, List<String> strings) {
        try {
            if (strings == null || strings.size() <= 0) return "";

            List<String> stringList = new ArrayList<>();
            for (String s : strings) if (!StringUtils.isEmpty(s)) stringList.add(s);
            if (stringList == null || stringList.size() <= 0) return "";

            return TextUtils.join(joinCharacter, stringList);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return "";
    }

    /**
     * check string empty, null...
     *
     * @param value
     * @return true or false
     */
    @SuppressLint("DefaultLocale")
    public static boolean isEmpty(@Nullable String value) {
        try {
            if (value == null || value.trim().equalsIgnoreCase("") ||
                    TextUtils.isEmpty(value) || value.equalsIgnoreCase("null")) {
                return true;
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return false;
    }

    /**
     * @param value
     * @return
     */
    public static boolean isEmpty(@Nullable CharSequence value) {
        try {
            if (value == null || value.toString().trim().equalsIgnoreCase("") ||
                    TextUtils.isEmpty(value) || value.toString().equalsIgnoreCase("null")) {
                return true;
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return false;
    }

    /**
     * @param value
     * @param objectConsumer
     */
    public static void isEmpty(@Nullable String value, Consumer<String> objectConsumer) {
        if (isEmpty(value) && objectConsumer != null) objectConsumer.accept(null);
    }

    /**
     * @param value
     * @param objectConsumer
     */
    public static void isEmpty(@Nullable CharSequence value, Consumer<String> objectConsumer) {
        if (!isEmpty(value) && objectConsumer != null) objectConsumer.accept(null);
    }

    /**
     * @param value
     * @param objectConsumer
     */
    public static void isNotEmpty(@Nullable String value, Consumer<String> objectConsumer) {
        if (!isEmpty(value) && objectConsumer != null) objectConsumer.accept(value);
    }

    /**
     * @param value
     * @param objectConsumer
     */
    public static void isNotEmpty(@Nullable CharSequence value, Consumer<String> objectConsumer) {
        if (!isEmpty(value) && objectConsumer != null) objectConsumer.accept(value + "");
    }

    /**
     * get random string
     *
     * @param sizeOfRandomString
     * @return
     */
    public static String getRandomString(final int sizeOfRandomString) {
        try {
            final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";
            final Random random = new Random();
            final StringBuilder sb = new StringBuilder(sizeOfRandomString);
            for (int i = 0; i < sizeOfRandomString; ++i) {
                sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
            }

            return sb.toString();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return "";
    }
}
