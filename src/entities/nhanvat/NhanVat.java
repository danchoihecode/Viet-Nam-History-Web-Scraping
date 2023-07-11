package entities.nhanvat;

import java.util.ArrayList;

public class NhanVat {

	private String ten;
	private String namSinh;
	private String namMat;
	private ArrayList<String> tenKhac;
	private String queQuan;
	private String thoiKy;
	private ArrayList<String> suKienLienQuan;
	private String mieuTa;
	private ArrayList<String> tacPham;
	private String anh;
	private int nguon;
	private String url;

	public void setTen(String ten) {
		this.ten = ten;
	}

	public String getTen() {
		return ten;
	}

	public void setNamSinh(String namSinh) {
		this.namSinh = namSinh;
	}

	public void setNamMat(String namMat) {
		this.namMat = namMat;
	}

	public void setTenKhac(ArrayList<String> tenKhac) {
		this.tenKhac = tenKhac;
	}

	public void setQueQuan(String queQuan) {
		this.queQuan = queQuan;
	}

	public void setThoiKy(String thoiKy) {
		this.thoiKy = thoiKy;
	}

	public void setSuKienLienQuan(ArrayList<String> suKienLienQuan) {
		this.suKienLienQuan = suKienLienQuan;
	}

	public void setMieuTa(String mieuTa) {
		this.mieuTa = mieuTa;
	}

	public void setTacPham(ArrayList<String> tacPham) {
		this.tacPham = tacPham;
	}

	public void setAnh(String anh) {
		this.anh = anh;
	}

	public void setNguon(int nguon) {
		this.nguon = nguon;
	}

	public String getNamSinh() {
		return namSinh;
	}

	public String getNamMat() {
		return namMat;
	}

	public String getMieuTa() {
		return mieuTa;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
