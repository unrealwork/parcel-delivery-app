package parcel.delivery.app.auth.util;

import lombok.experimental.UtilityClass;
import org.slf4j.helpers.MessageFormatter;

@UtilityClass
public class TestUtil {

    /**
     * Transform varargs to array
     *
     * @param elements varargs of some elements
     * @param <T>      type of varargs
     * @return array of elements
     */
    @SafeVarargs
    public static <T> T[] arrayOf(T... elements) {
        return elements;
    }


    /**
     * Format message using {@link MessageFormatter} from SLF4J
     *
     * @param template message template
     * @param args     variables that will be substituted into template
     * @return formatted string
     * @see MessageFormatter#arrayFormat(String, Object[])
     */
    public static String format(String template, Object... args) {
        return MessageFormatter.arrayFormat(template, args).getMessage();
    }
}
