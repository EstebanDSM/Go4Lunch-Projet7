package com.guzzler.go4lunch_p7;

import android.content.Context;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.guzzler.go4lunch_p7.ui.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class InstrumentedTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void useAppContext() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.guzzler.go4lunch_p7", appContext.getPackageName());
    }


    @Test
    public void verifyMapIsFirstFragment() {
        onView(ViewMatchers.withId(R.id.map)).check(matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void verifyListFragmentDisplayed() {
        onView(ViewMatchers.withId(R.id.navigation_list)).perform(click());
        onView(ViewMatchers.withId(R.id.rest_list)).check(matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void verifyWorkmatesFragmentDisplayed() {
        onView(ViewMatchers.withId(R.id.navigation_workmates)).perform(click());
        onView(ViewMatchers.withId(R.id.list_workmates)).check(matches(ViewMatchers.isDisplayed()));
    }


    @Test
    public void verifyShowSettingsWorks() {
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());
        onView(ViewMatchers.withId(R.id.nav_settings)).perform(click());
        onView(ViewMatchers.withId(R.id.settings_switch)).check(matches(ViewMatchers.isDisplayed()));
    }

}