Feature: Archive

  @C2389 @regression @id3991
  Scenario Outline: Verify unarchive by receiving data [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <ArchivedUser>
    Given User Myself archives single user conversation <ArchivedUser>
    Given <ArchivedUser> starts instance using <CallBackend>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    And I do not see conversation <ArchivedUser> in conversations list
    Given User <ArchivedUser> sends 1 encrypted message to user Myself
    Then I see first item in contact list named <ArchivedUser>
    When User Myself archives single user conversation <ArchivedUser>
    And I do not see conversation <ArchivedUser> in conversations list
    Given User <ArchivedUser> sends encrypted image <Picture> to single user conversation Myself
    Then I see first item in contact list named <ArchivedUser>
    When User Myself archives single user conversation <ArchivedUser>
    And I do not see conversation <ArchivedUser> in conversations list
    Given User <ArchivedUser> securely pings conversation <Name>
    Then I see first item in contact list named <ArchivedUser>
    When User Myself archives single user conversation <ArchivedUser>
    And I do not see conversation <ArchivedUser> in conversations list
    And <ArchivedUser> calls me
    And I see call status message contains "<ArchivedUser> calling"
    And I tap Ignore button on Calling overlay
    Then I see first item in contact list named <ArchivedUser>

    Examples:
      | Name      | ArchivedUser | Picture     | CallBackend |
      | user1Name | user2Name    | testing.jpg | autocall    |

  @C2390 @regression @id3992
  Scenario Outline: Verify unarchiving silenced conversation only by call [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <ArchivedUser>
    Given User Myself silences single user conversation <ArchivedUser>
    Given User Myself archives single user conversation <ArchivedUser>
    Given <ArchivedUser> starts instance using <CallBackend>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    And I do not see conversation <ArchivedUser> in conversations list
    Given User <ArchivedUser> sends 1 encrypted message to user Myself
    Then I do not see conversation <ArchivedUser> in conversations list
    Given User <ArchivedUser> sends encrypted image <Picture> to single user conversation Myself
    Then I do not see conversation <ArchivedUser> in conversations list
    When User <ArchivedUser> securely pings conversation <Name>
    And I do not see conversation <ArchivedUser> in conversations list
    And <ArchivedUser> calls me
    And I see call status message contains "<ArchivedUser> calling"
    And I tap Ignore button on Calling overlay
    Then I see first item in contact list named <ArchivedUser>

    Examples:
      | Name      | ArchivedUser | Picture     | CallBackend |
      | user1Name | user2Name    | testing.jpg | autocall    |

  @C2392 @regression @id3994
  Scenario Outline: Verify restoring from archive after adding to conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I swipe right on a <GroupChatName>
    And I tap Leave action button
    And I press leave
    Then I do not see conversation <GroupChatName> in conversations list
    When <Contact1> added me to group chat <GroupChatName>
    Given User <Contact1> sends 1 encrypted message to group conversation <GroupChatName>
    Then I see first item in contact list named <GroupChatName>

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | LeaveArchive  |

  @C95635 @regression
  Scenario Outline: Verify archive behaviour when one archive/unarchive a conversation [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <ArchivedUser>
    Given User Myself archives single user conversation <ArchivedUser>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When User <ArchivedUser> sends 1 message to user Myself
    Then I see conversation <ArchivedUser> in conversations list
    And I see Contacts label at the bottom of conversations list
    And I do not see Archive button at the bottom of conversations list
    When User Myself archives single user conversation <ArchivedUser>
    Then I do not see conversation <ArchivedUser> in conversations list
    And I do not see Contacts label at the bottom of conversations list
    And I see Archive button at the bottom of conversations list
    And I see NO ACTIVE CONVERSATIONS message in conversations list
    When I open archived conversations
    Then I see conversation <ArchivedUser> in conversations list
    When I swipe right on a <ArchivedUser>
    And I tap Unarchive action button
    Then I do not see conversation <ArchivedUser> in conversations list
    When I tap close Archive page button
    Then I see conversation <ArchivedUser> in conversations list
    And I do not see Archive button at the bottom of conversations list
    When I swipe right on a <ArchivedUser>
    And I tap Archive action button
    And I do not see conversation <ArchivedUser> in conversations list
    And I see Archive button at the bottom of conversations list
    And User <ArchivedUser> sends 1 messages to user Myself
    Then I see conversation <ArchivedUser> in conversations list
    And I do not see Archive button at the bottom of conversations list

    Examples:
      | Name      | ArchivedUser|
      | user1Name | user2Name   |
