package redfoxclassic.hehe.util;

import android.content.Context;
import android.util.Log;

import redfoxclassic.hehe.R;


public class MyColor {
    private final static String TAG = MyColor.class.getSimpleName();

    //    <!--Don't forget to change this boring colors-->


    public static int getRandomColor(Context context) {
        Log.e(TAG, "getRandomColor()");
        int[] colors = context.getResources().getIntArray(R.array.note_accent_colors);
        return colors[((int) (Math.random() * colors.length))];
    }


   /* public static int getRandomMaterialColor(Context context) {

        String[] allColors = context.getResources().getStringArray(R.array.note_accent_colors);
        int randomColor = Color.parseColor(allColors[new Random().nextInt(allColors.length)]);


        return randomColor;
    }*/

}
