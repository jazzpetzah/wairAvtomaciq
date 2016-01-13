Feature: Archive

  @C13 @regression @id1336
  Scenario Outline: Verify unarchive by receiving data
    Given There are 2 users where <Name> is me
    Given Myself is connected to <ArchivedUser>
    Given Myself archived conversation with <ArchivedUser>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    And I dont see conversation <ArchivedUser> in contact list
    Given User <ArchivedUser> sends 1 encrypted message to user Myself
    Then I see first item in contact list named <ArchivedUser>
    When Myself archived conversation with <ArchivedUser>
    And I dont see conversation <ArchivedUser> in contact list
    And Contact <ArchivedUser> sends image <Picture> to single user conversation <Name>
    Then I see first item in contact list named <ArchivedUser>
    When Myself archived conversation with <ArchivedUser>
    And I dont see conversation <ArchivedUser> in contact list
    And Contact <ArchivedUser> ping conversation <Name>
    Then I see first item in contact list named <ArchivedUser>
    When Myself archived conversation with <ArchivedUser>
    And I dont see conversation <ArchivedUser> in contact list
    And <ArchivedUser> calls me using <CallBackend>
    And I see incoming calling message for contact <ArchivedUser>
    And I ignore incoming call
    Then I see first item in contact list named <ArchivedUser>

    Examples: 
      | Name      | ArchivedUser | Picture     | CallBackend |
      | user1Name | user2Name    | testing.jpg | autocall    |

  @C14 @staging @id1337 @ZIOS-3884
  Scenario Outline: Verify unarchiving silenced conversation by ping and call
    Given There are 2 users where <Name> is me
    Given Myself is connected to <ArchivedUser>
    Given Myself silenced conversation with <ArchivedUser>
    Given Myself archived conversation with <ArchivedUser>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    And I dont see conversation <ArchivedUser> in contact list
    Given User <ArchivedUser> sends 1 encrypted message to user Myself
    Then I dont see conversation <ArchivedUser> in contact list
    When Contact <ArchivedUser> sends image <Picture> to single user conversation <Name>
    Then I dont see conversation <ArchivedUser> in contact list
    When Contact <ArchivedUser> ping conversation <Name>
    Then I see first item in contact list named <ArchivedUser>
    When Myself archived conversation with <ArchivedUser>
    And I dont see conversation <ArchivedUser> in contact list
    And <ArchivedUser> calls me using <CallBackend>
    And I see incoming calling message for contact <ArchivedUser>
    And I ignore incoming call
    Then I see first item in contact list named <ArchivedUser>

    Examples: 
      | Name      | ArchivedUser | Picture     | CallBackend |
      | user1Name | user2Name    | testing.jpg | autocall    |

  @C16 @regression @id1339
  Scenario Outline: Verify restoring from archive after adding to conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    When I sign in using my email or phone number
    And I see Contact list with my name <Name>
    And I swipe right on a <GroupChatName>
    And I press Leave button in action menu in Contact List
    And I press leave
    Then I dont see conversation <GroupChatName> in contact list
    When <Contact1> added me to group chat <GroupChatName>
    And User <Contact1> sent message <GroupChatName> to conversation <GroupChatName>
    Then I see first item in contact list named <GroupChatName>

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | LeaveArchive  |