Feature: Register new user

@mute
@smoke
@nonUnicode
@id9
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
    And I press continue registration
    And I swipe from Instructions page to Contact list page
    And Contact list appears with my name <Name>

    Examples: 
      | Email   | Password    | Name    |
      | aqaUser | aqaPassword | aqaUser |
