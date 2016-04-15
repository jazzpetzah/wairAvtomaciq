Feature: Archive

  @C2389 @regression @id3991
  Scenario Outline: Verify unarchive by receiving data [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <ArchivedUser>
    Given Myself archived conversation with <ArchivedUser>
    Given <ArchivedUser> starts instance using <CallBackend>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    And I do not see conversation <ArchivedUser> in conversations list
    Given User <ArchivedUser> sends 1 encrypted message to user Myself
    Then I see first item in contact list named <ArchivedUser>
    When Myself archived conversation with <ArchivedUser>
    And I do not see conversation <ArchivedUser> in conversations list
    Given User <ArchivedUser> sends encrypted image <Picture> to single user conversation Myself
    Then I see first item in contact list named <ArchivedUser>
    When Myself archived conversation with <ArchivedUser>
    And I do not see conversation <ArchivedUser> in conversations list
    Given User <ArchivedUser> securely pings conversation <Name>
    Then I see first item in contact list named <ArchivedUser>
    When Myself archived conversation with <ArchivedUser>
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
    Given Myself silenced conversation with <ArchivedUser>
    Given Myself archived conversation with <ArchivedUser>
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