package com.amatkivskiy.navigation.intent;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Helper class for passing parent activity through intents.
 */
public class Referrers {
    /**
     * Writes referrer activity to the specified intent.
     *
     * @param activityClass parent activity class.
     * @param target intent where to write parent activity class.
     */
    public static Intent putReferrer(@NonNull Intent target, @Nullable Class<?> activityClass) {
        return target.putExtra("referrer", activityClass);
    }

    /**
     * Extracts parent activity class from source intent.
     *
     * @param source intent which contains parent activity.
     * @return parent activity class.
     */
    public static Class<?> getReferrer(@NonNull Intent source) {
        return (Class<?>) source.getSerializableExtra("referrer");
    }

    /**
     * Checks whether source intent contains parent activity class.
     *
     * @param source intent.
     * @return true if parent activity class exists and not null, otherwise false.
     */
    public static boolean hasReferrer(@NonNull Intent source) {
        return source.hasExtra("referrer") && getReferrer(source) != null;
    }
}
