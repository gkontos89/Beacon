package com.marshmallow.beacon.login;

import android.os.SystemClock;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.marshmallow.beacon.R;
import com.marshmallow.beacon.ui.marketing.SponsorsActivity;
import com.marshmallow.beacon.ui.user.LoginActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by George on 9/13/2018.
 */
@RunWith(AndroidJUnit4.class)
public class SponsorActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> loginActivityActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void navigateToSponsorsPage() {
        // Login
        onView(ViewMatchers.withId(R.id.email_text)).perform(typeText("test@gmail.com"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.password_text)).perform(typeText("heyoheyo"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.sign_in_button)).perform(click());

        // Wait till login is complete...
        SystemClock.sleep(20000);

        // Navigate to sponsors page
        onView(withId(R.id.sponsors_button)).perform(click());

        SystemClock.sleep(5000);
    }
}
