package redfoxclassic.hehe.data.favdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import redfoxclassic.hehe.model.NoteModel;


public class DBManagerFav {

    private final static String TAG = DBManagerFav.class.getSimpleName();
    private static DBManagerFav dbManagerFav;

    private Context context;
    private DBHelperFav dbHelperFav;
    private SQLiteDatabase sqLiteDatabase;
    private List<NoteModel> noteModelList = new ArrayList<>();


    private DBManagerFav(Context context) {
        this.context = context;
        this.dbHelperFav = new DBHelperFav(context);
    }


    public static DBManagerFav getInstance(Context context) {

        if (dbManagerFav == null) {
            return new DBManagerFav(context);

        }
        return dbManagerFav;
    }


    public DBManagerFav openDataBase() {
        Log.w(TAG, "openDataBase()");

        try {

            sqLiteDatabase = dbHelperFav.getWritableDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this;
    }


    public void closeDataBase() {
        Log.w(TAG, "closeDataBase()");
        try {

            dbHelperFav.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean insertNoteFav(NoteModel noteModel) {
        Log.w(TAG, " insertNote() : passed to here , so NoteModel : " + noteModel.getTitle() + "," + noteModel.getContent() +
                " , " + noteModel.getId() + " , " + noteModel.getDate());
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(FavDBSchema.DATABASE_TITLE_NAME2, noteModel.getTitle());
            contentValues.put(FavDBSchema.DATABASE_CONTENT_NAME2, noteModel.getContent());
            contentValues.put(FavDBSchema.DATABASE_DATE2, noteModel.getDate());

            long rowId = sqLiteDatabase.insert(FavDBSchema.DATABASE_TABLE_NAME2, null, contentValues);
            Log.i(TAG, " return rowId  : " + String.valueOf(rowId));

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<NoteModel> getAllNoteList() {
        Log.w(TAG, "getAllNoteList()");

        Cursor cursor = sqLiteDatabase.rawQuery(FavDBSchema.DB_SELECT_ALL2 + " order by " + FavDBSchema.DATABASE_DATE2 + " DESC;", null);

        if (cursor.moveToFirst()) {
            do {

                NoteModel noteModel = new NoteModel();

                noteModel.setId(cursor.getInt(cursor.getColumnIndex(FavDBSchema.DATABASE_ROW_ID2)));
                noteModel.setTitle(cursor.getString(cursor.getColumnIndex(FavDBSchema.DATABASE_TITLE_NAME2)));
                noteModel.setContent(cursor.getString(cursor.getColumnIndex(FavDBSchema.DATABASE_CONTENT_NAME2)));
                noteModel.setDate(cursor.getString(cursor.getColumnIndex(FavDBSchema.DATABASE_DATE2)));


                noteModelList.add(noteModel);
                Log.i(TAG, " -- : " + noteModel.toString());

            } while (cursor.moveToNext());

        }

        cursor.close();
        return noteModelList;

    }


    public int getTotalNumberOfRecords() {
        Log.w(TAG, "getTotalNumberOfRecords()");

        Cursor cursor = sqLiteDatabase.rawQuery(FavDBSchema.DB_SELECT_ALL2, null);
        return cursor.getCount();
    }


}
