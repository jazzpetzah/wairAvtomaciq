Feature: Registration

  @id9 @id30 @smoke
  Scenario Outline: Register new user using front camera
    Given I see welcome screen
    And I input a new phone number for user <Name>
    And I input the verification code
    And I input my name
    And I press Camera button twice
    And I confirm selection
    And I see Contact list with no contacts

    Examples: 
      | AreaCode 	| Name      |
      | QA-Shortcut	| user1Name |