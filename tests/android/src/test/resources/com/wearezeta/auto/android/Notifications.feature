Feature: Notifications

  @C147866 @staging
  Scenario Outline: Verify push notifications are received after successful registration
    Given I am on Android 4.0 or better
    Given I see welcome screen
    Given I input a new phone number for user <Name>
    Given I input the verification code
    Given I input my name
    Given I select to choose my own picture
    Given I select Camera as picture source
    Given I tap Take Photo button on Take Picture view
    Given I tap Confirm button on Take Picture view
    Given User <Name> is me
    Given There is 1 additional user
    Given Myself is connected to <Contact>
    When I see contact list with name <Contact>
    And I minimize the application
    And User <Contact> sends encrypted message <Message> to user Myself
    Then I see the message "<Message>" in push notifications list

    Examples:
      | Name      | Contact   | Message |
      | user1Name | user2Name | hello   |