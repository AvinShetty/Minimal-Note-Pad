package redfoxclassic.hehe.data.favdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBHelperFav extends SQLiteOpenHelper {

    private final static String TAG = DBHelperFav.class.getSimpleName();

    public DBHelperFav(Context context) {
        super(context, FavDBSchema.DATABASE_NAME2, null, FavDBSchema.DATABASE_VERSION2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.w(TAG, "onCreate()");
        try {
            sqLiteDatabase.execSQL(FavDBSchema.DB_STATEMENT2);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.w(TAG, "onUpgrade()");
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + FavDBSchema.DATABASE_TABLE_NAME2);
        onCreate(sqLiteDatabase);

    }
}
