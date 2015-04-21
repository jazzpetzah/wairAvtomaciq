Feature: Conversation List

  @smoke @id1545
  Scenario Outline: Archive and unarchive conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    When I archive conversation <Contact>
    Then I do not see Contact list with name <Contact>
    When I open archive
    And I unarchive conversation <Contact>
    Then I see Contact list with name <Contact>

    Examples:
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @smoke @id1918
  Scenario Outline: Mute 1on1 conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    When I set muted state for conversation <Contact>
    And I open self profile
    Then I see that conversation <Contact> is muted

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @smoke @id1919
  Scenario Outline: Unmute 1on1 conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Name> muted conversation with <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    And I see that conversation <Contact> is muted
    When I set unmuted state for conversation <Contact>
    And I open self profile
    Then I see that conversation <Contact> is not muted

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
