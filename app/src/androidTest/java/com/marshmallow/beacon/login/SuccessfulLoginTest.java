package com.marshmallow.beacon.login;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import com.marshmallow.beacon.R;
import com.marshmallow.beacon.ui.user.LoginActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by George on 9/8/2018.
 */

@RunWith(AndroidJUnit4.class)
public class SuccessfulLoginTest {

    @Rule
    public ActivityTestRule<LoginActivity> loginActivityActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void successfulLogin() {
        onView(ViewMatchers.withId(R.id.email_text)).perform(typeText("test@gmail.com"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.password_text)).perform(typeText("heyoheyo"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.sign_in_button)).perform(click());
    }
}
