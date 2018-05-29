package edu.tacoma.uw.css.haylee11.spookyboiz;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

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

/**
 * Created by hayleeryan on 5/28/18.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityCreateAccountTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    public String getEmail() {
        Random random = new Random();
        return "e" + (random.nextInt(1000) + 1) + (random.nextInt(900) + 1)
                + (random.nextInt(700) + 1) + (random.nextInt(400) + 1)
                + (random.nextInt(100) + 1) + "@uw.edu";
    }

    public String getUsername() {
        Random random = new Random();
        return  "u" + (random.nextInt(1000) + 1) + (random.nextInt(900) + 1)
                + (random.nextInt(700) + 1) + (random.nextInt(400) + 1)
                + (random.nextInt(100) + 1);
    }

    public void inputData(String f, String l, String u, String e, String p, String c) {

        onView(withId(R.id.f_name))
                .perform(typeText(f));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.l_name))
                .perform(typeText(l));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.username))
                .perform(typeText(u));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.email))
                .perform(typeText(e));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.password))
                .perform(typeText(p));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.confirm))
                .perform(typeText(c));
        Espresso.closeSoftKeyboard();
    }

    @Test
    public void testCreateAccountValid() {


        try {
            onView(withId(R.id.action_menu))
                    .perform((click()));
            onView(withText("Logout"))
                    .perform(click());

            onView(withId(R.id.create))
                    .perform(click());

            inputData("mike", "smith", getUsername(), getEmail(), "testing", "testing");

            onView(withId(R.id.create))
                    .perform(click());

            onView(withText("Account Created!"))
                    .inRoot(withDecorView(not(is(
                            mActivityRule.getActivity()
                                    .getWindow()
                                    .getDecorView()))))
                    .check(matches(isDisplayed()));

        } catch (NoMatchingViewException e) {
            // View is not in hierarchy
            onView(withId(R.id.create))
                    .perform(click());


            inputData("mike", "smith", getUsername(), getEmail(), "testing", "testing");

            onView(withId(R.id.create))
                    .perform(click());

            onView(withText("Account Created!"))
                    .inRoot(withDecorView(not(is(
                            mActivityRule.getActivity()
                                    .getWindow()
                                    .getDecorView()))))
                    .check(matches(isDisplayed()));
        }

    }

    @Test
    public void testCreateAccountInvalidEmail() {


        try {
            onView(withId(R.id.action_menu))
                    .perform((click()));
            onView(withText("Logout"))
                    .perform(click());

            onView(withId(R.id.create))
                    .perform(click());

            inputData("mike", "smith", getUsername(), "noemail", "testing", "testing");

            onView(withId(R.id.create))
                    .perform(click());

            onView(withText("Failed: Please enter a valid email."))
                    .inRoot(withDecorView(not(is(
                            mActivityRule.getActivity()
                                    .getWindow()
                                    .getDecorView()))))
                    .check(matches(isDisplayed()));

        } catch (NoMatchingViewException e) {
            // View is not in hierarchy
            onView(withId(R.id.create))
                    .perform(click());


            inputData("mike", "smith", getUsername(), "noemail", "testing", "testing");

            onView(withId(R.id.create))
                    .perform(click());

            onView(withText("Failed: Please enter a valid email."))
                    .inRoot(withDecorView(not(is(
                            mActivityRule.getActivity()
                                    .getWindow()
                                    .getDecorView()))))
                    .check(matches(isDisplayed()));
        }

    }

    @Test
    public void testCreateAccountEmailInUse() {


        try {
            onView(withId(R.id.action_menu))
                    .perform((click()));
            onView(withText("Logout"))
                    .perform(click());

            onView(withId(R.id.create))
                    .perform(click());

            inputData("mike", "smith", getUsername(), "haylee11@uw.edu", "testing", "testing");

            onView(withId(R.id.create))
                    .perform(click());

            onView(withText("Failed: Email already used"))
                    .inRoot(withDecorView(not(is(
                            mActivityRule.getActivity()
                                    .getWindow()
                                    .getDecorView()))))
                    .check(matches(isDisplayed()));

        } catch (NoMatchingViewException e) {
            // View is not in hierarchy
            onView(withId(R.id.create))
                    .perform(click());


            inputData("mike", "smith", getUsername(), "haylee11@uw.edu", "testing", "testing");

            onView(withId(R.id.create))
                    .perform(click());

            onView(withText("Failed: Email already used."))
                    .inRoot(withDecorView(not(is(
                            mActivityRule.getActivity()
                                    .getWindow()
                                    .getDecorView()))))
                    .check(matches(isDisplayed()));
        }

    }

    @Test
    public void testCreateAccountUsernameTaken() {


        try {
            onView(withId(R.id.action_menu))
                    .perform((click()));
            onView(withText("Logout"))
                    .perform(click());

            onView(withId(R.id.create))
                    .perform(click());

            inputData("mike", "smith", "haylee11", getEmail(), "testing", "testing");

            onView(withId(R.id.create))
                    .perform(click());

            onView(withText("Failed: Username already taken."))
                    .inRoot(withDecorView(not(is(
                            mActivityRule.getActivity()
                                    .getWindow()
                                    .getDecorView()))))
                    .check(matches(isDisplayed()));

        } catch (NoMatchingViewException e) {
            // View is not in hierarchy
            onView(withId(R.id.create))
                    .perform(click());


            inputData("mike", "smith", "haylee11", getEmail(), "testing", "testing");

            onView(withId(R.id.create))
                    .perform(click());

            onView(withText("Failed: Username already taken."))
                    .inRoot(withDecorView(not(is(
                            mActivityRule.getActivity()
                                    .getWindow()
                                    .getDecorView()))))
                    .check(matches(isDisplayed()));
        }

    }

    @Test
    public void testCreateAccountUsernameEmpty() {


        try {
            onView(withId(R.id.action_menu))
                    .perform((click()));
            onView(withText("Logout"))
                    .perform(click());

            onView(withId(R.id.create))
                    .perform(click());

            inputData("mike", "smith", "", getEmail(), "testing", "testing");

            onView(withId(R.id.create))
                    .perform(click());

            onView(withText("Failed: Please enter a username."))
                    .inRoot(withDecorView(not(is(
                            mActivityRule.getActivity()
                                    .getWindow()
                                    .getDecorView()))))
                    .check(matches(isDisplayed()));

        } catch (NoMatchingViewException e) {
            // View is not in hierarchy
            onView(withId(R.id.create))
                    .perform(click());


            inputData("mike", "smith", "", getEmail(), "testing", "testing");

            onView(withId(R.id.create))
                    .perform(click());

            onView(withText("Failed: Please enter a username."))
                    .inRoot(withDecorView(not(is(
                            mActivityRule.getActivity()
                                    .getWindow()
                                    .getDecorView()))))
                    .check(matches(isDisplayed()));
        }

    }


    @Test
    public void testCreateAccountPasswordShort() {


        try {
            onView(withId(R.id.action_menu))
                    .perform((click()));
            onView(withText("Logout"))
                    .perform(click());

            onView(withId(R.id.create))
                    .perform(click());

            inputData("mike", "smith", getUsername(), getEmail(), "test", "test");

            onView(withId(R.id.create))
                    .perform(click());

            onView(withText("Failed: Please enter a valid password (longer than five characters)."))
                    .inRoot(withDecorView(not(is(
                            mActivityRule.getActivity()
                                    .getWindow()
                                    .getDecorView()))))
                    .check(matches(isDisplayed()));

        } catch (NoMatchingViewException e) {
            // View is not in hierarchy
            onView(withId(R.id.create))
                    .perform(click());


            inputData("mike", "smith", getUsername(), getEmail(), "test", "test");

            onView(withId(R.id.create))
                    .perform(click());

            onView(withText("Failed: Please enter a valid password (longer than five characters)."))
                    .inRoot(withDecorView(not(is(
                            mActivityRule.getActivity()
                                    .getWindow()
                                    .getDecorView()))))
                    .check(matches(isDisplayed()));
        }

    }

    @Test
    public void testCreateAccountPasswordsNoMatch() {


        try {
            onView(withId(R.id.action_menu))
                    .perform((click()));
            onView(withText("Logout"))
                    .perform(click());

            onView(withId(R.id.create))
                    .perform(click());

            inputData("mike", "smith", getUsername(), getEmail(), "testing", "testing1");

            onView(withId(R.id.create))
                    .perform(click());

            onView(withText("Failed: Passwords do not match."))
                    .inRoot(withDecorView(not(is(
                            mActivityRule.getActivity()
                                    .getWindow()
                                    .getDecorView()))))
                    .check(matches(isDisplayed()));

        } catch (NoMatchingViewException e) {
            // View is not in hierarchy
            onView(withId(R.id.create))
                    .perform(click());


            inputData("mike", "smith", getUsername(), getEmail(), "testing", "testing1");

            onView(withId(R.id.create))
                    .perform(click());

            onView(withText("Failed: Passwords do not match."))
                    .inRoot(withDecorView(not(is(
                            mActivityRule.getActivity()
                                    .getWindow()
                                    .getDecorView()))))
                    .check(matches(isDisplayed()));
        }

    }

}
