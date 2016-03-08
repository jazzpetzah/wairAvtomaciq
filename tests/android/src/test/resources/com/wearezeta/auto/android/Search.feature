Feature: Search

  @C313 @id218 @regression
  Scenario Outline: I can search for contact by full name
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I wait until <Contact1> exists in backend search results
    When I tap on contact name <Contact2>
    And I navigate back from dialog page
    And I open Search UI
    And I enter "<Contact1>" into Search input on People Picker page
    Then I see user <Contact1> in People picker

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C315 @id220 @regression
  Scenario Outline: I can search group conversation by full name
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I open Search UI
    And I enter "<GroupChatName>" into Search input on People Picker page
    Then I see group <GroupChatName> in People picker

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName          |
      | user1Name | user3Name | user2Name | PeoplePicker GroupChat |

  @C680 @id223 @regression @rc @rc42
  Scenario Outline: I can search for contact by partial name
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact2>
    And I navigate back from dialog page
    And I open Search UI
    And I input in search field part <Size> of user name to connect to <Contact1>
    Then I see user <Contact1> in People picker

    Examples:
      | Name      | Contact1  | Contact2  | Size |
      | user1Name | user2Name | user3Name | 7    |

  @C380 @id225 @regression
  Scenario Outline: I can search group converation by partial name
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I open Search UI
    And I input in search field part <Size> of user name to connect to <GroupChatName>
    Then I see group <GroupChatName> in People picker

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName           | Size |
      | user1Name | user3Name | user2Name | PeoplePicker GroupChat1 | 5    |

  @C690 @id327 @regression @rc
  Scenario Outline: Open Search by tap in search box and close by UI button
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with no contacts
    When I open Search UI
    And I press Clear button
    Then I see Contact list

    Examples:
      | Name      |
      | user1Name |

  @C56397 @staging
  Scenario Outline: Verify search results cleaned after closing search
    Given I delete all contacts from Address Book
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I wait until <Contact1> exists in backend search results
    Given I wait until <Contact2> exists in backend search results
    When I open Search UI
    And I enter "<Contact2>" into Search input on People Picker page
    And I see user <Contact2> found on People picker page
    And I do not see user <Contact1> found on People picker page
    And I clear search result by tap clear button or back button
    And I see Contact list
    And I open Search UI
    Then I see the search text is empty
    And I do not see search suggestions
    And I see user <Contact1> in contact list of People picker page
    And I do not see user <Contact2> in contact list of People picker page

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C683 @id319 @regression @rc
  Scenario Outline: I can create group chat from Search results
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I wait until <Contact1> exists in backend search results
    Given I wait until <Contact2> exists in backend search results
    And I open Search UI
    And I enter "<Contact1>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact1>
    And I add in search field user name to connect to <Contact2>
    And I tap on user name found on People picker page <Contact2>
    And I tap on create conversation
    Then I see group chat page with users <Contact1>,<Contact2>

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C703 @id1395 @regression @rc @rc42
  Scenario Outline: (AN-2834) Verify starting 1:1 conversation with a person from Top People
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given User <Contact1> sends encrypted message to user Myself
    Given User Me sends encrypted message to user <Contact1>
    When I open Search UI
    And I wait until Top People list appears
    And I tap on <Contact1> in Top People
    And I tap on create conversation
    Then I see dialog page

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C817 @id3867 @regression @rc
  Scenario Outline: Verify action buttons appear after choosing user from search results
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I wait until <Contact1> exists in backend search results
    When I open Search UI
    And I enter "<Contact1>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact1>
    Then I see Open Conversation action button on People Picker page

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C818 @id3871 @regression @rc
  Scenario Outline: Verify opening conversation with action button
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I wait until <Contact1> exists in backend search results
    When I open Search UI
    And I enter "<Contact1>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact1>
    And I tap Open Conversation action button on People Picker page
    Then I see dialog page

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C439 @id3873 @regression
  Scenario Outline: Verify sending a photo with action button
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I wait until <Contact1> exists in backend search results
    When I open Search UI
    And I enter "<Contact1>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact1>
    And I tap Send Image action button on People Picker page
    And I press "Gallery" button
    And I press "Confirm" button
    Then I see new photo in the dialog

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C438 @id3872 @regression
  Scenario Outline: Verify starting a call with action button
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I wait until <Contact1> exists in backend search results
    When I open Search UI
    And I enter "<Contact1>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact1>
    And I tap Call action button on People Picker page
    Then I see outgoing call

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C442 @id3876 @regression
  Scenario Outline: Verify sharing a photo to a newly created group conversation with action button
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I wait until <Contact1> exists in backend search results
    When I open Search UI
    And I enter "<Contact1>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact1>
    And I tap on Search input on People picker page
    And I enter "<Contact2>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact2>
    And I tap Send Image action button on People Picker page
    And I press "Gallery" button
    And I press "Confirm" button
    Then I see new photo in the dialog

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C436 @id3868 @regression
  Scenario Outline: Verify action buttons disappear by unchecking the avatar / deleting token from search field
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I wait until <Contact1> exists in backend search results
    When I open Search UI
    And I wait until Top People list appears
    And I tap on <Contact1> in Top People
    Then I see Open Conversation action button on People Picker page
    When I tap on <Contact1> in Top People
    Then I do not see Open Conversation action button on People Picker page

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C437 @id3870 @regression
  Scenario Outline: Verify button Open is changed to Create after checking second person
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I wait until <Contact1> exists in backend search results
    Given I wait until <Contact2> exists in backend search results
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

  @C440 @id3874 @regression
  Scenario Outline: (AN-2894) Verify starting a new group conversation with action button
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I wait until <Contact1> exists in backend search results
    When I open Search UI
    And I enter "<Contact1>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact1>
    And I tap on Search input on People picker page
    And I enter "<Contact2>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact2>
    And I see Create Conversation action button on People Picker page
    And I tap Create Conversation action button on People Picker page
    Then I see group chat page with users <Contact1>,<Contact2>

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C441 @id3875 @regression
  Scenario Outline: Verify starting a group conversation and a group call with action button
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I wait until <Contact1> exists in backend search results
    When I open Search UI
    And I enter "<Contact1>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact1>
    And I tap on Search input on People picker page
    And I enter "<Contact2>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact2>
    And I tap Call action button on People Picker page
    Then I see outgoing call
#    When I tap conversation details button
#    Then I see the correct number of participants in the title 2

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C447 @id4059 @regression
  Scenario Outline: Verify - swipe right on search results do nothing
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I wait until <Contact> exists in backend search results
    And I open Search UI
    And I enter "<Contact>" into Search input on People Picker page
    When I see user <Contact> in People Picker
    And I swipe right on contact avatar <Contact> in People Picker
    Then I see user <Contact> in People Picker

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C49974 @regression
  Scenario Outline: Verify video call button is only available for single conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I open Search UI
    And I enter "<Contact1>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact1>
    Then I see Video Call action button on People Picker page
    When I add in search field user name to connect to <Contact2>
    And I tap on user name found on People picker page <Contact2>
    Then I do not see Video Call action button on People Picker page
    When I tap on user name found on People picker page <Contact2>
    Then I see Video Call action button on People Picker page
    When I tap on user name found on People picker page <Contact1>
    Then I do not see Video Call action button on People Picker page

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |