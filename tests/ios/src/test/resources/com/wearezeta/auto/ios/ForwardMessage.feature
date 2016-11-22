Feature: Forward Message

  @C345370 @staging @fastLogin
  Scenario Outline: Verify forwarding own picture
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given User Myself sends encrypted image <Picture> to single user conversation <Contact1>
    Given I see conversations list
    Given I tap on contact name <Contact1>
    Given I long tap on image in conversation view
    When I tap on Forward badge item
    And I select <Contact2> conversation on Forward page
    And I tap Send button on Forward page
    Then I see conversation with user <Contact1>
    When I navigate back to conversations list
    And I tap on contact name <Contact2>
    Then I see 1 photo in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | Picture     |
      | user1Name | user2Name | user3name | testing.jpg |

  @C345383 @staging @fastLogin
  Scenario Outline: ZIOS-7673 Verify outgoing/incoming connection requests/ left conversations are not in a forward list
    Given There are 6 users where <Name> is me
    Given Myself is connected to <ConnectedUser1>,<ConnectedUser2>,<BlockedUser>
    Given Myself has group chat <GroupChatName> with <ConnectedUser1>,<ConnectedUser2>
    Given User Myself blocks user <BlockedUser>
    Given <NonConnectedIncomingUser> sent connection request to Me
    Given Myself sent connection request to <NonConnectedOutgoingUser>
    Given I sign in using my email or phone number
    Given <ConnectedUser1> removes Me from group chat <GroupChatName>
    Given User <ConnectedUser1> sends 1 encrypted message to user Myself
    Given I see conversations list
    Given I tap on contact name <ConnectedUser1>
    Given I long tap default message in conversation view
    When I tap on Forward badge item
    Then I do not see <NonConnectedIncomingUser> conversation on Forward page
    And I do not see <NonConnectedOutgoingUser> conversation on Forward page
    And I do not see <GroupChatName> conversation on Forward page
    And I do not see <BlockedUser> conversation on Forward page

    Examples:
      | Name      | ConnectedUser1 | ConnectedUser2 | NonConnectedIncomingUser | NonConnectedOutgoingUser | BlockedUser | GroupChatName |
      | user1Name | user2Name      | user3name      | user4name                | user5Name                | user6Name   | Group         |
