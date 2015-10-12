Feature: Search

  @id218 @regression
  Scenario Outline: I can search for contact by full name
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
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

  @id220 @regression
  Scenario Outline: I can search group conversation by full name
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I open search by tap
    And I see People picker page
    And I tap on Search input on People picker page
    And I enter "<GroupChatName>" into Search input on People Picker page
    Then I see group <GroupChatName> in People picker

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName          |
      | user1Name | user3Name | user2Name | PeoplePicker GroupChat |

  @id223 @regression @rc @rc42
  Scenario Outline: I can search for contact by partial name
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
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

  @id225 @regression
  Scenario Outline: I can search group converation by partial name
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I open search by tap
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field part <Size> of user name to connect to <GroupChatName>
    Then I see group <GroupChatName> in People picker

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName           | Size |
      | user1Name | user3Name | user2Name | PeoplePicker GroupChat1 | 5    |

  @id327 @regression @rc
  Scenario Outline: Open Search by tap in search box and close by UI button
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see Contact list with no contacts
    When I open search by tap
    And I see People picker page
    And I press Clear button
    Then I see Contact list

    Examples: 
      | Name      |
      | user1Name |

  @id2177 @regression
  Scenario Outline: Open/Close Search by different actions
    Given There are 2 users where <Name> is me
    # We need at least 1 user in the convo list, otherwise it will be impossible to swipe
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
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

  @id319 @regression @rc
  Scenario Outline: I can create group chat from Search results
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
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
      | Name      | Contact1  | Contact2  | GroupChatName          |
      | user1Name | user2Name | user3Name | PeoplePickerGroupChat2 |

  @id1395 @regression @rc @rc42
  Scenario Outline: (AN-2834) Verify starting 1:1 conversation with a person from Top People
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Contact <Contact1> send message to user <Name>
    Given Contact <Name> send message to user <Contact1>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I open Search by UI button
    And I wait until Top People list appears
    And I tap on <Contact1> in Top People
    And I tap on create conversation
    Then I see dialog page

    Examples: 
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @id2214 @regression @rc
  Scenario Outline: I can dismiss PYMK by Hide button
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact1> is connected to <Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I open search by tap
    And I see People picker page
    And I keep reopening People Picker until PYMK are visible
    And I remember the name of the first PYMK item
    And I do short swipe right on the first PYMK item
    And I click hide button on the first PYMK item
    Then I do not see the previously remembered PYMK item
    When I press Clear button
    And I open search by tap
    Then I do not see the previously remembered PYMK item

    Examples: 
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @id2213 @regression @rc @rc42
  Scenario Outline: (This test should be fixed in PR for the bug) I can dismiss PYMK by swipe
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact1> is connected to <Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I open search by tap
    And I see People picker page
    And I keep reopening People Picker until PYMK are visible
    And I remember the name of the first PYMK item
    And I do long swipe right on the first PYMK item
    Then I do not see the previously remembered PYMK item
    When I press Clear button
    And I open search by tap
    Then I do not see the previously remembered PYMK item

    Examples: 
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @id3867 @staging @rc
  Scenario Outline: Verify action buttons appear after choosing user from search results
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
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
