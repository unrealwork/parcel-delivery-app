package parcel.delivery.app;

import java.text.MessageFormat;

class GreetingsUtil {
    private GreetingsUtil() {

    }

    static String greetPerson(Person person) {
        return MessageFormat.format("Hello, {0}!", person.fullName());
    }
}
