package sample;

public class Book {
    protected long ISBN13;
    protected String title;
    protected String author;
    protected String genre;
    protected String publisher_name;
    protected float publisher_cut;
    protected int pages;
    protected float price;
    protected int inventory;
    protected int minInventory;

    public Book() {

    }

    public Book (long ISBN, String a, String t, String g, String pn, float pc, float p, int num_pages, int inv) {
        ISBN13 = ISBN;
        author = a;
        title = t;
        genre = g;
        publisher_name = pn;
        publisher_cut = pc;
        price = p;
        pages = num_pages;
        inventory = inv;
    }

    public Book (long ISBN, String a, String t, String g, String pn, float pc, float p, int num_pages, int inv, int minInv) {
        ISBN13 = ISBN;
        author = a;
        title = t;
        genre = g;
        publisher_name = pn;
        publisher_cut = pc;
        price = p;
        pages = num_pages;
        inventory = inv;
        minInventory = minInv;
    }
}
