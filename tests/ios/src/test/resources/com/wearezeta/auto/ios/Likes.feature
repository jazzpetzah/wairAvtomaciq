Feature: Likes

  @C225979 @C225994 @staging @fastLogin
  Scenario Outline: Verify liking/unliking a message by tapping on like icon
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User <Contact> sends encrypted message "<Msg>" to user Myself
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I tap on message "<Msg>"
    And I remember the state of Like icon in the conversation
    And I tap Like icon in the conversation
    Then I see the state of Like icon is changed in the conversation
    When I tap Unlike icon in the conversation
    Then I see the state of Like icon is not changed in the conversation

    Examples:
      | Name      | Contact   | Msg   |
      | user1Name | user2Name | Hello |
