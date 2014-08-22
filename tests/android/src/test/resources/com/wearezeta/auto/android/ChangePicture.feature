Feature: Change picture

@id205
@smoke
  Scenario Outline: Change user picture
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap on personal info screen
    And I tap change photo button
    And I press Gallery button
    And I select picture
    And I press Confirm button
    And I tap on personal info screen
    Then I see changed user picture
    
    Examples: 
      | Login   | Password     | Name    |
      | aqaUser | aqaPassword  | aqaUser |