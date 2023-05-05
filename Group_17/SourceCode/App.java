/***********************************************************************************************
 * Các chức năng chính của chương trình:
 * 1.Xem số dư
 * 2.Xem tổng số tiền đã tiêu trong tuần qua
 * 3.Xem danh sách tất cả giao dịch
 * 4.Thêm một giao dịch mới
 * 5.Thêm một mục chi tiêu mới
 * 6.Tìm giao dịch theo ngày
 * 7.Tìm giao dịch theo mục chi tiêu
************************************************************************************************/
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

public class App {
    private int income;

    public App(int income) {
        this.income = income;
    }

    //doc du lieu tu file cateDate va tra ve mot list string(done)
    public List<String> readDataCate() {
        List<String> categories = new ArrayList<>();
        try {
            FileReader fr = new FileReader("D:/oop/Project-OOP-INT2204-2/Group_17/SourceCode/data/CateData.txt");
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            while(true) {
                line = br.readLine();
                if(line == null) {
                    break;
                }
                categories.add(line);
            }
        } catch (Exception e) {
            System.out.println("Doc file loi");
            // TODO: handle exception
        }

        return categories;
    }

    //xet xem category moi da ton tai hay chua(done)

    public boolean isCategoryexit(String newCaName) {
        List<String> categories = readDataCate();

        for(int i = 0; i < categories.size(); i++) {
            if(newCaName.equals(categories.get(i))) {
                return true;
            }
        }
        return false;
    }

    //them mot muc chi tieu(done)
    public void addCategory() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your new category's name: ");
        String newCaName = sc.nextLine();

        try {
            FileWriter fw = new FileWriter("D:/oop/Project-OOP-INT2204-2/Group_17/SourceCode/data/CateData.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);

            if(isCategoryexit(newCaName) == true) {
                System.out.println("Category is exited.");
            }
            else {
                bw.write(newCaName);
                bw.newLine();
                System.out.println("Done.");
            }

            bw.close();
            fw.close();
        } catch (Exception e) {
            System.out.println("Viet vao file loi");
            // TODO: handle exception
        }
    }

    //them mot giao dich(done)
    public void addTransaction() {
        List<String> categories = readDataCate();
        Scanner sc = new Scanner(System.in);

        System.out.println("List category:");
        for(int i = 0; i < categories.size(); i++) {
            System.out.print(i+1 + ".");
            System.out.println(categories.get(i));
        }
        System.out.print("Enter category: ");
        String category = sc.nextLine();

        System.out.print("Enter amount: " );
        int amount = sc.nextInt();

        sc.nextLine();

        System.out.print("Enter date: ");
        String input = sc.nextLine();
        LocalDate date = LocalDate.parse(input);

        System.out.print("Enter description: ");
        String description = sc.nextLine();

        Transaction transaction = new Transaction(category, amount, date, description);

        try {
            FileWriter fw = new FileWriter("D:/oop/Project-OOP-INT2204-2/Group_17/SourceCode/data/TransData.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(transaction.toString());
            bw.newLine();

            System.out.println("Done.");
            bw.close();
            fw.close();
        } catch (Exception e) {
            System.out.println("Viet vao file loi");
            // TODO: handle exception
        }
    }

    //doc du lieu tu file va tra ve mot list transacsion(done)
    public List<Transaction> readFromFile() {
        List<Transaction> transactions = new ArrayList<>();
        try {
            FileReader fr = new FileReader("D:/oop/Project-OOP-INT2204-2/Group_17/SourceCode/data/TransData.txt");
            BufferedReader br = new BufferedReader(fr);

            String line = "";
            while(true) {
                line = br.readLine();
                if(line == null) {
                    break;
                }

                String txt[] = line.split(";");
                String category = txt[0];
                int amount = Integer.parseInt(txt[1]);
                LocalDate date = LocalDate.parse(txt[2]);
                String description = txt[3];

                transactions.add(new Transaction(category, amount, date, description));
            }
        } catch (Exception e) {
            System.out.println("Doc file loi");
            // TODO: handle exception
        }
        return transactions;
    }

    //in thong tin danh sach giao dich(done)
    public void printTransactions() {
        List<Transaction> transactions = readFromFile();
        System.out.printf("%-20s %-20s %-20s %-10s\n", "Category", "Date", "Description", "Amount");
        for(Transaction t : transactions) {
            System.out.printf("%-20s %-20s %-20s %-10s\n",
                        t.getCategory(),
                        t.getDate(),
                        t.getDescription(),
                        t.getAmount());
        }
    }

    //tim va in thong tin nhung giao dich theo category(done)
    public void findAndPrintCategory() {
        List<Transaction> transactions = readFromFile();
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter category: ");
        String cateName = sc.nextLine();

        System.out.printf("%-20s %-20s %-20s %-10s\n", "Category", "Date", "Description", "Amount");
        for(Transaction t : transactions) {
            if(cateName.equals(t.getCategory())) {
                System.out.printf("%-20s %-20s %-20s %-10s\n",
                        t.getCategory(),
                        t.getDate(),
                        t.getDescription(),
                        t.getAmount());
            }
        }
    }

    //tim va in thong tin nhung giao dich theo ngay(done)
    public void findAndPrintDate() {
        List<Transaction> transactions = readFromFile();
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter date(y/m/d): ");
        String input = sc.nextLine();
        LocalDate date = LocalDate.parse(input);

        System.out.printf("%-20s %-20s %-20s %-10s\n", "Category", "Date", "Description", "Amount");
        for(Transaction t : transactions) {
            if(date.compareTo(t.getDate()) == 0) {
                System.out.printf("%-20s %-20s %-20s %-10s\n",
                        t.getCategory(),
                        t.getDate(),
                        t.getDescription(),
                        t.getAmount());
            }
        }
    }

    //tinh so tien chi tieu trong mot tuan vua qua(done)
    public int getTotalSpentTime() {
        List<Transaction> transactions = readFromFile();
        int totalSpentTime = 0;
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(7);
        for(Transaction t : transactions) {
            if(t.getDate().isAfter(startDate) && t.getDate().isBefore(endDate)) {
                totalSpentTime += t.getAmount();
            }
        }

        return totalSpentTime;
    }

    //tinh so tien con lai(done)
    public int getBalance() {
        List<Transaction> transactions = readFromFile();
        int totalSpent = 0;
        for(int i = 0; i < transactions.size(); i++){
            totalSpent += transactions.get(i).getAmount();
        }
        int balance = income - totalSpent;
        return balance;
    }

    //getter, setter

    public int GetIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }
}
