package testing.proiectcolectivback.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TmdbMovieResult {

    @JsonProperty("title")
    private String title;

    @JsonProperty("poster_path")
    private String posterPath;

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }
}
