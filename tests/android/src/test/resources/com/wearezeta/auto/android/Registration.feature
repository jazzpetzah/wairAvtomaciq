Feature: Registration

  @C667 @id9 @regression @rc
  Scenario Outline: Register new user by phone and set profile picture using camera
    Given I see welcome screen
    When I input a new phone number for user <Name>
    And I input the verification code
    And I input my name
    And I select to choose my own picture
    And I select Camera as picture source
    And I press Camera button
    And I confirm selection
    Then I see Contact list with no contacts

    Examples:
      | Name      |
      | user1Name |

  @C566 @id4094 @regression @noAcceptAlert
  Scenario Outline: Wrong phone activation code is followed by correct error message
    Given I see welcome screen
    When I input a new phone number for user <Name>
    And I input random activation code
    Then I see invalid code alert

    Examples:
      | Name      |
      | user1Name |