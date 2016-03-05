package nds.ism.com.moviegossip.Fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import nds.ism.com.moviegossip.Database.DBHelper;
import nds.ism.com.moviegossip.JSON.JSONParser;
import nds.ism.com.moviegossip.R;
import retrofit.client.Response;
import retrofit.http.GET;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentFetcher extends Fragment implements View.OnClickListener{

    View view;
    Button fetchTwitterFriendListButton,fetchMovies;
    DBHelper dbHelper;
    private static String url = "http://api.themoviedb.org/3/discover/movie?&api_key=7bfdd5f2fb07f9c7b9fc6ecc0af6d334&page=1";
    TwitterLoginButton twitterLoginButton;
    public FragmentFetcher() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_fetch_caller, container, false);
        fetchTwitterFriendListButton=(Button)view.findViewById(R.id.TwitterFetch);
        twitterLoginButton = (TwitterLoginButton)view.findViewById(R.id.Twitterlogin_button);
        fetchMovies=(Button)view.findViewById(R.id.Movies);
        fetchMovies.setOnClickListener(this);
        fetchTwitterFriendListButton.setOnClickListener(this);
        twitterLoginButton.setOnClickListener(this);

        dbHelper= DBHelper.getInstance(getActivity().getApplicationContext());
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.e("Twitter", result.data.toString());
                fetchTwitterFriendList();
            }

            @Override
            public void failure(TwitterException e) {

            }
        } );
        return view;
    }


    private void fetchTwitterFriendList() {
        final TwitterSession session = Twitter.getSessionManager().getActiveSession();
        TwitterAuthToken authToken = session.getAuthToken();
        Twitter.getApiClient(session).getAccountService().verifyCredentials(true, false, new Callback<User>() {
            @Override
            public void success(Result<User> result) {
                Log.e("User Data", result.toString());
                User user = result.data;
                Log.e("Hey This user", "" + user.getId() + " " + user.name + " " + user.url + " " + user.friendsCount);
                TwitterClientApiClient twitterClientApiClient = new TwitterClientApiClient(session);
                twitterClientApiClient.getFriendsService().friends(
                        // user.getId(),
                        // user.screenName,
                        // 10L,
                        // 10,
                        // false, true,
                        new Callback<Response>() {
                            @Override
                            public void success(Result<Response> result) {
                                JSONArray friends = null;
                                BufferedReader reader = null;
                                final StringBuilder sb = new StringBuilder();
                                try {
                                    reader = new BufferedReader(new InputStreamReader(result.data.getBody().in()));
                                    String line;
                                    while ((line = reader.readLine()) != null) {
                                        sb.append(line);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                String userJson = sb.toString();
                                try {
                                    JSONObject friendsObject = (JSONObject) new JSONTokener(userJson).nextValue();
                                    Log.e("JSONObject", friendsObject.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.d("Friends JSON", userJson);
                                Log.e("Response", result.response.toString());
                            }
                            @Override
                            public void failure(TwitterException e) {
                                Log.e("Exception", e.toString());

                            }
                        }
                );
                twitterClientApiClient.getFollowersService().followers(new Callback<Response>() {
                    @Override
                    public void success(Result<Response> result) {
                        JSONArray friends = null;
                        BufferedReader reader = null;
                        final StringBuilder sb = new StringBuilder();
                        try {
                            reader = new BufferedReader(new InputStreamReader(result.data.getBody().in()));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                sb.append(line);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String userJson = sb.toString();
                        Log.d("Follower JSON", userJson);
                        Log.e("Response", result.response.toString());
                    }

                    @Override
                    public void failure(TwitterException e) {
                        Log.e("Exception", e.toString());
                    }
                });
            }
            public void failure(TwitterException e) {
                Log.e("Twittererror", e.toString());
            }

        });

    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.TwitterFetch : twitterLoginButton.performClick();
                break;
            case R.id.Movies :
                new JSONParse().execute();
                break;
        }
    }

    public class TwitterClientApiClient extends TwitterApiClient {
        public TwitterClientApiClient(TwitterSession session) {
            super(session);
        }
        public FriendsService getFriendsService() {
            return getService(FriendsService.class);
        }

        public FollewersService  getFollowersService() {
            return getService(FollewersService.class);
        }
    }

    interface FriendsService {
        @GET("/1.1/friends/list.json?cursor=-1&skip_status=true&include_user_entities=false")
        void friends(//@Query("user_id") long id,
                     //  @Query("screen_name") String screen_name,
                     // @Query("cursor") Long cursor,
                     //@Query("count") Integer count,
                     // @Query("skip_status") boolean skip_status,
                     // @Query("include_user_entities") boolean include_user_entities,
                     Callback<Response> cb);
    }

    interface FollewersService {
        @GET("/1.1/followers/list.json?cursor=-1&skip_status=true&include_user_entities=false")
        void followers(//@Query("user_id") long id,
                       //  @Query("screen_name") String screen_name,
                       // @Query("cursor") Long cursor,
                       //@Query("count") Integer count,
                       // @Query("skip_status") boolean skip_status,
                       // @Query("include_user_entities") boolean include_user_entities,
                       Callback<Response> cb);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //
        //mCallbackManager.onActivityResult(requestCode, resultCode, data);
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }
    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            JSONParser jParser = new JSONParser();
            // Getting JSON from URL
            JSONObject json = jParser.getJSONFromUrl(url);
            Log.e("Movies", json.toString());
            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            JSONObject ob = null;
            JSONArray arr= null;
            try {
                arr = json.getJSONArray("results");
                int i=0;
                while(i<arr.length())
                {
                    ob=(JSONObject)arr.get(i++);
                    parseObject(ob);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        void parseObject(JSONObject json) throws JSONException {
            String name=json.getString("title");
            String releaseDate=json.getString("release_date");
            String rating=json.getString("popularity");
            String count=json.getString("vote_count");
            String average=json.getString("vote_average");
            Log.e("Values",name+" "+releaseDate+" "+rating+" "+count+" "+average);
        }

    }
}
