package org.hibernate.tutorials;

import org.hibernate.tutorials.model.DeliveryRequest;
import org.hibernate.tutorials.model.embeddable.Comment;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Comparator.comparing;
import static org.hibernate.HibernateUtil.getAndCast;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CollectionsMappingTest extends AbstractDaoTest {

    @Test
    public void commentsShouldBeOrderedByDateOfPublishing() {
        DeliveryRequest dr = em.find(DeliveryRequest.class, 1179L);
        List<Comment> comments = dr.getComments();

        List<Comment> sortedComments = new ArrayList<>(comments);
        sortedComments.sort(comparing(Comment::getDate));

        assertEquals(sortedComments, comments);
    }

    @Test
    public void recipientsTableEntriesShouldBeMappedToAListOfStringsInDeliveryRequest() {
        DeliveryRequest dr = em.find(DeliveryRequest.class, 1179L);
        List<String> notificationRecipients = dr.getNotificationRecipients();

        List<String> recipientsFromTable = getAndCast(em.createNativeQuery(
                "SELECT email " +
                        "FROM notification_recipients")
                .getResultList());

        assertEquals(recipientsFromTable, notificationRecipients);
    }
}