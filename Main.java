package test;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;


import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


public class Main extends Application {
	Connect connect;
    FinanceManager financeManager = new FinanceManager();
    TextArea reportArea;
    DatePicker datePicker;
    TextField amountField;
    ComboBox<String> typeComboBox;
    DatePicker reportDatePicker;
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Aplikasi Simulasi Laba Rugi UMKM");

    
        Label inputLabel = new Label("Input Pemasukan / Pengeluaran");
        inputLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: 700; -fx-padding: 0 0 10 0;");

        typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("Pemasukan", "Pengeluaran");
        typeComboBox.setValue("Pemasukan");
        typeComboBox.setPrefWidth(120);

        amountField = new TextField();
        amountField.setPromptText("Jumlah (Rp)");
        amountField.setPrefWidth(150);

        datePicker = new DatePicker(LocalDate.now());
        datePicker.setConverter(new StringConverter<LocalDate>() {
            private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd - MM - yyyy");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
        datePicker.setPrefWidth(160);

        Button addButton = new Button("Tambah");
        addButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white; -fx-font-weight: 700;");
        addButton.setPrefWidth(80);
        addButton.setOnAction(e -> addTransaction());

        HBox inputBox = new HBox(15, typeComboBox, amountField, datePicker, addButton);
        inputBox.setAlignment(Pos.CENTER_LEFT);
        inputBox.setPadding(new Insets(10));
        inputBox.setStyle("-fx-background-color: #fafafa; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 1);");

        Label reportLabel = new Label("Laporan");
        reportLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: 700; -fx-padding: 20 0 10 0;");

        reportDatePicker = new DatePicker(LocalDate.now());
        reportDatePicker.setConverter(datePicker.getConverter());
        reportDatePicker.setPrefWidth(160);

        Button dailyReportBtn = new Button("Laporan Harian");
        dailyReportBtn.setOnAction(e -> showDailyReport());
        dailyReportBtn.setStyle("-fx-background-color: #111111; -fx-text-fill: white; -fx-font-weight: 600;");

        Button monthlyReportBtn = new Button("Laporan Bulanan");
        monthlyReportBtn.setOnAction(e -> showMonthlyReport());
        monthlyReportBtn.setStyle("-fx-background-color: #444444; -fx-text-fill: white; -fx-font-weight: 600;");

        Button breakevenBtn = new Button("Simulasi Titik Impas");
        breakevenBtn.setOnAction(e -> showBreakEvenPoint());
        breakevenBtn.setStyle("-fx-background-color: #888888; -fx-text-fill: white; -fx-font-weight: 600;");

        HBox reportButtons = new HBox(15, dailyReportBtn, monthlyReportBtn, breakevenBtn);
        reportButtons.setAlignment(Pos.CENTER_LEFT);
        reportButtons.setPadding(new Insets(10));

        HBox dateBox = new HBox(10, new Label("Pilih Tanggal:"), reportDatePicker);
        dateBox.setAlignment(Pos.CENTER_LEFT);

        reportArea = new TextArea();
        reportArea.setEditable(false);
        reportArea.setPrefHeight(300);
        reportArea.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-control-inner-background: #fefefe; -fx-background-radius: 8; -fx-padding: 10;");

        VBox root = new VBox(10,
                inputLabel,
                inputBox,
                reportLabel,
                dateBox,
                reportButtons,
                reportArea
        );
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #ffffff; -fx-font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; -fx-text-fill: #374151;");
        root.setPrefWidth(600);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

  
	private void addTransaction() {
        String type = typeComboBox.getValue();
        String amountText = amountField.getText();
        LocalDate date = datePicker.getValue();
        if (amountText == null || amountText.trim().isEmpty()) {
            showAlert("Input Error", "Jumlah harus diisi.");
            return;
        }
        int amount;
        try {
            amount = Integer.parseInt(amountText);
            if (amount <= 0) {
                showAlert("Input Error", "Jumlah harus angka positif.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Jumlah harus berupa angka valid.");
            return;
        }

        if ("Pengeluaran".equals(type)) {
        	connect = Connect.getInstance();
        	Integer Pengeluaran =  Integer .valueOf(amountText);
        	Integer Pemasukan = Integer.valueOf(amountText);
            String query =  "INSERT INTO pengeluaran " + "VALUES ('" + Pengeluaran +"' ,'\" + Pemasukan +\"')";	
        	amount = -amount;
        }

        financeManager.addTransaction(new Transaction(amount, date));
        reportArea.setText("Transaksi berhasil ditambahkan:\n" + new Transaction(amount, date).toString());
        amountField.clear();
    }

    private void showDailyReport() {
        LocalDate date = reportDatePicker.getValue();
        List<Transaction> transactions = financeManager.getTransactionsByDate(date);
        if (transactions.isEmpty()) {
            reportArea.setText("Tidak ada transaksi pada tanggal " + date.format(DateTimeFormatter.ofPattern("dd - MM - yyyy")) + ".");
            return;
        }
        StringBuilder sb = new StringBuilder("Laporan Harian - " + date.format(DateTimeFormatter.ofPattern("dd - MM - yyyy")) + "\n\n");
        for (Transaction t : transactions) {
            sb.append(t.toString()).append("\n");
        }
        int totalIncome = financeManager.getTotalIncome(transactions);
        int totalExpense = financeManager.getTotalExpense(transactions);
        sb.append("\nTotal Pemasukan: Rp ").append(totalIncome);
        sb.append("\nTotal Pengeluaran: Rp ").append(totalExpense);
        sb.append("\nLaba / Rugi Bersih: Rp ").append(totalIncome - totalExpense);
        reportArea.setText(sb.toString());
    }

    private void showMonthlyReport() {
        LocalDate date = reportDatePicker.getValue();
        int year = date.getYear();
        int month = date.getMonthValue();
        List<Transaction> transactions = financeManager.getTransactionsByMonth(year, month);
        if (transactions.isEmpty()) {
            reportArea.setText("Tidak ada transaksi pada bulan " + date.format(DateTimeFormatter.ofPattern("MM - yyyy")) + ".");
            return;
        }
        StringBuilder sb = new StringBuilder("Laporan Bulanan - " + date.format(DateTimeFormatter.ofPattern("MM - yyyy")) + "\n\n");
        int totalIncome = financeManager.getTotalIncome(transactions);
        int totalExpense = financeManager.getTotalExpense(transactions);
        sb.append("Total Pemasukan: Rp ").append(totalIncome).append("\n");
        sb.append("Total Pengeluaran: Rp ").append(totalExpense).append("\n");
        sb.append("Laba / Rugi Bersih: Rp ").append(totalIncome - totalExpense).append("\n");
        reportArea.setText(sb.toString());
    }

    private void showBreakEvenPoint() {

        List<Transaction> allTransactions = financeManager.getAllTransactions();
        int totalIncome = financeManager.getTotalIncome(allTransactions);
        int totalExpense = financeManager.getTotalExpense(allTransactions);
        StringBuilder sb = new StringBuilder("Simulasi Titik Impas\n\n");
        sb.append("Total Pemasukan: Rp ").append(totalIncome).append("\n");
        sb.append("Total Pengeluaran: Rp ").append(totalExpense).append("\n");
        int net = totalIncome - totalExpense;
        if (net == 0) {
            sb.append("Anda sudah mencapai titik impas (break-even point).");
        } else if (net > 0) {
            sb.append("Anda mendapatkan laba bersih sebesar Rp ").append(net).append(".");
        } else {
            sb.append("Anda mengalami rugi bersih sebesar Rp ").append(-net).append(".");
        }
        reportArea.setText(sb.toString());
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public static void main(String[] args) {
        launch(args);
    }
}


