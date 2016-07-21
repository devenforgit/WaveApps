package pe.com.jclpsoft.waveapps.models;

import com.orm.SugarRecord;

import java.util.List;

public class Type extends SugarRecord {
    public String type;

    public List<Category> getCategories() {
        return Category.find(Category.class, "type = ?",
                String.valueOf(this.getId()));
    }

    public List<Transact> getTransactions() {
        return Transact.find(Transact.class, "type = ?",
                String.valueOf(this.getId()));
    }

    public Type(){

    }

    public Type(String type){
        this.type=type;
    }

    @Override
    public String toString() {
        return type;
    }
}
