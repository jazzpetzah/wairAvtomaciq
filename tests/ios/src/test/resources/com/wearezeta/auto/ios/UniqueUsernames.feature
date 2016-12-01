Feature: Unique Usernames

  @C352028 @staging
  Scenario Outline: Verify autogeneration of a username for a user with latin characters only
    Given I see sign in screen
    Given I enter phone number for <Name>
    Given I enter activation code
    Given I accept terms of service
    Given I input name <Name> and commit it
    Given I accept alert if visible
    Given I tap Keep This One button
    Given I tap Not Now button on Share Contacts overlay
    # Wait until takeover screen appears
    Given I wait for 7 seconds
    When I see username <WireName> on Unique Username Takeover page
    Then I see unique username <ExpectedUniqueName> on Unique Username Takeover page
    When I tap Keep This One button on Unique Username Takeover page
    Then I see conversations list

    Examples:
      | Name      | ExpectedUniqueName   |
      | user1Name | @user1UniqueUsername |
