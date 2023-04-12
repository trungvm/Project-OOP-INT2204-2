package oop.bufihofa.classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class Storage {
    private int IDNOW = 1000000000;
    ObservableList<Item> itemList = FXCollections.observableArrayList();
    public Storage(){}
    public void addItemWithId(Item item){
        itemList.add(item);
        itemList.get(itemList.size()-1).setId(IDNOW++);
    }
    public void removeItemWithId(int id){
        for(int i=0; i < itemList.size(); ++i){
            if(itemList.get(i).getId() == id){
                itemList.remove(i);
                return;
            }
        }
    }
    public Item getItemWithId(int id){
        for(int i=0; i < itemList.size(); ++i){
            if(itemList.get(i).getId() == id){
                return itemList.get(i);
            }
        }
        return new Item();
    }
    public void removeItem(int index){
        if(index < itemList.size()){
            itemList.remove(index);
        }
    }
    public Item getItem(int index){
        if(index < itemList.size()){
            return itemList.get(index);
        }
        return new Item();
    }

}
