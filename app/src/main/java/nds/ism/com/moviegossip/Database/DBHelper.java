package nds.ism.com.moviegossip.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Raja on 3/5/2016.
 */
public class DBHelper  extends SQLiteOpenHelper {
//Singleton Pattern for Sqllite
    private static DBHelper sInstance;
    private SQLiteDatabase db=this.getWritableDatabase();
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES_FRIENDS =
                    "CREATE TABLE " + MovieGossipContract.Friends.TABLE_NAME + " (" +
                            MovieGossipContract.Friends.COLUMN_NAME_NAME + TEXT_TYPE +COMMA_SEP+
                            MovieGossipContract.Friends.COLUMN_NAME_PHOTO + "BLOB" +COMMA_SEP+
                            MovieGossipContract.Friends.COLUMN_NAME_URL + TEXT_TYPE +
            " );";

    private static final String SQL_DELETE_ENTRIES_FRIENDS =
            "DROP TABLE IF EXISTS " + MovieGossipContract.Friends.TABLE_NAME;

    private static final String SQL_CREATE_ENTRIES_MOVIES =
            "CREATE TABLE " + MovieGossipContract.Movies.TABLE_NAME + " (" +
                    MovieGossipContract.Movies.COLUMN_NAME_NAME + TEXT_TYPE +COMMA_SEP+
                    MovieGossipContract.Movies.COLUMN_NAME_RATING + TEXT_TYPE +COMMA_SEP+
                    MovieGossipContract.Movies.COLUMN_NAME_VOTE_COUNT + TEXT_TYPE +COMMA_SEP+
                    MovieGossipContract.Movies.COLUMN_NAME_VOTE_AVERAGE + TEXT_TYPE +COMMA_SEP+
                    MovieGossipContract.Movies.COLUMN_NAME_RELEASE_DATE + TEXT_TYPE +
                    " );";

    private static final String SQL_DELETE_ENTRIES_MOVIES =
            "DROP TABLE IF EXISTS " + MovieGossipContract.Movies.TABLE_NAME;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";
    public static synchronized DBHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_ENTRIES_FRIENDS);
        db.execSQL(SQL_CREATE_ENTRIES_MOVIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES_FRIENDS);
        db.execSQL(SQL_DELETE_ENTRIES_MOVIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db,oldVersion, newVersion);
    }

    public long insertFriends(String name,byte[] photo,String url){
        ContentValues cv = new  ContentValues();
        cv.put(MovieGossipContract.Friends.COLUMN_NAME_NAME,name);
        cv.put(MovieGossipContract.Friends.COLUMN_NAME_PHOTO,photo);
        cv.put(MovieGossipContract.Friends.COLUMN_NAME_URL,url);
        db.insert(MovieGossipContract.Friends.TABLE_NAME, null, cv);
        return 0;
    }
    public long insertMovies(String name,String rating,String date,String count,String average){
        ContentValues cv = new  ContentValues();
        cv.put(MovieGossipContract.Movies.COLUMN_NAME_NAME,name);
        cv.put(MovieGossipContract.Movies.COLUMN_NAME_RATING,rating);
        cv.put(MovieGossipContract.Movies.COLUMN_NAME_RELEASE_DATE,date);
        cv.put(MovieGossipContract.Movies.COLUMN_NAME_VOTE_COUNT,count);
        cv.put(MovieGossipContract.Movies.COLUMN_NAME_VOTE_AVERAGE,average);
        db.insert( MovieGossipContract.Movies.TABLE_NAME,null,cv);
        return 0;
    }
}
