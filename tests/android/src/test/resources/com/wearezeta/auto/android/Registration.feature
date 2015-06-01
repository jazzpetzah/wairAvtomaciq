Feature: Registration

  @id9 @id30 @smoke @torun
  Scenario Outline: Register new user using front camera
    Given I see welcome screen
    When I set the area code to <AreaCode>
    And I input a new phone number <PhoneNumber>
    And I confirm the phone number
    And I input the verification code
    And I confirm the verification code
    And I input my name
    And I confirm my name
    #And I press Camera button twice
    #And I See selected picture
    #And I confirm selection
    #And I enter name <Name>
    #And I enter email <Email>
    #And I enter password <Password>
    #And I submit registration data
    #Then I see confirmation page
    #And I minimize the application
    #And I activate user by URL
    #And I wait for 10 seconds
    #And I see Contact list with no contacts

    Examples: 
      | AreaCode 	| PhoneNumber 	| Password      | Name      |
      | QA-Shortcut	| 0106151249 	| user1Password | user1Name |

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