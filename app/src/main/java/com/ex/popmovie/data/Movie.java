package com.ex.popmovie.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private String idMovie;
    private String title;
    private String posterPath;
    private String overview;
    private String vote;
    private String pop;
    private String releaseDate;

    public Movie() {}

    public Movie CopyMovie(Movie FirstMovie) {
        this.setIdMovie(FirstMovie.getIdMovie());
        this.setTitle(FirstMovie.getTitle());
        this.setPosterPath(FirstMovie.getPosterPath());
        this.setOverview(FirstMovie.getOverview());
        this.setVote(FirstMovie.getVote());
        this.setPop(FirstMovie.getPop());
        this.setReleaseDate(FirstMovie.getReleaseDate());
        return this;
    }

/*
    public Movie(String idMovie, String title, String posterPath, String overview, String vote, String pop, String releaseDate) {
        this.idMovie = idMovie;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.vote = vote;
        this.pop =pop;
        this.releaseDate = releaseDate;
    }
*/

    public Movie(Parcel in) {
        String[] data = new String[7];
        in.readStringArray(data);
        this.idMovie = data[0];
        this.title = data[1];
        this.posterPath = data[2];
        this.overview = data[3];
        this.vote = data[4];
        this.pop = data[5];
        this.releaseDate = data[6];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{idMovie, title, posterPath, overview, vote, pop, releaseDate});
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(String idMovie) {
        this.idMovie = idMovie;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getPop() {
        return pop;
    }

    public void setPop(String pop) {
        this.pop = pop;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

}
