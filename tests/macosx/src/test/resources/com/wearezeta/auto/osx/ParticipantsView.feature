Feature: Participants View

@staging
@id95
Scenario Outline: Change conversation name
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Name>
	And I create group chat with <Contact1> and <Contact2>
	When I open conversation with <Contact1>, <Contact2>
	And I open Conversation info
	And I set name <NewName> for conversation
	Then I see Contact list with name <NewName>

Examples:
	|  Login		| Password		| Name			| Contact1		| Contact2		| NewName								|
	|  aqaUser		| aqaPassword	| aqaUser		| aqaContact1	| aqaContact2	| RANDOM								|
	|  aqaUser		| aqaPassword	| aqaUser		| aqaContact1	| aqaContact2	| ÄäÖöÜüß conv							|

@staging
@id100
Scenario Outline: Display conversation info correctly
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Name>
	And I create group chat with <Contact1> and <Contact2>
	When I open conversation with <Contact1>, <Contact2>
	And I open Conversation info
	Then I see conversation name <Contact1>, <Contact2> in conversation info
	And I see that conversation has <Number> people
	And I see <Number> participants avatars

Examples:
	|  Login		| Password		| Name			| Contact1		| Contact2		|	Number	|
	|  aqaUser		| aqaPassword	| aqaUser		| aqaContact1	| aqaContact2	|	3		|
	