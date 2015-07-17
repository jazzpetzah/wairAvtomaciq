Feature: Conversation List

  @smoke @id1545
  Scenario Outline: Archive and unarchive conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
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
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When I set muted state for conversation <Contact>
    And I open self profile
    Then I see that conversation <Contact> is muted

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @regression @id1919
  Scenario Outline: Unmute 1on1 conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I muted conversation with <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I see that conversation <Contact> is muted
    When I set unmuted state for conversation <Contact>
    And I open self profile
    Then I see that conversation <Contact> is not muted

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @regression @id1720
  Scenario Outline: Verify Ping icon in the conversation list
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    When I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open self profile
    When <Contact> pinged the conversation with <Name>
    And I see ping icon in conversation with <Contact>
    Then I verify ping icon in conversation with <Contact> has <ColorName> color

    Examples: 
      | Login      | Password      | Name      | ColorName  | Contact   |
      | user1Email | user1Password | user1Name | StrongBlue | user2Name |

  @staging @id2998
  Scenario Outline: Verify you silence the conversation when you press ⌥⇧⌘L (Mac) or alt + ctrl + L (Win)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    And I see that conversation <Contact> is not muted
    When I click on options button for conversation <Contact>
    #And I hover mute button for conversation <Contact>
    Then I see correct tooltip for silence button
    When I type shortcut combination to mute the conversation
    Then I see that conversation <Contact> is muted
    When I type shortcut combination to unmute the conversation
    Then I see that conversation <Contact> is not muted

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
