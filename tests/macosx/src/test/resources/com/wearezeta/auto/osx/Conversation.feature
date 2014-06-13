Feature: Conversation

Scenario Outline: Send message to conversation
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Name>
    And I open conversation with <User>
	When I write random message
	And I send message
	Then I see random message in conversation
	
Examples:
    |  Login                              | Password          | Name                | User   |
    |  Kyrylo.Aleksandrov@wearezeta.com   | 123456            | Kirill Aleksandrov  | aqa51  |

Scenario Outline: Send hello to conversation
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Name>
    And I open conversation with <User>
	When I am knocking to user
	Then I see message YOU KNOCKED in conversation

Examples:
    |  Login                              | Password          | Name                | User   |
    |  Kyrylo.Aleksandrov@wearezeta.com   | 123456            | Kirill Aleksandrov  | aqa51  |

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
	When I send picture
	Then I see picture in conversation

Examples:
    |  Login                              | Password          | Name                | User   |
    |  Kyrylo.Aleksandrov@wearezeta.com   | 123456            | Kirill Aleksandrov  | aqa51  |
