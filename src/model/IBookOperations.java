package model;

// Interface untuk operasi dasar pada buku
public interface IBookOperations {
    boolean addBook(Book book);
    boolean updateBook(Book book);
    boolean deleteBook(int bookId);
}
