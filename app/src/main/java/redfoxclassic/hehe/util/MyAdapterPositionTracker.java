package redfoxclassic.hehe.util;

public class MyAdapterPositionTracker {

    //used this pos to notify after delete to removeAt
    //I used this deleting single peration from adapter

    private final static String TAG = MyAdapterPositionTracker.class.getSimpleName();

    private static int myPos;


    public static int getAdapterPositionMiss() {
      //  Log.e(TAG, "MIS GET : " + myPos);
        return myPos;
    }

    public static void setAdapterPositionMiss(int adapterPosition) {
        //Log.e(TAG, "MIS SET : " + adapterPosition);
        myPos = adapterPosition;

    }
}
