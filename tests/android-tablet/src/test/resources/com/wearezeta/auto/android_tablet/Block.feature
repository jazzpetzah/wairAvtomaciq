Feature: Block

  @C520 @id3120 @regression
  Scenario Outline: (AN-2798) I want to unblock someone from pop-over opened from search (landscape)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given User Myself blocks user <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> sends encrypted message <Message> to user Myself
    And I see the Conversations list with no conversations
    And I wait until <Contact> exists in backend search results
    When I open Search UI
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
    And I do not see the message "<Message>" in the conversation view
    When I tap conversation name from top toolbar
    And I see the Single user popover
    And I tap Options button on Single user popover
    Then I see BLOCK menu item on Single user popover

    Examples:
      | Name      | Contact   | Message       |
      | user1Name | user2Name | Hellow friend |

  @C496 @id2861 @regression
  Scenario Outline: (AN-2798) I want to unblock someone from pop-over opened from search (portrait)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given User Myself blocks user <Contact>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> sends encrypted message <Message> to user Myself
    And I see the Conversations list with no conversations
    And I wait until <Contact> exists in backend search results
    When I open Search UI
    And I enter "<Contact>" into Search input on People Picker page
    When I tap the found item <Contact> on People Picker page
    Then I see Blocked Connection popover
    When I tap Unblock button on Blocked Connection popover
    Then I do not see Blocked Connection popover
    And I swipe right to show the conversations list
    # Workaround for https://wearezeta.atlassian.net/browse/AN-2560
    And I close the People Picker
    And I see the conversation <Contact> in my conversations list
    # Workaround for https://wearezeta.atlassian.net/browse/AN-2560
    And I tap the conversation <Contact>
    And I do not see the message "<Message>" in the conversation view
    When I tap conversation name from top toolbar
    And I see the Single user popover
    And I tap Options button on Single user popover
    Then I see BLOCK menu item on Single user popover

    Examples:
      | Name      | Contact   | Message       |
      | user1Name | user2Name | Hellow friend |

  @C765 @id2859 @regression @rc @rc44
  Scenario Outline: I block user from 1:1 pop-over and can see blocked user in search results with blocked badge (landscape)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given User Myself blocks user <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    And I see the Conversations list with no conversations
    And I wait until <Contact> exists in backend search results
    When I open Search UI
    When I enter "<Contact>" into Search input on People Picker page
    Then I see "<Contact>" avatar on People Picker page
    And I remember <Contact> avatar on People Picker page
    When I tap the found item <Contact> on People Picker page
    And I see Blocked Connection popover
    And I tap Unblock button on Blocked Connection popover
    And I do not see Blocked Connection popover
    Then I verify <Contact> avatar on People Picker page is not the same as the previous one

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C764 @id2858 @regression @rc
  Scenario Outline: I block user from 1:1 pop-over and can see blocked user in search results with blocked badge (portrait)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given User Myself blocks user <Contact>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with no conversations
    Given I wait until <Contact> exists in backend search results
    When I open Search UI
    And I enter "<Contact>" into Search input on People Picker page
    Then I see "<Contact>" avatar on People Picker page
    And I remember <Contact> avatar on People Picker page
    When I tap the found item <Contact> on People Picker page
    And I see Blocked Connection popover
    And I tap Unblock button on Blocked Connection popover
    And I do not see Blocked Connection popover
    And I swipe right to show the conversations list
    Then I verify <Contact> avatar on People Picker page is not the same as the previous one

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C767 @id2866 @regression @rc
  Scenario Outline: Verify you don't receive any messages from blocked person in 1:1 chat
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given User Myself blocks user <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with no conversations
    Given User <Contact> sends encrypted image <Picture> to single user conversation Myself
    Given User <Contact> sends encrypted message <Message> to user Myself
    Given I wait until <Contact> exists in backend search results
    When I open Search UI
    And I enter "<Contact>" into Search input on People Picker page
    Then I see "<Contact>" avatar on People Picker page
    When I tap the found item <Contact> on People Picker page
    And I see Blocked Connection popover
    And I tap Unblock button on Blocked Connection popover
    And I do not see Blocked Connection popover
    And I close the People Picker
    Then I see the conversation <Contact> in my conversations list
    # Workaround for https://wearezeta.atlassian.net/browse/AN-2560
    When I tap the conversation <Contact>
    Then I do not see the message "<Message>" in the conversation view
    And I do not see a new picture in the conversation view
    # FIXME: this work properly in manual tests
    # When User <Contact> sends encrypted message "<Message>" to user Myself
    # Then I see the message "<Message>" in the conversation view

    Examples:
      | Name      | Contact   | Message | Picture     |
      | user1Name | user2Name | Test    | testing.jpg |