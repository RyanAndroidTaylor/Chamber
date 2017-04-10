package com.dtp;

import java.util.List;

/**
 * Created by ner on 4/6/17.
 */

public class DataConnection {
    //TODO Implement upsert
    //TODO Implement Async getAndClose and doAndClose
    //TODO Implement Async with RxJava
    //TODO Implement table watchers with RxJava

    private static DatabaseManager databaseManager;

    private final Database database;

    private DataConnection(Database database) {
        this.database = database;
    }

    public static void init(DatabaseManager databaseManager) {
        DataConnection.databaseManager = databaseManager;
    }

    public static void doAndClose(DoAndClose block) {
        block.doAndClose(openConnection());
    }

    public static <T> T getAndClose(GetAndCloseBlock<T> block) {
        return block.getAndClose(openConnection());
    }

    private static DataConnection openConnection() {
        return new DataConnection(databaseManager.beginTransaction());
    }

    public void insert(DataTable dataTable) {
        Database database = databaseManager.beginTransaction();

        insert(dataTable, database);

        databaseManager.endTransaction();
    }

    public void insertAll(List<DataTable> dataTables) {
        Database database = databaseManager.beginTransaction();

        for (DataTable dataTable : dataTables) {
            insert(dataTable, database);
        }

        databaseManager.endTransaction();
    }

    private void insert(DataTable dataTable, Database database) {
        database.insert(dataTable);
    }

    public void update(DataTable dataTable) {
        Database database = databaseManager.beginTransaction();

        update(dataTable, database);

        databaseManager.endTransaction();
    }

    public void updateAll(List<DataTable> dataTables) {
        Database database = databaseManager.beginTransaction();

        for (DataTable dataTable : dataTables) {
            update(dataTable, database);
        }

        databaseManager.endTransaction();
    }

    private void update(DataTable dataTable, Database database) {
        database.update(dataTable);
    }

    public void delete(DataTable dataTable) {
        Database database = databaseManager.beginTransaction();

        delete(dataTable, database);

        databaseManager.endTransaction();
    }

    public void delete(List<DataTable> dataTables) {
        Database database = databaseManager.beginTransaction();

        for (DataTable dataTable : dataTables) {
            delete(dataTable, database);
        }

        databaseManager.endTransaction();
    }

    private void delete(DataTable dataTable, Database database) {
        database.delete(dataTable);
    }

    public interface DoAndClose {
        void doAndClose(DataConnection dataConnection);
    }

    public interface GetAndCloseBlock<T> {
        T getAndClose(DataConnection dataConnection);
    }
}
