package com.guzzler.go4lunch_p7;

import com.guzzler.go4lunch_p7.models.Booking;
import com.guzzler.go4lunch_p7.models.Workmate;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTest {

    private Workmate workmate;
    private Booking booking;

    @Before
    public void setUp() {
        workmate = new Workmate("123456789", "photoURL", "esteban");
        booking = new Booking("22-03-21", "123456789", "000000", "rialto");
    }

    @Test
    public void testUserData() {
        assertEquals("123456789", workmate.getUid());
        assertEquals("esteban", workmate.getName());
        assertEquals("photoURL", workmate.getUrlPicture());
        assertFalse(workmate.isNotification());
    }

    @Test
    public void testBookingData() {
        assertEquals("22-03-21", booking.getBookingDate());
        assertEquals("123456789", booking.getUserId());
        assertEquals("000000", booking.getRestaurantId());
        assertEquals("rialto", booking.getRestaurantName());
    }

}