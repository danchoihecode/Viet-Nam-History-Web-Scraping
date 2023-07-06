package scrapers.lehoi;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import lehoi.LeHoi;

public class MergeFestival {
    public static void main(String[] args) {
        // Đường dẫn tới các file input.json và output.json
        String[] inputFileNames = {
            "file\\festival-source-2.json",
            "file\\festival-source-8.json",
            "file\\festival-source-9.json",
            "file\\festival-source-10.json"
        };
        String outputFile = "file\\merged-festivals.json";

        try {
            // Tạo một danh sách để lưu trữ các lễ hội từ các file input.json
            List<LeHoi> mergedFestivals = new ArrayList<>();

            // Đọc dữ liệu từ các file input.json
            Gson gson = new Gson();
            for (String inputFileName : inputFileNames) {
                JsonArray jsonArray = gson.fromJson(new FileReader(inputFileName), JsonArray.class);

                // Chuyển đổi dữ liệu từ JsonObject thành các đối tượng LeHoi
                Type festivalListType = new TypeToken<List<LeHoi>>() {}.getType();
                List<LeHoi> festivals = gson.fromJson(jsonArray, festivalListType);

                // Hợp nhất lễ hội với danh sách lễ hội đã có
                mergeFestivals(mergedFestivals, festivals);
            }

            // Ghi ra file output.json
            FileWriter fileWriter = new FileWriter(outputFile);
            gson.toJson(mergedFestivals, fileWriter);
            fileWriter.close();

            System.out.println("Successfully merged festivals.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void mergeFestivals(List<LeHoi> mergedFestivals, List<LeHoi> festivals) {
        for (LeHoi festival : festivals) {
            boolean merged = false;

            // Kiểm tra xem lễ hội đã được hợp nhất hay chưa
            for (LeHoi mergedFestival : mergedFestivals) {
                if (isSameFestival(festival, mergedFestival)) {
                    mergeFestivalAttributes(festival, mergedFestival);
                    merged = true;
                    break;
                }
            }

            // Nếu chưa được hợp nhất, thêm lễ hội vào danh sách hợp nhất
            if (!merged) {
                mergedFestivals.add(festival);
            }
        }
    }

    private static boolean isSameFestival(LeHoi festival1, LeHoi festival2) {
        // So sánh thuộc tính "Tên" của hai lễ hội (không phân biệt hoa thường)
        return festival1.getTen().equalsIgnoreCase(festival2.getTen());
    }

    private static void mergeFestivalAttributes(LeHoi sourceFestival, LeHoi targetFestival) {
        mergeStringAttribute(sourceFestival.getDiaDiem(), targetFestival.getDiaDiem());
        mergeStringAttribute(sourceFestival.getThoiGian(), targetFestival.getThoiGian());
        mergeStringAttribute(sourceFestival.getLanDauToChuc(), targetFestival.getLanDauToChuc());
        mergeStringAttribute(sourceFestival.getMieuTa(), targetFestival.getMieuTa());
        mergeStringAttribute(sourceFestival.getAnh(), targetFestival.getAnh());
        mergeStringAttribute(sourceFestival.getUrl(), targetFestival.getUrl());
        mergeStringListAttribute(sourceFestival.getDiTichLienQuan(), targetFestival.getDiTichLienQuan());
        mergeStringListAttribute(sourceFestival.getNhanVatLienQuan(), targetFestival.getNhanVatLienQuan());
        mergeStringAttribute(sourceFestival.getSuKienLienQuan(), targetFestival.getSuKienLienQuan());
    }

    private static void mergeStringAttribute(String sourceValue, String targetValue) {
        if (sourceValue != null && !sourceValue.equalsIgnoreCase(targetValue)) {
            // Add sourceValue to targetValue if they are different
            if (targetValue != null) {
                targetValue += "; " + sourceValue;
            } else {
                targetValue = sourceValue;
            }
        }
    }


    private static void mergeStringListAttribute(List<String> sourceList, List<String> targetList) {
        if (sourceList != null && targetList != null) {
            Set<String> mergedSet = new HashSet<>(targetList);
            mergedSet.addAll(sourceList);
            targetList.clear();
            targetList.addAll(mergedSet);
        } else if (sourceList != null && targetList == null) {
            targetList = new ArrayList<>(sourceList);
        }
    }

}
