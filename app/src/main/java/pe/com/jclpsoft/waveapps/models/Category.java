package pe.com.jclpsoft.waveapps.models;

import com.orm.SugarRecord;

import java.util.List;

public class Category extends SugarRecord {
    public String category;
    public Type type;
    public List<Transact> getTransactions(){
        return Transact.find(Transact.class, "category = ?",
                String.valueOf(this.getId()));
    }
    public Category(){

    }

    public Category(String category,Type type){
        this.category=category;
        this.type=type;
    }

    @Override
    public String toString() {
        return category;
    }
}
