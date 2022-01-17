package graphs;

import org.junit.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;


import java.util.*;

public class OutgoingLabelingTest {
    private OutgoingLabelingIterator iterator1;
    List<Map<Integer, Integer>> labelings = new ArrayList<>();

    @Before
    public void setUp() {
        labelings.clear();
    }

    @Test
    public void simplestLabelingTest() {

        iterator1 = new OutgoingLabelingIterator(4, 4,
                Map.of(1, 4));
        while (iterator1.hasNext()) {
            labelings.add(new HashMap<>(iterator1.next()));
        }
        assertThat(labelings.size(), is(1));
    }

    @Test
    public void tooBigInFlowTest() {
        iterator1 = new OutgoingLabelingIterator(4, 6,
                Map.of(1, 4));
        while (iterator1.hasNext()) {
            labelings.add(new HashMap<>(iterator1.next()));
        }
        assertTrue(labelings.isEmpty());
    }

    @Test
    public void tooSmallInFlowTest() {
        iterator1 = new OutgoingLabelingIterator(3, 1, Map.of(3, 1,
                                                                                      4, 1,
                                                                                      6, 1));
        while ((iterator1.hasNext())) labelings.add(new HashMap<>(iterator1.next()));
        assertTrue(labelings.isEmpty());
    }

    @Test
    public void OutgoingLabelingTest1() {
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
    }

}
