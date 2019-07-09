package org.hibernate.utils;

import lombok.AllArgsConstructor;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@AllArgsConstructor
public class HibernateUtil {

    private EntityManagerFactory emf;

    public static <T> List<T> getAndCast(List list) {
        //noinspection unchecked
        return (List<T>) list;
    }
}
