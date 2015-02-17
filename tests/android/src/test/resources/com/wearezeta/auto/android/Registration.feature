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
    And I minimize the application
    And I activate user by URL
    And I restore the application
    #And I verify registration address
    And I see Contact list with no contacts and my name <Name>

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |
