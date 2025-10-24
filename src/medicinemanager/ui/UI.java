package ui;

public class UI {
    public static void showMainMenu(){
        System.out.println("===  ДОМАШНЯЯ АПТЕЧКА ===");
        System.out.println("1.  Показать все лекарства");
        System.out.println("2.  Управление лекарствами");
        System.out.println("3.  Переименовать категорию");
        System.out.println("4.  Удалить категорию");
        System.out.println("5.  Настройки");
        System.out.println("0.  Выход");

    }

    public static void showMedicinesMenu(){
        System.out.println("===  СПИСОК ЛЕКАРСТВ ===");
        System.out.println("1.  Показать все");
        System.out.println("2.  Показать по категориям");
        System.out.println("3.  Сортировать по названию (А-Я)");
        System.out.println("4.  Сортировать по сроку годности");
        System.out.println("5.  Показать просроченные");
        System.out.println("0.  Назад");

    }

    public static void showMenuForManagementMedicines(){
        System.out.println("===  Управление лекарствами ===");
        System.out.println("1.  Добавить лекарство");
        System.out.println("2.  Найти лекарство");
        System.out.println("3.  Изменить лекарство");
        System.out.println("4.  Удалить лекарство");
        System.out.println("0.  Назад");

    }
}
