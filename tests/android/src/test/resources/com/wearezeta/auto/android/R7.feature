Feature: Registration7

  @C6677 @regression @rc @torun
  Scenario Outline: Register new user by phone and set profile picture using camera 7
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

  @C438077 @rc @regression @torun
  Scenario Outline: Verify sign in with email address only 7
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

  @C3827 @regression @torun
  Scenario Outline: Sign in to Wire by mail 7
    Given There is 1 user where <Name> is me
    Given I see welcome screen
    When I switch to email sign in screen
    And I have entered login <Login>
    And I have entered password <Password>
    And I tap Log in button
    And I accept First Time overlay as soon as it is visible
    Then I see Conversations list with no conversations

    Examples:
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |
