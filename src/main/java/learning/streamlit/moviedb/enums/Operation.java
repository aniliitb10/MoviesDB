package learning.streamlit.moviedb.enums;

public enum Operation {
    New("New"), Update("Update"), Delete("Delete");

    private final String value;

    Operation(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
