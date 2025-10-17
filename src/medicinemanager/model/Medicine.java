package model;

import java.time.LocalDate;


public class Medicine {
    private String name;
    private String description;
    private String category;
    private LocalDate date;
    private int count;

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public String getCategory() {return category;}
    public void setCategory(String category) {this.category = category;}
    public LocalDate getDate() {return date;}
    public void setDate(LocalDate date) {this.date = date;}
    public int getCount() {return count;}
    public void setCount(int count) {this.count = count;}

    public Medicine(String name,String description , LocalDate date,String category,  int count) {
        this.description = description;
        this.name = name;
        this.category = category;
        this.date = date;
        this.count = count;
    }

    @Override
    public String toString() {
        return  "Название: " + name +
                ", Описание: " + description +
                ", Категория: " + category +
                ", Срок годности до: " + date +
                ", Колличество: " + count;
    }


}

