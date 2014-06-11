Feature: People Picker

Scenario Outline: Add contact
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Name>
    And I open conversation with <Name>
    When I open People Picker from contact list
	And I search for user <User>
	And I see user <User> in search results
	And I add user <User> from search results
	And I send invitation to user
	Then I see Contact list with name <User>
	
Examples:
    |  Login                              | Password          | Name                | User   |
    |  Kyrylo.Aleksandrov@wearezeta.com   | 123456            | Kirill Aleksandrov  | mqa9   |