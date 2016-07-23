package pe.com.jclpsoft.waveapps.models;

import com.orm.SugarRecord;

public class Transact extends SugarRecord {
    public String date;
    public String description;
    public float amount;
    public String url;
    public Category category;
    public Type type;
    public int enable;
    public int state;

    public Transact(){

    }

    public Transact(String date, String description, float amount, String url, Category category, Type type,int enable,int state){
        this.date=date;
        this.description=description;
        this.amount=amount;
        this.url=url;
        this.category=category;
        this.type=type;
        this.enable=enable;
        this.state=state;
    }
}
