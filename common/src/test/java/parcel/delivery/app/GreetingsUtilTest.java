package parcel.delivery.app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GreetingsUtilTest {

    @Test
    void greetPerson() {
        Person person = new Person("John", "Doe");
        assertEquals("Hello, John Doe!", GreetingsUtil.greetPerson(person));
    }
}
