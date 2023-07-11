package entities.lehoi;

import java.util.ArrayList;

public class LeHoi {

	private String ten;
	private String diaDiem;
	private String thoiGian;
	private String lanDauToChuc;
	private String mieuTa;
	private String suKienLienQuan;
	private ArrayList<String> diTichLienQuan;
	private ArrayList<String> nhanVatLienQuan;
	private String anh;
	private String url;
	private int nguon;

	public void setTen(String ten) {
		this.ten = ten;
	}

	public String getDiaDiem() {
		return diaDiem;
	}

	public String getThoiGian() {
		return thoiGian;
	}

	public String getLanDauToChuc() {
		return lanDauToChuc;
	}

	public String getMieuTa() {
		return mieuTa;
	}

	public String getSuKienLienQuan() {
		return suKienLienQuan;
	}

	public ArrayList<String> getNhanVatLienQuan() {
		return nhanVatLienQuan;
	}

	public String getAnh() {
		return anh;
	}

	public String getUrl() {
		return url;
	}

	public int getNguon() {
		return nguon;
	}

	public void setDiaDiem(String diaDiem) {
		this.diaDiem = diaDiem;
	}

	public void setThoiGian(String thoiGian) {
		this.thoiGian = thoiGian;
	}

	public void setLanDauToChuc(String lanDauToChuc) {
		this.lanDauToChuc = lanDauToChuc;
	}

	public void setMieuTa(String mieuTa) {
		this.mieuTa = mieuTa;
	}

	public void setSuKienLienQuan(String suKienLienQuan) {
		this.suKienLienQuan = suKienLienQuan;
	}

	public void setDiTichLienQuan(ArrayList<String> diTichLienQuan) {
		this.diTichLienQuan = diTichLienQuan;
	}

	public void setNhanVatLienQuan(ArrayList<String> nhanVatLienQuan) {
		this.nhanVatLienQuan = nhanVatLienQuan;
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

	public String getTen() {
		return ten;
	}

	public ArrayList<String> getDiTichLienQuan() {
		return diTichLienQuan;
	}

}
