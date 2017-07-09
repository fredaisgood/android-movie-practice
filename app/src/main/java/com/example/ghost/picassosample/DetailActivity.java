package com.example.ghost.picassosample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;



public class DetailActivity extends AppCompatActivity{

    private ImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        imageView = (ImageView) findViewById(R.id.image);
        TextView release_date = (TextView)findViewById(R.id.year);
        TextView original_title = (TextView)findViewById(R.id.title);
        TextView vote_average = (TextView)findViewById(R.id.vote);
        TextView overview_w = (TextView)findViewById(R.id.overview);

        //把 url 拿出來
        String url = getIntent().getStringExtra("poster_path");
        String date = getIntent().getStringExtra("release_date");
        String title = getIntent().getStringExtra("original_title");
        String vote = getIntent().getStringExtra("vote_average");
        String overview = getIntent().getStringExtra("overview");
        release_date.setText("\t"+"\t" + date);
        original_title.setText("\t"+ title);
        vote_average.setText("\t"+"\t"+ vote +"/10");
        overview_w.setText(overview);
        Picasso
                .with(this)
                .load("http://image.tmdb.org/t/p/w185" + url)
                .fit()
                .into(imageView);
    }
}
