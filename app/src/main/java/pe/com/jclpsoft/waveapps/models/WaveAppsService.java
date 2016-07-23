package pe.com.jclpsoft.waveapps.models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.orm.SugarContext;
import com.orm.SugarDb;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class WaveAppsService{

    private Type expense=new Type("Expense");
    private Type income=new Type("Income");

    /*FOR CATEGORIES*/
    public boolean addNewCategory(Category category) {
        return category.save() > 0;
    }

    public List<Category> listAllCategories() {
        return Category.listAll(Category.class);
    }

    public long getTotalCategories() {
        return Category.count(Category.class);
    }

    public Category findCategoryById(int id){
        Category category=new Category();
        try {
            Cursor cursor = getDatabase().rawQuery("SELECT * FROM CATEGORY WHERE ID="+id, null);
            if(cursor.moveToFirst()){
                category.category=cursor.getString(cursor.getColumnIndex("CATEGORY"));
                category.setId(Long.valueOf(id));
            }
        }
        catch(NullPointerException e) {
            e.printStackTrace();
        }
        return category;
    }

    /*FOR TYPES*/
    public boolean addNewType(Type type) {
        return type.save() > 0;
    }

    public List<Type> listAllTypes(){
        return Type.listAll(Type.class);
    }

    public long getTotalTypes() {
        return Type.count(Type.class);
    }

    public Type findTypeById(int id){
        Type type=new Type();
        try {
            Cursor cursor = getDatabase().rawQuery("SELECT * FROM TYPE WHERE ID="+id, null);
            if(cursor.moveToFirst()){
                type.type=cursor.getString(cursor.getColumnIndex("TYPE"));
                type.setId(Long.valueOf(id));
            }
        }
        catch(NullPointerException e) {
            e.printStackTrace();
        }
        return type;
    }


    /*FOR TRANSACTIONS*/
    public boolean addNewTransaction(Transact transact) {
        return transact.save() > 0;
    }

    public List<Transact> listAllTransactions(){
        /*return Transact.listAll(Transact.class);*/
        /*List<Transact> list=new ArrayList<>();
        try {
            Cursor cursor = getDatabase().rawQuery("SELECT *  FROM TRANSACT WHERE ENABLE=1", null);
            int numRows=cursor.getCount();
            cursor.moveToFirst();
            for (int i=0;i<numRows;i++){
                Transact transact=new Transact();
                transact.date=cursor.getString(cursor.getColumnIndex("DATE"));
                transact.amount=cursor.getFloat(cursor.getColumnIndex("AMOUNT"));
                transact.enable=cursor.getInt(cursor.getColumnIndex("ENABLE"));
                transact.state=cursor.getInt(cursor.getColumnIndex("STATE"));
                transact.type=findTypeById(cursor.getInt(cursor.getColumnIndex("TYPE")));
                transact.category=findCategoryById(cursor.getInt(cursor.getColumnIndex("CATEGORY")));
                transact.description=cursor.getString(cursor.getColumnIndex("DESCRIPTION"));
                transact.url=cursor.getString(cursor.getColumnIndex("URL"));
                list.add(transact);
                cursor.moveToNext();
            }
        }
        catch(NullPointerException e) {
            e.printStackTrace();
        }
        return list;*/
        List<Transact> transacts=Select.from(Transact.class).where(Condition.prop("enable").eq(1)).orderBy("id").list();
        Collections.reverse(transacts);
        return transacts;
    }

    public void deleteTransaction(int id){
        Transact transact=Transact.findById(Transact.class,id);
        transact.enable=0;
        transact.save();
    }

    public Transact obtainTransactById(int id){
        return Transact.findById(Transact.class,id);
    }

    public void updateTransaction(Transact transact){
        transact.save();
    }


    public long getTotalTransactions() {
        return Transact.count(Transact.class);
    }

    /*GENERAL*/
    public void initDatabase(){
        if(listAllTypes().size()==0 && listAllCategories().size()==0) {
            addNewType(income);
            addNewType(expense);
            addNewCategory(new Category("Income & Bonuses", income));
            addNewCategory(new Category("Interest Income", income));
            addNewCategory(new Category("Home Phone", expense));
            addNewCategory(new Category("Insurance – Health", expense));
            addNewCategory(new Category("Insurance – Property", expense));
            addNewCategory(new Category("Insurance – Vehicles", expense));
            addNewCategory(new Category("Insurance – Life & Disability", expense));
            addNewCategory(new Category("Internet", expense));
            addNewCategory(new Category("Lawn & Garden", expense));
            addNewCategory(new Category("Miscellaneous Expense", expense));
            addNewCategory(new Category("Mobile Phone", expense));
            addNewCategory(new Category("Mortgage or Rent", expense));
            addNewCategory(new Category("Other Transportation", expense));
            addNewCategory(new Category("Parking", expense));
            addNewCategory(new Category("Personal Items", expense));
            addNewCategory(new Category("Pets", expense));
            addNewCategory(new Category("Pharmacy & Prescriptions", expense));
            addNewCategory(new Category("Public Transportation", expense));
            addNewCategory(new Category("Rental Car & Taxi", expense));
            addNewCategory(new Category("Restaurants, Coffee & Bars", expense));
            addNewCategory(new Category("Subscriptions", expense));
            addNewCategory(new Category("Television", expense));
            addNewCategory(new Category("Travel & Vacation", expense));
            addNewCategory(new Category("Utilities", expense));
            addNewCategory(new Category("Vehicle – Fuel", expense));
            addNewCategory(new Category("Vehicle – Repairs & Maintenance", expense));
        }
    }

    private SQLiteDatabase getDatabase() {
        try {
            Field f = SugarContext.getSugarContext().getClass().getDeclaredField("sugarDb");
            f.setAccessible(true);
            return ((SugarDb) f.get(SugarContext.getSugarContext())).getDB();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public float incomeThisMonth() {
        try {
            Cursor cursor = getDatabase().rawQuery(
                    "SELECT SUM(AMOUNT) AS result_amount FROM TRANSACT WHERE TYPE=1 AND ENABLE=1", null);
            return (cursor.moveToFirst() ?
                    cursor.getFloat(cursor.getColumnIndex("result_amount")) :
                    0);
        }
        catch(NullPointerException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public float expenseThisMonth() {
        try {
            Cursor cursor = getDatabase().rawQuery(
                    "SELECT SUM(AMOUNT) AS result_expense FROM TRANSACT WHERE TYPE=2 AND ENABLE=1", null);
            return (cursor.moveToFirst() ?
                    cursor.getFloat(cursor.getColumnIndex("result_expense")) :
                    0);
        }
        catch(NullPointerException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public float netIncomeThisMonth(){
        return incomeThisMonth()-expenseThisMonth();
    }
}
