package oop.bufihofa.classes;


public class BasicDate {
    private int ngay;
    private int thang;
    private int nam;
    public BasicDate(){}
    //31/12/1987
    public BasicDate(String s){
        ngay  = (s.charAt(0) - 48) * 10 + (s.charAt(1) - 48);
        thang = (s.charAt(3) - 48) * 10 + (s.charAt(4) - 48);
        nam   = (s.charAt(6) - 48) * 1000 + (s.charAt(7) - 48) * 100 + (s.charAt(8) - 48) * 10 + (s.charAt(9) - 48);
    }
    public BasicDate(int ngay, int thang, int nam){
        this.ngay = ngay;
        this.thang = thang;
        this.nam = nam;
    }
    public String toData(){
        String a = "";
        String b = "";
        String c = "";
        if(ngay <10) a = a + "0";
        if(thang<10) b = b + "0";
        if(nam  <10) c = c + "0";
        if(nam <100) c = c + "0";
        if(nam<1000) c = c + "0";
        a = a + Integer.toString(ngay);
        b = b + Integer.toString(thang);
        c = c + Integer.toString(nam);
        return a+"/"+b+"/"+c;
    }

    public int getNgay() {
        return ngay;
    }

    public void setNgay(int ngay) {
        this.ngay = ngay;
    }

    public int getThang() {
        return thang;
    }

    public void setThang(int thang) {
        this.thang = thang;
    }

    public int getNam() {
        return nam;
    }

    public void setNam(int nam) {
        this.nam = nam;
    }
}
