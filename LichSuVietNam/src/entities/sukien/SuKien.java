package entities.sukien;

import java.util.ArrayList;

public class SuKien {

	private String ten;
	private String thoiGian;
	private String thoiKy;
	private String diaDiem;
	private String mieuTa;
	private String nguyenNhan;
	private String ketQua;
	private ArrayList<String> nhanVatLienQuan;
	private ArrayList<ArrayList<String>> thamChien;
	private ArrayList<ArrayList<String>> chiHuy;
	private ArrayList<String> lucLuong;
	private ArrayList<String> tonThat;
	private int nguon;
	private String url;

	public void setTen(String ten) {
		this.ten = ten;
	}

	public String getTen() {
		return ten;
	}

	public void setThoiGian(String thoiGian) {
		this.thoiGian = thoiGian;
	}

	public void setThoiKy(String thoiKy) {
		this.thoiKy = thoiKy;
	}

	public void setDiaDiem(String diaDiem) {
		this.diaDiem = diaDiem;
	}

	public void setNguyenNhan(String nguyenNhan) {
		this.nguyenNhan = nguyenNhan;
	}

	public void setKetQua(String ketQua) {
		this.ketQua = ketQua;
	}

	public void setNguon(int nguon) {
		this.nguon = nguon;
	}

	public void setMieuTa(String mieuTa) {
		this.mieuTa = mieuTa;
	}

	public void setNhanVatLienQuan(ArrayList<String> nhanVatLienQuan) {
		this.nhanVatLienQuan = nhanVatLienQuan;
	}

	public ArrayList<String> getNhanVatLienQuan() {
		return nhanVatLienQuan;
	}

	public void setThamChien(ArrayList<ArrayList<String>> thamChien) {
		this.thamChien = thamChien;
	}

	public void setLucLuong(ArrayList<String> lucLuong) {
		this.lucLuong = lucLuong;
	}

	public void setTonThat(ArrayList<String> tonThat) {
		this.tonThat = tonThat;
	}

	public void setChiHuy(ArrayList<ArrayList<String>> chiHuy) {
		this.chiHuy = chiHuy;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
