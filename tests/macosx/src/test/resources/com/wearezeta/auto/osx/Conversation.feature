Feature: Conversation

Scenario Outline: Send message to conversation
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Name>
    And I open conversation with <Contact>
	When I write random message
	And I send message
	Then I see random message in conversation
	
Examples:
    |  Login		| Password			| Name			| Contact   		|
    |  aqaUser		| aqaPassword		| aqaUser		| aqaContact1		|

Scenario Outline: Send hello to conversation
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Name>
    And I open conversation with <Contact>
	When I am knocking to user
	Then I see message YOU KNOCKED in conversation

Examples:
    |  Login		| Password			| Name			| Contact   		|
    |  aqaUser		| aqaPassword		| aqaUser		| aqaContact1		|

#Scenario Outline: Send hey to conversation
#    Given I Sign in using login <Login> and password <Password>
#    And I see Contact list with name <Name>
#    And I open conversation with <Contact>
#	 When I am knocking to user
#	 And I am knocking to user
#	 Then I see message YOU KNOCKED in conversation

#Examples:
#    |  Login		| Password			| Name			| Contact   		|
#    |  aqaUser		| aqaPassword		| aqaUser		| aqaContact1		|

Scenario Outline: Send picture to conversation
	Given I Sign in using login <Login> and password <Password>
	And I see Contact list with name <Name>
	And I open conversation with <Contact>
	When I send picture test.jpg
	Then I see picture in conversation

Examples:
    |  Login		| Password			| Name			| Contact   		|
    |  aqaUser		| aqaPassword		| aqaUser		| aqaContact1		|

Scenario Outline: Create group chat from 1on1 conversation
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Name>
    And I open conversation with <Contact1>
	When I open People Picker from conversation
	And I search for user <Contact2>
	And I see user <Contact2> in search results
	And I add user <Contact2> from search results
	Then I see message YOU ADDED <Contact2>, <Contact1> in conversation

Examples:
    |  Login		| Password		| Name			| Contact1		| Contact2		|
    |  aqaUser		| aqaPassword	| aqaUser		| aqaContact1	| aqaContact2	|

Scenario Outline: Send message to group chat
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Name>
	And I create group chat with <Contact1> and <Contact2>
	And I open conversation with <Contact1>, <Contact2> 
	When I write random message
	And I send message
	Then I see random message in conversation
		
Examples:
    |  Login		| Password		| Name			| Contact1		| Contact2		|
    |  aqaUser		| aqaPassword	| aqaUser		| aqaContact1	| aqaContact2	|

#Scenario Outline: Send hello to group chat
#	Given I Sign in using login <Login> and password <Password>
#	And I see Contact list with name <Name>
#	And I create group chat with <Contact1> and <Contact2>
#	And I open conversation with <Contact1>, <Contact2>
#	When I am knocking to user
#	Then I see message YOU KNOCKED in conversation
#		
#Examples:
#    |  Login		| Password		| Name			| Contact1		| Contact2		|
#    |  aqaUser		| aqaPassword	| aqaUser		| aqaContact1	| aqaContact2	|
#
#Scenario Outline: Send picture to group chat
#	Given I Sign in using login <Login> and password <Password>
#	And I see Contact list with name <Name>
#	And I create group chat with <Contact1> and <Contact2>
#	And I open conversation with <Contact1>, <Contact2>
#	When I send picture
#	Then I see picture in conversation
#		
#Examples:
#    |  Login		| Password		| Name			| Contact1		| Contact2		|
#    |  aqaUser		| aqaPassword	| aqaUser		| aqaContact1	| aqaContact2	|

Scenario Outline: Leave group chat
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Name>
	And I create group chat with <Contact1> and <Contact2>
	When I open conversation with <Contact1>, <Contact2>
	And I open Conversation info
	And I leave conversation
	Then I see message YOU LEFT in conversation

Examples:
	|  Login		| Password		| Name			| Contact1		| Contact2		|
	|  aqaUser		| aqaPassword	| aqaUser		| aqaContact1	| aqaContact2	|

Scenario Outline: Remove user from group chat
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Name>
	And I create group chat with <Contact1> and <Contact2>
	When I open conversation with <Contact1>, <Contact2>
	And I open Conversation info
	And I choose user <Contact1> in Conversation info
	And I remove selected user from conversation
	Then I see message YOU REMOVED <Contact1> in conversation

Examples:
	|  Login		| Password		| Name			| Contact1		| Contact2		|
	|  aqaUser		| aqaPassword	| aqaUser		| aqaContact1	| aqaContact2	|
