Feature: Search

  @C1035 @rc @clumsy @regression @fastLogin
  Scenario Outline: Verify search by email does not work
    Given There are 2 users where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<ContactEmail>" in Search UI input field
    Then I see the conversation "<ContactName>" does not exist in Search results

    Examples:
      | Name      | ContactEmail | ContactName |
      | user1Name | user2Email   | user2Name   |

  @C1036 @rc @clumsy @regression @fastLogin
  Scenario Outline: Verify search by name
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given <Contact2> is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<Contact>" in Search UI input field
    Then I see the conversation "<Contact>" exists in Search results

    Examples:
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @C3167 @rc @regression @fastLogin
  Scenario Outline: Start 1:1 chat with users from Top Connections
    Given There are <UserCount> users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I wait until my Top People list is not empty on the backend
    And I open search UI
    And I accept alert if visible
    And I see top people list on Search UI page
    Then I tap on first 1 top connections
    And I tap Open conversation action button on Search UI page
    And I wait for 2 seconds
    And I see conversation view page

    Examples:
      | Name      | UserCount |
      | user1Name | 4         |

  @C1069 @rc @regression @fastLogin
  Scenario Outline: Start group chat with users from Top Connections
    Given There are 4 users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I wait until my Top People list is not empty on the backend
    And I open search UI
    And I accept alert if visible
    And I see top people list on Search UI page
    Then I tap on first 2 top connections
    When I tap Create conversation action button on Search UI page
    # Wait for animation
    And I wait for 3 seconds
    And I see conversation view page
    And I open group conversation details
    Then I see "<ParticipantsCount> people" label on Group info page

    Examples:
      | Name      | ParticipantsCount |
      | user1Name | 2                 |

  @C40 @rc @regression @fastLogin
  Scenario Outline: Verify sending a connection request to user chosen from search
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given <Contact2> is connected to <UnconnectedUser>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I wait until <UnconnectedUser> exists in backend search results
    When I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<UnconnectedUser>" in Search UI input field
    And I tap on conversation <UnconnectedUser> in search result
    # Wait until user info is loaded
    And I wait for 3 seconds
    And I tap Connect button on Single user Pending outgoing connection page
    And I tap X button on Search UI page
    And I tap on contact name <UnconnectedUser>
    Then I see conversation with user <UnconnectedUser>

    Examples:
      | Name      | UnconnectedUser | Contact2  |
      | user1Name | user2Name       | user3Name |

  @C3220 @regression @fastLogin
  Scenario Outline: I can still search for other people using the search field, regardless of whether I already added people from Top conversations
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I wait until my Top People list is not empty on the backend
    When I open search UI
    And I accept alert if visible
    And I tap on 1 top connection but not <Contact>
    And I tap input field on Search UI page
    And I type "<Contact>" in Search UI input field
    And I tap on conversation <Contact> in search result
    And I tap Create conversation action button on Search UI page
    # Wait for animation
    And I wait for 3 seconds
    Then I see group conversation with users <Contact>,<Contact2>

    Examples:
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @C3244 @regression @fastLogin
  Scenario Outline: Verify you can unblock someone from search list
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to Myself
    Given User <Name> blocks user <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I do not see conversation <Contact> in conversations list
    And I wait until <Contact> exists in backend search results
    And I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<Contact>" in Search UI input field
    And I tap on conversation <Contact> in search result
    And I tap Unblock button on Search UI page
    And I type the default message and send it
    Then I see 1 default message in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2785 @regression @fastLogin
  Scenario Outline: Verify search by part of the name
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I wait until <Contact> exists in backend search results
    When I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type first <LettersCount> letters of user name "<Contact>" into Search UI input field
    Then I see the conversation "<Contact>" exists in Search results

    Examples:
      | Name      | Contact   | LettersCount |
      | user1Name | user2Name | 5            |

  @C1049 @rc @clumsy @regression @fastLogin
  Scenario Outline: Verify starting a call with action button
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I wait until <Contact> exists in backend search results
    When I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<Contact>" in Search UI input field
    And I tap on conversation <Contact> in search result
    And I tap Call action button on Search UI page
    Then I see Calling overlay

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C1053 @rc @regression @fastLogin
  Scenario Outline: (ZIOS-7744) Verify sharing a photo to a newly created group conversation with action button
    Given There are 4 users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I wait until my Top People list is not empty on the backend
    When I open search UI
    And I accept alert if visible
    And I see top people list on Search UI page
    Then I tap on first 3 top connections
    When I see Send image action button on Search UI page
    And I tap Send image action button on Search UI page
    And I tap Camera Roll button on Camera page
    # Wait for animation
    And I wait for 5 seconds
    And I accept alert if visible
    And I select the first picture from Camera Roll
    And I tap Confirm button on Picture preview page
    Then I see group conversation with users <Contact1>,<Contact2>,<Contact3>
    And I see 1 photo in the conversation view
    When I navigate back to conversations list
    Then I see in conversations list group chat with <Contact1>,<Contact2>,<Contact3>

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  |
      | user1Name | user2Name | user3Name | user4Name |

  @C1043 @regression @fastLogin
  Scenario Outline: Verify action buttons appear after selecting person from Top People
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I wait until my Top People list is not empty on the backend
    When I open search UI
    And I accept alert if visible
    And I see top people list on Search UI page
    When I tap on first 1 top connections
    Then I see Open conversation action button on Search UI page

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C1044 @regression @fastLogin
  Scenario Outline: Verify action buttons appear after choosing user from search results
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<Contact>" in Search UI input field
    When I tap on conversation <Contact> in search result
    Then I see Open conversation action button on Search UI page

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C1047 @regression @fastLogin
  Scenario Outline: Verify button Open is changed on Create after checking second person
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I wait until my Top People list is not empty on the backend
    When I open search UI
    And I accept alert if visible
    And I see top people list on Search UI page
    When I tap on 1st top connection contact
    Then I see Open conversation action button on Search UI page
    When I tap on 2nd top connection contact
    Then I see Create conversation action button on Search UI page

    Examples:
      | Name      |
      | user1Name |

  @C1045 @regression @fastLogin
  Scenario Outline: Verify action buttons disappear by unchecking the avatar
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I wait until my Top People list is not empty on the backend
    When I open search UI
    And I accept alert if visible
    And I see top people list on Search UI page
    When I tap on 1st top connection contact
    Then I see Open conversation action button on Search UI page
    When I tap on 1st top connection contact
    Then I do not see Open conversation action button on Search UI page

    Examples:
      | Name      |
      | user1Name |

  @C1046 @regression @fastLogin
  Scenario Outline: Verify action buttons disappear by deleting token from a search field
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I wait until my Top People list is not empty on the backend
    When I open search UI
    And I accept alert if visible
    And I see top people list on Search UI page
    When I tap input field on Search UI page
    And I tap on 1st top connection contact
    And I see Open conversation action button on Search UI page
    And I tap on 2nd top connection contact
    Then I see Create conversation action button on Search UI page
    # We need to tap the button twice, because first tap only selects the element
    When I press Backspace button in search field
    And I press Backspace button in search field
    Then I do not see Create conversation action button on Search UI page
    When I press Backspace button in search field
    And I press Backspace button in search field
    Then I do not see Open conversation action button on Search UI page

    Examples:
      | Name      |
      | user1Name |

  @C1048 @regression @fastLogin
  Scenario Outline: Verify opening conversation with action button
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I wait until my Top People list is not empty on the backend
    When I open search UI
    And I accept alert if visible
    And I see top people list on Search UI page
    And I tap on 1st top connection contact
    And I tap Open conversation action button on Search UI page
    Then I see conversation view page

    Examples:
      | Name      |
      | user1Name |

  @C1042 @regression @fastLogin
  Scenario Outline: Verify action buttons appear after choosing user from search results
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I wait until my Top People list is not empty on the backend
    When I open search UI
    And I accept alert if visible
    And I see Send Invite button on Search UI page
    And I tap on 1st top connection contact
    And I do not see Send Invite button on Search UI page
    And I see Open conversation action button on Search UI page
    And I tap on 1st top connection contact
    And I do not see Open conversation action button on Search UI page
    And I see Send Invite button on Search UI page
    And I tap input field on Search UI page
    And I type "<Contact>" in Search UI input field
    And I tap on conversation <Contact> in search result
    And I do not see Send Invite button on Search UI page
    And I see Open conversation action button on Search UI page
    And I press Backspace button in search field
    And I press Backspace button in search field
    And I do not see Open conversation action button on Search UI page
    Then I see Send Invite button on Search UI page

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |
