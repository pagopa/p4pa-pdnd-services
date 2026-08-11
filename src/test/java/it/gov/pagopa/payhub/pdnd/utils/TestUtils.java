package it.gov.pagopa.payhub.pdnd.utils;

import java.util.Locale;
import java.util.TimeZone;

public class TestUtils {

    private TestUtils() {}

    static {
        clearDefaultTimezone();
        clearLocale();
    }

    public static void clearDefaultTimezone() {
        TimeZone.setDefault(Constants.DEFAULT_TIMEZONE);
    }

    public static void clearLocale() {
        Locale.setDefault(Locale.ITALY);
    }
}
