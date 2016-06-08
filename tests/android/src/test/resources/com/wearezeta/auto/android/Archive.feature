Feature: Archive

  @C413 @regression
  Scenario Outline: Verify you can archive and unarchive
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given Myself is connected to <Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    # This is to eliminate invitation banner
    Given I open Search UI
    Given I press Clear button
    And I see contact list with name <Contact1>
    When I swipe right on a <Contact1>
    And I select ARCHIVE from conversation settings menu
    Then I do not see contact list with name <Contact1>
    And I swipe up contact list
    And I see contact list with name <Contact1>
    And I swipe right on a <Contact1>
    And I select UNARCHIVE from conversation settings menu
    And I see conversation view

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
    Given I see Contact list with contacts
    # This is to eliminate invitation banner
    Given I open Search UI
    Given I press Clear button
    And I see contact list with name <GroupChatName>
    When I swipe right on a <GroupChatName>
    And I select ARCHIVE from conversation settings menu
    And I navigate back from dialog page
    Then I do not see contact list with name <GroupChatName>
    And I swipe up contact list
    And I see contact list with name <GroupChatName>
    And I swipe right on a <GroupChatName>
    And I select UNARCHIVE from conversation settings menu
    And I see conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName     |
      | user1Name | user2Name | user3Name | ArchivedGroupChat |