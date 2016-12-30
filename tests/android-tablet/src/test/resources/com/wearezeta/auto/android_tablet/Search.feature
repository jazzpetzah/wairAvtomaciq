Feature: Search

  @C490 @regression
  Scenario Outline: I ignore someone from search and clear my inbox (portrait)
    Given There are 2 users where <Name> is me
    Given <Contact> sent connection request to me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with conversation
    Given I wait until <Contact> exists in backend search results
    When I open Search UI
    And I enter "<Contact>" into Search input on Search page
    And I tap on user name found on Search page <Contact>
    And I see user name "<Contact>" on Single pending incoming connection page
    And I tap ignore button on Single pending incoming connection page
    And I swipe right to show the conversations list
    Then I see the Conversations list with no conversations

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C524 @regression
  Scenario Outline: I ignore someone from search and clear my inbox (landscape)
    Given There are 2 users where <Name> is me
    Given <Contact> sent connection request to me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with conversation
    Given I wait until <Contact> exists in backend search results
    When I open Search UI
    And I enter "<Contact>" into Search input on Search page
    And I tap on user name found on Search page <Contact>
    And I see user name "<Contact>" on Single pending incoming connection page
    And I tap ignore button on Single pending incoming connection page
    Then I see the Conversations list with no conversations

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C762 @regression @rc
  Scenario Outline: I want to discard the new connect request (sending) by returning to the search results after selecting someone I’m not connected to
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with no conversations
    And I wait until <Contact> exists in backend search results
    When I open Search UI
    And I enter "<Contact>" into Search input on Search page
    And I tap on user name found on Search page <Contact>
    And I see Single unconnected user details popover
    And I press Back button 1 time
    And I do not see Single unconnected user details popover
    And I see Search page
    And I close Search
    Then I see the Conversations list with no conversations

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C819 @regression @rc @rc44
  Scenario Outline: Verify opening conversation with action button (landscape)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with conversations
    And I wait until <Contact> exists in backend search results
    And I open Search UI
    And I enter "<Contact>" into Search input on Search page
    And I tap on user name found on Search page <Contact>
    When I tap Open Conversation action button on Search page
    Then I do not see Search page
    And I see the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C820 @regression @rc
  Scenario Outline: Verify opening conversation with action button (portrait)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with conversations
    And I wait until <Contact> exists in backend search results
    And I open Search UI
    And I enter "<Contact>" into Search input on Search page
    And I tap on user name found on Search page <Contact>
    When I tap Open Conversation action button on Search page
    Then I do not see Search page
    And I see the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C537 @regression
  Scenario Outline: Verify starting a new group conversation with action button (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I wait until <Contact1> exists in backend search results
    Given I wait until <Contact2> exists in backend search results
    Given I see the Conversations list with conversations
    And I open Search UI
    And I enter "<Contact1>" into Search input on Search page
    And I tap on user name found on Search page <Contact1>
    And I enter "<Contact2>" into Search input on Search page
    And I tap on user name found on Search page <Contact2>
    When I tap Create Conversation action button on Search page
    Then I do not see Search page
    When I tap conversation name from top toolbar
    Then I see participant <Contact1> on Group info popover
    And I see participant <Contact2> on Group info popover
    And I do not see participant Myself on Group info popover

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C545 @regression
  Scenario Outline: Verify starting a new group conversation with action button (portrait)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I wait until <Contact1> exists in backend search results
    Given I wait until <Contact2> exists in backend search results
    Given I see the Conversations list with conversations
    And I open Search UI
    And I enter "<Contact1>" into Search input on Search page
    And I tap on user name found on Search page <Contact1>
    And I enter "<Contact2>" into Search input on Search page
    And I tap on user name found on Search page <Contact2>
    When I tap Create Conversation action button on Search page
    Then I do not see Search page
    When I tap conversation name from top toolbar
    Then I see participant <Contact1> on Group info popover
    And I see participant <Contact2> on Group info popover
    And I do not see participant Myself on Group info popover

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C536 @regression
  Scenario Outline: Verify sending a photo with action button (landscape)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with conversations
    Given I wait until <Contact> exists in backend search results
    And I open Search UI
    And I enter "<Contact>" into Search input on Search page
    And I tap on user name found on Search page <Contact>
    When I tap Send Image action button on Search page
    And I tap Take Photo button on Take Picture view
    And I tap Confirm button on Take Picture view
    Then I see a new picture in the conversation view
    And I do not see Search page

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C539 @regression
  Scenario Outline: Verify sharing a photo to a newly created group conversation with action button (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I wait until <Contact1> exists in backend search results
    Given I wait until <Contact2> exists in backend search results
    Given I see the Conversations list with conversations
    And I open Search UI
    And I enter "<Contact1>" into Search input on Search page
    And I tap on user name found on Search page <Contact1>
    And I enter "<Contact2>" into Search input on Search page
    And I tap on user name found on Search page <Contact2>
    When I tap Send Image action button on Search page
    And I tap Take Photo button on Take Picture view
    And I tap Confirm button on Take Picture view
    Then I see a new picture in the conversation view
    And I do not see Search page
    When I tap conversation name from top toolbar
    And I see Group info popover
    Then I do not see participant Myself on Group info popover
    And I see participant <Contact1> on Group info popover
    And I see participant <Contact2> on Group info popover

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C547 @regression
  Scenario Outline: Verify sharing a photo to a newly created group conversation with action button (portrait)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with conversations
    Given I wait until <Contact1> exists in backend search results
    Given I wait until <Contact2> exists in backend search results
    And I open Search UI
    And I enter "<Contact1>" into Search input on Search page
    And I tap on user name found on Search page <Contact1>
    And I enter "<Contact2>" into Search input on Search page
    And I tap on user name found on Search page <Contact2>
    When I tap Send Image action button on Search page
    And I tap Take Photo button on Take Picture view
    And I tap Confirm button on Take Picture view
    Then I see a new picture in the conversation view
    And I do not see Search page
    When I tap conversation name from top toolbar
    And I see Group info popover
    Then I do not see participant Myself on Group info popover
    And I see participant <Contact1> on Group info popover
    And I see participant <Contact2> on Group info popover

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C534 @regression
  Scenario Outline: (AN-4031) Verify button Open is changed to Create after checking second person (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with conversations
    Given I wait until <Contact1> exists in backend search results
    Given I wait until <Contact2> exists in backend search results
    And I open Search UI
    And I enter "<Contact1>" into Search input on Search page
    When I tap on user name found on Search page <Contact1>
    Then I see Open Conversation action button on Search page
    When I enter "<Contact2>" into Search input on Search page
    And I tap on user name found on Search page <Contact2>
    Then I see Create Conversation action button on Search page
    When I type backspace in Search input on Search page
    Then I see Open Conversation action button on Search page
    When I type backspace in Search input on Search page
    Then I do not see Open Conversation action button on Search page

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C542 @regression
  Scenario Outline: (AN-4031) Verify button Open is changed to Create after checking second person (portrait)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with conversations
    Given I wait until <Contact1> exists in backend search results
    Given I wait until <Contact2> exists in backend search results
    And I open Search UI
    And I enter "<Contact1>" into Search input on Search page
    When I tap on user name found on Search page <Contact1>
    Then I see Open Conversation action button on Search page
    When I enter "<Contact2>" into Search input on Search page
    And I tap on user name found on Search page <Contact2>
    Then I see Create Conversation action button on Search page
    When I type backspace in Search input on Search page
    Then I see Open Conversation action button on Search page
    When I type backspace in Search input on Search page
    Then I do not see Open Conversation action button on Search page

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C535 @regression
  Scenario Outline: Verify starting a call with action button (landscape)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with conversations
    And I wait until <Contact> exists in backend search results
    And I open Search UI
    And I enter "<Contact>" into Search input on Search page
    And I tap on user name found on Search page <Contact>
    When I tap Call action button on Search page
    Then I do not see Search page
    And I see outgoing call

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C543 @regression
  Scenario Outline: Verify starting a call with action button (portrait)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with conversations
    And I wait until <Contact> exists in backend search results
    And I open Search UI
    And I enter "<Contact>" into Search input on Search page
    And I tap on user name found on Search page <Contact>
    When I tap Call action button on Search page
    Then I do not see Search page
    And I see outgoing call

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C538 @regression
  Scenario Outline: Verify starting a group conversation and a group call with action button (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I wait until <Contact1> exists in backend search results
    Given I wait until <Contact2> exists in backend search results
    Given I see the Conversations list with conversations
    And I open Search UI
    And I enter "<Contact1>" into Search input on Search page
    And I tap on user name found on Search page <Contact1>
    And I enter "<Contact2>" into Search input on Search page
    And I tap on user name found on Search page <Contact2>
    When I tap Call action button on Search page
    Then I see outgoing call

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C546 @regression
  Scenario Outline: Verify starting a group conversation and a group call with action button (portrait)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I wait until <Contact1> exists in backend search results
    Given I wait until <Contact2> exists in backend search results
    Given I see the Conversations list with conversations
    And I open Search UI
    And I enter "<Contact1>" into Search input on Search page
    And I tap on user name found on Search page <Contact1>
    And I enter "<Contact2>" into Search input on Search page
    And I tap on user name found on Search page <Contact2>
    When I tap Call action button on Search page
    Then I do not see Search page
    Then I see outgoing call

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |