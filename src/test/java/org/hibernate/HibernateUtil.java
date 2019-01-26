package org.hibernate;

import java.util.List;

public interface HibernateUtil<T> {

    static <T> List<T> getAndCast(List list) {
        //noinspection unchecked
        return (List<T>) list;
    }

    default void executeInit() {
        List<T> td = prepareTestData();
        storeTestData(td);
    }

    List<T> prepareTestData();

    void storeTestData(List<T> data);
}
