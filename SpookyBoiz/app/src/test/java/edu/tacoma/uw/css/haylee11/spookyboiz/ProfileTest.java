package edu.tacoma.uw.css.haylee11.spookyboiz;

import org.junit.Test;

import edu.tacoma.uw.css.haylee11.spookyboiz.Profile.Profile;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;

/**
 * Created by hayleeryan on 5/24/18.
 */

public class ProfileTest {

    @Test
    public void testProfileConstructor() {
        assertNotNull(new Profile("haylee1234", "Haylee", "Ryan", 0, "Bigfoot", "I'm haylee I love bigfoot"));
    }

    @Test
    public void testProfileConstructorNoUsername() {
        try {
            new Profile("", "Haylee", "Ryan", 0, "Bigfoot", "I'm haylee I love bigfoot");
            fail("Cannot create profile: There is no username");
        } catch (IllegalArgumentException e) {

        }
    }

    @Test
    public void testProfileConstructorNoFirstName() {
        try {
            new Profile("haylee1234", "", "Ryan", 0, "Bigfoot", "I'm haylee I love bigfoot");
            fail("Cannot create profile: There is no first name");
        } catch (IllegalArgumentException e) {

        }
    }

    @Test
    public void testProfileConstructoNoLastName() {
        try {
            new Profile("haylee1234", "Haylee", "", 0, "Bigfoot", "I'm haylee I love bigfoot");
            fail("Cannot create profile: There is no last name");
        } catch (IllegalArgumentException e) {

        }
    }

}
