/*  Copyright 2013 Problem Solutions

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

/*
 * Added this class to the jar from Rustici for the SP2 project. 
 * 
 */

package com.rusticisoftware.tincan;

import java.net.MalformedURLException;
import java.util.ArrayList;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.rusticisoftware.tincan.v10x.StatementsQuery;

@Data
@NoArgsConstructor
public class StatementRetreiver
{

	private String endpoint = "https://sp2.waxlrs.com/TCAPI/";
	private String username = "NIRzUtWx0rSpEsa0IXt9";
	private String password = "lJLm0WQTwQwJQ8tZzeQV";
	private String targetEmail = "";

	/*
	 * the following functions are used for the BuilderDesign pattern, 
	 * as described here http://stackoverflow.com/questions/222214/managing-constructors-with-many-parameters-in-java-1-4/222295#222295
	 */

	public StatementRetreiver TargetEmail(String _email)
	{
		if (!(_email.toLowerCase().contains(("mailto:").toLowerCase()))) 
		{	
			_email = "mailto:" + _email; 
		}
		this.targetEmail = _email;
		return this;
	}
	public StatementRetreiver Endpoint(String _endpoint)
	{
		this.endpoint = _endpoint;
		return this;
	}
	public StatementRetreiver Username(String _username)
	{
		this.username = _username;
		return this;
	}
	public StatementRetreiver Password(String _password)
	{
		this.password = _password;
		return this;
	}

	private RemoteLRS SetLRS() throws MalformedURLException
	{	
		return (new RemoteLRS(TCAPIVersion.V100, this.endpoint, this.username, this.password));
	}

	public ArrayList<Statement> GetStatements() throws MalformedURLException, Exception
	{
		// Local varibles 
		ArrayList<Statement> rv  = new ArrayList<Statement>();

		// set endpoint
		RemoteLRS lrs = SetLRS(); 

		// make query 
		// TODO: make myself a ctor
		StatementsQuery agentObjectQuery = new StatementsQuery();

		// this must be set to true to get only statements where target is in the object
		agentObjectQuery.setRelatedAgents(true);

		// Set target for whihc mbox to look for in the object
		agentObjectQuery.setAgent(new Agent(targetEmail));

		// run query
		// query for an object 
		StatementsResult result2 = lrs.queryStatements(agentObjectQuery);
		for (Statement item : result2.getStatements()) {

			// get target mbox from the statement's object
			String objectEmail = item.getObject().toJSONNode(TCAPIVersion.V100).findValues("mbox").get(0).toString();
			// string comes back as "email@sp2.com" -- this removes the quotes
			objectEmail = objectEmail.substring(1, objectEmail.length()-1);
			if(objectEmail.equalsIgnoreCase(targetEmail))
			{
				rv.add(item);
			}

		}
		return rv; 


	}

	public void PostTestStatements(int numberToPost, Statement st) throws Exception
	{
		// set endpoint
		RemoteLRS lrs = SetLRS(); 
		for (int i = 0; i < numberToPost; i++) {
			Statement temp = new Statement();
			// copy statement into new statement so a new statements gets inserted
			temp.setActor(st.getActor());
			temp.setObject(st.getObject());
			temp.setVerb(st.getVerb());
			System.out.println(lrs.saveStatement(temp));
		}
	}

}
