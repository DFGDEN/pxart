package dfgden.pxart.com.pxart.utils;

import android.content.Context;
import android.content.res.Resources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import dfgden.pxart.com.pxart.PxartApplication;
import dfgden.pxart.com.pxart.R;


public class TimeUtil {
    private static Context getContext() {
        return PxartApplication.context;
    }

    public static String getPubDate(String date) {
        long deltaTime = System.currentTimeMillis() - convertTime(date);
        long week = deltaTime / 604800000;
        long day = deltaTime / 86400000;
        long hour = deltaTime / 3600000;
        long min = deltaTime / 60000;

        if (week == 1) {
            return  getContext().getString(R.string.timeutils_weekago);
        }
        if (week >11 && week%10 ==1) {
            return getContext().getString(R.string.timeutils_over)  + week + getContext().getString(R.string.timeutils_21weeksago);
        }
        if (week >= 2) {
            return getContext().getString(R.string.timeutils_over) + week + getContext().getString(R.string.timeutils_weeksago);
        }
        if (day == 1) {
            return getContext().getString(R.string.R_string_timeutils_dayago);
        }
        if (day >11 &&day%10 ==1) {
            return getContext().getString(R.string.timeutils_over) + day + getContext().getString(R.string.timeutils_21daysago);
        }
        if (day >= 2) {
            return getContext().getString(R.string.timeutils_over) + day + getContext().getString(R.string.timeutils_daysago);
        }
        if (hour == 1) {
            return getContext().getString(R.string.timeutils_hourago);
        }
        if (hour >11 &&hour%10 ==1) {
            return getContext().getString(R.string.timeutils_over) + hour + getContext().getString(R.string.timeutils_21hoursago);
        }
        if (hour >= 2) {
            return getContext().getString(R.string.timeutils_over) + hour +getContext().getString(R.string.timeutils_hoursago);
        }
        if (min == 1) {
            return getContext().getString(R.string.timeutils_minago);
        }
        if (min >11 &&min%10 ==1) {
            return getContext().getString(R.string.timeutils_over) + min + getContext().getString(R.string.timeutils_21minutsago);
        }
        if (min >= 2) {
            return getContext().getString(R.string.timeutils_over) + min + getContext().getString(R.string.timeutils_minutsago);
        }
        return getContext().getString(R.string.timeutils_rightnow);

    }




    public static long convertTime(String date) {
        long deltaTime = 0;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss");
            TimeZone timeZone = TimeZone.getDefault();
            timeZone.setRawOffset(0);
            dateFormat.setTimeZone(timeZone);
            Date parseDate = dateFormat.parse(date);
            deltaTime = parseDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return deltaTime;
    }

}
