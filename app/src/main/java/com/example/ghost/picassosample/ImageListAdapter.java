package com.example.ghost.picassosample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageListAdapter extends ArrayAdapter<Movie> {
    private Context context;
    private LayoutInflater inflater;

    private List<Movie> imageUrls;

    public ImageListAdapter(Context context, List<Movie> imageUrls) {
        super(context, R.layout.griditemimage, imageUrls);

        this.context = context;
        this.imageUrls = imageUrls;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.griditemimage, parent, false);

        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
        Movie movie = imageUrls.get(position);
        String posterPath = movie.getPoster_path();
        Picasso
                .with(context)
                .load("http://image.tmdb.org/t/p/w185" + posterPath)
                .fit()
                .into(imageView);

        return convertView;
    }
}
