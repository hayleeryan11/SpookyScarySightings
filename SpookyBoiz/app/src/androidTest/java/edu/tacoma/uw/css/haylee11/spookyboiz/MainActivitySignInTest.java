package edu.tacoma.uw.css.haylee11.spookyboiz;

/**
 * Created by hayleeryan on 5/25/18.
 */

import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivitySignInTest {


    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);


    @Test
    public void testLoginFragmentValid() {


        try {
            onView(withId(R.id.action_menu))
                    .perform((click()));
            onView(withText("Logout"))
                    .perform(click());

            onView(withId(R.id.username_input))
                    .perform(typeText("haylee11"));
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.password_input))
                    .perform(typeText("blahblah"));
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.sign_in))
                    .perform(click());

            onView(withText("Signed In!"))
                    .inRoot(withDecorView(not(is(
                            mActivityRule.getActivity()
                                    .getWindow()
                                    .getDecorView()))))
                    .check(matches(isDisplayed()));

        } catch (NoMatchingViewException e) {
            // View is not in hierarchy
            onView(withId(R.id.username_input))
                    .perform(typeText("haylee11"));
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.password_input))
                    .perform(typeText("blahblah"));
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.sign_in))
                    .perform(click());

            onView(withText("Signed In!"))
                    .inRoot(withDecorView(not(is(
                            mActivityRule.getActivity()
                                    .getWindow()
                                    .getDecorView()))))
                    .check(matches(isDisplayed()));
        }

    }

    @Test
    public void testLoginFragmentInvalidUsername() {

       try {
           onView(withId(R.id.action_menu))
                   .perform((click()));
           onView(withText("Logout"))
                   .perform(click());

           onView(withId(R.id.username_input))
                   .perform(typeText("notindatabase"));
           Espresso.closeSoftKeyboard();
           onView(withId(R.id.password_input))
                   .perform(typeText("blahblah"));
           Espresso.closeSoftKeyboard();
           onView(withId(R.id.sign_in))
                   .perform(click());

           onView(withText("Failed: Incorrect username."))
                   .inRoot(withDecorView(not(is(
                           mActivityRule.getActivity()
                                   .getWindow()
                                   .getDecorView()))))
                   .check(matches(isDisplayed()));
       } catch (NoMatchingViewException e) {

           onView(withId(R.id.username_input))
                   .perform(typeText("notindatabase"));
           Espresso.closeSoftKeyboard();
           onView(withId(R.id.password_input))
                   .perform(typeText("blahblah"));
           Espresso.closeSoftKeyboard();
           onView(withId(R.id.sign_in))
                   .perform(click());

           onView(withText("Failed: Incorrect username."))
                   .inRoot(withDecorView(not(is(
                           mActivityRule.getActivity()
                                   .getWindow()
                                   .getDecorView()))))
                   .check(matches(isDisplayed()));
       }



    }

    @Test
    public void testLoginFragmentInvalidPassword() {


        try {

            onView(withId(R.id.action_menu))
                    .perform((click()));
            onView(withText("Logout"))
                    .perform(click());
            onView(withId(R.id.username_input))
                    .perform(typeText("haylee11"));
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.password_input))
                    .perform(typeText("testing"));
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.sign_in))
                    .perform(click());

            onView(withText("Failed: Incorrect password."))
                    .inRoot(withDecorView(not(is(
                            mActivityRule.getActivity()
                                    .getWindow()
                                    .getDecorView()))))
                    .check(matches(isDisplayed()));
        } catch (NoMatchingViewException e) {
            onView(withId(R.id.username_input))
                    .perform(typeText("haylee11"));
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.password_input))
                    .perform(typeText("testing"));
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.sign_in))
                    .perform(click());

            onView(withText("Failed: Incorrect password."))
                    .inRoot(withDecorView(not(is(
                            mActivityRule.getActivity()
                                    .getWindow()
                                    .getDecorView()))))
                    .check(matches(isDisplayed()));
        }


    }
}
