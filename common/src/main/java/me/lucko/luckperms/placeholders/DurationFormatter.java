/*
 * This file is part of LuckPerms, licensed under the MIT License.
 *
 *  Copyright (c) lucko (Luck) <luck@lucko.me>
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package me.lucko.luckperms.placeholders;

import com.google.common.collect.ImmutableMap;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Map;

// Copied from https://github.com/lucko/LuckPerms/blob/master/common/src/main/java/me/lucko/luckperms/common/util/DurationFormatter.java
// Uses String instead of Chat Components

/**
 * Formats durations to a readable form
 */
class DurationFormatter {
    public static final DurationFormatter LONG = new DurationFormatter(false);
    public static final DurationFormatter CONCISE = new DurationFormatter(true);
    public static final DurationFormatter CONCISE_LOW_ACCURACY = new DurationFormatter(true, 3);

    public static final ChronoUnit[] FULL_UNITS = new ChronoUnit[]{
            ChronoUnit.YEARS,
            ChronoUnit.MONTHS,
            ChronoUnit.WEEKS,
            ChronoUnit.DAYS,
            ChronoUnit.HOURS,
            ChronoUnit.MINUTES,
            ChronoUnit.SECONDS
    };

    public static final ChronoUnit[] DAY_UNITS = new ChronoUnit[]{
            ChronoUnit.DAYS,
            ChronoUnit.HOURS,
            ChronoUnit.MINUTES,
            ChronoUnit.SECONDS
    };

    private final boolean concise;
    private final int accuracy;

    public DurationFormatter(boolean concise) {
        this(concise, Integer.MAX_VALUE);
    }

    public DurationFormatter(boolean concise, int accuracy) {
        this.concise = concise;
        this.accuracy = accuracy;
    }

    /**
     * Formats {@code duration} as a string.
     *
     * @param duration the duration
     * @return the formatted string
     */
    public String format(Duration duration) {
        return this.format(FULL_UNITS, duration);
    }

    /**
     * Formats {@code duration} as a string.
     *
     * @param units the units to formatting duration
     * @param duration the duration
     * @return the formatted string
     */
    public String format(ChronoUnit[] units, Duration duration) {
        long seconds = duration.getSeconds();

        for (ChronoUnit unit : units) {
            long n = seconds / unit.getDuration().getSeconds();

            if (n <= 0) {
                continue;
            }

            return formatPart(n, unit);
        }

        return formatPart(0, ChronoUnit.SECONDS);
    }

    // Taken from https://github.com/lucko/LuckPerms/blob/master/common/src/main/resources/luckperms_en.properties
    private static final Map<String, String> TRANSLATIONS = ImmutableMap.<String, String>builder()
            .put("luckperms.duration.unit.years.plural", "%s лет")
            .put("luckperms.duration.unit.years.singular", "%s год")
            .put("luckperms.duration.unit.years.short", "%sг")
            .put("luckperms.duration.unit.months.plural", "%s месяцев")
            .put("luckperms.duration.unit.months.singular", "%s месяц")
            .put("luckperms.duration.unit.months.short", "%sмес")
            .put("luckperms.duration.unit.weeks.plural", "%s недель")
            .put("luckperms.duration.unit.weeks.singular", "%s неделя")
            .put("luckperms.duration.unit.weeks.short", "%sнед")
            .put("luckperms.duration.unit.days.plural", "%s дней")
            .put("luckperms.duration.unit.days.singular", "%s день")
            .put("luckperms.duration.unit.days.short", "%sд")
            .put("luckperms.duration.unit.hours.plural", "%s часов")
            .put("luckperms.duration.unit.hours.singular", "%s час")
            .put("luckperms.duration.unit.hours.short", "%sч")
            .put("luckperms.duration.unit.minutes.plural", "%s минут")
            .put("luckperms.duration.unit.minutes.singular", "%s минута")
            .put("luckperms.duration.unit.minutes.short", "%sм")
            .put("luckperms.duration.unit.seconds.plural", "%s секунд")
            .put("luckperms.duration.unit.seconds.singular", "%s секунда")
            .put("luckperms.duration.unit.seconds.short", "%sс")
            .build();
    
    private String formatPart(long amount, ChronoUnit unit) {
        String format = this.concise ? "short" : amount == 1 ? "singular" : "plural";
        String translationKey = "luckperms.duration.unit." + unit.name().toLowerCase(Locale.ROOT) + "." + format;
        return String.format(TRANSLATIONS.get(translationKey), amount);
    }

}