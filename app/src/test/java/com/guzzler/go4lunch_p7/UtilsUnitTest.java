package com.guzzler.go4lunch_p7;

import com.guzzler.go4lunch_p7.utils.notifications.MakeMessage;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class UtilsUnitTest {

    private final List<String> workmatesList = new ArrayList<>();

    @Before
    public void setUp() {
        workmatesList.add("rachel");
        workmatesList.add("stef");
        workmatesList.add("enzo");
        workmatesList.add("livio");
        workmatesList.add("emma");
    }

    @Test
    public void testMakeMessage() {
        assertEquals(MakeMessage.makeMessage(workmatesList).toString(), "rachel, stef, enzo, livio, emma.");
    }
}

