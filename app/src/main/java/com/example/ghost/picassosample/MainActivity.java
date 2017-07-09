package com.example.ghost.picassosample;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private static final String LOGTAG = "MainActivity";

    //implements HeadlinesFragment.OnHeadlineSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sort_by_pop) {
            Log.e(LOGTAG, "item checked: " + item.isChecked()); //item.isChecked 其實就是 true & false
            item.setChecked(!item.isChecked());  //前面加一個 ! 就是 true 變 false, false 變 true（更改打勾的顯示）

            //getSupportFragmentManager().findFragmentById(R.id.gridview).publicMethod();

            MovieFragment movieFragment = (MovieFragment) getSupportFragmentManager().findFragmentByTag("movieFragment");//找到activity_main裡面的tag
            //MovieFragment movieFragment = (MovieFragment) getSupportFragmentManager().findFragmentById(R.id.movie_view);
            try {
                movieFragment.get_API(item.isChecked());
            } catch (Exception e) {
                e.printStackTrace();
            }
            SharedPreferences settings = getSharedPreferences("settings", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("checkbox", item.isChecked());
            editor.commit();
            return true;
        } else {
            //false
        }
        return super.onOptionsItemSelected(item);
    }
}

