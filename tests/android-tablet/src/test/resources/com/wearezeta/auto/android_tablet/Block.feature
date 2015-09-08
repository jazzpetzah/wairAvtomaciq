Feature: Block

  @id3120 @regression
  Scenario Outline: I want to unblock someone from pop-over opened from search (landscape)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given User Myself blocks user <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given Contact <Contact> sends message "<Message>" to user Myself
    And I see the Conversations list with no conversations
    And I wait until <Contact> exists in backend search results
    When I tap Search input
    And I see People Picker page
    And I enter "<Contact>" into Search input on People Picker page
    When I tap the found item <Contact> on People Picker page
    Then I see Blocked Connection popover
    When I tap Unblock button on Blocked Connection popover
    Then I do not see Blocked Connection popover
    # Workaround for https://wearezeta.atlassian.net/browse/AN-2560
    And I close the People Picker
    And I see the conversation <Contact> in my conversations list
    # Workaround for https://wearezeta.atlassian.net/browse/AN-2560
    And I tap the conversation <Contact>
    And I see the message "<Message>" in the conversation view
    When I tap Show Tools button on conversation view page
    And I tap Show Details button on conversation view page
    And I see the Single user popover
    And I tap Options button on Single user popover
    Then I see BLOCK menu item on Single user popover

    Examples: 
      | Name      | Contact   | Message       |
      | user1Name | user2Name | Hellow friend |

  @id2861 @regression
  Scenario Outline: I want to unblock someone from pop-over opened from search (portrait)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given User Myself blocks user <Contact>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given Contact <Contact> sends message "<Message>" to user Myself
    And I see the Conversations list with no conversations
    And I wait until <Contact> exists in backend search results
    When I tap Search input
    And I see People Picker page
    And I enter "<Contact>" into Search input on People Picker page
    When I tap the found item <Contact> on People Picker page
    Then I see Blocked Connection popover
    When I tap Unblock button on Blocked Connection popover
    Then I do not see Blocked Connection popover
    # Workaround for https://wearezeta.atlassian.net/browse/AN-2560
    And I close the People Picker
    And I see the conversation <Contact> in my conversations list
    # Workaround for https://wearezeta.atlassian.net/browse/AN-2560
    And I tap the conversation <Contact>
    And I see the message "<Message>" in the conversation view
    When I tap Show Tools button on conversation view page
    And I tap Show Details button on conversation view page
    And I see the Single user popover
    And I tap Options button on Single user popover
    Then I see BLOCK menu item on Single user popover

    Examples: 
      | Name      | Contact   | Message       |
      | user1Name | user2Name | Hellow friend |

  @id2859 @regression @rc
  Scenario Outline: I block user from 1:1 pop-over and can see blocked user in search results with blocked badge (landscape)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given User Myself blocks user <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    And I see the Conversations list with no conversations
    And I wait until <Contact> exists in backend search results
    When I tap Search input
    And I see People Picker page
    When I enter "<Contact>" into Search input on People Picker page
    Then I see <Contact> avatar on People Picker page
    And I remember <Contact> avatar on People Picker page
    When I tap the found item <Contact> on People Picker page
    And I see Blocked Connection popover
    And I tap Unblock button on Blocked Connection popover
    And I do not see Blocked Connection popover
    Then I verify <Contact> avatar on People Picker page is not the same as the previous one

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @id2858 @regression @rc
  Scenario Outline: I block user from 1:1 pop-over and can see blocked user in search results with blocked badge (portrait)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given User Myself blocks user <Contact>
    Given I rotate UI to portrait
    Given I sign in using my email
    And I see the Conversations list with no conversations
    And I wait until <Contact> exists in backend search results
    When I tap Search input
    And I see People Picker page
    When I enter "<Contact>" into Search input on People Picker page
    Then I see <Contact> avatar on People Picker page
    And I remember <Contact> avatar on People Picker page
    When I tap the found item <Contact> on People Picker page
    And I see Blocked Connection popover
    And I tap Unblock button on Blocked Connection popover
    And I do not see Blocked Connection popover
    Then I verify <Contact> avatar on People Picker page is not the same as the previous one

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @id2866 @staging @torun
  Scenario Outline: Verify you don't receive any messages from blocked person in 1:1 chat
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given User Myself blocks user <Contact>
    When Contact <Contact> sends message "<Message>" to user Myself
    And Contact <Contact> sends image <Picture> to single user conversation <Name>
    And I rotate UI to landscape
    And I sign in using my email
    Then I see the Conversations list with no conversations
    When I tap Search input
    And I see People Picker page
    And I enter "<Contact>" into Search input on People Picker page
    Then I see <Contact> avatar on People Picker page
    When I tap the found item <Contact> on People Picker page
    And I see Blocked Connection popover
    And I tap Unblock button on Blocked Connection popover
    And I do not see Blocked Connection popover
    And I close the People Picker
    Then I see the conversation <Contact> in my conversations list
    # Workaround for https://wearezeta.atlassian.net/browse/AN-2560
    When I tap the conversation <Contact>
    Then I see the message "<Message>" in the conversation view
    And I see a new picture in the conversation view

    Examples:
      | Name      | Contact   | Message | Picture     |
      | user1Name | user2Name | Test    | testing.jpg |
