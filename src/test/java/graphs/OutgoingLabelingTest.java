package graphs;

import org.junit.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;


import java.util.*;

public class OutgoingLabelingTest {
    private OutgoingLabelingIterator iterator1;

  /*  @Before
    public void setUp() {
        System.out.println("This code executes before each test method");
        iterator1 = new OutgoingLabelingIterator(4, 4,
                Map.of(1, 2, 2, 2));

    }*/

    @Test
    public void simplestLabelingTest() {
        List<Map<Integer, Integer>> labelings = new ArrayList<>();
        iterator1 = new OutgoingLabelingIterator(4, 4,
                Map.of(1, 4));
        while (iterator1.hasNext()) {
            labelings.add(new HashMap<>(iterator1.next()));
        }
        assertThat(labelings.size(), is(1));
    }

    @Test
    public void OutgoingLabelingTest1() {
        List<Map<Integer, Integer>> labelings = new ArrayList<>();
        iterator1 = new OutgoingLabelingIterator(4, 4,
                Map.of(1, 2, 2, 2));
        while (iterator1.hasNext()) {
            labelings.add(new HashMap<>(iterator1.next()));
        }

        assertThat(labelings.size(), is(1));

        assertEquals(Map.of(1, 2, 2, 2), labelings.get(0));
        labelings.clear();

        OutgoingLabelingIterator iterator2 = new OutgoingLabelingIterator(4, 11,
                Map.of(1, 4, 2, 4, 3, 4));
        while (iterator2.hasNext()) {
            labelings.add(new HashMap<>(iterator2.next()));
        }
        assertThat(labelings.size(), is(3));

        assertTrue(labelings.contains(Map.of(1, 3, 2, 4, 3, 4)));
        assertTrue(labelings.contains(Map.of(1, 4, 2, 3, 3, 4)));
        assertTrue(labelings.contains(Map.of(1, 4, 2, 4, 3, 3)));
        labelings.clear();
    }

}
