Feature: Edit Message

  @C202349 @staging @fastLogin
  Scenario Outline: Verify I cannot edit/delete everywhere another users message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User <Contact> sends 1 encrypted message to user Myself
    Given I see conversations list
    When I tap on contact name <Contact>
    Then I see 1 default message in the conversation view
    When I long tap default message in conversation view
    Then I do not see Edit badge item
    And I tap on Delete badge item
    And I do not see Delete for everyone item in Delete menu

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C206256 @staging @fastLogin
  Scenario Outline: Verify impossibility of editing/deleting everywhere message after leaving/being removed from a conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <GroupChatName>
    And I type the default message and send it
    And <Contact1> removes Myself from group chat <GroupChatName>
    And I long tap default message in conversation view
    Then I do not see Edit badge item
    And I tap on Delete badge item
    And I do not see Delete for everyone item in Delete menu

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | RemoveToEdit  |
