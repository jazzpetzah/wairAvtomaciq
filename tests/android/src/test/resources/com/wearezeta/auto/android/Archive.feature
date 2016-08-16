Feature: Archive

  @C413 @regression
  Scenario Outline: Verify you can archive and unarchive
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given Myself is connected to <Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    # This is to eliminate invitation banner
    Given I open Search UI
    Given I press Clear button
    And I see Conversations list with name <Contact1>
    When I swipe right on a <Contact1>
    And I select ARCHIVE from conversation settings menu
    Then I do not see Conversations list with name <Contact1>
    When I swipe up Conversations list
    Then I see Conversations list with name <Contact1>
    When I swipe right on a <Contact1>
    And I select UNARCHIVE from conversation settings menu
    Then I see conversation view

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C718 @regression @rc @rc42
  Scenario Outline: Verify you can archive and unarchive group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    # This is to eliminate invitation banner
    Given I open Search UI
    Given I press Clear button
    Then I see Conversations list with name <GroupChatName>
    When I swipe right on a <GroupChatName>
    And I select ARCHIVE from conversation settings menu
    And I navigate back from conversation
    Then I do not see Conversations list with name <GroupChatName>
    Then I swipe up Conversations list
    When I see Conversations list with name <GroupChatName>
    And I swipe right on a <GroupChatName>
    And I select UNARCHIVE from conversation settings menu
    Then I see conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName     |
      | user1Name | user2Name | user3Name | ArchivedGroupChat |