package org.hibernate.tutorials;

import org.hibernate.stat.Statistics;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class InheritanceTest extends AbstractDaoTest {

    @Before
    public void init() {
        sessionFactory.getStatistics().clear();
    }

    @Test
    public void shouldExecuteOneStatementInCaseOfSingleTableMapping() {
        em.createQuery(
                "from org.hibernate.tutorials.model.inheritance.single_table.BillingDetails")
                .getResultList();

        Statistics statistics = sessionFactory.getStatistics();
        long queryExecutionCount = statistics.getQueryExecutionCount();

        //select from single table
        assertEquals(1, queryExecutionCount);
    }

    @Test
    public void shouldExecuteOneStatementInCaseOfTablePerClassMapping() {
        em.createQuery(
                "from org.hibernate.tutorials.model.inheritance.table_per_class.BillingDetails")
                .getResultList();

        Statistics statistics = sessionFactory.getStatistics();
        long queryExecutionCount = statistics.getQueryExecutionCount();

        //select from union
        assertEquals(1, queryExecutionCount);
    }

    @Test
    public void shouldExecuteOneInCaseOfJoinedMapping() {
        em.createQuery(
                "from org.hibernate.tutorials.model.inheritance.joined.BillingDetails")
                .getResultList();

        Statistics statistics = sessionFactory.getStatistics();
        long queryExecutionCount = statistics.getQueryExecutionCount();

        //select from left outer join
        assertEquals(1, queryExecutionCount);
    }

    @Test
    public void shouldExecuteOneInCaseOfMixedMapping() {
        em.createQuery(
                "from org.hibernate.tutorials.model.inheritance.mixed.BillingDetails")
                .getResultList();

        Statistics statistics = sessionFactory.getStatistics();
        long queryExecutionCount = statistics.getQueryExecutionCount();

        //select from left outer join
        assertEquals(1, queryExecutionCount);
    }
}
