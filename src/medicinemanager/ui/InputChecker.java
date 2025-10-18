package ui;

import javax.swing.plaf.PanelUI;
import java.time.LocalDate;
import java.util.Date;
import java.util.Scanner;

public class InputChecker {

   public int readInt(String message, int min, int max, Scanner scanner) {
        while (true) {
            try {
                System.out.print(message);
                int res = Integer.parseInt(scanner.nextLine().trim());
                if (res >= min && res <= max) return res;
                System.out.println("Число должно быть от " + min + " до " + max);
            } catch (Exception e) {
                System.out.println("Ошибка! Введите целое число.");
            }
        }
    }

   public String readString(String message, Scanner scanner) {
       while (true){
           System.out.print(message);
           String str = scanner.nextLine().strip();
           if (!str.isBlank()) return str;
           else System.out.println("Вы ввели что то не так, попробуйте еще раз");
       }
   }

   public String changeMedicineString(String message, String oldValue, Scanner scanner){
           System.out.print(message);
           String str = scanner.nextLine().trim();
           if (!str.isBlank()) return str;
           else return oldValue;
   }
    public int changeMedicineInt(String message, int oldValue, Scanner scanner){
        while (true) {
            try {
                System.out.print(message);
                int res = Integer.parseInt(scanner.nextLine().trim());
                if (res == 0) return oldValue;

                return res;
            } catch (Exception e) {
                System.out.println("Ошибка! Введите целое число.");
            }
        }
    }
    public LocalDate changeDate(String message, LocalDate oldValue, Scanner scanner) {
        while (true) {
            try {
                System.out.print(message);
                String input = scanner.nextLine().trim();
                if (input.isBlank()) return oldValue;
                return LocalDate.parse(input);
            } catch (Exception e) {
                System.out.println("Ошибка! Введите дату в формате ГГГГ-ММ-ДД");
            }
        }
    }

    public LocalDate readDate(String message, Scanner scanner) {
        while (true) {
            try {
                System.out.print(message);
                String input = scanner.nextLine().trim();
                return LocalDate.parse(input);
            } catch (Exception e) {
                System.out.println("Ошибка! Введите дату в формате ГГГГ-ММ-ДД");
            }
        }
    }

}
