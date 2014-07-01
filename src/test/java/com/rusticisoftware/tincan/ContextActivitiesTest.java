package com.rusticisoftware.tincan;

import static com.rusticisoftware.tincan.TestUtils.*;

import java.util.Arrays;
import java.util.List;

import com.rusticisoftware.tincan.exceptions.IncompatibleTCAPIVersion;
import org.junit.Test;

public class ContextActivitiesTest {

    @Test
    public void serializeDeserialize() throws Exception {
        ContextActivities ctxActs = new ContextActivities();
        ctxActs.setParent(getCtxActList("http://example.com/parent"));
        ctxActs.setGrouping(getCtxActList("http://example.com/grouping"));
        ctxActs.setOther(getCtxActList("http://example.com/other"));
        assertSerializeDeserialize(ctxActs);
    }

    //
    // 'category' arrived in 1.0.0, so confirm it works in everything
    // after that point, which is to say not 0.95
    //
    @Test
    public void serializeDeserializePost10() throws Exception {
        ContextActivities ctxActs = new ContextActivities();
        ctxActs.setParent(getCtxActList("http://example.com/parent"));
        ctxActs.setGrouping(getCtxActList("http://example.com/grouping"));
        ctxActs.setOther(getCtxActList("http://example.com/other"));
        ctxActs.setCategory(getCtxActList("http://example.com/category"));
        assertSerializeDeserialize(ctxActs, TCAPIVersion.V101);
        assertSerializeDeserialize(ctxActs, TCAPIVersion.V100);
    }

    //
    // 'category' is not supported in 0.95, make sure it isn't
    //
    @Test(expected = IncompatibleTCAPIVersion.class)
    public void serializeDeserialize095Category() throws Exception {
        ContextActivities ctxActs = new ContextActivities();
        ctxActs.setCategory(getCtxActList("http://example.com/category"));
        assertSerializeDeserialize(ctxActs, TCAPIVersion.V095);
    }

    private List<Activity> getCtxActList(String id) throws Exception {
        return Arrays.asList(new Activity[]{ new Activity(id) });
    }
}
