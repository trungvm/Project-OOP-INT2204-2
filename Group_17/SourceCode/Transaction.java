/*******************************************************************************
 * Một giao dịch, bao gồm thông tin về mục giao dịch, số tiền, ngày thực hiện và mô tả
********************************************************************************/

import java.time.LocalDate;

public class Transaction {
    private String category;
    private int amount;
    private LocalDate date; //ngay thuc hien
    private String description;

    public Transaction(String category, int amount, LocalDate date, String description) {
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    //setter

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setMoTa(String description) {
        this.description = description;
    }

    //getter

    public String getCategory() {
        return category;
    }

    public int getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    //toString

    public String toString() {
        return category + ";" + amount + ";" + date + ";" + description;
    }

}