Feature: User Profile

  @id328 @smoke @mute
  Scenario Outline: ZClient change name
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap on my name
    And I change <Name> to <NewName>
    And I swipe right to contact list
    Then I see contact list loaded with User name <NewName>
    When I tap on my name <NewName>
    Then I see my new name <NewName> and return old <Name>

    Examples: 
      | Login   | Password    | Name    | NewName     |
      | aqaUser | aqaPassword | aqaUser | NewTestName |

 @id67 @id68 @regression
  Scenario Outline: Open and Close settings page
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap options button
    And I tap settings button
    Then I see settings page
    When I press back button
    Then I see personal info page

    Examples: 
      | Login   | Password    | Name    |
      | aqaUser | aqaPassword | aqaUser |

  @id91 @id92 @regression
  Scenario Outline: Open Close About page from Settings page
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap options button
    And I tap about button
    Then I see About page
	When I tap on About page
	Then I see personal info page 
	
    Examples: 
      | Login   | Password    | Name    |
      | aqaUser | aqaPassword | aqaUser |

 @id71 @regression
  Scenario Outline: Can not open Settings page when editing user name
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap on my name
	Then Settings button is unreachable
	
    Examples: 
      | Login   | Password    | Name    |
      | aqaUser | aqaPassword | aqaUser |
      
      