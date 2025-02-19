package ru.gr26x.db;

import java.util.List;

public class MainDao extends Dao {
    public static int add(MainTable entity) {
        return 0;
    }

    public static boolean update(MainTable entity) {
        return false;
    }

    public static void delete(int id) {
    }

    public static List<MainTable> findAll() {
        return (List<MainTable>) execute((session) -> session.createQuery("from MainTable", MainTable.class).list());
    }

    public static List<MainTable> findAllById(int id) {
        return null;
    }
}
