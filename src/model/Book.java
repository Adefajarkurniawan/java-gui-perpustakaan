package model;

public class Book {
    private int id;
    private String title;
    private String author;
    private int yearPublished;
    private String genre;
    private int stock;  // baru

    public Book() {}

    public Book(int id, String title, String author, int yearPublished, String genre, int stock) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.yearPublished = yearPublished;
        this.genre = genre;
        this.stock = stock;
    }

    // getter & setter semua atribut
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public int getYearPublished() { return yearPublished; }
    public void setYearPublished(int yearPublished) { this.yearPublished = yearPublished; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

     public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

}
