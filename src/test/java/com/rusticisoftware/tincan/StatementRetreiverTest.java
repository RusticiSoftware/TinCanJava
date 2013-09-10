/*
    Copyright 2013 Problem Solutions LLC

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

import java.util.ArrayList;

import lombok.extern.java.Log;

import org.junit.Assert;
import org.junit.Test;

@Log
public class StatementRetreiverTest {

	@Test
	public void testEndpoint() throws Exception {

		////		//create statement like the structure we are going to have
		// actor
		Agent doctor = new Agent();
		doctor.setMbox("mailto:observer@sp2.com");
		doctor.setName("The Doctor");

		//agent object
		Agent angelBob = new Agent();
		angelBob.setMbox("mailto:pilot@sp2.com");
		angelBob.setName("Angel Bob");

		// the LRS creds
		String endpoint = "https://sp2.waxlrs.com/TCAPI/";
		String username = "NIRzUtWx0rSpEsa0IXt9";
		String password = "lJLm0WQTwQwJQ8tZzeQV";

		// with out setting the LRS creds
		StatementRetreiver getDoctors = new StatementRetreiver()
		.TargetEmail(doctor.getMbox());

		// with setting the LRS creds
		StatementRetreiver getBob = new StatementRetreiver()
		.TargetEmail(angelBob.getMbox())
		.Endpoint(endpoint)
		.Username(username)
		.Password(password);

		// set to true to populate the LRS with test data
		boolean popTestData = true;
		int howMany = 1; 
		if (popTestData)
		{	
			//		// verb
			Verb verb = new Verb();
			LanguageMap display = new LanguageMap();
			display.put("en", "assessed");
			verb.setDisplay(display);
			verb.setId("http://www.example.com/VerbId");
			//
			// object 
			AgentActivity target = new AgentActivity();
			target.setObjectType("Agent");
			target.setMbox(angelBob.getMbox());
			target.setName(angelBob.getName());
			//
			Statement st = new Statement();
			st.stamp(); // triggers a PUT -- sure?

			st.setActor(doctor);
			st.setVerb(verb);
			st.setObject(target);

			getBob.PostTestStatements(howMany, st);

			// object 
			AgentActivity otherTarget = new AgentActivity();
			otherTarget.setObjectType("Agent");
			otherTarget.setMbox(doctor.getMbox());
			otherTarget.setName(doctor.getName());

			Statement OtherStatement = new Statement();
			OtherStatement.setActor(angelBob);
			OtherStatement.setVerb(verb);
			OtherStatement.setObject(otherTarget);
			OtherStatement.stamp();

			System.out.print(OtherStatement.getObject());

			getDoctors.PostTestStatements(howMany, OtherStatement);


		}

		// Gets the statements
		ArrayList<Statement> onlyBobs = getBob.GetStatements();
		Assert.assertTrue(onlyBobs.size() > 1);

		// Gets the statements
		ArrayList<Statement> onlyDocs = getDoctors.GetStatements();
		Assert.assertTrue(onlyDocs.size() > 1);



	}

}
