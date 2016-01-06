Feature: Archive

  @C2384 @regression @id2325
  Scenario Outline: Verify unarchive by receiving data [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <ArchivedUser>
    Given Myself archived conversation with <ArchivedUser>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    And I dont see conversation <ArchivedUser> in contact list
    When Contact <ArchivedUser> send message to user <Name>
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

  @C2389 @regression @id3991
  Scenario Outline: Verify unarchive by receiving data [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <ArchivedUser>
    Given Myself archived conversation with <ArchivedUser>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    And I dont see conversation <ArchivedUser> in contact list
    When Contact <ArchivedUser> send message to user <Name>
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

  @C2385 @staging @id2326 @ZIOS-3884
  Scenario Outline: Verify unarchiving silenced conversation by ping and call [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <ArchivedUser>
    Given Myself silenced conversation with <ArchivedUser>
    Given Myself archived conversation with <ArchivedUser>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    And I dont see conversation <ArchivedUser> in contact list
    When Contact <ArchivedUser> send message to user <Name>
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

  @C2390 @staging @id3992 @ZIOS-3884
  Scenario Outline: Verify unarchiving silenced conversation by ping and call [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <ArchivedUser>
    Given Myself silenced conversation with <ArchivedUser>
    Given Myself archived conversation with <ArchivedUser>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    And I dont see conversation <ArchivedUser> in contact list
    When Contact <ArchivedUser> send message to user <Name>
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

  @C2387 @regression @id2328
  Scenario Outline: Verify restoring from archive after adding to conversation [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in on tablet using my email
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

  @C2392 @regression @id3994
  Scenario Outline: Verify restoring from archive after adding to conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
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