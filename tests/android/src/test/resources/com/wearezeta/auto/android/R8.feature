Feature: Registration8

  @C667 @regression @rc
  Scenario Outline: Register new user by phone and set profile picture using camera 8
    Given I see welcome screen
    When I input a new phone number for user <Name>
    And I input the verification code
    And I input my name
    And I select to choose my own picture
    And I select Camera as picture source
    And I tap Take Photo button on Take Picture view
    And I tap Confirm button on Take Picture view
    Then I see Conversations list with no conversations

    Examples:
      | Name      |
      | user1Name |

  @C43807 @rc @regression
  Scenario Outline: Verify sign in with email address only 8
    Given There is 1 user with email address only where <Name> is me
    Given I see welcome screen
    When I switch to email sign in screen
    And I have entered login <Login>
    And I have entered password <Password>
    And I tap Log in button
    And I input a new phone number for user <Name>
    And I input the verification code
    Then I see Conversations list with no conversations

    Examples:
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |
