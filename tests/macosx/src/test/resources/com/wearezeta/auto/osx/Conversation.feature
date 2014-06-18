Feature: Conversation

Scenario Outline: Send message to conversation
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Name>
    And I open conversation with <User>
	When I write random message
	And I send message
	Then I see random message in conversation
	
Examples:
    |  Login							| Password	| Name		| User   |
    |  smoketester+aqa31@wearezeta.com	| aqa123456	| aqa31		| aqa32  |

Scenario Outline: Send hello to conversation
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Name>
    And I open conversation with <User>
	When I am knocking to user
	Then I see message YOU KNOCKED in conversation

Examples:
    |  Login							| Password	| Name		| User   |
    |  smoketester+aqa31@wearezeta.com	| aqa123456	| aqa31		| aqa32  |

#Scenario Outline: Send hey to conversation
#    Given I Sign in using login <Login> and password <Password>
#    And I see Contact list with name <Name>
#    And I open conversation with <User>
#	 When I am knocking to user
#	 And I am knocking to user
#	 Then I see message YOU KNOCKED in conversation

#Examples:
#    |  Login                              | Password          | Name                | User   |
#    |  Kyrylo.Aleksandrov@wearezeta.com   | 123456            | Kirill Aleksandrov  | Sergey |

Scenario Outline: Send picture to conversation
	Given I Sign in using login <Login> and password <Password>
	And I see Contact list with name <Name>
	And I open conversation with <User>
	When I send picture test.jpg
	Then I see picture in conversation

Examples:
    |  Login							| Password	| Name		| User   |
    |  smoketester+aqa31@wearezeta.com	| aqa123456	| aqa31		| aqa32  |

Scenario Outline: Create group chat from 1on1 conversation
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Name>
    And I open conversation with <User1>
	When I open People Picker from conversation
	And I search for user <User2>
	And I see user <User2> in search results
	And I add user <User2> from search results
	Then I see message YOU ADDED <User2>, <User1> in conversation

Examples:
    |  Login                              | Password          | Name   | User1   | User2 |
    |  smoketester+aqa31@wearezeta.com    | aqa123456         | aqa31  | aqa32   | aqa33 |

Scenario Outline: Send message to group chat
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Name>
    And I create group chat with <User1> and <User2>
	And I open conversation with <User1>, <User2> 
	When I write random message
	And I send message
	Then I see random message in conversation
		
Examples:
    |  Login                              | Password          | Name   | User1   | User2 |
    |  smoketester+aqa31@wearezeta.com    | aqa123456         | aqa31  | aqa32   | aqa33 |

#Scenario Outline: Send hello to group chat
#	Given I Sign in using login <Login> and password <Password>
#	And I see Contact list with name <Name>
#	And I create group chat with <User1> and <User2>
#	And I open conversation with <User1>, <User2> 
#	When I am knocking to user
#	Then I see message YOU KNOCKED in conversation
#		
#Examples:
#    |  Login                              | Password          | Name   | User1   | User2 |
#    |  smoketester+aqa31@wearezeta.com    | aqa123456         | aqa31  | aqa32   | aqa33 |
#
#Scenario Outline: Send picture to group chat
#	Given I Sign in using login <Login> and password <Password>
#	And I see Contact list with name <Name>
#	And I create group chat with <User1> and <User2>
#	And I open conversation with <User1>, <User2>
#	When I send picture
#	Then I see picture in conversation
#		
#Examples:
#    |  Login                              | Password          | Name   | User1   | User2 |
#    |  smoketester+aqa31@wearezeta.com    | aqa123456         | aqa31  | aqa32   | aqa33 |

Scenario Outline: Leave group chat
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Name>
    And I create group chat with <User1> and <User2>
	When I open conversation with <User1>, <User2> 
	And I open Conversation info
	And I leave conversation
	Then I see message YOU LEFT in conversation

Examples:
    |  Login                              | Password          | Name   | User1   | User2 |
    |  smoketester+aqa31@wearezeta.com    | aqa123456         | aqa31  | aqa32   | aqa33 |

Scenario Outline: Remove user from group chat
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Name>
    And I create group chat with <User1> and <User2>
	When I open conversation with <User1>, <User2> 
	And I open Conversation info
	And I choose user <User1> in Conversation info
	And I remove selected user from conversation
	Then I see message YOU REMOVED <User1> in conversation

Examples:
    |  Login                              | Password          | Name   | User1   | User2 |
    |  smoketester+aqa31@wearezeta.com    | aqa123456         | aqa31  | aqa32   | aqa33 |
