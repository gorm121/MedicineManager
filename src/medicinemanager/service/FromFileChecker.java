package service;

import model.Medicine;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FromFileChecker {
    public static List<Medicine> toListMedicine(List<String> lines){
        List<Medicine> medicines = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            try {
                String line = lines.get(i);
                String[] parts = line.split("\\|");
                if (parts.length < 5 || !checkMedicine(parts[0],parts[1],parts[3])) continue;

                medicines.add(new Medicine(parts[0], parts[1], LocalDate.parse(parts[2]), parts[3], Integer.parseInt(parts[4])));

            } catch (Exception e) {
                System.out.println("Пропускаем строку " + (i + 1) + ": ошибка - " + e.getMessage());
            }
        }
        System.out.println("Успешно загружено: " + medicines.size() + " лекарств");
        return medicines;
    }

    private static boolean checkMedicine(String name, String description,String category){
        return !(name.isBlank() || description.isBlank() || category.isBlank());
    }


}
