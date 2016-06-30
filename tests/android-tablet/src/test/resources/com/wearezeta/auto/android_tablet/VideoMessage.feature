Feature: Video Message

  @C164772 @regression @rc
  Scenario Outline: Verify I can send video message
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    Given I tap the conversation <Contact>
    When I tap Video message button from cursor toolbar
    Then I see Video Message container in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |