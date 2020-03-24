
package sample;

public class Book {
    protected long ISBN;
    protected String title;
    protected String author;
    protected String genre;
    protected String publisher_name;
    protected float publisher_cut;
    protected int pages;
    protected float price;
    protected int inventory;
    protected int min_inventory;

    public Book() {

    }

    public Book (long ISB, String a, String t, String g, String pn, float pc, float p, int num_pages, int inv, int min_inv) {
        ISBN = ISB;
        author = a;
        title = t;
        genre = g;
        publisher_name = pn;
        publisher_cut = pc;
        price = p;
        pages = num_pages;
        inventory = inv;
        min_inventory = min_inv;
    }

    public long getISBN() {
        return ISBN;
    }

    public String getTitle() {
        return title;
    }

}
