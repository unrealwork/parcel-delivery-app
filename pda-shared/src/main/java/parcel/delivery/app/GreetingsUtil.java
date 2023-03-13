package parcel.delivery.app;

import java.text.MessageFormat;

final class GreetingsUtil {
    private GreetingsUtil() {

    }

    public static String greetPerson(Person person) {
        return MessageFormat.format("Hello, {0}!", person.fullName());
    }
}
