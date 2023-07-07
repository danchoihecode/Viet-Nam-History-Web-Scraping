package ditich;

import java.util.ArrayList;

public class DiTich {

	private String ten;
	private String diaChi;
	private String khoiLap;
	private String phanLoai;
	private ArrayList<String> suKienLienQuan;
	private ArrayList<String> nhanVatLienQuan;
	private ArrayList<String> leHoiLienQuan;
	private String mieuTa;
	private String anh;
	private String url;
	private int nguon;

	public String getDiaChi(){
		return DiaChi;
	}

	public void setTen(String ten) {
		this.ten = ten;
	}

	public String getTen() {
		return ten;
	}

	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}

	public void setKhoiLap(String khoiLap) {
		this.khoiLap = khoiLap;
	}

	public void setPhanLoai(String phanLoai) {
		this.phanLoai = phanLoai;
	}

	public void setSuKienLienQuan(ArrayList<String> suKienLienQuan) {
		this.suKienLienQuan = suKienLienQuan;
	}

	public void setNhanVatLienQuan(ArrayList<String> nhanVatLienQuan) {
		this.nhanVatLienQuan = nhanVatLienQuan;
	}

	public void setLeHoiLienQuan(ArrayList<String> leHoiLienQuan) {
		this.leHoiLienQuan = leHoiLienQuan;
	}

	public void setMieuTa(String mieuTa) {
		this.mieuTa = mieuTa;
	}

	public void setAnh(String anh) {
		this.anh = anh;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setNguon(int nguon) {
		this.nguon = nguon;
	}

}
