package com.rusticisoftware.tincan;

import static com.rusticisoftware.tincan.TestUtils.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ContextActivitiesTest {

    @Test
    public void serializeDeserialize() throws Exception {
        ContextActivities ctxActs = new ContextActivities();
        ctxActs.setParent(getCtxActList("http://example.com/parent"));
        ctxActs.setGrouping(getCtxActList("http://example.com/grouping"));
        ctxActs.setOther(getCtxActList("http://example.com/other"));
        ctxActs.setCategory(getCtxActList("http://example.com/category"));
        assertSerializeDeserialize(ctxActs);
    }
    
    private List<Activity> getCtxActList(String id) throws Exception {
        return Arrays.asList(new Activity[]{ new Activity(id) });
    }
}
