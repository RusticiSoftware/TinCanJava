/*
    Copyright 2013 Rustici Software

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package com.rusticisoftware.tincan;

import static com.rusticisoftware.tincan.TestUtils.assertSerializeDeserialize;

import lombok.Data;

import org.joda.time.Period;
import org.joda.time.format.ISOPeriodFormat;
import org.junit.Assert;
import org.junit.Test;

/**
 * ResultTest Class Description
 */
@Data
public class ResultTest {
    
    @Test
    public void serializeDeserialize() throws Exception {
        Result res = new Result();
        res.setCompletion(true);
        res.setDuration(new Period("P1DT8H"));
        res.setExtensions(new Extensions());
        res.getExtensions().put("http://example.com/extension", "extensionValue");
        res.setResponse("Here's a response");
        res.setScore(new Score());
        res.getScore().setRaw(0.43);
        res.setSuccess(false);
        assertSerializeDeserialize(res);
    }

    @Test
    public void testToJSONResultDurationTruncation() throws Exception {
        Result res = new Result();
        res.setDuration(new Period(1, 2, 16, 43));

        Assert.assertEquals("{\"duration\":\"PT1H2M16.04S\"}", res.toJSON());
    }
}
