package org.hibernate.tutorials;

import lombok.*;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class SerializationTest {

    @Test
    @SneakyThrows
    public void test() {
        FileInputStream fileInputStream = new FileInputStream("ser-result.txt");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Person personLoaded = (Person) objectInputStream.readObject();
        objectInputStream.close();

        assertSame(20, personLoaded.getAge());
        assertEquals("Joe", personLoaded.getName());
    }
}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class Person implements Serializable {
    private static final long serialVersionUID = 1L;

    private String address;
    private String name;
    private int age;
}
