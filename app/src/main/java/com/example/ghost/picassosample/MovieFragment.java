package com.example.ghost.picassosample;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MovieFragment extends Fragment {
    private AdapterView.OnItemSelectedListener listener;
    private static final String TAG = MovieFragment.class.getSimpleName();

    private GridView gridview;
    private String API_kEY = "347f738ee06475b4104bcbd0cb575210";
    OnHeadlineSelectedListener mCallback;
    public interface OnHeadlineSelectedListener {
        public void onArticleSelected(int position);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);
        gridview = (GridView) rootView.findViewById(R.id.movie_view);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = (Movie) gridview.getAdapter().getItem(position);
                Intent intent = new Intent(getContext(), DetailActivity.class);     //告訴目的是 DetailActivity
                intent.putExtra("poster_path", movie.getPoster_path());
                intent.putExtra("release_date", movie.getDate());
                intent.putExtra("original_title", movie.getTitle());
                intent.putExtra("vote_average", movie.getVote());
                intent.putExtra("overview", movie.getOverview());
                startActivity(intent);                   //進入 DetailActivity
            }
        });
        return rootView;
    }

    @Override //lifecycle，預設false不打勾
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new ImageLoadTask(false).execute();
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    public void get_API(boolean checked){
        new ImageLoadTask(checked).execute();
    }
    public class ImageLoadTask extends AsyncTask<Void, Void, List<Movie>> {
        private boolean checked;
        public ImageLoadTask(boolean checked) {
            this.checked = checked;
        }

        @Override
        protected List<Movie> doInBackground(Void... params) {                    //在背景做事情
            try {
                Movie[] result = getPathsFromAPI(checked);
                return Arrays.asList(result);
            } catch (Exception e) {
                return Collections.emptyList();
            }
        }

        protected void onPostExecute(List<Movie> result) {
            if (result != null && getActivity() != null) {
                ImageListAdapter adapter = new ImageListAdapter(getActivity(), result);
                gridview.setAdapter(adapter);                                      //跑getview
            }
        }
    }

    private Movie[] getPathsFromAPI(boolean sortbypop) throws Exception {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String JsonResult;
        try {
            String urlString = null;
            if (sortbypop) {
                urlString = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=" + API_kEY;
            } else {
                urlString = "http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&vote_count.gte=10&api_key=" + API_kEY;
            }
            Log.e(TAG, "url: " + urlString);

            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();  //建立連線

            InputStream inputStream = urlConnection.getInputStream();  //接資料
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                return null;
            }

            JsonResult = buffer.toString();
            return getPathsFromJSON(JsonResult);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                }
            }
        }
    }

    public Movie[] getPathsFromJSON(String JSONStringParam) throws JSONException {
        Log.e(TAG, "JSONStringParam: " + JSONStringParam);


        JSONObject jsonObject = new JSONObject(JSONStringParam);

        JSONArray moviesArray = jsonObject.getJSONArray("results"); //放全域
        Movie[] result = new Movie[moviesArray.length()];


        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject movie = moviesArray.getJSONObject(i);
            String moviePath = movie.getString("poster_path");
            String movierelease = movie.getString("release_date");
            String title = movie.getString("original_title");
            String vote = movie.getString("vote_average");
            String overview_w = movie.getString("overview");
            Movie movieinfo = new Movie();
            movieinfo.setDate(movierelease);
            movieinfo.setPoster_path(moviePath);
            movieinfo.setTitle(title);
            movieinfo.setVote(vote);
            movieinfo.setOverview(overview_w);
            result[i] = movieinfo;

        }

        return result;
    }

}


