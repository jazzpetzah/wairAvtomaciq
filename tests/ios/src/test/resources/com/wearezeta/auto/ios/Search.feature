Feature: Search

  @C1035 @rc @clumsy @regression @id2147
  Scenario Outline: Verify search by email
    Given There are 2 users where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I open search UI
    And I input in People picker search field user email <ContactEmail>
    Then I see the conversation "<ContactName>" exists in Search results

    Examples:
      | Name      | ContactEmail | ContactName |
      | user1Name | user2Email   | user2Name   |

  @C1036 @rc @clumsy @regression @id2148 @id2543
  Scenario Outline: Verify search by name
    Given There are 2 users where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I open search UI
    And I input in People picker search field user name <Contact>
    Then I see the conversation "<Contact>" exists in Search results

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C3167 @rc @regression @id1394
  Scenario Outline: Start 1:1 chat with users from Top Connections
    Given There are <UserCount> users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I wait until my Top People list is not empty on the backend
    And I open search UI
    And I see top people list on People picker page
    Then I tap on first 1 top connections
    And I tap Open conversation action button on People picker page
    And I wait for 2 seconds
    And I see conversation view page

    Examples:
      | Name      | UserCount |
      | user1Name | 4         |

  @C1069 @rc @regression @id1150
  Scenario Outline: Start group chat with users from Top Connections
    Given There are 4 users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I wait until my Top People list is not empty on the backend
    And I open search UI
    And I see top people list on People picker page
    Then I tap on first 2 top connections
    When I tap Create conversation action button on People picker page
    And I see conversation view page
    And I open group conversation details
    Then I see <ParticipantsCount> participant avatars

    Examples:
      | Name      | ParticipantsCount |
      | user1Name | 2                 |

  @C40 @rc @regression @id1454
  Scenario Outline: Verify sending a connection request to user chosen from search
    Given There are 2 users where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I open search UI
    And I input in People picker search field conversation name <UnconnectedUser>
    And I tap on conversation <UnconnectedUser> in search result
    Then I see connect to <UnconnectedUser> dialog
    And I click Connect button on connect to dialog
    And I click close button to dismiss people view
    And I tap on contact name <UnconnectedUser>
    And I open conversation details
    And I see <UnconnectedUser> user pending profile page

    Examples:
      | Name      | UnconnectedUser |
      | user1Name | user2Name       |

  @C3220 @regression @id763
  Scenario Outline: I can still search for other people using the search field, regardless of whether I already added people from Top conversations
    Given There are <UserCount> users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I wait until my Top People list is not empty on the backend
    And I open search UI
    And I tap on 3 top connections but not <Contact>
    And I input in People picker search field user name <Contact>
    And I tap on conversation <Contact> in search result
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
    When I do not see conversation <Contact> in conversations list
    And I open search UI
    And I input in People picker search field user name <Contact>
    And I tap on conversation <Contact> in search result
    And I unblock user
    And I type the default message and send it
    Then I see 1 default message in the dialog

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2785 @regression @id2149
  Scenario Outline: Verify search by part of the name
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I open search UI
    And I wait until <Contact> exists in backend search results
    And I input in People picker search field first 5 letters of user name <Contact>
    Then I see the conversation "<Contact>" exists in Search results

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C1049 @rc @clumsy @regression @id3282
  Scenario Outline: Verify starting a call with action button
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I open search UI
    And I input in People picker search field user name <Contact>
    And I tap on conversation <Contact> in search result
    And I tap Call action button on People picker page
    Then I see Calling overlay

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C1053 @rc @regression @id3286
  Scenario Outline: Verify sharing a photo to a newly created group conversation with action button
    Given There are 4 users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I wait until my Top People list is not empty on the backend
    When I open search UI
    And I see top people list on People picker page
    Then I tap on first 3 top connections
    When I see Send image action button on People picker page
    And I tap Send image action button on People picker page
    And I press Camera Roll button
    And I choose a picture from camera roll
    And I confirm my choice
    Then I see group chat page with users <Contact1>,<Contact2>,<Contact3>
    And I see 1 photo in the dialog
    When I navigate back to conversations list
    Then I see in conversations list group chat with <Contact1>,<Contact2>,<Contact3>

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  |
      | user1Name | user2Name | user3Name | user4Name |

  @C1043 @regression @id3276
  Scenario Outline: Verify action buttons appear after selecting person from Top People
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I wait until my Top People list is not empty on the backend
    When I open search UI
    And I see top people list on People picker page
    When I tap on first 1 top connections
    Then I see Open conversation action button on People picker page

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C1044 @regression @id3277
  Scenario Outline: Verify action buttons appear after choosing user from search results
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I open search UI
    And I input in People picker search field user name <Contact>
    When I tap on conversation <Contact> in search result
    Then I see Open conversation action button on People picker page

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C1047 @regression @id3280
  Scenario Outline: Verify button Open is changed on Create after checking second person
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I wait until my Top People list is not empty on the backend
    When I open search UI
    And I see top people list on People picker page
    When I tap on 1st top connection contact
    Then I see Open conversation action button on People picker page
    When I tap on 2nd top connection contact
    Then I see Create conversation action button on People picker page

    Examples:
      | Name      |
      | user1Name |

  @C1045 @regression @id3278
  Scenario Outline: Verify action buttons disappear by unchecking the avatar
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I wait until my Top People list is not empty on the backend
    When I open search UI
    And I see top people list on People picker page
    When I tap on 1st top connection contact
    Then I see Open conversation action button on People picker page
    When I tap on 1st top connection contact
    Then I do not see Open conversation action button on People picker page

    Examples:
      | Name      |
      | user1Name |

  @C1046 @regression @id3279
  Scenario Outline: Verify action buttons disappear by deleting token from a search field
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I wait until my Top People list is not empty on the backend
    When I open search UI
    And I see top people list on People picker page
    When I tap on Search input on People picker page
    And I tap on 1st top connection contact
    And I see Open conversation action button on People picker page
    And I tap on 2nd top connection contact
    Then I see Create conversation action button on People picker page
    When I press backspace button
    And I press backspace button
    Then I do not see Create conversation action button on People picker page
    When I press backspace button
    And I press backspace button
    Then I do not see Open conversation action button on People picker page

    Examples:
      | Name      |
      | user1Name |

  @C1048 @regression @id3281
  Scenario Outline: Verify opening conversation with action button
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I wait until my Top People list is not empty on the backend
    When I open search UI
    And I see top people list on People picker page
    And I tap on 1st top connection contact
    And I tap Open conversation action button on People picker page
    Then I see conversation view page

    Examples:
      | Name      |
      | user1Name |

  @C1042 @regression @id4118
  Scenario Outline: Verify action buttons appear after choosing user from search results
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I wait until my Top People list is not empty on the backend
    When I open search UI
    And I see Invite more people button
    And I tap on 1st top connection contact
    And I do not see Invite more people button
    And I see Open conversation action button on People picker page
    And I tap on 1st top connection contact
    And I do not see Open conversation action button on People picker page
    And I see Invite more people button
    And I input in People picker search field user name <Contact>
    And I tap on conversation <Contact> in search result
    And I do not see Invite more people button
    And I see Open conversation action button on People picker page
    And I press backspace button
    And I press backspace button
    And I do not see Open conversation action button on People picker page
    Then I see Invite more people button

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |
