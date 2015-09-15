 Feature: Archive
 
  @id2885 @regression @rc
  Scenario Outline: Verify you can archive and unarchive conversation (portrait)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact1> in my conversations list
    When I swipe right the conversations list item <Contact1>
    Then I see Conversation Actions popover
    When I select <ArchiveItem> menu item on Conversation Actions popover
    Then I do not see Conversation Actions popover
    And I do not see conversation <Contact1> in my conversations list
    When I do long swipe up on conversations list
    Then I see the conversation <Contact1> in my conversations list
    When I swipe right the conversations list item <Contact1>
    Then I see Conversation Actions popover
    When I select <UnarchiveItem> menu item on Conversation Actions popover
    Then I do not see Conversation Actions popover
    And I see the conversation view
    When I swipe right to show the conversations list
    Then I see the conversation <Contact1> in my conversations list

    Examples: 
      | Name      | Contact1  | Contact2  | ArchiveItem | UnarchiveItem |
      | user1Name | user2Name | user3Name | ARCHIVE     | UNARCHIVE     |

  @id2886 @regression @rc
  Scenario Outline: Verify you can archive and unarchive conversation (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact1> in my conversations list
    When I swipe right the conversations list item <Contact1>
    Then I see Conversation Actions popover
    When I select <ArchiveItem> menu item on Conversation Actions popover
    Then I do not see Conversation Actions popover
    And I do not see conversation <Contact1> in my conversations list
    When I do long swipe up on conversations list
    Then I see the conversation <Contact1> in my conversations list
    When I swipe right the conversations list item <Contact1>
    Then I see Conversation Actions popover
    When I select <UnarchiveItem> menu item on Conversation Actions popover
    Then I do not see Conversation Actions popover
    And I see the conversation view
    And I see the conversation <Contact1> in my conversations list

    Examples: 
      | Name      | Contact1  | Contact2  | ArchiveItem | UnarchiveItem |
      | user1Name | user2Name | user3Name | ARCHIVE     | UNARCHIVE     |
