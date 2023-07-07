package demo;

import java.io.*;

public class TextFileConcatenation {
    public static void main(String[] args) {
        String sourceFile = "file\\figure-source-2.txt";
        String additionalFile = "file\\figure-source-2-add.txt";

        try {
            FileWriter fileWriter = new FileWriter(sourceFile, true); // true để bổ sung vào cuối tệp
            FileReader fileReader = new FileReader(additionalFile);

            int character;
            while ((character = fileReader.read()) != -1) {
                fileWriter.write(character);
            }

            fileReader.close();
            fileWriter.close();

            System.out.println("Nối tệp thành công!");
        } catch (IOException e) {
            System.out.println("Đã xảy ra lỗi khi nối tệp: " + e.getMessage());
        }
    }
}

