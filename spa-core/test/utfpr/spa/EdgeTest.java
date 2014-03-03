package utfpr.spa;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class EdgeTest
{
	private Edge<String, String> edge;
	
	private Edge<String, String> anotherEdge;
	
	private String s1 = "test1";
	
	private String s2 = "test1";
	
	private String d1 = "anothertest";
	
	@Before
	public void setUp() throws Exception {
		edge = new Edge<String, String>();
		anotherEdge = new Edge<String, String>();
	}

	@Test
	public void testHashCode_DifferenteSourceTarget() {
		edge.setSource(s1);
		edge.setTarget(s2);
		
		anotherEdge.setSource(s1);
		anotherEdge.setTarget(d1);
		
		assertFalse(edge.hashCode() == anotherEdge.hashCode());
	}

	@Test
	public void testHashCode_SameSourceTarget() {
		edge.setSource(s1);
		edge.setTarget(s2);
		
		anotherEdge.setSource(s1);
		anotherEdge.setTarget(s2);
		
		assertTrue(edge.hashCode() == anotherEdge.hashCode());
	}
	
	@Test
	public void testGetId() {
		assertEquals(0, edge.getId());
	}

	@Test
	public void testSetId() {
		edge.setId(1);
		assertEquals(1, edge.getId());
	}

	@Test
	public void testGetSource() {
		assertNull(edge.getSource());
		assertNull(edge.getTarget());
	}

	@Test
	public void testSetSource() {
		edge.setSource(s1);
		assertEquals(s1, edge.getSource());
		assertNull(edge.getTarget());
	}

	@Test
	public void testGetTarget() {
		assertNull(edge.getSource());
		assertNull(edge.getTarget());
	}

	@Test
	public void testSetTarget() {
		edge.setTarget(s1);
		assertEquals(s1, edge.getTarget());
		assertNull(edge.getSource());
	}

	@Test
	public void testEqualsObject() {
		edge.setSource(s1);
		edge.setTarget(s1);
		anotherEdge.setSource(s1);
		anotherEdge.setTarget(s1);
		assertEquals(edge, anotherEdge);
	}

	@Test
	public void testEqualsObject_DifferentObjects() {
		edge.setSource(s1);
		edge.setTarget(s2);
		anotherEdge.setSource(s1);
		anotherEdge.setTarget(d1);
		anotherEdge.setId(2);
		assertFalse(edge.equals(anotherEdge));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testEqualsObject_DifferentObjects_SameId() {
		edge.setSource(s1);
		edge.setTarget(s2);
		anotherEdge.setSource(s1);
		anotherEdge.setTarget(d1);
		assertFalse(edge.equals(anotherEdge));
	}

	
	@Test(expected=IllegalArgumentException.class)
	public void testEqualsObject_SameObjectDifferentId() {
		edge.setSource(s1);
		edge.setTarget(s1);
		edge.setId(1);
		anotherEdge.setSource(s1);
		anotherEdge.setTarget(s1);
		anotherEdge.setId(2);
		assertEquals(edge, anotherEdge);
	}
	
	@Test
	public void testToString_SameTargetSouce() {
		edge.setSource(s1);
		edge.setTarget(s2);
		assertEquals(s1.toString() + Edge.SEPARATOR + s2.toString(), edge.toString());
	}

}
