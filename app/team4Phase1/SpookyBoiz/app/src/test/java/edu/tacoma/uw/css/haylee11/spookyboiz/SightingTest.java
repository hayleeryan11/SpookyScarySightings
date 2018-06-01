package edu.tacoma.uw.css.haylee11.spookyboiz;

import org.junit.Test;

import edu.tacoma.uw.css.haylee11.spookyboiz.Sighting.Sighting;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by hayleeryan on 5/24/18.
 */

public class SightingTest {

    @Test
    public void testSightingConstructor() {
        assertNotNull(new Sighting(1, "haylee11", "Bigfoot","2018-05-22", "12:00:00", "Tacoma", "Washington", "Big hairy guy" ));
    }

    @Test
    public void testSightingConstructorBadUsername() {
    }

    @Test
    public void testSightingConstructorBadFirstName() {

    }

    @Test
    public void testSightingConstructorBadLastName() {

    }

    @Test
    public void testSightingConstructorBadFavorite() {

    }

    @Test
    public void testSightingConstructorBadBio() {

    }

    @Test
    public void testSightingConstructorBadSightings() {

    }
}
