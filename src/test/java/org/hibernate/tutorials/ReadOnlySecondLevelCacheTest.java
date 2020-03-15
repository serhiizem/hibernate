package org.hibernate.tutorials;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.stat.Statistics;
import org.hibernate.tutorials.model.Bill;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

public class ReadOnlySecondLevelCacheTest extends AbstractDaoTest {

    @Before
    public void init() {
        sessionFactory.getStatistics().clear();
    }

    @Test
    public void test() {
        Statistics stats = sessionFactory.getStatistics();
        Session session = sessionFactory.openSession();
        Session otherSession = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Transaction otherTransaction = otherSession.beginTransaction();

        printStats(stats, 0);

        // bill is loaded into first and second level caches
        Bill bill = session.load(Bill.class, 1200L);
        printData(bill, stats, 1);

        // bill is retrieved from 1st level cache
        bill = session.load(Bill.class, 1200L);
        printData(bill, stats, 2);

        session.evict(bill);
        // bill is removed from 1st level cache, so it will be found in second level cache
        bill = session.load(Bill.class, 1200L);
        printData(bill, stats, 3);

        bill = session.load(Bill.class, 1201L);
        printData(bill, stats, 4);

        bill = otherSession.load(Bill.class, 1200L);
        printData(bill, stats, 5);

        transaction.commit();
        otherTransaction.commit();
    }

    private static void printData(Bill emp, Statistics stats, int count) {
        System.out.println(count + ":: Amount = " + emp.getAmount() + ", Title = " + emp.getTitle());
        printStats(stats, count);
    }

    private static void printStats(Statistics stats, int i) {
        System.out.println("***** " + i + " *****");
        System.out.println("Fetch Count = " + stats.getEntityFetchCount());
        System.out.println("Second Level Hit Count = " + stats.getSecondLevelCacheHitCount());
        System.out.println("Second Level Miss Count = " + stats.getSecondLevelCacheMissCount());
        System.out.println("Second Level Put Count = " + stats.getSecondLevelCachePutCount());
    }
}
