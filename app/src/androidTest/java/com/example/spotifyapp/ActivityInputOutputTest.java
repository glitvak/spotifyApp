package com.example.spotifyapp;

import android.content.Context;

import androidx.test.espresso.Espresso;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ActivityInputOutputTest {
    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(
            SplashActivity.class);
    @Test
    public void recommendationTest(){
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
        onView(withId(R.id.loginButton)).perform(click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.localButton)).check(matches(isDisplayed()));
        onView(withId(R.id.analysis)).check(matches(withText("")));
        onView(withId(R.id.rec)).perform(click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.analysis)).check(matches(not(withText(""))));
        onView(withId(R.id.likeBtn)).perform(click());
        onView(withId(R.id.rec)).perform(click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.analysis)).check(matches(not(withText(""))));
        onView(withId(R.id.dislikeBtn)).perform(click());
    }
    @Test
    public void localPlaylistTest(){
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
        onView(withId(R.id.loginButton)).perform(click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.localButton)).check(matches(isDisplayed()));
        onView(withId(R.id.localButton)).perform(click());
        onView(withId(R.id.getLocation)).check(matches(isDisplayed()));
        onView(withId(R.id.playlistDisplay)).check(matches(withText("")));
    }
    @Test
    public void activityNavigation() {
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
        onView(withId(R.id.loginButton)).perform(click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.localButton)).check(matches(isDisplayed()));
        onView(withId(R.id.localButton)).perform(click());
        onView(withId(R.id.getLocation)).check(matches(isDisplayed()));
        Espresso.pressBack();
        onView(withId(R.id.localButton)).check(matches(isDisplayed()));
        onView(withId(R.id.historyBtn)).perform(click());
        onView(withId(R.id.tabLayout)).check(matches(isDisplayed()));
    }
}