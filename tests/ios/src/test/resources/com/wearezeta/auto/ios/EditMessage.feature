Feature: Edit Message

  @C202349 @regression @fastLogin
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

  @C206256 @regression @fastLogin
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

  @C202354 @regression @fastLogin
  Scenario Outline: Verify I can undo my editing
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the default message and send it
    And I long tap default message in conversation view
    And I tap on Edit badge item
    And I type the "<Text>" message
    And I tap Undo button on Edit control
    Then I see the default message in the conversation input

    Examples:
      | Name      | Contact   | Text    |
      | user1Name | user2Name | message |

  @C202350 @regression @fastLogin
  Scenario Outline: Verify I can cancel editing a message by button
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the default message and send it
    And I long tap default message in conversation view
    And I tap on Edit badge item
    And I type the "<Text>" message
    And I tap Cancel button on Edit control
    Then I see 1 default message in the conversation view
    And I see input placeholder text

    Examples:
      | Name      | Contact   | Text    |
      | user1Name | user2Name | message |

  @C206271 @regression @fastLogin
  Scenario Outline: Verify I can delete message for everyone editing it with nothing/space
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new device <DeviceName>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the default message and send it
    And I long tap default message in conversation view
    And I tap on Edit badge item
    And I clear conversation text input
    And I type the "  " message
    And I tap Confirm button on Edit control
    Then I see 0 default messages in the conversation view
    And User <Contact> verifies that the most recent message type from user Myself is RECALLED via device <DeviceName>

    Examples:
      | Name      | Contact   | DeviceName |
      | user1Name | user2Name | HisDevice  |

  @C202345 @staging @fastLogin
  Scenario Outline: Verify I can edit my message in 1:1
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I type the default message and send it
    And I long tap default message in conversation view
    And I tap on Edit badge item
    And I clear conversation text input
    And I type the "<Text>" message
    And I tap Confirm button on Edit control
    Then I see last message in the conversation view is expected message <Text>
    And I see 0 default messages in the conversation view

    Examples:
      | Name      | Contact   | Text |
      | user1Name | user2Name | new  |

  @C202372 @staging @fastLogin
  Scenario Outline: Verify editing link with a preview
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I type the "<FacebookPrefix> <FacebookLink>" message and send it
    And I long tap on link preview in conversation view
    And I tap on Edit badge item
    And I clear conversation text input
    And I type the "<WirePrefix> <WireLink>" message
    And I tap Confirm button on Edit control
    Then I see link preview container in the conversation view
    And I see link preview source is equal to <WireLink>

    Examples:
      | Name      | Contact   | FacebookLink        | FacebookPrefix | WirePrefix | WireLink        |
      | user1Name | user2Name | http://facebook.com | Check FB       | Look for   | http://wire.com |
