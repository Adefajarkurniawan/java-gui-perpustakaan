package model;

public class UserLoanRecord {
    private int loanId;
    private String username;
    private String bookTitle;
    private String loanDate;
    private String status;

    public UserLoanRecord(int loanId, String username, String bookTitle, String loanDate, String status) {
        this.loanId = loanId;
        this.username = username;
        this.bookTitle = bookTitle;
        this.loanDate = loanDate;
        this.status = status;
    }

    public int getLoanId() { return loanId; }
    public String getUsername() { return username; }
    public String getBookTitle() { return bookTitle; }
    public String getLoanDate() { return loanDate; }
    public String getStatus() { return status; }
}
