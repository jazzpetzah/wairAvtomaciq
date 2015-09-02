Feature: Registration

  @id9 @smoke @rc
  Scenario Outline: Register new user by phone and set profile picture using camera
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