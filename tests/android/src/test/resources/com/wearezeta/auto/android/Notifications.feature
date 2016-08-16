Feature: Notifications

  @C147866 @regression @rc
  Scenario Outline: Verify push notifications are received after successful registration
    Given I am on Android 4.4 or better
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
    When I see Conversations list with name <Contact>
    And I minimize the application
    And User <Contact> sends encrypted message <Message> to user Myself
    Then I see the message "<Message>" in push notifications list

    Examples:
      | Name      | Contact   | Message |
      | user1Name | user2Name | hello   |

  @C165125 @regression @rc
  Scenario Outline: CM-1023 Verify no GCM notifications are shown for muted chats
    Given I am on Android 4.4 or better
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given User <Contact> sends encrypted message <Message> to user Myself
    Given I swipe right on a <Contact>
    Given I select MUTE from conversation settings menu
    Given Conversation <Contact> is muted
    When I minimize the application
    And I wait for 2 seconds
    And User <Contact> sends encrypted message <Message> to user Myself
    Then I do not see the message "<Message>" in push notifications list

    Examples:
      | Name      | Contact   | Message |
      | user1Name | user2Name | hello   |
