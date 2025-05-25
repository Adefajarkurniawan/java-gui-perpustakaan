package model;

public class BorrowHistory {
    private String bookTitle;
    private String loanDate;
    private String returnDate;
    private String status;

    public BorrowHistory(String bookTitle, String loanDate, String returnDate, String status) {
        this.bookTitle = bookTitle;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getLoanDate() {
        return loanDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public String getStatus() {
        return status;
    }
}
