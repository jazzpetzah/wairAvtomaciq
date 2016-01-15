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
    And I open search by tap
    And I see People picker page
    And I tap on Search input on People picker page
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
    When I open search by tap
    And I see People picker page
    And I tap on Search input on People picker page
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
    And I open search by tap
    And I see People picker page
    And I tap on Search input on People picker page
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
    When I open search by tap
    And I see People picker page
    And I tap on Search input on People picker page
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
    When I open search by tap
    And I see People picker page
    And I press Clear button
    Then I see Contact list

    Examples:
      | Name      |
      | user1Name |

  @C420 @id2177 @regression
  Scenario Outline: Open/Close Search by different actions
    Given There are 2 users where <Name> is me
    # We need at least 1 user in the convo list, otherwise it will be impossible to swipe
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I open Search by tap
    And I see People picker page
    And I press Clear button
    Then I see Contact list with contacts
    And I do not see TOP PEOPLE
    When I open Search by UI button
    And I see People picker page
    And I swipe down people picker
    Then I see Contact list with contacts
    And I do not see TOP PEOPLE
    When I swipe down contact list
    And I see People picker page
    And I navigate back to Conversations List
    Then I see Contact list with contacts
    And I do not see TOP PEOPLE

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C683 @id319 @regression @rc
  Scenario Outline: I can create group chat from Search results
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I wait until <Contact1> exists in backend search results
    Given I wait until <Contact2> exists in backend search results
    And I open search by tap
    And I see People picker page
    And I tap on Search input on People picker page
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
    Given Contact <Contact1> send message to user <Name>
    Given Contact <Name> send message to user <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I open Search by UI button
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
    When I open search by tap
    And I see People picker page
    And I tap on Search input on People picker page
    And I enter "<Contact1>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact1>
    Then I see action buttons appeared on People picker page

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
    When I open search by tap
    And I see People picker page
    And I tap on Search input on People picker page
    And I enter "<Contact1>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact1>
    And I see open conversation action button on People picker page
    And I click on open conversation action button on People picker page
    Then I see dialog page

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C439 @id3873 @regression
  Scenario Outline: (AN-2894) Verify sending a photo with action button
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I wait until <Contact1> exists in backend search results
    When I open search by tap
    And I see People picker page
    And I tap on Search input on People picker page
    And I enter "<Contact1>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact1>
    And I see Send image action button on People picker page
    And I click Send image action button on People picker page
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
    When I open search by tap
    And I see People picker page
    And I tap on Search input on People picker page
    And I enter "<Contact1>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact1>
    And I see call action button on People picker page
    And I click Call action button on People picker page
    Then I see call overlay
    Then I see calling overlay Big bar

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C442 @id3876 @regression
  Scenario Outline: (AN-2894) Verify sharing a photo to a newly created group conversation with action button
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I wait until <Contact1> exists in backend search results
    When I open search by tap
    And I see People picker page
    And I tap on Search input on People picker page
    And I enter "<Contact1>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact1>
    And I tap on Search input on People picker page
    And I enter "<Contact2>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact2>
    And I see Send image action button on People picker page
    And I click Send image action button on People picker page
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
    When I open search by tap
    And I see People picker page
    And I wait until Top People list appears
    And I tap on <Contact1> in Top People
    Then I see action buttons appeared on People picker page
    When I tap on <Contact1> in Top People
    Then I see action buttons disappear from People Picker page

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C437 @id3870 @regression
  Scenario Outline: (BUG AN-2894) Verify button Open is changed to Create after checking second person
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I wait until <Contact1> exists in backend search results
    Given I wait until <Contact2> exists in backend search results
    When I open search by tap
    And I see People picker page
    And I wait until Top People list appears
    And I see TOP PEOPLE
    And I tap on <Contact1> in Top People
    And I see Open Conversation button on People picker page
    And I tap on <Contact2> in Top People
    And I see Create Conversation button on people picker page
    And I tap on <Contact2> in Top People
    And I see Open Conversation button on People picker page
    And I tap on <Contact1> in Top People
    Then I see action buttons disappear from People Picker page

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
    When I open search by tap
    And I see People picker page
    And I tap on Search input on People picker page
    And I enter "<Contact1>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact1>
    And I tap on Search input on People picker page
    And I enter "<Contact2>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact2>
    And I see Create Conversation button on people picker page
    And I tap Create Conversation button on People picker page
    Then I see dialog page
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
    When I open search by tap
    And I see People picker page
    And I tap on Search input on People picker page
    And I enter "<Contact1>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact1>
    And I tap on Search input on People picker page
    And I enter "<Contact2>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact2>
    And I see call action button on People picker page
    And I click Call action button on People picker page
    Then I see call overlay
    Then I see calling overlay Big bar
    When I tap conversation details button
    Then I see the correct number of participants in the title 3

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
    And I open search by tap
    And I see People picker page
    And I tap on Search input on People picker page
    And I enter "<Contact>" into Search input on People Picker page
    When I see user <Contact> in People Picker
    And I swipe right on contact avatar <Contact> in People Picker
    Then I see user <Contact> in People Picker

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |