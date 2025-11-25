package testing.proiectcolectivback.DTO;

import java.util.List;

public class GoogleBooksResponse {
    private List<GoogleBookItem> items;

    public List<GoogleBookItem> getItems() { return items; }
    public void setItems(List<GoogleBookItem> items) { this.items = items; }
}
