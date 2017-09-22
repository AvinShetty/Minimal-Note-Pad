package redfoxclassic.hehe.util;

import android.content.Context;

import redfoxclassic.hehe.R;


public class MyColor {
    private final static String TAG = MyColor.class.getSimpleName();

    //    <!--Don't forget to change this boring colors-->


    public static int getRandomColor(Context context) {
      //  Log.e(TAG, "getRandomColor()");
        int[] colors = context.getResources().getIntArray(R.array.note_accent_colors);
        return colors[((int) (Math.random() * colors.length))];
    }



}
