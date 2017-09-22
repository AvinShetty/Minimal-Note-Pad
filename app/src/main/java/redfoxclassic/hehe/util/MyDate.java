package redfoxclassic.hehe.util;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDate {
    private final static String TAG = MyDate.class.getSimpleName();


    public static String formatDate(long dateInMillis) {

     //   Log.e(TAG, "formatDate()");

        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        Date date = new Date(dateInMillis);
        String stringFormat = dateFormat.format(date);
        System.out.println(stringFormat);
        return stringFormat;


    }

}
