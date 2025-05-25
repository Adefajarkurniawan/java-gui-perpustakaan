package model;

public class LoanRecord {
    private int loanId;       // id dari tabel loans
    private String bookTitle;
    private String loanDate;
    private String status;

    public LoanRecord(int loanId, String bookTitle, String loanDate, String status) {
        this.loanId = loanId;
        this.bookTitle = bookTitle;
        this.loanDate = loanDate;
        this.status = status;
    }

    public int getLoanId() {
        return loanId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getLoanDate() {
        return loanDate;
    }

    public String getStatus() {
        return status;
    }
}
