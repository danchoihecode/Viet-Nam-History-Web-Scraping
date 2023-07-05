package demo;

public class Demo {

	public static void truncate(String str) {
		int idx1 = -1;
		int idx2 = -1;
		boolean firstDigit = true;
		for (int i = 0; i < str.length(); i++) {
			if (firstDigit && '0' <= str.charAt(i) && str.charAt(i) <= '9') {
				firstDigit = false;
				idx1 = i;
			}
			if ('0' <= str.charAt(i) && str.charAt(i) <= '9') {
				idx2 = i;
			}
		}
		System.out.println(str.substring(idx1, idx2 + 1));
		if (!str.substring(idx2 + 2).isBlank())
			System.out.println(str.substring(idx2 + 2));

	}

	public static void main(String[] args) {
		truncate("1 tháng 12 năm 1920 huyện Phú Lộc, Thừa Thiên, Liên bang Đông Dương");
	}

}
