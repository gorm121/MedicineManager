package ui;

import model.Medicine;
import service.FileService;
import service.MedicineService;

import java.time.LocalDate;
import java.util.*;

public class Menu {
    private final MedicineService medicineService = new MedicineService();
    private final Scanner scanner = new Scanner(System.in);
    private int pageSize;
    private final InputChecker inputChecker = new InputChecker();


    public Menu(){
        pageSize = FileService.readSettings();
    }

    public void start() {
        System.out.println("Размер страницы:  " + pageSize);
        while (true) {
            UI.showMainMenu();
            int choice = inputChecker.readInt("Ваш выбор: ",0, 6,scanner);

            switch (choice) {
                case 1 -> showMedicines();
                case 2 -> manageMedicines();
                case 3 -> renameCategory();
                case 4 -> removeCategory();
                case 5 -> changePageSize();
                case 0 -> {
                    System.out.println("Выход...");
                    return;
                }
            }
        }
    }

    private void showMedicines() {
        UI.showMedicinesMenu();
        int choice = inputChecker.readInt("Ваш выбор: ",0, 5,scanner);

        List<Medicine> medicines = medicineService.getAllMedicines();

        switch (choice) {
            case 1 -> pagination(medicines, medicineService.getMedicinesSize()); // все
            case 2 -> showByCategory(medicines);   // по категориям
            case 3 -> showSortedByName(medicines); // сортировка по имени
            case 4 -> showSortedByDate(medicines); // сортировка по дате
            case 5 -> showExpired(medicines);      // просроченные
            case 0 -> {} // назад
        }
    }

    private void manageMedicines() {
        UI.showMenuForManagementMedicines();
        int choice = inputChecker.readInt("Ваш выбор: ",0, 4,scanner);

        switch (choice) {
            case 1 -> addMedicine();
            case 2 -> findMedicine();
            case 3 -> changeMedicine();
            case 4 -> deleteMedicine();
            case 0 -> {}
        }
    }





    private void showByCategory(List<Medicine> medicines) {
        System.out.println("--- Текущие категории ---");
        List<String> categories = medicineService.getAllCategories();
        for (int i = 0; i < categories.size(); i++ ){
            System.out.println((i+1) + ": "+ categories.get(i));
        }
        if (medicines.isEmpty()) return;
        int choice = inputChecker.readInt("Выберите категорию (номер): ", 1, categories.size(), scanner);
        String category = categories.get(choice - 1);
        List<Medicine> medicinesByCategory = medicines.stream().filter(x -> x.getCategory().equalsIgnoreCase(category.trim())).toList();
        if (medicinesByCategory.isEmpty()) {System.out.println("Список с данной категорией пуст"); return;}
        pagination(medicinesByCategory,medicinesByCategory.size());
    }

    private void showSortedByName(List<Medicine> medicines) {
        medicines.sort(Comparator.comparing(Medicine::getName));
        System.out.println();
        System.out.println("Отсортированно по имени");
        pagination(medicines,medicines.size());
    }

    private void showSortedByDate(List<Medicine> medicines) {
        medicines.sort(Comparator.comparing(Medicine::getDate));
        System.out.println();
        System.out.println("Отсортированно по дате");
        pagination(medicines,medicines.size());
    }

    private void showExpired(List<Medicine> medicines) {
        medicines = medicines.stream().filter(x-> x.getDate().toEpochDay() < LocalDate.now().toEpochDay()).toList();
        System.out.println();
        System.out.println("Просрочка");
        pagination(medicines,medicines.size());

    }



    private void addMedicine() {
        System.out.println("=== ➕ ДОБАВЛЕНИЕ ЛЕКАРСТВА ===");
        String name = inputChecker.readNonEmptyString("Название: ",scanner);
        String description = inputChecker.readNonEmptyString("Описание: ",scanner);
        LocalDate date = inputChecker.readDate("Срок годности (ГГГГ-ММ-ДД): ",scanner);
        String category = inputChecker.readNonEmptyString("Категория: ",scanner);
        int count = inputChecker.readInt("Количество: ", 1, 1000,scanner);

        Medicine medicine = new Medicine(name, description, date, category, count);
        medicineService.addMedicine(medicine);
        System.out.println(" Лекарство добавлено!");
    }

    private void findMedicine(){
        System.out.println("=== \uD83D\uDD0D ПОИСК ЛЕКАРСТВА ===");

        String str = inputChecker.readNonEmptyString("Поиск: ", scanner);
        List<Medicine> medicines = medicineService.getAllMedicines().stream().filter(x -> x.toString().toLowerCase().contains(str.toLowerCase())).toList();
        if (!medicines.isEmpty()) pagination(medicines,medicines.size());
    }

    private void changeMedicine(){
        List<Medicine> medicines = medicineService.getAllMedicines();
        if (medicines.isEmpty()) {
            System.out.println("Лекарства отсутсвуют");
            return;
        }
        for (int i = 0; i < medicines.size(); i++){
            System.out.println((i+1) + ": " + medicines.get(i).toString());
        }
        System.out.println("-".repeat(143));
        System.out.println("=== ИЗМЕНИЕ ЛЕКАРСТВА ===");
        int index = inputChecker.readInt("Введите номер лекарства которе хотите изменить: ",1,medicines.size(),scanner) - 1;
        Medicine med = medicines.get(index);

        String name = inputChecker.changeMedicineString("Название (Enter - без изменений): ",med.getName(),scanner);
        String description = inputChecker.changeMedicineString("Описание (Enter - без изменений): ",med.getDescription(),scanner);
        LocalDate date = inputChecker.changeDate("Срок годности (ГГГГ-ММ-ДД) (Enter - без изменений): ",med.getDate(),scanner);
        String category = inputChecker.changeMedicineString("Категория (Enter - без изменений): ",med.getCategory(),scanner);
        int count = inputChecker.changeMedicineInt("Количество (0 - без изменений): ", med.getCount(),scanner);

        Medicine newMed = new Medicine(name,description, date,category,count);
        medicineService.changeMedicine(index,newMed);
        System.out.println(" Лекарство успешно изменено");
        System.out.println(newMed);
    }

    private void deleteMedicine(){
        List<Medicine> medicines = medicineService.getAllMedicines();
        if (medicines.isEmpty()) {
            System.out.println("Лекарства отсутсвуют");
            return;
        }

        System.out.println("=== ИЗМЕНИЕ ЛЕКАРСТВА ===");
        int index = inputChecker.readInt("Введите номер лекарства которе хотите изменить: ",1,medicines.size(),scanner) - 1;
        medicineService.deleteMedicine(index);
        System.out.println("Лекарство успешно удалено");
    }



    private void pagination(List<Medicine> medicines, int size) {
        if (medicines.isEmpty()) {
            System.out.println("Лекарств нет");
            return;
        }

        boolean inLogsMenu = true;
        int currentPage = 1;

        showMedicinesWithPage(medicines,1,size);

        while (inLogsMenu) {
            System.out.println("\n1. Следующая страница");
            System.out.println("2. Предыдущая страница");
            System.out.println("0. Вернуться в главное меню");
            int choice = inputChecker.readInt("Ваше действие: ", 0 , 2,scanner);
            int checkPage = currentPage;
            switch (choice) {
                case 1 -> {
                    if (((double) size / pageSize) > currentPage) {
                        currentPage++;
                        showMedicinesWithPage(medicines,currentPage,size);
                    } else System.out.println("Больше нет лекарств");
                }
                case 2 -> {
                    currentPage = Math.max(1, currentPage - 1);
                    if (!(checkPage == currentPage))
                        showMedicinesWithPage(medicines,currentPage,size);
                    else System.out.println("Больше нет лекарств");
                }
                case 0 -> inLogsMenu = false;
            }

        }
    }

    private void showMedicinesWithPage(List<Medicine> medicines, int currentPage, int size) {
        if (medicines.isEmpty()) return;
        currentPage *= pageSize;
        System.out.println("-".repeat(143));
        for (int i = 0; i < medicines.size(); i++) {
            if (i >= currentPage - pageSize && i < currentPage)
                System.out.println((i + 1) + ". " + medicines.get(i).toString());
        }
        System.out.println("-".repeat(143));
        System.out.println("Текущая страница: " + currentPage/pageSize + " из: " + (int)(Math.ceil(((double) size/pageSize))));
        System.out.println("-".repeat(143));
    }

    private void renameCategory() {
        List<String> cats = medicineService.getAllCategories();
        if (cats.isEmpty()) {
            System.out.println("Нет категорий для переименования.");
            return;
        }
        System.out.println("Существующие категории: " + String.join(", ", cats));

        String oldName = inputChecker.readNonEmptyString(
                "Введите категорию для переименования: ",scanner);
        if (cats.stream().noneMatch(oldName::equalsIgnoreCase)) {
            System.out.println("Категория не найдена.");
            return;
        }

        String newName = inputChecker.readNonEmptyString("Введите новое название: ",scanner);
        medicineService.renameCategory(oldName, newName);
        System.out.println("Категория переименована.");
    }

    private void removeCategory() {
        List<String> cats = medicineService.getAllCategories();
        if (cats.isEmpty()) {
            System.out.println("Нет категорий для удаления.");
            return;
        }
        System.out.println("Существующие категории: " + String.join(", ", cats));

        String cat = inputChecker.readNonEmptyString(
                "Введите категорию для удаления (лекарства станут 'Без категории'): ",scanner);
        if (cats.stream().noneMatch(cat::equalsIgnoreCase)) {
            System.out.println("Категория не найдена.");
            return;
        }

        medicineService.removeCategory(cat);
        System.out.println("Категория удалена.");
    }



    private void changePageSize() {
        System.out.println("===  НАСТРОЙКИ ===");
        System.out.println("Текущий размер страницы: " + pageSize);
        pageSize = inputChecker.readInt("Новый размер страницы: ", 1, 100,scanner);
        System.out.println(" Размер страницы изменен на: " + pageSize);
        FileService.saveSettings(pageSize);
    }

}
