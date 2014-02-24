package utfpr.spa;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;

public class YellowList
{
	private Set<Person> people;
	
	private float nameSimilarityThreshold = 0.05f;

	private float emailSimilarityThreshold = 0.05f;
	
	public YellowList() {
		people = new LinkedHashSet<Person>();
	}
	
	public void add(Person person) {
		people.add(person);
	}
	
	public void remove(Person person) {
		people.remove(person);
	}
	
	public Set<Person> getPeople() {
		return people;
	}
	
	public void merge() {
		int decisionTable[][];
		Set<Person> peopleToRemove = new HashSet<Person>();
		Iterator<Person> i;
		Iterator<Person> j;
		int i2;
		int j2;
		AbstractStringMetric metric = new Levenshtein();

		decisionTable = new int[people.size()][people.size()];
		for (i2 = 0; i2 < people.size(); i2++) {
			for (j2 = 0; j2 < people.size(); j2++) {
				decisionTable[i2][j2] = 0;
			}
		}
		
		i = people.iterator();
		i2 = 0;
		while (i.hasNext()) {
			Person person = i.next();
			j = people.iterator();
			j2 = 0;
			while (j.hasNext()) {
				Person anotherPerson = j.next();
				Iterator<String> iString1;
				Iterator<String> iString2;
				String s1, s2;
				float distance;
				boolean similarName = false;
				boolean similarEmail = false;
				
				iString1 = person.getNames();
				while (iString1.hasNext() && similarName == false) {
					s1 = iString1.next();
					iString2 = anotherPerson.getNames();
					while (iString2.hasNext() && similarName == false) {
						s2 = iString2.next();
						distance = metric.getSimilarity(s1, s2);
						if (distance < (1 + nameSimilarityThreshold) || distance > (1 - nameSimilarityThreshold)) {
							decisionTable[i2][j2] += 1;
							similarName = true;
						}
					}
				}
				
				iString1 = person.getEmails();
				while (iString1.hasNext() && similarEmail == false) {
					s1 = iString1.next();
					iString2 = anotherPerson.getEmails();
					while (iString2.hasNext() && similarEmail == false) {
						s2 = iString2.next();
						distance = metric.getSimilarity(s1, s2);
						if (distance < (1 + emailSimilarityThreshold) || distance > (1 - emailSimilarityThreshold)) {
							decisionTable[i2][j2] += 1;
							similarEmail = true;
						}
					}
				}
				j2++;
			}
			i2++;
		}

		i = people.iterator();
		for (i2 = 0; i2 < people.size(); i2++) {
			Person person = i.next();
			j = people.iterator();
			for (j2 = 0; j2 < people.size(); j2++) {
				Person personToBeMerged = j.next();
				if (decisionTable[i2][j2] >= 1) {
					person.merge(personToBeMerged);
					peopleToRemove.add(personToBeMerged);
				}
			}
		}
		
		people.removeAll(peopleToRemove);
	}
}
