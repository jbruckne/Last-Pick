package joebruckner.lastpick.data;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import joebruckner.lastpick.R;

public class MovieViewHolder {
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.summary)
    TextView summary;
    @Bind(R.id.cast)
    TextView cast;
    @Bind(R.id.year)
    TextView year;
    @Bind(R.id.rating)
    TextView rating;
    @Bind(R.id.length)
    TextView length;

    public MovieViewHolder(@NonNull View view) {
        ButterKnife.bind(this, view);
    }

    public void setData(Movie movie) {
        setTitle(movie.getTitle());
        setSummary(movie.getOverview());
        setCast(new ArrayList<>());
        setYear(movie.getReleaseDate());
        setRating(movie.getPopularity());
        setLength(movie.getRuntime());
    }

    public void setTitle(@NonNull String text) {
        title.setText(text);
    }

    public void setSummary(@NonNull String text) {
        summary.setText(text);
    }

    public void setCast(@NonNull List<Object> castList) {
        cast.setText("Cast: No acknowledgements found");
    }

    public void setYear(@NonNull String text) {
        year.setText(text.substring(0, 4));
    }

    public void setRating(double userRating) {
        rating.setText(Double.toString(userRating));
    }

    public void setLength(int runtime) {
        int hours = runtime / 60;
        int minutes = runtime % 60;
        StringBuilder text = new StringBuilder();
        text.append(hours);
        text.append(hours < 2 ? " hour " : " hours ");
        text.append(minutes);
        text.append(minutes < 2 ? " minute" : " minutes");
        length.setText(text);
    }
}