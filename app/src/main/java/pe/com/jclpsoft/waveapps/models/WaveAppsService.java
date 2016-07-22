package pe.com.jclpsoft.waveapps.models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.orm.SugarContext;
import com.orm.SugarDb;
import java.lang.reflect.Field;
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


    /*FOR TRANSACTIONS*/
    public boolean addNewTransaction(Transact transact) {
        return transact.save() > 0;
    }

    public List<Transact> listAllTransactions(){
        return Transact.listAll(Transact.class);
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
                    "SELECT SUM(AMOUNT) AS result_amount FROM TRANSACT WHERE TYPE=1", null);
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
                    "SELECT SUM(AMOUNT) AS result_expense FROM TRANSACT WHERE TYPE=2", null);
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
