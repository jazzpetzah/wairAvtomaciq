Feature: Settings

  @id67 @id68 @regression
  Scenario Outline: Open and Close settings page
    Given I have 1 users and 0 contacts for 1 users
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    #workaround
    And I minimize the application
    And I restore the application
    #workaround
    And I tap options button
    And I tap settings button
    Then I see settings page
    When I press back button
    Then I see personal info page

    Examples: 
      | Login   | Password    | Name    |
      | aqaUser | aqaPassword | aqaUser |

  @id71 @regression
  Scenario Outline: Can not open Settings page when editing user name
    Given I have 1 users and 0 contacts for 1 users
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    #workaround
    And I minimize the application
    And I restore the application
    #workaround
    And I tap on my name
    Then Settings button is unreachable

    Examples: 
      | Login   | Password    | Name    |
      | aqaUser | aqaPassword | aqaUser |

  @id90 @id91 @id92 @regression
  Scenario Outline: Open Close About page from Settings page
    Given I have 1 users and 0 contacts for 1 users
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    #workaround
    And I minimize the application
    And I restore the application
    #workaround
    And I tap options button
    And I tap about button
    Then I see About page
    When I tap on About page
    Then I see personal info page

    Examples: 
      | Login   | Password    | Name    |
      | aqaUser | aqaPassword | aqaUser |
