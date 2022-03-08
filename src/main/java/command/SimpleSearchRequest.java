package command;

public class SimpleSearchRequest {
    protected String category = "";
    protected String value = "";
    protected int page = 0;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String toPathParam() {
        return "category=" + category + "&value=" + value;
    }
}
