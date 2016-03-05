package nds.ism.com.moviegossip.Database;

import android.provider.BaseColumns;

/**
 * Created by Raja on 3/5/2016.
 */
public class MovieGossipContract {
    public MovieGossipContract(){};
    public static abstract class Friends implements BaseColumns {
        public static final String TABLE_NAME = "friends";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_PHOTO = "photo";
        public static final String COLUMN_NAME_URL = "url";
    }
    public static abstract class Movies implements BaseColumns {
        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_RATING = "popularity";
        public static final String COLUMN_NAME_RELEASE_DATE = "releasedate";
        public static final String COLUMN_NAME_VOTE_COUNT = "country";
        public static final String COLUMN_NAME_VOTE_AVERAGE = "company";
    }
}
