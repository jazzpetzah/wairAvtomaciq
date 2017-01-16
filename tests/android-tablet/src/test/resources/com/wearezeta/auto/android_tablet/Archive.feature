Feature: Archive

  @C775 @regression @rc
  Scenario Outline: Verify you can archive and unarchive conversation (portrait)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    When I open options menu of <Contact1> on conversation list page
    Then I see Single conversation options menu
    When I tap <ArchiveItem> button on Single conversation options menu
    Then I do not see Single conversation options menu
    And I swipe right to show the conversations list
    And I do not see conversation <Contact1> in my conversations list
    When I do long swipe up on conversations list
    Then I see the conversation <Contact1> in my conversations list
    When I open options menu of <Contact1> on conversation list page
    Then I see Single conversation options menu
    When I tap <UnarchiveItem> button on Single conversation options menu
    Then I do not see Single conversation options menu
    And I see the conversation view
    When I swipe right to show the conversations list
    Then I see the conversation <Contact1> in my conversations list

    Examples:
      | Name      | Contact1  | Contact2  | ArchiveItem | UnarchiveItem |
      | user1Name | user2Name | user3Name | ARCHIVE     | UNARCHIVE     |

  @C776 @regression @rc @rc44
  Scenario Outline: Verify you can archive and unarchive conversation (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    When I open options menu of <Contact1> on conversation list page
    Then I see Single conversation options menu
    When I tap <ArchiveItem> button on Single conversation options menu
    Then I do not see Single conversation options menu
    And I do not see conversation <Contact1> in my conversations list
    When I do long swipe up on conversations list
    Then I see the conversation <Contact1> in my conversations list
    When I open options menu of <Contact1> on conversation list page
    Then I see Single conversation options menu
    When I tap <UnarchiveItem> button on Single conversation options menu
    Then I do not see Single conversation options menu
    And I see the conversation view
    And I see the conversation <Contact1> in my conversations list

    Examples:
      | Name      | Contact1  | Contact2  | ArchiveItem | UnarchiveItem |
      | user1Name | user2Name | user3Name | ARCHIVE     | UNARCHIVE     |