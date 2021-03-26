package com.guzzler.go4lunch_p7;

import com.guzzler.go4lunch_p7.models.Booking;
import com.guzzler.go4lunch_p7.models.Workmate;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ModelsUnitTest {

    private Workmate workmate;
    private Booking booking;

    @Before
    public void setUp() {
        workmate = new Workmate("123456789", "photoURL", "esteban");
        booking = new Booking("22-03-21", "123456789", "000000", "rialto");
    }

    @Test
    public void testGetWorkmatesData() {
        assertEquals("123456789", workmate.getUid());
        assertEquals("esteban", workmate.getName());
        assertEquals("photoURL", workmate.getUrlPicture());
        assertFalse(workmate.isNotification());


    }

    @Test
    public void testGetBookingData() {
        assertEquals("22-03-21", booking.getBookingDate());
        assertEquals("123456789", booking.getUserId());
        assertEquals("000000", booking.getRestaurantId());
        assertEquals("rialto", booking.getRestaurantName());
    }

    @Test
    public void testSetBookingData() {

        booking.setBookingDate("11-11-11");
        booking.setRestaurantId("123456");
        booking.setRestaurantName("la grange");
        booking.setUserId("111111");

        assertEquals("11-11-11", booking.getBookingDate());
        assertEquals("111111", booking.getUserId());
        assertEquals("123456", booking.getRestaurantId());
        assertEquals("la grange", booking.getRestaurantName());

    }

    @Test
    public void testSetWorkmateData() {
        workmate.setName("pascal");
        workmate.setUid("666666");
        workmate.setUrlPicture("photo.com");
        workmate.setNotification(true);

        assertEquals("pascal", workmate.getName());
        assertEquals("666666", workmate.getUid());
        assertEquals("photo.com", workmate.getUrlPicture());
        assertTrue(workmate.isNotification());

    }

}