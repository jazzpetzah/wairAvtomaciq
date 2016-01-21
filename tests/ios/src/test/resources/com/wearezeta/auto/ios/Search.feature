Feature: Search

  @C2783 @regression @id2147
  Scenario Outline: Verify search by email
    Given There are 2 users where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user email <ContactEmail>
    And I press keyboard Return button
    Then I see user <ContactName> found on People picker page

    Examples:
      | Name      | ContactEmail | ContactName |
      | user1Name | user2Email   | user2Name   |

  @C1036 @regression @rc @id2148 @id2543
  Scenario Outline: Verify search by name
    Given There are 2 users where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I open search by taping on it
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    Then I see user <Contact> found on People picker page

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C1060 @regression @id299 @noAcceptAlert @obsolete
  Scenario Outline: Verify denying address book uploading
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    And I dismiss all alerts
    And I dismiss settings warning
    And I open search by taping on it
    And I see Upload contacts dialog
    And I click Continue button on Upload dialog
    And I dismiss alert
    And I dont see CONNECT label
    And I press maybe later button
    And I click clear button
    And I open search by taping on it
    And I scroll up page a bit
    And I dont see Upload contacts dialog

    Examples:
      | Name      |
      | user1Name |

  #regression
  @C1061 @staging @id311 @deployAddressBook @noAcceptAlert @obsolete
  Scenario Outline: Verify uploading address book to the server
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    And I dismiss all alerts
    And I dismiss settings warning
    And I open search by taping on it
    And I see Upload contacts dialog
    And I click Continue button on Upload dialog
    And I accept alert
    Then I see CONNECT label
    And I see user <Contact1> found on People picker page
    And I see user <Contact2> found on People picker page

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C3167 @regression @rc @id1394
  Scenario Outline: Start 1:1 chat with users from Top Connections
    Given There are <UserCount> users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
    Given I see conversations list
    When I wait for 30 seconds
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    Then I tap on first 1 top connections
    And I click open conversation button on People picker page
    And I wait for 2 seconds
    And I see dialog page

    Examples:
      | Name      | UserCount |
      | user1Name | 4         |

  @C1069 @regression @rc @id1150
  Scenario Outline: Start group chat with users from Top Connections
    Given There are <UserCount> users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
    Given I see conversations list
    When I wait for 30 seconds
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    Then I tap on first 2 top connections
    And I click Create Conversation button on People picker page
    And I wait for 2 seconds
    And I open group conversation details
    And I change group conversation name to <ConvoName>
    And I exit the group info page
    And I return to the chat list
    And I see first item in contact list named <ConvoName>

    Examples:
      | Name      | ConvoName    | UserCount |
      | user1Name | TopGroupTest | 4         |

  @C40 @regression @rc @id1454
  Scenario Outline: Verify sending a connection request to user chosen from search
    Given There are 2 users where <Name> is me
    Given User <UnconnectedUser> name starts with <StartLetter>
    Given User <Name> change accent color to <Color>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I search for user name <UnconnectedUser> and tap on it on People picker page
    Then I see connect to <UnconnectedUser> dialog
    And I click Connect button on connect to dialog
    Then I see the user <UnconnectedUser> avatar with a clock
    And I click close button to dismiss people view
    And I see conversation with not connected user <UnconnectedUser>
    And I tap on contact name <UnconnectedUser>
    And I swipe up on pending dialog page to open other user pending personal page
    And I see <UnconnectedUser> user pending profile page

    Examples:
      | Name      | UnconnectedUser | StartLetter | Color        |
      | user1Name | user2Name       | T           | BrightOrange |

  @C3220 @regression @id763
  Scenario Outline: I can still search for other people using the search field, regardless of whether I already added people from Top conversations
    Given There are <UserCount> users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
    Given I see conversations list
    When I wait for 30 seconds
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    And I tap on 3 top connections but not <Contact>
    #And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I tap on connected user <Contact> on People picker page
    Then I see that <Number> contacts are selected

    Examples:
      | Name      | UserCount | Contact   | Number |
      | user1Name | 7         | user2Name | 4      |

  @C3244 @regression @id1456
  Scenario Outline: Verify you can unblock someone from search list
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Name> blocks user <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I dont see conversation <Contact> in contact list
    And I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I tap on connected user <Contact> on People picker page
    And I unblock user
    And I type the default message
    And I send the message
    Then I see 1 default message in the dialog

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2785 @regression @id2149
  Scenario Outline: Verify search by second name (something after space)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Contact> change name to <NewName>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I open search by taping on it
    And I wait until <LastName> exists in backend search results
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <LastName>
    Then I see user <NewName> found on People picker page

    Examples:
      | Name      | Contact   | NewName  | LastName |
      | user1Name | user2Name | NEW NAME | NAME     |

  @C1033 @regression @rc @id2540 @id2118
  Scenario Outline: Verify sending connection request from PYMK
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact1> is connected to <Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if CONNECT label is not there
    And I see CONNECT label
    And I press the instant connect button
    And I click close button to dismiss people view
    Then I see first item in contact list named <Contact2>

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C1049 @regression @rc @id3282
  Scenario Outline: Verify starting a call with action button
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    Then I see user <Contact> found on People picker page
    When I tap on connected user <Contact> on People picker page
    And I see call action button on People picker page
    And I click call action button on People picker page
    Then I see mute call, end call and speakers buttons
    And I see calling to contact <Contact> message

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C1053 @regression @rc @id3286
  Scenario Outline: Verify sharing a photo to a newly created group conversation with action button
    Given There are 4 users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
    Given I see conversations list
    When I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    Then I tap on first 3 top connections
    When I see Send image action button on People picker page
    And I click Send image action button on People picker page
    And I press Camera Roll button
    And I choose a picture from camera roll
    And I press Confirm button
    Then I see group chat page with 3 users <Contact1> <Contact2> <Contact3>
    And I see new photo in the dialog
    When I navigate back to conversations view
    Then I see in contact list group chat with <Contact1> <Contact2> <Contact3>

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  |
      | user1Name | user2Name | user3Name | user4Name |

  @C1043 @regression @id3276
  Scenario Outline: Verify action buttons appear after selecting person from Top People
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    When I tap on first 1 top connections
    Then I see action buttons appeared on People picker page

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C1044 @regression @id3277
  Scenario Outline: Verify action buttons appear after choosing user from search results
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I open search by taping on it
    And I see People picker page
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    When I tap on connected user <Contact> on People picker page
    Then I see action buttons appeared on People picker page

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C1047 @regression @id3280
  Scenario Outline: Verify button Open is changed on Create after checking second person
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
    Given I see conversations list
    When I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    When I tap on 1st top connection contact
    Then I see open conversation action button on People picker page
    When I tap on 2nd top connection contact
    Then I see Create Conversation button on People picker page

    Examples:
      | Name      |
      | user1Name |

  @C1045 @regression @id3278
  Scenario Outline: Verify action buttons disappear by unchecking the avatar
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
    Given I see conversations list
    When I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    When I tap on 1st top connection contact
    Then I see action buttons appeared on People picker page
    When I tap on 1st top connection contact
    Then I see action buttons disappeared on People picker page

    Examples:
      | Name      |
      | user1Name |

  @C1046 @regression @id3279
  Scenario Outline: Verify action buttons disappear by deleting token from a search field
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
    Given I see conversations list
    When I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    And I tap on 1st top connection contact
    And I see action buttons appeared on People picker page
    And I tap on 2nd top connection contact
    And I see Create Conversation button on People picker page
    And I press backspace button
    And I press backspace button
    Then I see open conversation action button on People picker page
    And I press backspace button
    And I press backspace button
    Then I see action buttons disappeared on People picker page

    Examples:
      | Name      |
      | user1Name |

  @C1048 @regression @id3281
  Scenario Outline: Verify opening conversation with action button
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
    Given I see conversations list
    When I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    And I tap on 1st top connection contact
    And I see open conversation action button on People picker page
    And I click open conversation action button on People picker page
    Then I see dialog page

    Examples:
      | Name      |
      | user1Name |

  @C1042 @regression @id4118
  Scenario Outline: Verify action buttons appear after choosing user from search results
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
    Given I see conversations list
    When I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see Invite more people button
    And I tap on 1st top connection contact
    And I DONT see Invite more people button
    And I see action buttons appeared on People picker page
    And I tap on 1st top connection contact
    And I see action buttons disappeared on People picker page
    And I see Invite more people button
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I tap on connected user <Contact> on People picker page
    And I DONT see Invite more people button
    And I see action buttons appeared on People picker page
    And I press backspace button
    And I press backspace button
    And I see action buttons disappeared on People picker page
    Then I see Invite more people button

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |