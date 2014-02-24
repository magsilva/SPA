package utfpr.spa;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class YellowListTest
{
	private YellowList yl;
	
	private Person p1;
	
	private Person p2;
	
	private Person p3;
	
	private Person p1Equal;
	
	private Person p2Equal;
	
	private Set<Person> people;
	
	@Before
	public void setUp() throws Exception {
		yl = new YellowList();
		
		p1 = new Person();
		p1.setName("Marco Aurélio Graciotto Silva");
		p1.setEmail("magsilva@gmail.com");
		
		p1Equal = new Person();
		p1.setName("Marco");
		p1.setEmail("magsilva@gmail.com");

		p2 = new Person();
		p2.setName("João da Silva");
		p2.setEmail("joao@outofthisuniverse.go");
		
		p2Equal = new Person();
		p2Equal.setName("João da Silva");
		p2Equal.setEmail("joao@example.com");
		
		p3 = new Person();
		p3.setName("Maria dos Santos");
		p3.setEmail("nothing@hill");
	}

	@Test
	public void testMerge() {
		yl.add(p1);
		yl.add(p1Equal);
		people = yl.getPeople();
		assertEquals(2, people.size());
		yl.merge();
		people = yl.getPeople();
		assertEquals(1, people.size());
	}

}
