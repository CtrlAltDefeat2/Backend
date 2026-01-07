package testing.proiectcolectivback.DTO;

import java.util.List;

public class TmdbSearchResponse {
    private List<TmdbMovieResult> results;

    public List<TmdbMovieResult> getResults() {
        return results;
    }

    public void setResults(List<TmdbMovieResult> results) {
        this.results = results;
    }
}
