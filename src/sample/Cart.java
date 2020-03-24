package sample;

import java.util.HashMap;

public class Cart {
    HashMap<Long, Integer> storage;

    public Cart(){
        storage = new HashMap<>();
    }

    public void add(Long ISBN, Integer quantity){
        storage.put(ISBN, quantity);
    }

    public void removeAll(){
        storage.clear();
    }

    public Integer size(){
        return storage.size();
    }
}
