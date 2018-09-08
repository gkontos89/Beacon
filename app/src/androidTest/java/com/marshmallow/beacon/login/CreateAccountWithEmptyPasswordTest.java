package com.marshmallow.beacon.login;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.marshmallow.beacon.R;
import com.marshmallow.beacon.ui.user.LoginActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by George on 9/8/2018.
 */

@RunWith(AndroidJUnit4.class)
public class CreateAccountWithEmptyPasswordTest {

    @Rule
    public ActivityTestRule<LoginActivity> loginActivityActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void createAccountWithEmptyPassword() {
        onView(withId(R.id.email_text)).perform(typeText("test@gmail.com"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.create_account_button)).perform(click());
        onView(withId(R.id.password_text)).check(matches(hasErrorText("Empty Password Not Allowed")));
    }
}
