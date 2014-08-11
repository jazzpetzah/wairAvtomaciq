Feature: Participants View

#Muted till new sync engine client stabilization
@mute
@regression
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

#Muted till new sync engine client stabilization
@mute
@regression
@id186
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

@staging
@id61
Scenario Outline: Check confirmation request on removing person from group chat
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Name>
	And I create group chat with <Contact1> and <Contact2>
	When I open conversation with <Contact1>, <Contact2>
	And I open Conversation info
	And I choose user <Contact1> in Conversation info
	And I select to remove user from group chat
	Then I see confirmation request about removing user

Examples:
	|  Login		| Password		| Name			| Contact1		| Contact2		|
	|  aqaUser		| aqaPassword	| aqaUser		| aqaContact1	| aqaContact2	|

@staging
@id97 
Scenario Outline: I can navigate forth and back between participant view and personal info
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Name>
	And I create group chat with <Contact1> and <Contact2>
	When I open conversation with <Contact1>, <Contact2>
	And I open Conversation info
	And I choose user <Contact1> in Conversation info
	Then I see user <Contact1> personal info
	And I return to participant view from personal info
	And I see conversation name <Contact1>, <Contact2> in conversation info

Examples:
	|  Login		| Password		| Name			| Contact1		| Contact2		|
	|  aqaUser		| aqaPassword	| aqaUser		| aqaContact1	| aqaContact2	|

@staging
@id95
Scenario Outline: Edit name of group conversation
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Name>
	And I create group chat with <Contact1> and <Contact2>
	When I open conversation with <Contact1>, <Contact2>
	And I open Conversation info
	And I set name <NewName> for conversation
	Then I see Contact list with name <NewName>
	And I see message YOU RENAMED THE CONVERSATION in conversation
	And I see conversation name <NewName> in conversation

Examples:
	|  Login		| Password		| Name			| Contact1		| Contact2		| NewName								|
	|  aqaUser		| aqaPassword	| aqaUser		| aqaContact1	| aqaContact2	| RANDOM						|

@staging
@id96
Scenario Outline: Do not accept erroneous input as group conversation name (only spaces)
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Name>
	And I create group chat with <Contact1> and <Contact2>
	When I open conversation with <Contact1>, <Contact2>
	And I open Conversation info
	And I set name       for conversation
	Then I do not see conversation       in contact list
	And I see Contact list with name <Contact1>, <Contact2>

Examples:
	|  Login		| Password		| Name			| Contact1		| Contact2		|
	|  aqaUser		| aqaPassword	| aqaUser		| aqaContact1	| aqaContact2	|

@staging
@id96
Scenario Outline: Do not accept erroneous input as group conversation name (line breaks)
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Name>
	And I create group chat with <Contact1> and <Contact2>
	When I open conversation with <Contact1>, <Contact2>
	And I open Conversation info
	And I set name \n\n\n\n\n for conversation
	Then I do not see conversation \n\n\n\n\n in contact list
	And I see Contact list with name <Contact1>, <Contact2>

Examples:
	|  Login		| Password		| Name			| Contact1		| Contact2		|
	|  aqaUser		| aqaPassword	| aqaUser		| aqaContact1	| aqaContact2	|

@staging
@id96
Scenario Outline: Do not accept erroneous input as group conversation name (leading spaces)
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Name>
	And I create group chat with <Contact1> and <Contact2>
	When I open conversation with <Contact1>, <Contact2>
	And I open Conversation info
	And I set name    TestLeadingSpaces    for conversation
	Then I do not see conversation    TestLeadingSpaces    in contact list
	And I see Contact list with name <Contact1>, <Contact2>

Examples:
	|  Login		| Password		| Name			| Contact1		| Contact2		|
	|  aqaUser		| aqaPassword	| aqaUser		| aqaContact1	| aqaContact2	|
