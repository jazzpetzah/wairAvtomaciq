Feature: Search

  @C2788 @rc @regression @fastLogin
  Scenario Outline: Verify search by email [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I wait until <ContactEmail> exists in backend search results
    When I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<ContactEmail>" in Search UI input field
    Then I see the conversation "<ContactName>" exists in Search results

    Examples:
      | Name      | ContactEmail | ContactName |
      | user1Name | user2Email   | user2Name   |

  @C2789 @rc @regression @fastLogin
  Scenario Outline: Verify search by name [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <IntermediateContact>
    Given <Contact> is connected to <IntermediateContact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I wait until <Contact> exists in backend search results
    When I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<Contact>" in Search UI input field
    Then I see the conversation "<Contact>" exists in Search results

    Examples:
      | Name      | Contact   | IntermediateContact |
      | user1Name | user2Name | user3Name           |

  @C2838 @regression @fastLogin
  Scenario Outline: Start 1:1 chat with users from Top Connections [PORTRAIT]
    Given There are <UserCount> users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I wait until my Top People list is not empty on the backend
    When I open search UI
    And I accept alert if visible
    And I see top people list on Search UI page
    Then I tap on first 1 top connections
    #And I click Go button to create 1:1 conversation
    And I tap Open conversation action button on Search UI page
    And I wait for 2 seconds
    And I see conversation view page

    Examples:
      | Name      | UserCount |
      | user1Name | 2         |

  @C2839 @regression @fastLogin
  Scenario Outline: Start 1:1 chat with users from Top Connections [LANDSCAPE]
    Given There are <UserCount> users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I wait until my Top People list is not empty on the backend
    When I open search UI
    And I accept alert if visible
    And I see top people list on Search UI page
    Then I tap on first 1 top connections
    #And I click Go button to create 1:1 conversation
    And I tap Open conversation action button on Search UI page
    And I wait for 2 seconds
    And I see conversation view page

    Examples:
      | Name      | UserCount |
      | user1Name | 2         |

  @C2835 @regression @fastLogin
  Scenario Outline: Start group chat with users from Top Connections [PORTRAIT]
    Given There are <UserCount> users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I wait until my Top People list is not empty on the backend
    When I open search UI
    And I accept alert if visible
    And I see top people list on Search UI page
    Then I tap on first 2 top connections
    And I tap Create conversation action button on Search UI page
    And I wait for 5 seconds
    And I open group conversation details
    And I change group conversation name to "<ConvoName>" on Group info page
    And I dismiss popover on iPad
    And I navigate back to conversations list
    Then I see first item in contact list named <ConvoName>

    Examples:
      | Name      | ConvoName    | UserCount |
      | user1Name | TopGroupTest | 3         |

  @C2840 @rc @regression @fastLogin
  Scenario Outline: Start group chat with users from Top Connections [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I wait until my Top People list is not empty on the backend
    When I open search UI
    And I accept alert if visible
    And I see top people list on Search UI page
    And I tap on first 2 top connections
    And I tap Create conversation action button on Search UI page
    And I wait for 5 seconds
    And I open group conversation details
    And I change group conversation name to "<ConvoName>" on Group info page
    And I dismiss popover on iPad
    And I see first item in contact list named <ConvoName>

    Examples:
      | Name      | ConvoName    |
      | user1Name | TopGroupTest |

  @C2456 @C2778 @regression @fastLogin
  Scenario Outline: Verify you can unblock someone from search list [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Name> blocks user <Contact>
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
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

  @C2790 @rc @regression @fastLogin
  Scenario Outline: Verify you can unblock someone from search list [LANDSAPE]
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Name> blocks user <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I do not see conversation <Contact> in conversations list
    And I wait until <Contact> exists in backend search results
    And I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<Contact>" in Search UI input field
    And I tap Hide keyboard button
    And I tap on conversation <Contact> in search result
    And I tap Unblock button on Search UI page
    And I type the default message and send it
    Then I see 1 default message in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2792 @rc @regression @fastLogin
  Scenario Outline: Verify search by second name (something after space) [LANDSAPE]
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Contact> changes name to <FirstName> <LastName>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I wait until <LastName> exists in backend search results
    Given I open search UI
    Given I accept alert if visible
    Given I tap input field on Search UI page
    When I type "<LastName>" in Search UI input field
    Then I see the conversation "<FirstName> <LastName>" exists in Search results

    Examples:
      | Name      | Contact   | FirstName | LastName |
      | user1Name | user2Name | FirstName | LastName |

  @C2795 @rc @regression @fastLogin
  Scenario Outline: Verify search by part of the name [LANDSAPE]
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I wait until <Contact> exists in backend search results
    Given I open search UI
    Given I accept alert if visible
    Given I tap input field on Search UI page
    When I type first <LettersCount> letters of user name "<Contact>" into Search UI input field
    Then I see the conversation "<Contact>" exists in Search results

    Examples:
      | Name      | Contact   | LettersCount |
      | user1Name | user2Name | 7            |

  @C2793 @regression @fastLogin
  Scenario Outline: Verify search is possible after selection users from Top People [LANDSAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I wait until <Contact> exists in backend search results
    Given I wait until my Top People list is not empty on the backend
    When I open search UI
    And I accept alert if visible
    And I tap on 1 top connection but not <Contact>
    And I tap input field on Search UI page
    And I type "<Contact>" in Search UI input field
    And I tap on conversation <Contact> in search result
    And I tap Create conversation action button on Search UI page
    Then I see group chat page with users <Contact>,<Contact2>

    Examples:
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |


  @C2803 @regression @fastLogin
  Scenario Outline: Verify starting a call with action button [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
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

  @C2804 @rc @regression @fastLogin
  Scenario Outline: Verify starting a call with action button [LANDSAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
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

  @C2805 @regression @fastLogin
  Scenario Outline: Verify sharing a photo to a newly created group conversation with action button [PORTRAIT]
    Given There are 4 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I wait until my Top People list is not empty on the backend
    When I open search UI
    And I accept alert if visible
    And I see top people list on Search UI page
    Then I tap on first 3 top connections
    When I tap Send image action button on Search UI page
    And I tap Camera Roll button on Camera page
    And I accept alert if visible
    And I select the first picture from Camera Roll
    And I tap Confirm button on Picture preview page
    Then I see group chat page with users <Contact1>,<Contact2>,<Contact3>
    And I see 1 photo in the conversation view
    When I navigate back to conversations list
    Then I see in conversations list group chat with <Contact1>,<Contact2>,<Contact3>

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  |
      | user1Name | user2Name | user3Name | user4Name |

  @C2806 @rc @regression @fastLogin
  Scenario Outline: Verify sharing a photo to a newly created group conversation with action button [LANDSAPE]
    Given There are 4 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I wait until my Top People list is not empty on the backend
    When I open search UI
    And I accept alert if visible
    And I see top people list on Search UI page
    Then I tap on first 3 top connections
    When I tap Send image action button on Search UI page
    And I tap Camera Roll button on Camera page
    And I accept alert if visible
    And I select the first picture from Camera Roll
    And I tap Confirm button on Picture preview page
    Then I see group chat page with users <Contact1>,<Contact2>,<Contact3>
    And I see 1 photo in the conversation view
    And I see in conversations list group chat with <Contact1>,<Contact2>,<Contact3>

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  |
      | user1Name | user2Name | user3Name | user4Name |

  @C2808 @regression @fastLogin
  Scenario Outline: Verify action buttons appear after selecting person from Top People [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I wait until my Top People list is not empty on the backend
    When I open search UI
    And I accept alert if visible
    And I see top people list on Search UI page
    And I tap on first 1 top connections
    Then I see Open conversation action button on Search UI page

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2810 @regression @fastLogin
  Scenario Outline: Verify action buttons appear after choosing user from search results [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<Contact>" in Search UI input field
    And I tap on conversation <Contact> in search result
    Then I see Open conversation action button on Search UI page

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2812 @regression @fastLogin
  Scenario Outline: Verify button Open is changed on Create after checking second person [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I wait until my Top People list is not empty on the backend
    When I open search UI
    And I accept alert if visible
    When I tap on 1st top connection contact
    Then I see Open conversation action button on Search UI page
    When I tap on 2nd top connection contact
    Then I see Create conversation action button on Search UI page

    Examples:
      | Name      |
      | user1Name |

  @C2814 @regression @fastLogin
  Scenario Outline: Verify action buttons disappear by unchecking the avatar [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
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

  @C2816 @regression @fastLogin
  Scenario Outline: Verify action buttons disappear by deleting token from a search field [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I wait until my Top People list is not empty on the backend
    When I open search UI
    And I accept alert if visible
    And I see top people list on Search UI page
    And I tap input field on Search UI page
    And I tap on 1st top connection contact
    And I see Open conversation action button on Search UI page
    And I tap on 2nd top connection contact
    Then I see Create conversation action button on Search UI page
    When I tap input field on Search UI page
    And I press Backspace button in search field
    And I press Backspace button in search field
    Then I do not see Create conversation action button on Search UI page
    When I press Backspace button in search field
    And I press Backspace button in search field
    Then I do not see Open conversation action button on Search UI page

    Examples:
      | Name      |
      | user1Name |

  @C2817 @regression @fastLogin
  Scenario Outline: Verify opening conversation with action button [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
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

  @C2818 @regression @fastLogin
  Scenario Outline: Verify opening conversation with action button [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
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

  @C2802 @regression @fastLogin
  Scenario Outline: Verify action buttons appear after choosing user from search results [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
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