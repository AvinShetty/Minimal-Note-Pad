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


public class DBManagerFab {

    private final static String TAG = DBManagerFab.class.getSimpleName();
    private static DBManagerFab dbManagerFab;

    private Context context;
    private DBHelperFav dbHelperFav;
    private SQLiteDatabase sqLiteDatabase;
    private List<NoteModel> noteModelList = new ArrayList<>();


    private DBManagerFab(Context context) {
        this.context = context;
        this.dbHelperFav = new DBHelperFav(context);
    }


    public static DBManagerFab getInstance(Context context) {

        if (dbManagerFab == null) {
            return new DBManagerFab(context);

        }
        return dbManagerFab;
    }


    public DBManagerFab openDataBase() {
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
            contentValues.put(FavDBSchema.DATABASE_TITLE_NAME, noteModel.getTitle());
            contentValues.put(FavDBSchema.DATABASE_CONTENT_NAME, noteModel.getContent());
            contentValues.put(FavDBSchema.DATABASE_DATE, noteModel.getDate());

            long rowId = sqLiteDatabase.insert(FavDBSchema.DATABASE_TABLE_NAME, null, contentValues);
            Log.i(TAG, " return rowId  : " + String.valueOf(rowId));

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<NoteModel> getAllNoteList() {
        Log.w(TAG, "getAllNoteList()");

        Cursor cursor = sqLiteDatabase.rawQuery(FavDBSchema.DB_SELECT_ALL, null);

        if (cursor.moveToFirst()) {
            do {

                NoteModel noteModel = new NoteModel();

                noteModel.setId(cursor.getInt(cursor.getColumnIndex(FavDBSchema.DATABASE_ROW_ID)));
                noteModel.setTitle(cursor.getString(cursor.getColumnIndex(FavDBSchema.DATABASE_TITLE_NAME)));
                noteModel.setContent(cursor.getString(cursor.getColumnIndex(FavDBSchema.DATABASE_CONTENT_NAME)));
                noteModel.setDate(cursor.getString(cursor.getColumnIndex(FavDBSchema.DATABASE_DATE)));


                noteModelList.add(noteModel);
                Log.i(TAG, " -- : " + noteModel.toString());

            } while (cursor.moveToNext());

        }

        cursor.close();
        return noteModelList;

    }


    public int getTotalNumberOfRecords() {
        Log.w(TAG, "getTotalNumberOfRecords()");

        Cursor cursor = sqLiteDatabase.rawQuery(FavDBSchema.DB_SELECT_ALL, null);
        return cursor.getCount();
    }


}
