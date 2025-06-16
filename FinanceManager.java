package test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class FinanceManager {
    private final java.util.List<Transaction> transactions;

    public FinanceManager() {
        transactions = new java.util.ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public java.util.List<Transaction> getAllTransactions() {
        return new java.util.ArrayList<>(transactions);
    }

    public java.util.List<Transaction> getTransactionsByDate(LocalDate date) {
        return transactions.stream()
                .filter(t -> t.getDate().equals(date))
                .collect(Collectors.toList());
    }

    public java.util.List<Transaction> getTransactionsByMonth(int year, int month) {
        return transactions.stream()
                .filter(t -> t.getDate().getYear() == year && t.getDate().getMonthValue() == month)
                .collect(Collectors.toList());
    }

    public int getTotalIncome(java.util.List<Transaction> transactionList) {
        return transactionList.stream()
                .filter(t -> t.getAmount() > 0)
                .mapToInt(Transaction::getAmount)
                .sum();
    }

    public int getTotalExpense(java.util.List<Transaction> transactionList) {
        return transactionList.stream()
                .filter(t -> t.getAmount() < 0)
                .mapToInt(t -> -t.getAmount())
                .sum();
    }
}
