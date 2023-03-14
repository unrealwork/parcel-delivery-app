package parcel.delivery.app.common.util;

import lombok.experimental.UtilityClass;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@UtilityClass
public class DateTimeUtil {
    public static Date instantToDate(Instant instant) {
        return new Date(instant.toEpochMilli());
    }

    public static Date addDurationToDate(Date issueDate, Duration duration) {
        return new Date(issueDate.getTime() + duration.toMillis());
    }
}
