package sample;

public class Book {
    long ISBN;
    String author;
    String title;
    String genre;
    String publisher_name;
    Integer publisher_cut;
    Float price;
    Integer num_pages;
    Integer inventory;
    Integer min_inventory;


    public Book(long ISBN, String author, String title, String genre, String publisher_name, Integer publisher_cut, Float price, Integer num_pages, Integer inventory, Integer min_inventory){
        this.ISBN = ISBN;
        this.author = author;
        this.title = title;
        this.genre = genre;
        this.publisher_name = publisher_name;
        this.publisher_cut = publisher_cut;
        this.price = price;
        this.num_pages = num_pages;
        this.inventory = inventory;
        this.min_inventory = min_inventory;
    }

    public void addQuantity(Integer quantity){
        this.inventory+=quantity;
    }

    public void removeQuantity(Integer quantity){
        this.inventory-=quantity;
    }
}
