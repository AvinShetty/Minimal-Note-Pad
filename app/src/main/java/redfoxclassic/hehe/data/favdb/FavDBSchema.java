package redfoxclassic.hehe.data.favdb;

public class FavDBSchema {

    public final static String DATABASE_NAME = "WISH_DB_2";
    public final static int DATABASE_VERSION = 2;
    public final static String DATABASE_TABLE_NAME = "WISH_TABLE_NAME_2";
    public final static String DATABASE_TITLE_NAME = "WISH_TITLE_NAME_2";
    public final static String DATABASE_CONTENT_NAME = "WISH_CONTENT_NAME_2";
    public final static String DATABASE_DATE = "WISH_DATE_2";
    public final static String DATABASE_ROW_ID = "_id2";

    public final static String DB_STATEMENT = " CREATE TABLE " + DATABASE_TABLE_NAME + "(" + DATABASE_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +

            DATABASE_TITLE_NAME + " TEXT NOT NULL , " +
            DATABASE_CONTENT_NAME + " TEXT NOT NULL, " +
            DATABASE_DATE + " TEXT NOT NULL" + ");";


    public final static String DB_SELECT_ALL = " SELECT * FROM " + DATABASE_TABLE_NAME;
}
