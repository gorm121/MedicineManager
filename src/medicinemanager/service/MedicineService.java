package service;

import model.Medicine;
import java.util.ArrayList;
import java.util.List;

public class MedicineService {
    private List<Medicine> medicines = new ArrayList<>();
    private List<String> categories = new ArrayList<>();
    private final FileService fileService = new FileService();

    public int getMedicinesSize(){ return medicines.size(); }


    public MedicineService() {
        loadFromFile();
    }

    public void loadFromFile() {
        List<Medicine> loadedMedicines = fileService.loadMedicines();
        if (loadedMedicines != null) {
            this.medicines = loadedMedicines;
            loadCategories();
        }
    }

    private void loadCategories(){
        for (Medicine med : medicines){
            String category = med.getCategory();
            if (!categories.contains(category)) categories.add(category);
        }
    }

    public void saveToFile() {
        fileService.saveMedicines(medicines);
    }

    public void addMedicine(Medicine medicine) {
        medicines.add(medicine);
        saveToFile();
    }

    public void changeMedicine(int oldValue, Medicine newMedicine){
        medicines.remove(oldValue);
        medicines.add(newMedicine);
        saveToFile();
    }

    public void deleteMedicine(int id) {
        if (id >= 0 && id < medicines.size()) {
            medicines.remove(id);
            saveToFile();
        }
    }

    public List<Medicine> getAllMedicines() { return medicines;}
    public List<String> getAllCategories() { return categories; }


    public void renameCategory(String oldName, String newName) {
        medicines.stream()
                .filter(m -> oldName.equalsIgnoreCase(m.getCategory()))
                .forEach(m -> m.setCategory(newName));

        categories.replaceAll(c -> oldName.equalsIgnoreCase(c) ? newName : c);
        saveToFile();
    }

    public void removeCategory(String category) {
        medicines.stream()
                .filter(m -> category.equalsIgnoreCase(m.getCategory()))
                .forEach(m -> m.setCategory("Без категории"));
        categories.removeIf(c -> c.equalsIgnoreCase(category));
        saveToFile();
    }

}