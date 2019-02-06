package org.hibernate.tutorials;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.tutorials.model.*;
import org.hibernate.tutorials.model.embeddable.Comment;
import org.junit.Test;

import javax.persistence.PersistenceException;
import java.util.*;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toMap;
import static org.hibernate.HibernateUtil.getAndCast;
import static org.hibernate.tutorials.model.ContactMethod.fromString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        Set<String> notificationRecipients = dr.getNotificationRecipients();

        Set<String> recipientsFromTable = new HashSet<>(getAndCast(em.createNativeQuery(
                "SELECT email " +
                        "FROM notification_recipients")
                .getResultList()));

        assertEquals(recipientsFromTable, notificationRecipients);
    }

    @Test
    public void contactMethodsUserPropertyShouldCorrespondToValuesFromContactMethodsTable() {
        Long userId = 2L;
        User user = em.find(User.class, userId);
        Map<ContactMethod, String> contactMethods = user.getContactMethods();

        Map<ContactMethod, String> contactMethodsFromTable = jdbcTemplate.query(
                "SELECT method_type, value " +
                        "FROM contact_methods " +
                        "WHERE user_id = ?",
                new Object[]{userId},
                (rs, rowNum) -> {
                    ContactMethod contactMethod = fromString(rs.getString("method_type"));
                    String value = rs.getString("value");
                    return new ContactMethodMapping(contactMethod, value);
                })
                .stream()
                .collect(toMap(
                        ContactMethodMapping::getContactMethod,
                        ContactMethodMapping::getValue));

        assertEquals(contactMethodsFromTable, contactMethods);
    }

    @Test
    public void shouldThrowConstraintViolationExceptionWhenAddingEntityWithDuplicateFilenameForTheSameUser() {
        assertThrows(ConstraintViolationException.class, () -> {
            try {
                User user = em.find(User.class, 1L);
                FileName duplicateFilename = this.generateDuplicateKeyFilename();
                user.getUserContracts().put(duplicateFilename, new UserContract());
                em.persist(user);
                em.flush();
            } catch (PersistenceException e) {
                throw e.getCause();
            }
        });
    }

    @Test
    public void shouldAllowDuplicateImagesToBeAddedToOrderImagesCollection() {
        Image image = new Image("test_image", "ftp://ftp://location", 150, 200);
        Image duplicate = new Image("test_image", "ftp://ftp://location", 150, 200);

        Order order = em.find(Order.class, 1L);

        order.getOrderImages().add(image);
        order.getOrderImages().add(duplicate);

        em.persist(order);
        em.flush();
    }

    private FileName generateDuplicateKeyFilename() {
        return new FileName("contract112234", "doc");
    }

    @Getter
    @AllArgsConstructor
    private class ContactMethodMapping {
        private ContactMethod contactMethod;
        private String value;
    }
}
