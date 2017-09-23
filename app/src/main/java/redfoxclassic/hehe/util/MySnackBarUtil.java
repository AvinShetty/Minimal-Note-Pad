package redfoxclassic.hehe.util;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

public class MySnackBarUtil {

    private final static String TAG = MySnackBarUtil.class.getSimpleName();

    public static void showSnackBar(Activity activity, String message, String whichOne) {
        //  Log.e(TAG, "showSnackBar()");
        View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);

        View sView = snackbar.getView();
        TextView textView = (TextView) sView.findViewById(android.support.design.R.id.snackbar_text);

        if (whichOne.equalsIgnoreCase("delete")) {
            sView.setBackgroundColor(Color.parseColor("#E91E63"));
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(14);
            textView.setTypeface(Typeface.DEFAULT_BOLD);

        } else if (whichOne.equalsIgnoreCase("update")) {
            sView.setBackgroundColor(Color.parseColor("#4CAF50"));
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(14);
            textView.setTypeface(Typeface.DEFAULT_BOLD);


        } else if (whichOne.equalsIgnoreCase("saved")) {
            sView.setBackgroundColor(Color.parseColor("#FFEB3B"));
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(14);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
        }

        snackbar.show();
    }
}
