package com.example.project;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.Map;

public class MyContentProvider extends ContentProvider {
    private EmployeeDBHelper dbHelper;

    static final String PROVIDER_NAME = "com.demo.user.provider";
    static final Uri URL = Uri.parse("content://" + PROVIDER_NAME + "/employees");
    private static final int TOUTES_EMPLOYEES = 0;
    private static final int EMPLOYEE_UNIQUE = 1;
    private static final UriMatcher uriMatcher ;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "employees", TOUTES_EMPLOYEES);
        uriMatcher.addURI(PROVIDER_NAME, "employees/#", EMPLOYEE_UNIQUE);
    }
    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int retVal = 0;

        switch (uriMatcher.match(uri)) {
            case EMPLOYEE_UNIQUE:
                int employeeId = 0;
                try {
                    employeeId = Integer.parseInt(uri.getPathSegments().get(1));
                    retVal = dbHelper.deleteEmployee(employeeId);
                } catch (Exception ex) {
                    throw new IllegalArgumentException("Unsupported URI: " + uri);
                }
                break;
            case TOUTES_EMPLOYEES:
                retVal = dbHelper.deleteEmployees();
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        if (retVal > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return retVal;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case TOUTES_EMPLOYEES:
                // Return the MIME type for a directory of items
                return "vnd.android.cursor.dir/vnd.com.demo.user.provider.employees";
            case EMPLOYEE_UNIQUE:
                // Return the MIME type for a single item
                return "vnd.android.cursor.item/vnd.com.demo.user.provider.employees";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = dbHelper.addEmployee(values);
        if (id > 0) {
            Uri retUri = ContentUris.withAppendedId(URL, id);
            getContext().getContentResolver().notifyChange(retUri, null);
            return retUri;
        }
        return null;

    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        dbHelper = new EmployeeDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables("employees");

        switch (uriMatcher.match(uri)) {
            case TOUTES_EMPLOYEES:
                // Set projection map if needed
                Map<String, String> projectionMap = null;
                qb.setProjectionMap(projectionMap);
                break;
            case EMPLOYEE_UNIQUE:
                // Adjust query for a specific employee
                qb.appendWhere("id" + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (sortOrder == null || sortOrder.isEmpty()) {
            sortOrder = "id";
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = qb.query(db, projection, selection, selectionArgs,
                null, null, sortOrder);

        // Notify any listeners about data changes
        if (getContext() != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;

        switch (uriMatcher.match(uri)) {
            case TOUTES_EMPLOYEES:
                count = db.update("employees", values, selection, selectionArgs);
                break;
            case EMPLOYEE_UNIQUE:
                String employeeId = uri.getLastPathSegment();
                count = db.update("employees", values, "id" + " = ?",
                        new String[]{employeeId});
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (count > 0) {
            // Notify any listeners about data changes
            if (getContext() != null) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
        }

        return count;
    }
}