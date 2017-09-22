package redfoxclassic.hehe.data.favdb;

public class FavDBSchema {

    public final static String DATABASE_NAME2 = "WISH_DB_2";
    public final static int DATABASE_VERSION2 = 2;
    public final static String DATABASE_TABLE_NAME2 = "WISH_TABLE_NAME_2";
    public final static String DATABASE_TITLE_NAME2 = "WISH_TITLE_NAME_2";
    public final static String DATABASE_CONTENT_NAME2 = "WISH_CONTENT_NAME_2";
    public final static String DATABASE_DATE2 = "WISH_DATE_2";
    public final static String DATABASE_ROW_ID2 = "_id2";

    public final static String DB_STATEMENT2 = " CREATE TABLE " + DATABASE_TABLE_NAME2 + "(" + DATABASE_ROW_ID2 + " INTEGER PRIMARY KEY AUTOINCREMENT , " +

            DATABASE_TITLE_NAME2 + " TEXT NOT NULL , " +
            DATABASE_CONTENT_NAME2 + " TEXT NOT NULL, " +
            DATABASE_DATE2 + " TEXT NOT NULL" + ");";

    public final static String DB_SELECT_ALL2 = " SELECT * FROM " + DATABASE_TABLE_NAME2;
}
