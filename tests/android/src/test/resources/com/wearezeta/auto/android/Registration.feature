Feature: Registration

  @id9 @id30 @smoke @torun
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

  @id38 @regression
  Scenario: Verify the taken photo/selected picture could be changed during registration process
    Given I see welcome screen
    When I press Join button
    And I press Camera button twice
    And I See selected picture
    And I confirm selection
    And I hide keyboard
    And I take screenshot
    And I press Registration back button
    And I press Picture button
    And I choose photo from album
    And I confirm selection
    And I hide keyboard
    And I take screenshot
    Then I compare 1st and 2nd screenshots and they are different