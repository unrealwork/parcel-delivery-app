package parcel.delivery.app.common.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class WebUtil {
    public static final String BEARER = "Bearer";

    public static String bearerHeader(String token) {
        return BEARER + " " + token;
    }
}
