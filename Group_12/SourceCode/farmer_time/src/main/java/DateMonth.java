public class DateMonth {
    public int year;
    public int month;

    public DateMonth() {

    }

    public DateMonth(int year, int month) {
        this.year = year;
        this.month = month;
    }

    public void prev() {
        this.month--;
        if (this.month < 1) {
            this.month = 12;
            this.year--;
        }
    }

    public void next() {
        this.month++;
        if (this.month > 12) {
            this.month = 1;
            this.year++;
        }
    }
}