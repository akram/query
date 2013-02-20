package com.ctp.cdi.query.property.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.ctp.cdi.query.property.Property;

/**
 * Validate the property query mechanism.
 *
 * @author Dan Allen
 */
public class PropertyQueryTest {
    /**
     * Querying for a single result with a criteria that matches multiple
     * properties should throw an exception.
     *
     * @see PropertyQuery#getSingleResult()
     */
    @Test(expected = RuntimeException.class)
    public void testNonUniqueSingleResultThrowsException() {
        PropertyQuery<String> q = PropertyQueries.<String>createQuery(Person.class);
        q.addCriteria(new TypedPropertyCriteria(String.class));
        q.getSingleResult();
    }

    /**
     * Querying for a single result with a criteria that does not match
     * any properties should throw an exception.
     *
     * @see PropertyQuery#getSingleResult()
     */
    @Test(expected = RuntimeException.class)
    public void testEmptySingleResultThrowsException() {
        PropertyQuery<String> q = PropertyQueries.<String>createQuery(Person.class);
        q.addCriteria(new TypedPropertyCriteria(Integer.class));
        q.getSingleResult();
    }

    /**
     * Querying for a single result with a criterai that matches exactly one
     * property should return the property.
     *
     * @see PropertyQuery#getSingleResult()
     */
    @Test
    public void testSingleResult() {
        PropertyQuery<String> q = PropertyQueries.<String>createQuery(Person.class);
        q.addCriteria(new NamedPropertyCriteria("name"));
        Property<String> p = q.getSingleResult();
        assertNotNull(p);
        Person o = new Person();
        o.setName("Trap");
        assertEquals("Trap", p.getValue(o));
    }

    public static class Person {

        private String name;
        private String title;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

}
