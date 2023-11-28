package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class EmployeeDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="EmployeesDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_Employees ="employees";
    private static final String COLONNE_ID = "id";
    private static final String COLONNE_Nom = "nom";
    private static final String COLONNE_Age = "age";
    private static final String COLONNE_Dep = "dep";
    private static final String REQUETE_CREATION_BD ="CREATE TABLE " + TABLE_Employees + " (" +
            COLONNE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLONNE_Nom + " TEXT NOT NULL, " +
            COLONNE_Age + " INTEGER NOT NULL, " +
            COLONNE_Dep + " TEXT NOT NULL);";


    public EmployeeDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public EmployeeDBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
   sqLiteDatabase.execSQL(REQUETE_CREATION_BD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE" +
                TABLE_Employees + ";");
        onCreate(sqLiteDatabase);
    }
    public long addEmployee(String nom, int age, String dep) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLONNE_Nom, nom);
        values.put(COLONNE_Age, age);
        values.put(COLONNE_Dep, dep);

        return db.insert(TABLE_Employees, null, values);
    }

    public ArrayList<Employee> getAllEmployees() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Employee> list=new ArrayList<>();

        Cursor c= db.query(TABLE_Employees, null, null, null, null, null, null);
        if (c==null || c.getCount() == 0)

            return list;
        c.moveToFirst();
        do{
            Employee employee=new Employee();
            employee.setId(c.getInt(0));
            employee.setNom(c.getString(1));
            employee.setAge(c.getInt(2));
            employee.setDep(c.getString(3));
        }while(c.moveToNext());
        return list;
    }


    public int deleteEmployee(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_Employees, COLONNE_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public int updateEmployee(int id, String nom, int age, String dep) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLONNE_Nom, nom);
        values.put(COLONNE_Age, age);
        values.put(COLONNE_Dep, dep);

        return db.update(TABLE_Employees, values, COLONNE_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public int deleteEmployees() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("employees",null,null);
    }

    public long addEmployee(ContentValues values) {
        SQLiteDatabase db=this.getWritableDatabase();
        return db.insert(TABLE_Employees,null,values);
    }
}
