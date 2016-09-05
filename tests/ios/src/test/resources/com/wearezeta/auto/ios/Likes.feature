Feature: Likes

  @C225979 @C225994 @staging @fastLogin
  Scenario Outline: Verify liking/unliking a message by tapping on like icon
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User <Contact> sends 1 encrypted message to user Myself
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I tap default message in conversation view
    And I remember the state of Like icon in the conversation
    And I tap Like icon in the conversation
    Then I see the state of Like icon is changed in the conversation
    When I tap Unlike icon in the conversation
    Then I see the state of Like icon is not changed in the conversation

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C225980 @C225995 @staging @fastLogin
  Scenario Outline: Verify liking/unliking a message from a message menu
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User <Contact> sends 1 encrypted message to user Myself
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I long tap default message in conversation view
    And I tap on Like badge item
    Then I see Like icon in the conversation
    When I long tap default message in conversation view
    And I tap on Unlike badge item
    Then I do not see Like icon in the conversation
    And I see 1 default message in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C226008 @staging @fastLogin
  Scenario Outline: Verify impossibility of liking the message after leaving (being removed) from a conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <Group> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact1> sends 1 encrypted message to group conversation <Group>
    Given I tap on contact name <Group>
    When <Contact1> removes Myself from group chat <Group>
    And I tap default message in conversation view
    Then I do not see Like icon in the conversation
    When I double tap default message in conversation view
    Then I do not see Like icon in the conversation
    When I long tap default message in conversation view
    Then I do not see Like badge item

    Examples:
      | Name      | Contact1  | Contact2  | Group            |
      | user1Name | user2Name | user3Name | RemovedFromGroup |

  @C225993 @staging @fastLogin
  Scenario Outline: Verify liking a message tapping on like icon, when someone liked this message before
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User Myself sends 1 encrypted message to user <Contact>
    Given I see conversations list
    Given User <Contact> likes the recent message from user Myself
    Given I tap on contact name <Contact>
    When I remember the state of Like icon in the conversation
    And I tap Like icon in the conversation
    Then I see the state of Like icon is changed in the conversation
    When I tap Unlike icon in the conversation
    Then I see the state of Like icon is not changed in the conversation

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C225999 @staging @fastLogin
  Scenario Outline: Verify deleted for myself my message doesn't reappear after someone liked it
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User Myself sends 1 encrypted message to user <Contact>
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I long tap default message in conversation view
    And I tap on Delete badge item
    And I select Delete for Me item from Delete menu
    Then I see 0 default messages in the conversation view
    When User <Contact> likes the recent message from user Myself
    Then I see 0 default messages in the conversation view
    And I do not see Like icon in the conversation

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |
