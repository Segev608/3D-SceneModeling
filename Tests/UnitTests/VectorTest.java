package Tests.UnitTests;

import primitives.*;
import org.junit.Test;

import static org.junit.Assert.*;
import static primitives.Util.*;

/**
 * Tests for Vector
 */
public class VectorTest {

    final Vector v1 = new Vector(1, 2, 3);
    final Vector v2 = new Vector(-2, -4, -6);
    final Vector v3 = new Vector(0, 3, -2);

    /**
     * Test method for {@link Vector#add(Vector)}
     */
    @Test
    public void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        Vector exp = new Vector(-1, -2, -3);
        assertEquals("ERROR: add() wrong value", exp, v1.add(v2));

        // =============== Boundary Values Tests ==================
        // test zero vector from added vectors
        try {
            v1.add(exp);
            // in case the add method does not throws exception - Failed test!
            fail("add() does not throw an exception when answer is zero");
        } catch (Exception e) {}
    }

    /**
     * Test method for {@link Vector#subtract(Vector)}
     */
    @Test
    public void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        Vector exp = new Vector(3, 6, 9);
        assertEquals("ERROR: subtract() wrong value", exp, v1.subtract(v2));

        // =============== Boundary Values Tests ==================
        // test zero vector from subtracted vectors
        try {
            v1.subtract(v1);
            fail("subtract() does not throw an exception when answer is zero");
        } catch (Exception e) {}
    }

    /**
     * Test method for {@link Vector#scale(double)}
     */
    @Test
    public void testScale() {
        // ============ Equivalence Partitions Tests ==============
        Vector exp = new Vector(3, 6, 9);
        assertEquals("ERROR: scale() wrong value", exp, v1.scale(3));

        // =============== Boundary Values Tests ==================
        // test zero vector from scaled vector
        try {
            v1.scale(0);
            fail("subtract() does not throw an exception when answer is zero");
        } catch (Exception e) {}
    }

    /**
     * Test method for {@link Vector#dotProduct(Vector)}
     */
    @Test
    public void testDotProduct() {
        // ============ Equivalence Partitions Tests ==============
        assertTrue("ERROR: dotProduct() wrong value", isZero(v1.dotProduct(v2) + 28));

        // =============== Boundary Values Tests ==================
        assertTrue("ERROR: dotProduct() for orthogonal vectors is not zero", isZero(v1.dotProduct(v3)));
    }

    /**
     * Test method for {@link Vector#crossProduct(Vector)}
     */
    @Test
    public void testCrossProduct() {
        // ============ Equivalence Partitions Tests ==============
        Vector vr = v1.crossProduct(v3);

        // Test that length of cross-product is proper (orthogonal vectors taken for simplicity)
        assertEquals("crossProduct() wrong result length", v1.length() * v3.length(), vr.length(), 0.00001);

        // Test cross-product result orthogonality to its operands
        assertTrue("crossProduct() result is not orthogonal to 1st operand", isZero(vr.dotProduct(v1)));
        assertTrue("crossProduct() result is not orthogonal to 2nd operand", isZero(vr.dotProduct(v3)));

        // =============== Boundary Values Tests ==================
        // test zero vector from cross-productof co-lined vectors
        try {
            v1.crossProduct(v2);
            fail("crossProduct() for parallel vectors does not throw an exception");
        } catch (Exception e) {}

    }

    /**
     * Test method for {@link Vector#lengthSquared()}
     */
    @Test
    public void testLengthSquared() {
        // ============ Equivalence Partitions Tests ==============
        assertTrue("ERROR: lengthSquared() wrong value", isZero(v1.lengthSquared() - 14));
    }

    /**
     * Test method for {@link Vector#length()}
     */
    @Test
    public void testLength() {
        // ============ Equivalence Partitions Tests ==============
        assertTrue("ERROR: length() wrong value", isZero(new Vector(0, 3, 4).length() - 5));
    }

    /**
     * Test method for {@link Vector#normalize()}
     */
    @Test
    public void testNormalize() {
        // ============ Equivalence Partitions Tests ==============
        Vector v = new Vector(1, 2, 3);
        Vector vCopy = new Vector(v);
        Vector vCopyNormalize = vCopy.normalize();
        // to check the difference between normalize() and normalized()
        // normalize() - calculate the value on the same instance
        // normalized() - calculate the value and returns new instance
        assertSame("ERROR: normalize() function creates a new vector", vCopy, vCopyNormalize);
        assertTrue("ERROR: normalize() result is not a unit vector", isZero(vCopyNormalize.length() - 1));
    }

    /**
     * Test method for {@link Vector#normalized()}
     */
    @Test
    public void testNormalized() {
        // ============ Equivalence Partitions Tests ==============
        Vector v = new Vector(1, 2, 3);
        Vector u = v.normalized();
        assertNotSame("ERROR: normalized() function does not create a new vector", u, v);
    }

    /**
     * Test method for {@link Vector#getOrthogonal()}
     */
    @Test
    public void testGetOrthogonal() {
        // ============ Equivalence Partitions Tests ==============
        Vector v = v2.getOrthogonal();
        assertTrue("ERROR: getOrthogonal() wrong value", isZero(v2.dotProduct(v)));
    }

    /**
     * Test method for {@link Vector#rotate(Vector, double)}
     */
    @Test
    public void testRotate() {
        // ============ Equivalence Partitions Tests ==============
        Vector v = new Vector(1,0,0);
        Vector k = new Vector(0,0,1);
        assertEquals("ERROR: rotate() wrong value", v.rotate(k,Math.PI), new Vector(-1,0,0));
    }
}