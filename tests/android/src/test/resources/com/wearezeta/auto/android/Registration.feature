Feature: Registration

  @smoke @id9
  Scenario Outline: Register new user using front camera
    Given I see sign in screen
    When I press Join button
    And I press Camera button twice
    And I See selected picture
    And I confirm selection
    And I enter name <Name>
    And I enter email <Email>
    And I enter password <Password>
    And I submit registration data
    Then I see confirmation page
    And I verify registration address
    And I see Contact list with my name <Name>

    Examples: 
      | Email   | Password    | Name    |
      | aqaUser | aqaPassword | aqaUser |
