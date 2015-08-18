package joebruckner.lastpick.models;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.movito.themoviedbapi.model.people.PersonCast;
import joebruckner.lastpick.R;

public class MovieViewHolder {
	@Bind( R.id.poster )  ImageView poster;
	@Bind( R.id.title )   TextView title;
	@Bind( R.id.summary ) TextView summary;
	@Bind( R.id.cast )    TextView cast;
	@Bind( R.id.year )    TextView year;
	@Bind( R.id.rating )  TextView rating;
	@Bind( R.id.length )  TextView length;

	Context context;

	private static final String BASE_URL = "http://image.tmdb.org/t/p/";
	private static final String BACKDROP_SIZE = "w1280";
	private static final String POSTER_SIZE = "w342";

	public MovieViewHolder(Context context, @NonNull View view) {
		ButterKnife.bind(this, view);
		this.context = context;
	}

	public void setBackdrop(@NonNull String imagePath, ImageView view) {
		Glide.with(context).load(BASE_URL + BACKDROP_SIZE + imagePath)
				.centerCrop()
				.into(view);
	}

	public void setPoster(@NonNull String imagePath) {
		Glide.with(context).load(BASE_URL + POSTER_SIZE + imagePath)
				.centerCrop()
				.into(poster);
	}

	public void setTitle(@NonNull String text) {
		title.setText(text);
	}

	public void setSummary(@NonNull String text) {
		summary.setText(text);
	}

	public void setCast(@NonNull List<PersonCast> castList) {
		if (castList.isEmpty()) {
			cast.setText("Cast: No acknowledgments");
			return;
		}

		StringBuilder text = new StringBuilder("Cast: " + castList.remove(0).getName());
		for (PersonCast person : castList.size() > 4 ? castList.subList(0, 4) : castList) {
			text.append(", ");
			text.append(person.getName());
		}
		cast.setText(text.toString());
	}

	public void setYear(@NonNull String text) {
		year.setText(text.substring(0, 4));
	}

	public void setRating(float userRating) {
		rating.setText(Float.toString(userRating));
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
