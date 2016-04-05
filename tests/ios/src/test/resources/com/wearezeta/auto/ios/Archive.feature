Feature: Archive

  @C13 @regression @id1336
  Scenario Outline: Verify unarchive by receiving data
    Given There are 2 users where <Name> is me
    Given Myself is connected to <ArchivedUser>
    Given <ArchivedUser> starts instance using <CallBackend>
    Given Myself archived conversation with <ArchivedUser>
    Given I sign in using my email or phone number
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
    And User <ArchivedUser> securely pings conversation <Name>
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

  @C14 @regression @id1337
  Scenario Outline: Verify unarchiving silenced conversation only by call
    Given There are 2 users where <Name> is me
    Given Myself is connected to <ArchivedUser>
    Given <ArchivedUser> starts instance using <CallBackend>
    Given Myself silenced conversation with <ArchivedUser>
    Given Myself archived conversation with <ArchivedUser>
    Given I sign in using my email or phone number
    Given I see conversations list
    And I do not see conversation <ArchivedUser> in conversations list
    Given User <ArchivedUser> sends 1 encrypted message to user Myself
    Then I do not see conversation <ArchivedUser> in conversations list
    Given User <ArchivedUser> sends encrypted image <Picture> to single user conversation Myself
    Then I do not see conversation <ArchivedUser> in conversations list
    When User <ArchivedUser> securely pings conversation <Name>
    Then I do not see conversation <ArchivedUser> in conversations list
    And <ArchivedUser> calls me
    And I see call status message contains "<ArchivedUser> calling"
    And I tap Ignore button on Calling overlay
    Then I see first item in contact list named <ArchivedUser>

    Examples: 
      | Name      | ArchivedUser | Picture     | CallBackend |
      | user1Name | user2Name    | testing.jpg | autocall    |

  @C16 @regression @id1339
  Scenario Outline: Verify restoring from archive after adding to conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I swipe right on a <GroupChatName>
    And I press Leave button in action menu in Contact List
    And I press leave
    Then I do not see conversation <GroupChatName> in conversations list
    When <Contact1> added me to group chat <GroupChatName>
    Given User <Contact1> sends 1 encrypted message to group conversation <GroupChatName>
    Then I see first item in contact list named <GroupChatName>

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | LeaveArchive  |