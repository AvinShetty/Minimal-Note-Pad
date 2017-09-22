package redfoxclassic.hehe.data.maindb;

public class MainDBSchema {

    public final static String DATABASE_NAME = "WISH_DB";
    public final static int DATABASE_VERSION = 1;
    public final static String DATABASE_TABLE_NAME = "WISH_TABLE_NAME";
    //columns
    public final static String DATABASE_TITLE_NAME = "WISH_TITLE_NAME";
    public final static String DATABASE_CONTENT_NAME = "WISH_CONTENT_NAME";
    public final static String DATABASE_DATE = "WISH_DATE";
    public final static String DATABASE_ROW_ID = "_id";


    public final static String DB_STATEMENT = " CREATE TABLE " + DATABASE_TABLE_NAME + "(" + DATABASE_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +

            DATABASE_TITLE_NAME + " TEXT NOT NULL , " +
            DATABASE_CONTENT_NAME + " TEXT NOT NULL, " +
            DATABASE_DATE + " TEXT NOT NULL" + ");";


    public final static String DB_SELECT_ALL = " SELECT * FROM " + DATABASE_TABLE_NAME;
}
