package test;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


class Transaction {
    private int amount; // Positive for income, negative for expense
    private LocalDate date;

    public Transaction(int amount, LocalDate date) {
        this.amount = amount;
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        String type = amount > 0 ? "Pemasukan" : "Pengeluaran";
        int displayAmount = amount > 0 ? amount : -amount;
        String formattedDate = date.format(DateTimeFormatter.ofPattern("dd - MM - yyyy"));
        return String.format("[%s] %s : Rp %,d", formattedDate, type, displayAmount);
    }
}
