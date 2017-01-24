Feature: Archive

  @C13 @regression @fastLogin
  Scenario Outline: Verify unarchive by receiving data
    Given There are 2 users where <Name> is me
    Given Myself is connected to <ArchivedUser>
    Given User <ArchivedUser> sets the unique username
    Given <ArchivedUser> starts instance using <CallBackend>
    Given User Myself archives single user conversation <ArchivedUser>
    Given I sign in using my email or phone number
    Given I see conversations list
    And I do not see conversation <ArchivedUser> in conversations list
    And User <ArchivedUser> sends 1 default message to conversation Myself
    Then I see first item in contact list named <ArchivedUser>
    When User Myself archives single user conversation <ArchivedUser>
    And I do not see conversation <ArchivedUser> in conversations list
    And User <ArchivedUser> sends 1 image files <Picture> to conversation Myself
    Then I see first item in contact list named <ArchivedUser>
    When User Myself archives single user conversation <ArchivedUser>
    And I do not see conversation <ArchivedUser> in conversations list
    And User <ArchivedUser> pings conversation <Name>
    Then I see first item in contact list named <ArchivedUser>
    When User Myself archives single user conversation <ArchivedUser>
    And I do not see conversation <ArchivedUser> in conversations list
    And <ArchivedUser> calls me
    And I see call status message contains "<ArchivedUser> calling"
    And I tap Ignore button on Calling overlay
    Then I see first item in contact list named <ArchivedUser>

    Examples:
      | Name      | ArchivedUser | Picture     | CallBackend |
      | user1Name | user2Name    | testing.jpg | chrome      |

  @C14 @regression @fastLogin
  Scenario Outline: Verify unarchiving silenced conversation only by call
    Given There are 2 users where <Name> is me
    Given Myself is connected to <ArchivedUser>
    Given User <ArchivedUser> sets the unique username
    Given <ArchivedUser> starts instance using <CallBackend>
    Given User Myself silences single user conversation <ArchivedUser>
    Given User Myself archives single user conversation <ArchivedUser>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I do not see conversation <ArchivedUser> in conversations list
    And User <ArchivedUser> sends 1 default message to conversation Myself
    Then I do not see conversation <ArchivedUser> in conversations list
    And User <ArchivedUser> sends 1 image file <Picture> to conversation Myself
    Then I do not see conversation <ArchivedUser> in conversations list
    When User <ArchivedUser> pings conversation <Name>
    Then I do not see conversation <ArchivedUser> in conversations list
    And <ArchivedUser> calls me
    And I see call status message contains "<ArchivedUser> calling"
    And I tap Ignore button on Calling overlay
    Then I see first item in contact list named <ArchivedUser>

    Examples:
      | Name      | ArchivedUser | Picture     | CallBackend |
      | user1Name | user2Name    | testing.jpg | chrome      |

  @C16 @regression @fastLogin
  Scenario Outline: Verify restoring from archive after adding to conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I swipe right on a <GroupChatName>
    And I tap Leave conversation action button
    And I confirm Leave conversation action
    Then I do not see conversation <GroupChatName> in conversations list
    When User <Contact1> adds me to group chat <GroupChatName>
    Given User <Contact1> sends 1 default message to conversation <GroupChatName>
    Then I see first item in contact list named <GroupChatName>

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | LeaveArchive  |

  @C82827 @regression @fastLogin
  Scenario Outline: ZIOS-7328 Verify archive behaviour when one archive/unarchive a conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <ArchivedUser>
    Given User Myself archives single user conversation <ArchivedUser>
    Given I sign in using my email or phone number
    Given I see conversations list
    When User <ArchivedUser> sends 1 default message to conversation Myself
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
    And I tap Unarchive conversation action button
    Then I do not see conversation <ArchivedUser> in conversations list
    When I tap close Archive page button
    Then I see conversation <ArchivedUser> in conversations list
    And I do not see Archive button at the bottom of conversations list
    When I swipe right on a <ArchivedUser>
    And I tap Archive conversation action button
    And I do not see conversation <ArchivedUser> in conversations list
    And I see Archive button at the bottom of conversations list
    And User <ArchivedUser> sends 1 default message to conversation Myself
    Then I see conversation <ArchivedUser> in conversations list
    And I do not see Archive button at the bottom of conversations list

    Examples:
      | Name      | ArchivedUser |
      | user1Name | user2Name    |
