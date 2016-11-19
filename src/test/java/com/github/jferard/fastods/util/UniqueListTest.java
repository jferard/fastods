package com.github.jferard.fastods.util;

import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.collect.Sets;

public class UniqueListTest {
	private static class FirstLetter implements NamedObject {
		private final String s;

		FirstLetter(final String s) {
			this.s = s;
		}

		@Override
		public String getName() {
			return this.s.substring(0, 1);
		}
	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test()
	public final void testDuplicate() {
		final List<FirstLetter> ul = new UniqueList<FirstLetter>();
		ul.add(new FirstLetter("FastODS"));

		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("already in list");
		ul.add(new FirstLetter("FastODS2"));
	}

	@Test()
	public final void testRemoveByIndex() {
		final List<FirstLetter> ul = new UniqueList<FirstLetter>();
		final FirstLetter element = new FirstLetter("FastODS");
		ul.add(element);
		ul.add(new FirstLetter("GastODS"));

		Assert.assertEquals(element, ul.remove(0));
	}

	@Test()
	public final void testRemoveByIndex2() {
		final List<FirstLetter> ul = new UniqueList<FirstLetter>();
		final FirstLetter element = new FirstLetter("FastODS");
		ul.add(element);
		ul.add(new FirstLetter("GastODS"));

		this.thrown.expect(IndexOutOfBoundsException.class);
		Assert.assertEquals(element, ul.remove(2));
	}

	@Test()
	public final void testRemoveByName() {
		final UniqueList<FirstLetter> ul = new UniqueList<FirstLetter>();
		final FirstLetter element = new FirstLetter("FastODS");
		ul.add(element);
		ul.add(new FirstLetter("GastODS"));

		Assert.assertEquals(element, ul.removeByName("F"));
	}

	@Test()
	public final void testRemoveByName2() {
		final UniqueList<FirstLetter> ul = new UniqueList<FirstLetter>();
		final FirstLetter element = new FirstLetter("FastODS");
		ul.add(element);
		ul.add(new FirstLetter("GastODS"));

		Assert.assertEquals(null, ul.removeByName("H"));
	}

	@Test()
	public final void testRemoveObject() {
		final UniqueList<FirstLetter> ul = new UniqueList<FirstLetter>();
		final FirstLetter element = new FirstLetter("FastODS");
		ul.add(element);
		ul.add(new FirstLetter("GastODS"));

		Assert.assertEquals(true, ul.remove(element));
	}

	@Test()
	public final void testRemoveObject2() {
		final UniqueList<FirstLetter> ul = new UniqueList<FirstLetter>();
		final FirstLetter element = new FirstLetter("FastODS");
		ul.add(element);
		ul.add(new FirstLetter("GastODS"));

		Assert.assertEquals(false, ul.remove("foo"));
	}

	@Test()
	public final void testSet() {
		final UniqueList<FirstLetter> ul = new UniqueList<FirstLetter>();
		ul.add(new FirstLetter("FastODS"));
		ul.add(new FirstLetter("GastODS"));

		Assert.assertEquals(Sets.newHashSet("F", "G"), ul.nameSet());
	}

	@Test()
	public final void testSet2() {
		final UniqueList<FirstLetter> ul = new UniqueList<FirstLetter>();
		ul.add(new FirstLetter("FastODS"));
		ul.set(0, new FirstLetter("GastODS"));

		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("already in list");
		ul.set(0, new FirstLetter("FastODS"));
	}
}
