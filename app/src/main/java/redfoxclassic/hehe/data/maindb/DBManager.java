package redfoxclassic.hehe.data.maindb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import redfoxclassic.hehe.model.NoteModel;


public class DBManager {

    private final static String TAG = DBManager.class.getSimpleName();
    private static DBManager dbManager;

    private Context context;
    private DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    private List<NoteModel> noteModelList = new ArrayList<>();


    private DBManager(Context context) {
        this.context = context;
        this.dbHelper = new DBHelper(context);
    }


    public static DBManager getInstance(Context context) {

        if (dbManager == null) {
            return new DBManager(context);

        }
        return dbManager;
    }

//-----------------------------------------------------------------------------------------------------------------------

    public DBManager openDataBase() {
      //  Log.w(TAG, "openDataBase()");

        try {

            sqLiteDatabase = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this;
    }


    public void closeDataBase() {
      //  Log.w(TAG, "closeDataBase()");
        try {

            dbHelper.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//-----------------------------------------------------------------------------------------------------------------------


    public boolean insertNote(NoteModel noteModel) {
   /*     Log.w(TAG, " insertNote() : passed to here , so NoteModel : " + noteModel.getTitle() + "," + noteModel.getContent() +
                " , " + noteModel.getId() + " , " + noteModel.getDate());*/

        try {

            ContentValues contentValues = new ContentValues();
            contentValues.put(MainDBSchema.DATABASE_TITLE_NAME, noteModel.getTitle());
            contentValues.put(MainDBSchema.DATABASE_CONTENT_NAME, noteModel.getContent());
            contentValues.put(MainDBSchema.DATABASE_DATE, noteModel.getDate());

            long rowId = sqLiteDatabase.insert(MainDBSchema.DATABASE_TABLE_NAME, null, contentValues);
          //  Log.i(TAG, " return rowId  : " + String.valueOf(rowId));

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //-----------------------------------------------------------------------------------------------------------------------
    public int deleteNote(int whichOne) {
       // Log.w(TAG, "deleteNote()" + " , " + " whichOne : " + whichOne);

        try {

            int result = sqLiteDatabase.delete(MainDBSchema.DATABASE_TABLE_NAME, MainDBSchema.DATABASE_ROW_ID + "=?",
                    new String[]{String.valueOf(whichOne)});

            return result;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
    //-----------------------------------------------------------------------------------------------------------------------

    public long updateNote(NoteModel noteModel, int whichOne) {
       /* Log.w(TAG, " updatedWish() :" +
                " passed to here , NoteModel : " + noteModel.getTitle()
                + ",passed ID :" + noteModel.getContent()
                + " , date : " + noteModel.getDate());*/

        try {
            System.out.println(" --- " + String.valueOf(noteModel.getId()) + " , " + String.valueOf(whichOne));

            ContentValues contentValues = new ContentValues();
            contentValues.put(MainDBSchema.DATABASE_TITLE_NAME, noteModel.getTitle());
            contentValues.put(MainDBSchema.DATABASE_CONTENT_NAME, noteModel.getContent());
            contentValues.put(MainDBSchema.DATABASE_DATE, noteModel.getDate());

            long updatedRow = sqLiteDatabase.update(
                    MainDBSchema.DATABASE_TABLE_NAME,
                    contentValues,
                    MainDBSchema.DATABASE_ROW_ID + " =?", new String[]{String.valueOf(noteModel.getId())});

           // Log.i(" update Affected Row : ", String.valueOf(updatedRow));
            return updatedRow;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //------------------------------------------------------------------------------------------------------------------------
    public List<NoteModel> getAllNoteList() {
      //  Log.w(TAG, "getAllNoteList()");

        //alternative
      /*  String[] columns = {
                MainDBSchema.DATABASE_TITLE_NAME,
                MainDBSchema.DATABASE_CONTENT_NAME,
                MainDBSchema.DATABASE_DATE,
                MainDBSchema.DATABASE_ROW_ID,
        };
        // sorting orders
        String sortOrder =
                MainDBSchema.DATABASE_TITLE_NAME + " ASC";


        Cursor cursor2 = sqLiteDatabase.query(MainDBSchema.DATABASE_TABLE_NAME, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order*/

        //Wholy molly, advantage of using date is to sort + as well as last updated keeps in top

        Cursor cursor = sqLiteDatabase.rawQuery(MainDBSchema.DB_SELECT_ALL + " order by " + MainDBSchema.DATABASE_DATE + " DESC;", null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {

                    NoteModel noteModel = new NoteModel();

                    noteModel.setId(cursor.getInt(cursor.getColumnIndex(MainDBSchema.DATABASE_ROW_ID)));
                    noteModel.setTitle(cursor.getString(cursor.getColumnIndex(MainDBSchema.DATABASE_TITLE_NAME)));
                    noteModel.setContent(cursor.getString(cursor.getColumnIndex(MainDBSchema.DATABASE_CONTENT_NAME)));
                    noteModel.setDate(cursor.getString(cursor.getColumnIndex(MainDBSchema.DATABASE_DATE)));


                    noteModelList.add(noteModel);
                    //Log.i(TAG, " -- : " + noteModel.toString());

                } while (cursor.moveToNext());

            }

            cursor.close();
            return noteModelList;
        } else {
            return noteModelList;
        }

    }
//-------------------------------------------------------------------------------------------------------------------------

    public int getTotalNumberOfRecords() {
       // Log.w(TAG, "getTotalNumberOfRecords()");

        Cursor cursor = sqLiteDatabase.rawQuery(MainDBSchema.DB_SELECT_ALL, null);
        return cursor.getCount();
    }
//-------------------------------------------------------------------------------------------------------------------------

    public List<NoteModel> experiment(String searchKey) {

        Cursor cursor = sqLiteDatabase.rawQuery(MainDBSchema.DB_SELECT_ALL +
                " WHERE " + MainDBSchema.DATABASE_TITLE_NAME + "  LIKE  '%" + searchKey + "%' ", null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {

                    NoteModel noteModel = new NoteModel();

                    noteModel.setId(cursor.getInt(cursor.getColumnIndex(MainDBSchema.DATABASE_ROW_ID)));
                    noteModel.setTitle(cursor.getString(cursor.getColumnIndex(MainDBSchema.DATABASE_TITLE_NAME)));
                    noteModel.setContent(cursor.getString(cursor.getColumnIndex(MainDBSchema.DATABASE_CONTENT_NAME)));
                    noteModel.setDate(cursor.getString(cursor.getColumnIndex(MainDBSchema.DATABASE_DATE)));


                    noteModelList.add(noteModel);
                  //  Log.i(TAG, " -- : " + noteModel.toString());

                } while (cursor.moveToNext());

            }

            cursor.close();


            return noteModelList;

        }

        return null;
    }

}
