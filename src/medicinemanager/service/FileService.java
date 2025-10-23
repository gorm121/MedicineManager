package service;

import model.Medicine;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileService {

    private final String pathMedicines = "medicines.dat";
    private static final String pathSettings = getSettingsPath();


    private static String getSettingsPath() {

        String classPath = FileService.class.getProtectionDomain()
                .getCodeSource().getLocation().getPath();
        if (classPath.endsWith(".jar")) {
            return "settings.txt";
        } else {
            return "src/settings/settings.txt";
        }
    }

    public static int readSettings(){
        Path path = Path.of(pathSettings);
        try {
            String str = Files.readString(path).strip();
            return Integer.parseInt(str.substring(str.indexOf("=") + 1));

        }
        catch (Exception e) {
            System.out.println("При загрузке настроек что-то пошло не так: " + e.getMessage());
            return 10;
        }
    }

    public static void saveSettings(int pageSize){
        Path path = Path.of(pathSettings);
        try {
            Files.writeString(path,"PAGE_SIZE=" + pageSize);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    public void saveMedicines(List<Medicine> medicines) {
        try {
            byte[] data = convertToBytes(medicines);
            Files.write(Paths.get(pathMedicines), data);
            System.out.println("Лекарства сохранены в файл");
        } catch (IOException e) {
            System.out.println("Ошибка сохранения: " + e.getMessage());
        }
    }

    public List<Medicine> loadMedicines() {
        try {
            Path path = Paths.get(pathMedicines);
            if (!Files.exists(path)) {
                return new ArrayList<>();
            }

            byte[] data = Files.readAllBytes(path);
            return convertFromBytes(data);

        } catch (IOException e) {
            System.out.println("Ошибка загрузки: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private byte[] convertToBytes(List<Medicine> medicines) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             DataOutputStream dos = new DataOutputStream(baos)) {


            dos.writeInt(medicines.size());


            for (Medicine medicine : medicines) {

                dos.writeUTF(medicine.getName() != null ? medicine.getName() : "");
                dos.writeUTF(medicine.getDescription() != null ? medicine.getDescription() : "");
                dos.writeUTF(medicine.getDate() != null ? medicine.getDate().toString() : "");
                dos.writeUTF(medicine.getCategory() != null ? medicine.getCategory() : "");
                dos.writeInt(medicine.getCount());
            }

            return baos.toByteArray();
        }
    }

    private List<Medicine> convertFromBytes(byte[] data) throws IOException {
        List<String> medicinesStrings = new ArrayList<>();

        try (DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data))) {

            int size = dis.readInt();
            System.out.println("Найдено записей в файле: " + size);

            for (int i = 0; i < size; i++) {
                try {

                    String name = dis.readUTF();
                    String description = dis.readUTF();
                    String dateStr = dis.readUTF();
                    String category = dis.readUTF();
                    int count = dis.readInt();

                    String line = String.join("|", name, description, dateStr, category, String.valueOf(count));
                    medicinesStrings.add(line);

                } catch (Exception e) {
                    System.out.println("❌ Ошибка чтения лекарства " + i + ": " + e.getMessage());
                }
            }
        }
        return FromFileChecker.toListMedicine(medicinesStrings);
    }
}
