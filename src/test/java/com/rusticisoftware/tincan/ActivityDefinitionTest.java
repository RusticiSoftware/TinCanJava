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

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

/**
 * Description
 */
public class ActivityDefinitionTest {
    
    @Test
    public void serializeDeserialize() throws Exception {
        ActivityDefinition def = new ActivityDefinition();

        def.setChoices(createInteractionComponent("choice1", "Choice 1"));
        
        def.setCorrectResponsesPattern(
                new ArrayList<String>(
                        Arrays.asList(new String[]{"correct_response"})));
        
        def.setDescription(new LanguageMap());
        def.getDescription().put("en-US", "Activity Definition Description");
        
        Extensions extensions = new Extensions();
        extensions.put("http://example.com/extensions", "extensionValue");
        def.setExtensions(extensions);
        
        
        
        def.setName(new LanguageMap());
        def.getName().put("en-US", "Activity Definition");
        
        def.setScale(createInteractionComponent("scale1", "Scale 1"));
        def.setSource(createInteractionComponent("source1", "Source 1"));
        def.setSteps(createInteractionComponent("steps1", "Steps 1"));
        def.setTarget(createInteractionComponent("target1", "Target 1"));
        
        def.setType("http://adlnet.gov/expapi/activities/assessment");
        
        for (InteractionType intType : InteractionType.values()) {
            def.setInteractionType(intType);
            assertSerializeDeserialize(def);
        }
    }
    
    protected ArrayList<InteractionComponent> createInteractionComponent(String id, String description) {
        InteractionComponent comp = new InteractionComponent();
        comp.setId(id);
        comp.setDescription(new LanguageMap());
        comp.getDescription().put("en-US", description);
        ArrayList<InteractionComponent> lst = new ArrayList<InteractionComponent>();
        lst.add(comp);
        return lst;
    }
}
