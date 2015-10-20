Feature: Archive

  @staging @rc @id1336
  Scenario Outline: Verify unarchive by receiving data
    Given There are 2 users where <Name> is me
    Given Myself is connected to <ArchivedUser>
    Given Myself archived conversation with <ArchivedUser>
    Given I sign in using my email or phone number
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
