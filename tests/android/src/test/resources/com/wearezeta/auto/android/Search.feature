Feature: Search

  @C313 @regression
  Scenario Outline: I can search for contact by full name
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I wait until <Contact1> exists in backend search results
    When I tap on conversation name <Contact2>
    And I navigate back from conversation
    And I open Search UI
    And I type user name "<Contact1>" in search field
    Then I see user <Contact1> in Search result list

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C315 @regression
  Scenario Outline: I can search group conversation by full name
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I open Search UI
    And I type group name "<GroupChatName>" in search field
    Then I see group <GroupChatName> in Search result list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName          |
      | user1Name | user3Name | user2Name | PeoplePicker GroupChat |

  @C680 @regression @rc @rc42
  Scenario Outline: I can search for contact by partial name
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I wait until <Contact1> exists in backend search results
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact2>
    And I navigate back from conversation
    And I open Search UI
    And I type the first <Size> chars of user name "<Contact1>" in search field
    Then I see user <Contact1> in Search result list

    Examples:
      | Name      | Contact1  | Contact2  | Size |
      | user1Name | user2Name | user3Name | 7    |

  @C380 @regression
  Scenario Outline: I can search group conversation by partial name
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I open Search UI
    And I type the first <Size> chars of group name "<GroupChatName>" in search field
    Then I see group <GroupChatName> in Search result list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName           | Size |
      | user1Name | user3Name | user2Name | PeoplePicker GroupChat1 | 5    |

  @C690 @regression @rc
  Scenario Outline: Open Search by tap in search box and close by UI button
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with no conversations
    When I open Search UI
    And I press Clear button
    Then I see Conversations list

    Examples:
      | Name      |
      | user1Name |

  @C56397 @regression
  Scenario Outline: Verify search results cleaned after closing search
    Given I delete all contacts from Address Book
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact1> is connected to <Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I wait until <Contact1> exists in backend search results
    Given I wait until <Contact2> exists in backend search results
    When I open Search UI
    And I type user name "<Contact2>" in search field
    And I see user <Contact2> in Search result list
    And I do not see user <Contact1> in Search result list
    And I clear search result by tap clear button or back button
    And I see Conversations list
    And I open Search UI
    Then I see the search text is empty
    And I do not see search suggestions

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C683 @regression @rc
  Scenario Outline: I can create group chat from Search results
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I wait until <Contact1> exists in backend search results
    Given I wait until <Contact2> exists in backend search results
    And I open Search UI
    And I type user name "<Contact1>" in search field
    And I tap on user name found on People picker page <Contact1>
    And I type user name "<Contact2>" in search field
    And I tap on user name found on People picker page <Contact2>
    And I tap on create conversation
    # Workaround for AN-4011, for following two steps
    And I tap conversation name from top toolbar
    And I press back button
    Then I see group chat page with users <Contact1>,<Contact2>

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C703 @regression @rc @rc42
  Scenario Outline: Verify starting 1:1 conversation with a person from Top People
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given Myself wait until 1 person is in the Top People list on the backend
    When I open Search UI
    And I wait until Top People list appears
    And I tap on <Contact1> in Top People
    And I tap on create conversation
    Then I see conversation view

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C817 @regression @rc
  Scenario Outline: Verify action buttons appear after choosing user from search results
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I wait until <Contact1> exists in backend search results
    When I open Search UI
    And I type user name "<Contact1>" in search field
    And I tap on user name found on People picker page <Contact1>
    Then I see Open Conversation action button on People Picker page

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C818 @regression @rc
  Scenario Outline: Verify opening conversation with action button
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I wait until <Contact1> exists in backend search results
    When I open Search UI
    And I type user name "<Contact1>" in search field
    And I tap on user name found on People picker page <Contact1>
    And I tap Open Conversation action button on People Picker page
    Then I see conversation view

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C439 @regression
  Scenario Outline: Verify sending a photo with action button
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I wait until <Contact1> exists in backend search results
    When I open Search UI
    And I type user name "<Contact1>" in search field
    And I tap on user name found on People picker page <Contact1>
    And I tap Send Image action button on People Picker page
    And I tap Gallery Camera button on Take Picture view
    And I tap Confirm button on Take Picture view
    Then I see a picture in the conversation view

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C438 @regression
  Scenario Outline: Verify starting a call with action button
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I wait until <Contact1> exists in backend search results
    When I open Search UI
    And I type user name "<Contact1>" in search field
    And I tap on user name found on People picker page <Contact1>
    And I tap Call action button on People Picker page
    Then I see outgoing call

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C442 @regression
  Scenario Outline: Verify sharing a photo to a newly created group conversation with action button
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I wait until <Contact1> exists in backend search results
    When I open Search UI
    And I type user name "<Contact1>" in search field
    And I tap on user name found on People picker page <Contact1>
    And I tap on Search input on People picker page
    And I type user name "<Contact2>" in search field
    And I tap on user name found on People picker page <Contact2>
    And I tap Send Image action button on People Picker page
    And I tap Gallery Camera button on Take Picture view
    And I tap Confirm button on Take Picture view
    Then I see a picture in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C436 @regression
  Scenario Outline: Verify action buttons disappear by unchecking the avatar / deleting token from search field
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given Myself wait until 1 person is in the Top People list on the backend
    When I open Search UI
    And I wait until Top People list appears
    And I tap on <Contact1> in Top People
    Then I see Open Conversation action button on People Picker page
    When I tap on <Contact1> in Top People
    Then I do not see Open Conversation action button on People Picker page

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C437 @regression
  Scenario Outline: Verify button Open is changed to Create after checking second person
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given Myself wait until 2 people are in the Top People list on the backend
    When I open Search UI
    And I wait until Top People list appears
    And I see TOP PEOPLE
    And I tap on <Contact1> in Top People
    And I see Open Conversation action button on People Picker page
    And I tap on <Contact2> in Top People
    And I see Create Conversation action button on People Picker page
    And I tap on <Contact2> in Top People
    And I see Open Conversation action button on People Picker page
    And I tap on <Contact1> in Top People
    Then I do not see Open Conversation action button on People Picker page

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C440 @regression
  Scenario Outline: Verify starting a new group conversation with action button
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I wait until <Contact1> exists in backend search results
    When I open Search UI
    And I type user name "<Contact1>" in search field
    And I tap on user name found on People picker page <Contact1>
    And I tap on Search input on People picker page
    And I type user name "<Contact2>" in search field
    And I tap on user name found on People picker page <Contact2>
    And I see Create Conversation action button on People Picker page
    And I tap Create Conversation action button on People Picker page
    # Workaround for issue AN-4011 with following two stpes
    And I tap conversation name from top toolbar
    And I press back button
    Then I see group chat page with users <Contact1>,<Contact2>

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C441 @regression
  Scenario Outline: Verify starting a group conversation and a group call with action button
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I wait until <Contact1> exists in backend search results
    When I open Search UI
    And I type user name "<Contact1>" in search field
    And I tap on user name found on People picker page <Contact1>
    And I tap on Search input on People picker page
    And I type user name "<Contact2>" in search field
    And I tap on user name found on People picker page <Contact2>
    And I tap Call action button on People Picker page
    Then I see outgoing call
#    When I tap conversation details button
#    Then I see the correct number of participants in the title 2

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C447 @regression
  Scenario Outline: Verify - swipe right on search results do nothing
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I wait until <Contact> exists in backend search results
    And I open Search UI
    And I type user name "<Contact>" in search field
    And I see user <Contact> in Search result list
    And I swipe right on contact avatar <Contact> in People Picker
    Then I see user <Contact> in Search result list

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C49974 @regression
  Scenario Outline: (AN-3688 / AN-4292) Verify video call button is only available for single conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    # Top people will be generated in 15 minutes after sign up
    Given Myself wait until 2 people are in the Top People list on the backend
    When I open Search UI
    And I type user name "<Contact1>" in search field
    And I tap on user name found on People picker page <Contact1>
    Then I see Video Call action button on People Picker page
    When I type user name "<Contact2>" in search field
    And I tap on user name found on People picker page <Contact2>
    Then I do not see Video Call action button on People Picker page
    When I tap on user name found on People picker page <Contact2>
    Then I see Video Call action button on People Picker page
    When I tap on user name found on People picker page <Contact1>
    Then I do not see Video Call action button on People Picker page

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |