package com.ex.popmovie.data;

public class Movie {
    private String idMovie;
    private String title;
    private String posterPath;
    private String overview;
    private String vote;
    private String pop;
    private String releaseDate;

    public Movie() {}

    public Movie(String idMovie, String title, String posterPath, String overview, String vote, String pop, String releaseDate) {
        this.idMovie = idMovie;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.vote = vote;
        this.pop =pop;
        this.releaseDate = releaseDate;
    }

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
