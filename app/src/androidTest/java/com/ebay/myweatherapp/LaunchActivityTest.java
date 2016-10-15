package com.ebay.myweatherapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ebay.myweatherapp.launchactivity.LaunchActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class LaunchActivityTest {
    ViewInteraction searchFragment;


    @Rule
    public ActivityTestRule<LaunchActivity> mActivityTestRule = new ActivityTestRule<>(LaunchActivity.class);


    @Test
    public void placeNameTest() throws Exception {
        onView( allOf(withId(R.id.textViewPlaceName), isDisplayed()));
    }
    @Test
    public void weatherConditionTest() throws Exception {
        onView( allOf(withId(R.id.textViewWeatherCondition), isDisplayed()));
    }
    @Test
    public void temperatureTest() throws Exception {
        onView( allOf(withId(R.id.textViewTemperature), isDisplayed()));
    }
    @Test
    public void googleAutoPlaceVisibleTest(){
        searchFragment = onView(allOf(withId(R.id.place_autocomplete_search_input), isDisplayed()));
    }
}