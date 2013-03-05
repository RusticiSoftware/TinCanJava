package tincan;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GroupTest {
    @Test
    public void testGetObjectType() throws Exception {
        Group mock = new Group();
        assertEquals(mock.getObjectType(), "Group");
    }
}
