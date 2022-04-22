/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2022 J. Férard <https://github.com/jferard>
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 * This file is part of FastODS.
 *
 * FastODS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * FastODS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.jferard.fastods.util;

import org.apache.commons.compress.utils.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class FullListTest {
    @Test
    public final void testAddBlank() {
        final String be = "blank";
        final FullList<String> fl =
                FullList.<String>builder().blankElement(be).capacity(10).build();
        fl.set(100, "non blank");

        Assert.assertFalse(fl.add(be));
        Assert.assertEquals(101, fl.usedSize());
        Assert.assertEquals(Integer.MAX_VALUE, fl.size());

        fl.add(1000, be);
        Assert.assertEquals(101, fl.usedSize());
        Assert.assertEquals(Integer.MAX_VALUE, fl.size());

        Assert.assertFalse(fl.addAll(Arrays.asList(be, be, be)));
        Assert.assertEquals(101, fl.usedSize());
        Assert.assertEquals(Integer.MAX_VALUE, fl.size());

        Assert.assertFalse(fl.addAll(1000, Arrays.asList(be, be, be)));
        Assert.assertEquals(101, fl.usedSize());
        Assert.assertEquals(Integer.MAX_VALUE, fl.size());
    }

    @Test
    public final void testClear() {
        final String be = "blank";
        final FullList<String> fl =
                FullList.<String>builder().blankElement(be).capacity(10).build();
        fl.set(100, "non blank2");
        fl.clear();
        Assert.assertEquals(0, fl.usedSize());
        Assert.assertFalse(fl.isEmpty());
        Assert.assertEquals(be, fl.get(100));
    }

    @Test
    public final void testClone() {
        final String be = "blank";
        final List<String> fl = FullList.<String>builder().blankElement(be).capacity(10).build();
        fl.set(100, "non blank");
        final List<String> l = new ArrayList<String>(101);
        for (int i = 0; i < 100; i++) {
            l.add(be);
        }
        l.add("non blank");
        Assert.assertEquals(l, fl);
        Assert.assertEquals(fl, l);
        Assert.assertEquals(fl.hashCode(), l.hashCode());
    }

    @Test
    public final void testContains() {
        final String be = "blank";
        final FullList<String> fl =
                FullList.<String>builder().blankElement(be).capacity(10).build();
        Assert.assertTrue(fl.contains(be));

        Assert.assertTrue(fl.containsAll(Arrays.asList(be, be, be)));

        fl.set(100, "non blank2");
        Assert.assertEquals(101, fl.usedSize());
        Assert.assertEquals(Integer.MAX_VALUE, fl.size());
        Assert.assertTrue(fl.contains("non blank2"));

        Assert.assertFalse(fl.contains("non blank3"));
        Assert.assertFalse(fl.containsAll(Arrays.asList(be, "non blank3")));
    }

    @Test
    public final void testIndexOf() {
        final String be = "blank";
        final List<String> fl = FullList.<String>builder().blankElement(be).capacity(10).build();

        Assert.assertEquals(-1, fl.indexOf("foo"));
        Assert.assertEquals(-1, fl.lastIndexOf("foo"));
    }

    @Test
    public final void testIndexOfBlankElement() {
        final String be = "blank";
        final List<String> fl = FullList.<String>builder().blankElement(be).capacity(10).build();

        Assert.assertEquals(0, fl.indexOf(be));
        Assert.assertEquals(0, fl.lastIndexOf(be));

        fl.set(100, "non blank2");
        Assert.assertEquals(0, fl.indexOf(be));
        Assert.assertEquals(99, fl.lastIndexOf(be));
    }

    @Test
    public final void testListIterator() {
        final String be = "blank";
        final List<String> fl = FullList.<String>builder().blankElement(be).capacity(10).build();
        fl.set(100, "non blank2");

        ListIterator<String> li = fl.listIterator();
        Assert.assertTrue(li.hasNext());
        li = fl.listIterator(100);
        Assert.assertTrue(li.hasNext());
        Assert.assertEquals("non blank2", li.next());
    }

    @Test
    public final void testRemove() {
        final String be = "blank";
        final FullList<String> fl =
                FullList.<String>builder().blankElement(be).capacity(10).build();
        fl.set(100, "non blank2");

        Assert.assertEquals(be, fl.remove(10));
        Assert.assertEquals(100, fl.usedSize());
        Assert.assertEquals(Integer.MAX_VALUE, fl.size());
        Assert.assertEquals(be, fl.remove(1000));
        Assert.assertEquals(100, fl.usedSize());
        Assert.assertEquals(Integer.MAX_VALUE, fl.size());

        Assert.assertTrue(fl.remove(be));
        Assert.assertEquals(99, fl.usedSize());
        Assert.assertEquals(Integer.MAX_VALUE, fl.size());
        Assert.assertTrue(fl.remove("non blank2"));
        Assert.assertFalse(fl.isEmpty());
    }

    @Test
    public final void testRemoveAll() {
        final String be = "blank";
        final FullList<String> fl =
                FullList.<String>builder().blankElement(be).capacity(10).build();
        fl.set(50, "non blank");
        fl.set(100, "non blank2");

        Assert.assertTrue(fl.removeAll(Collections.singletonList("non blank2")));
        Assert.assertEquals(51, fl.usedSize());
    }

    @Test
    public final void testRetain() {
        final String be = "blank";
        final FullList<String> fl =
                FullList.<String>builder().blankElement(be).capacity(10).build();
        fl.set(100, "non blank2");
        fl.retainAll(Arrays.asList("foo"));
        Assert.assertEquals(0, fl.usedSize());
        Assert.assertFalse(fl.isEmpty());

        fl.set(100, "non blank2");
        fl.retainAll(Arrays.asList(be));
        Assert.assertEquals(0, fl.usedSize());

        fl.set(100, "non blank2");
        fl.retainAll(Arrays.asList("non blank2"));
        Assert.assertEquals(1, fl.usedSize());
    }

    @Test
    public final void testSetAndAdd() {
        final String be = "blank";
        final FullList<String> fl =
                FullList.<String>builder().blankElement(be).capacity(10).build();

        Assert.assertEquals(0, fl.usedSize());
        Assert.assertEquals(be, fl.get(100));

        fl.set(100, "non blank");
        Assert.assertFalse(fl.isEmpty());
        Assert.assertEquals(101, fl.usedSize());
        Assert.assertEquals("non blank", fl.get(100));

        fl.add("non blank2");
        Assert.assertEquals(102, fl.usedSize());
        Assert.assertEquals("non blank2", fl.get(101));

        fl.add(be);
        Assert.assertEquals(102, fl.usedSize());
        Assert.assertEquals(be, fl.get(102));

        fl.set(1000, be);
        Assert.assertEquals(102, fl.usedSize());
        Assert.assertEquals(be, fl.get(1000));

        fl.addAll(Arrays.asList(be, "non blank3", be, be, be, be));
        Assert.assertEquals(104, fl.usedSize());
        Assert.assertEquals(be, fl.get(104));

        fl.addAll(100, Arrays.asList(be, "non blank3", be, be, be, be));
        Assert.assertEquals(110, fl.usedSize());
        Assert.assertEquals("non blank", fl.get(106));
        Assert.assertEquals("non blank3", fl.get(109));

        fl.add(100, "non blank4");
        Assert.assertEquals(111, fl.usedSize());
        Assert.assertEquals("non blank", fl.get(107));
        Assert.assertEquals("non blank3", fl.get(110));
    }

    @Test
    public void testToString() {
        final List<String> fl = FullList.<String>builder().build();
        fl.add("a");
        fl.add(4, "e");

        Assert.assertEquals("[a, null, null, null, e]", fl.toString());
    }

    @Test
    public void testToArray() {
        final List<String> fl = FullList.<String>builder().build();
        fl.add("a");
        fl.add(4, "e");

        Assert.assertArrayEquals(new String[]{"a", null, null, null, "e"},
                fl.toArray(new String[]{}));
    }

    @Test
    public void testSubList() {
        final List<String> fl = FullList.<String>builder().build();
        fl.add("a");
        fl.add(4, "e");
        Assert.assertEquals(Arrays.asList(null, null), fl.subList(1, 3));
    }

    @Test
    public final void testGet() {
        final FullList<String> fl = FullList.newList();
        Assert.assertNull(fl.get(10));
        Assert.assertFalse(fl.isEmpty());
        Assert.assertEquals(0, fl.usedSize());
    }

    @Test
    public final void testSetNonNull() {
        final List<String> l = FullList.newList();
        l.set(10, "a");
        final List<String> l2 = new ArrayList<String>(l);
        Assert.assertEquals(
                Arrays.asList(null, null, null, null, null, null, null, null, null, null, "a"), l2);
        l.set(10, "b");
        final List<String> l3 = new ArrayList<String>(l);
        Assert.assertEquals(
                Arrays.asList(null, null, null, null, null, null, null, null, null, null, "b"), l3);
    }

    @Test
    public final void testSetNullAfter() {
        final FullList<String> fl = FullList.newList();
        fl.set(10, null);
        Assert.assertFalse(fl.isEmpty());
        Assert.assertEquals(0, fl.usedSize());
    }

    @Test
    public final void testSetNullBefore() {
        final FullList<String> fl = FullList.newList();
        fl.add("a");
        fl.add("b");
        Assert.assertEquals("a", fl.set(0, null));
        Assert.assertEquals(Arrays.asList(null, "b"), fl);
        Assert.assertEquals("b", fl.set(1, null));
        Assert.assertFalse(fl.isEmpty());
        Assert.assertEquals(0, fl.usedSize());
    }

    @Test
    public final void testSetOne() {
        final List<String> l = FullList.newList();
        l.set(1, "a");
        Assert.assertEquals(Arrays.asList(null, "a"), l);
    }

    @Test
    public final void testSize() {
        final FullList<String> fl = FullList.newList();
        Assert.assertEquals(0, fl.usedSize());
        Assert.assertEquals(Integer.MAX_VALUE, fl.size());
        Assert.assertFalse(fl.isEmpty());
    }

    @Test
    public final void testCapacity() {
        final FullList<Integer> fl = FullList.newListWithCapacity(5);
        Assert.assertEquals(5, fl.capacity());
        for (int i=0; i<10; i++) {
            fl.add(i);
        }
        Assert.assertEquals(10, fl.capacity());
    }

    @Test
    public final void testIterator() {
        final FullList<Integer> fl = FullList.newListWithCapacity(5, 1, 2, 3, 4, 5, 6);
        Assert.assertEquals(6, fl.capacity());
        Assert.assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6), Lists.newArrayList(fl.iterator()));
    }
}
