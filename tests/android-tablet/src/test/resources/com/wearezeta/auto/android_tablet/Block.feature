Feature: Block

  @C520 @regression
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
    And I enter "<Contact>" into Search input on Search page
    When I tap on user name found on Search page <Contact>
    Then I see Single blocked user details popover
    When I tap unblock button on Single blocked user details popover
    Then I do not see Single blocked user details popover
    # Workaround for https://wearezeta.atlassian.net/browse/AN-2560
    And I close the Search
    And I see the conversation <Contact> in my conversations list
    # Workaround for https://wearezeta.atlassian.net/browse/AN-2560
    And I tap the conversation <Contact>
    And I do not see the message "<Message>" in the conversation view
    When I tap conversation name from top toolbar
    And I see Single connected user details popover
    And I tap open menu button on Single connected user details popover
    Then I see BLOCK button on Single conversation options menu

    Examples:
      | Name      | Contact   | Message       |
      | user1Name | user2Name | Hellow friend |

  @C496 @regression
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
    And I enter "<Contact>" into Search input on Search page
    When I tap on user name found on Search page <Contact>
    Then I see Single blocked user details popover
    When I tap unblock button on Single blocked user details popover
    Then I do not see Single blocked user details popover
    And I swipe right to show the conversations list
    # Workaround for https://wearezeta.atlassian.net/browse/AN-2560
    And I close the Search
    And I see the conversation <Contact> in my conversations list
    # Workaround for https://wearezeta.atlassian.net/browse/AN-2560
    And I tap the conversation <Contact>
    And I do not see the message "<Message>" in the conversation view
    When I tap conversation name from top toolbar
    And I see Single connected user details popover
    And I tap open menu button on Single connected user details popover
    Then I see BLOCK button on Single conversation options menu

    Examples:
      | Name      | Contact   | Message       |
      | user1Name | user2Name | Hellow friend |

  @C765 @regression @rc @rc44
  Scenario Outline: (AN-4538) I block user from 1:1 pop-over and can see blocked user in search results with blocked badge (landscape)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given User Myself blocks user <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    And I see the Conversations list with no conversations
    And I wait until <Contact> exists in backend search results
    When I open Search UI
    When I enter "<Contact>" into Search input on Search page
    Then I see "<Contact>" avatar in Search result list
    And I remember <Contact> avatar on Search page
    When I tap on user name found on Search page <Contact>
    And I see Single blocked user details popover
    And I tap unblock button on Single blocked user details popover
    And I do not see Single blocked user details popover
    Then I verify <Contact> avatar on Search page is not the same as the previous one

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C764 @regression @rc
  Scenario Outline: (AN-4538) I block user from 1:1 pop-over and can see blocked user in search results with blocked badge (portrait)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given User Myself blocks user <Contact>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with no conversations
    Given I wait until <Contact> exists in backend search results
    When I open Search UI
    And I enter "<Contact>" into Search input on Search page
    Then I see "<Contact>" avatar in Search result list
    And I remember <Contact> avatar on Search page
    When I tap on user name found on Search page <Contact>
    And I see Single blocked user details popover
    And I tap unblock button on Single blocked user details popover
    And I do not see Single blocked user details popover
    And I swipe right to show the conversations list
    Then I verify <Contact> avatar on Search page is not the same as the previous one

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C767 @regression @rc
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
    And I enter "<Contact>" into Search input on Search page
    Then I see "<Contact>" avatar in Search result list
    When I tap on user name found on Search page <Contact>
    And I see Single blocked user details popover
    And I tap unblock button on Single blocked user details popover
    And I do not see Single blocked user details popover
    And I close the Search
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