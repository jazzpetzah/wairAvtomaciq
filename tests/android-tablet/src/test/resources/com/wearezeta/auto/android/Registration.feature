Feature: Registration

  @id2286 @staging
  Scenario Outline: Register new user using front camera in landscape mode
    Given I see welcome screen
    And I rotate UI to landscape
    When I press tablet Join button
    And I press Camera button twice
    And I See selected picture
    And I confirm selection
    And I enter name <Name>
    And I enter email <Email>
    And I enter password <Password>
    And I submit registration data
    Then I see confirmation page
    And I verify registration address
    And I passed login automatically
    And I see personal info page loaded with my name <Name>

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @id2287 @staging
  Scenario Outline: Register new user using front camera in portrait mode
    Given I see welcome screen
    And I rotate UI to portrait
    When I press tablet Join button
    And I press Camera button twice
    And I See selected picture
    And I confirm selection
    And I enter name <Name>
    And I enter email <Email>
    And I enter password <Password>
    And I submit registration data
    Then I see confirmation page
    And I verify registration address
    And I passed login automatically
    And I see personal info page loaded with my name <Name>

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |
