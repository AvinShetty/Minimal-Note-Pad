package redfoxclassic.hehe.data.maindb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBHelper extends SQLiteOpenHelper {

    private final static String TAG = DBHelper.class.getSimpleName();

    public DBHelper(Context context) {
        super(context, MainDBSchema.DATABASE_NAME, null, MainDBSchema.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.w(TAG, "onCreate()");
        try {
            sqLiteDatabase.execSQL(MainDBSchema.DB_STATEMENT);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.w(TAG, "onUpgrade()");
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + MainDBSchema.DATABASE_TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
