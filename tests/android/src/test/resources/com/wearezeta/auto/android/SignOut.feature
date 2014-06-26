Feature: Sign Out

  @torun
  Scenario Outline: Sign out from ZClient
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap options button
    And I tap sign out button
    Then I see welcome screen

    Examples: 
      | Login                          | Password | Name  | OptionsButton |
      | maksym.kuvshynov@wearezeta.com | 25ef24ss | Maxim | Sign out      |
