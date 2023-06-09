package Shared;

public class Game {
    private String id;
    private String title;
    private String developer;
    private String genre;
    private double price;
    private int release_year;
    private boolean controller_support;
    private int reviews;
    private int size;
    private String file_path;

    public Game(String id, String title, String developer, String genre, double price, int release_year, boolean controller_support, int reviews, int size, String file_path) {
        this.id = id;
        this.title = title;
        this.developer = developer;
        this.genre = genre;
        this.price = price;
        this.release_year = release_year;
        this.controller_support = controller_support;
        this.reviews = reviews;
        this.size = size;
        this.file_path = file_path;
    }

    public Game() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getRelease_year() {
        return release_year;
    }

    public void setRelease_year(int release_year) {
        this.release_year = release_year;
    }

    public boolean isController_support() {
        return controller_support;
    }

    public void setController_support(boolean controller_support) {
        this.controller_support = controller_support;
    }

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", developer='" + developer + '\'' +
                ", genre='" + genre + '\'' +
                ", price=" + price +
                ", release_year=" + release_year +
                ", controller_support=" + controller_support +
                ", reviews=" + reviews +
                ", size=" + size +
                ", file_path='" + file_path + '\'' +
                '}';
    }
}
