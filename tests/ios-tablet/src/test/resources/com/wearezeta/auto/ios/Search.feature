Feature: Search

  @regression @rc @id2147
  Scenario Outline: Verify search by email [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user email <ContactEmail>
    Then I see user <ContactName> found on People picker page

    Examples: 
      | Name      | ContactEmail | ContactName |
      | user1Name | user2Email   | user2Name   |

  @regression @id2926
  Scenario Outline: Verify search by email [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user email <ContactEmail>
    Then I see user <ContactName> found on People picker page

    Examples: 
      | Name      | ContactEmail | ContactName |
      | user1Name | user2Email   | user2Name   |

  @regression @rc @id2148
  Scenario Outline: Verify search by name [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I swipe down contact list on iPad
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    Then I see user <Contact> found on People picker page

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id2927
  Scenario Outline: Verify search by name [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I swipe down contact list on iPad
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    Then I see user <Contact> found on People picker page

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @staging @id2531 @noAcceptAlert
  Scenario Outline: Verify denying address book uploading [PORTRAIT]
    Given There is 1 user where <Name> is me
    Given I Sign in on tablet using my email
    And I dismiss all alerts
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see Upload contacts dialog
    And I click Continue button on Upload dialog
    And I dismiss alert
    And I press maybe later button
    And I click clear button
    And I swipe down contact list on iPad
    And I click hide keyboard button
    Then I dont see Upload contacts dialog

    Examples: 
      | Name      |
      | user1Name |

  @staging @id2928 @noAcceptAlert
  Scenario Outline: Verify denying address book uploading [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I dismiss all alerts
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see Upload contacts dialog
    And I click Continue button on Upload dialog
    And I dismiss alert
    And I press maybe later button
    And I click clear button
    And I swipe down contact list on iPad
    And I click hide keyboard button
    Then I dont see Upload contacts dialog

    Examples: 
      | Name      |
      | user1Name |

  @regression @id2656
  Scenario Outline: Start 1:1 chat with users from Top Connections [PORTRAIT]
    Given There are <UserCount> users where <Name> is me
    Given Myself is connected to all other users
    Given Contact <Contact> send message to user <Name>
    Given Contact <Name> send message to user <Contact>
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I wait for 30 seconds
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    Then I tap on first 1 top connections
    #And I click Go button to create 1:1 conversation
    And I click open conversation button on People picker page
    And I wait for 2 seconds
    And I see dialog page

    Examples: 
      | Name      | UserCount | Contact   |
      | user1Name | 2         | user2Name |

  @regression @id2929
  Scenario Outline: Start 1:1 chat with users from Top Connections [LANDSCAPE]
    Given There are <UserCount> users where <Name> is me
    Given Myself is connected to all other users
    Given Contact <Contact> send message to user <Name>
    Given Contact <Name> send message to user <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I wait for 30 seconds
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    Then I tap on first 1 top connections
    #And I click Go button to create 1:1 conversation
    And I click open conversation button on People picker page
    And I wait for 2 seconds
    And I see dialog page

    Examples: 
      | Name      | UserCount | Contact   |
      | user1Name | 2         | user2Name |

  @regression @rc @id2550
  Scenario Outline: Start group chat with users from Top Connections [PORTRAIT]
    Given There are <UserCount> users where <Name> is me
    Given Myself is connected to all other users
    Given Contact <Contact> send message to user <Name>
    Given Contact <Name> send message to user <Contact>
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    Then I tap on first 2 top connections
    And I click Create Conversation button on People picker page
    And I wait for 2 seconds
    And I open group conversation details
    And I change group conversation name to <ConvoName>
    And I dismiss popover on iPad
    And I swipe right on group chat page
    Then I see first item in contact list named <ConvoName>

    Examples: 
      | Name      | ConvoName    | UserCount | Contact   |
      | user1Name | TopGroupTest | 3         | user2Name |

  @regression @id2930
  Scenario Outline: Start group chat with users from Top Connections [LANDSCAPE]
    Given There are <UserCount> users where <Name> is me
    Given Myself is connected to all other users
    Given Contact <Contact> send message to user <Name>
    Given Contact <Name> send message to user <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    #And I wait for 30 seconds
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    And I tap on first 2 top connections
    #And I click hide keyboard button
    And I click Create Conversation button on People picker page
    And I wait for 2 seconds
    And I open group conversation details
    And I change group conversation name to <ConvoName>
    And I dismiss popover on iPad
    And I see first item in contact list named <ConvoName>

    Examples: 
      | Name      | ConvoName    | UserCount | Contact   |
      | user1Name | TopGroupTest | 3         | user2Name |

  @regression @rc @id2342 @id1456
  Scenario Outline: Verify you can unblock someone from search list [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Name> blocks user <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I dont see conversation <Contact> in contact list
    And I wait until <Contact> exists in backend search results
    And I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I tap on connected user <Contact> on People picker page
    And I unblock user on iPad
    And I type the message
    And I send the message
    Then I see message in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id2931
  Scenario Outline: Verify you can unblock someone from search list [LANDSAPE]
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Name> blocks user <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I dont see conversation <Contact> in contact list
    And I wait until <Contact> exists in backend search results
    And I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I click hide keyboard button
    And I tap on connected user <Contact> on People picker page
    And I unblock user on iPad
    And I type the message
    And I send the message
    Then I see message in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @rc @id2547
  Scenario Outline: Verify dismissing with clicking on Hide [PORTRAIT]
    Given There are 5 users where <Name> is me
    Given <ContactWithFriends> is connected to <Name>
    Given <ContactWithFriends> is connected to <Friend1>
    Given <ContactWithFriends> is connected to <Friend2>
    Given <ContactWithFriends> is connected to <Friend3>
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if CONNECT label is not there
    And I see CONNECT label
    And I swipe to reveal hide button for suggested contact <Friend1>
    And I tap hide for suggested contact <Friend1>
    Then I do not see suggested contact <Friend1>

    Examples: 
      | Name      | ContactWithFriends | Friend1   | Friend2   | Friend3   |
      | user1Name | user2Name          | user3Name | user4Name | user5Name |

  @regression @id2932
  Scenario Outline: Verify dismissing with clicking on Hide [LANDSAPE]
    Given There are 5 users where <Name> is me
    Given <ContactWithFriends> is connected to <Name>
    Given <ContactWithFriends> is connected to <Friend1>
    Given <ContactWithFriends> is connected to <Friend2>
    Given <ContactWithFriends> is connected to <Friend3>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if CONNECT label is not there
    And I see CONNECT label
    And I swipe to reveal hide button for suggested contact <Friend1>
    And I tap hide for suggested contact <Friend1>
    Then I do not see suggested contact <Friend1>

    Examples: 
      | Name      | ContactWithFriends | Friend1   | Friend2   | Friend3   |
      | user1Name | user2Name          | user3Name | user4Name | user5Name |

  @regression @rc @id2546
  Scenario Outline: Verify dismissing with one single gesture [PORTRAIT]
    Given There are 5 users where <Name> is me
    Given <ContactWithFriends> is connected to <Name>
    Given <ContactWithFriends> is connected to <Friend1>
    Given <ContactWithFriends> is connected to <Friend2>
    Given <ContactWithFriends> is connected to <Friend3>
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if CONNECT label is not there
    And I see CONNECT label
    And I swipe completely to dismiss suggested contact <Friend1>
    Then I do not see suggested contact <Friend1>

    Examples: 
      | Name      | ContactWithFriends | Friend1   | Friend2   | Friend3   |
      | user1Name | user2Name          | user3Name | user4Name | user5Name |

  @regression @id2933
  Scenario Outline: Verify dismissing with one single gesture [LANDSAPE]
    Given There are 5 users where <Name> is me
    Given <ContactWithFriends> is connected to <Name>
    Given <ContactWithFriends> is connected to <Friend1>
    Given <ContactWithFriends> is connected to <Friend2>
    Given <ContactWithFriends> is connected to <Friend3>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if CONNECT label is not there
    And I see CONNECT label
    And I swipe completely to dismiss suggested contact <Friend1>
    Then I do not see suggested contact <Friend1>

    Examples: 
      | Name      | ContactWithFriends | Friend1   | Friend2   | Friend3   |
      | user1Name | user2Name          | user3Name | user4Name | user5Name |

  @regression @rc @id2118
  Scenario Outline: Verify sending connection request from PYMK [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact1> is connected to <Contact2>
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if CONNECT label is not there
    And I see CONNECT label
    And I press the instant connect button
    And I click close button to dismiss people view
    Then I see first item in contact list named <Contact2>

    Examples: 
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @regression @id2934
  Scenario Outline: Verify sending connection request from PYMK [LANDSAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact1> is connected to <Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if CONNECT label is not there
    And I see CONNECT label
    And I press the instant connect button
    And I click close button to dismiss people view
    Then I see first item in contact list named <Contact2>

    Examples: 
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @regression @rc @id2149
  Scenario Outline: Verify search by second name (something after space) [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Contact> change name to <NewName>
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I wait until <LastName> exists in backend search results
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <LastName>
    Then I see user <NewName> found on People picker page

    Examples: 
      | Name      | Contact   | NewName  | LastName |
      | user1Name | user2Name | NEW NAME | NAME     |

  @regression @id2935
  Scenario Outline: Verify search by second name (something after space) [LANDSAPE]
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Contact> change name to <NewName>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I wait until <LastName> exists in backend search results
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <LastName>
    Then I see user <NewName> found on People picker page

    Examples: 
      | Name      | Contact   | NewName  | LastName |
      | user1Name | user2Name | NEW NAME | NAME     |

  @regression @rc @id2150
  Scenario Outline: Verify search by second name (something after space) [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Contact> change name to <NewName>
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I wait until <NewName> exists in backend search results
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <PartName>
    Then I see user <NewName> found on People picker page

    Examples: 
      | Name      | Contact   | NewName           | PartName |
      | user1Name | user2Name | Djulieta Carnobat | Djuli    |

  @regression @id2945
  Scenario Outline: Verify search by second name (something after space) [LANDSAPE]
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Contact> change name to <NewName>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I wait until <NewName> exists in backend search results
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <PartName>
    Then I see user <NewName> found on People picker page

    Examples: 
      | Name      | Contact   | NewName           | PartName |
      | user1Name | user2Name | Djulieta Carnobat | Djuli    |

  @regression @id2703
  Scenario Outline: Verify search is possible after selection users from Top People [PORTRAIT]
    Given There are <UserCount> users where <Name> is me
    Given Myself is connected to all other users
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I wait until <Contact> exists in backend search results
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    And I tap on 3 top connections but not <Contact>
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I tap on connected user <Contact> on People picker page
    And I click space keyboard button
    Then I see that <Number> contacts are selected

    Examples: 
      | Name      | UserCount | Contact   | Number |
      | user1Name | 7         | user2Name | 4      |

  @regression @id2936
  Scenario Outline: Verify search is possible after selection users from Top People [LANDSAPE]
    Given There are <UserCount> users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I wait until <Contact> exists in backend search results
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    And I tap on 3 top connections but not <Contact>
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I tap on connected user <Contact> on People picker page
    And I click space keyboard button
    Then I see that <Number> contacts are selected

    Examples: 
      | Name      | UserCount | Contact   | Number |
      | user1Name | 7         | user2Name | 4      |

  @staging @id3289
  Scenario Outline: Verify starting a call with action button [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    Then I see user <Contact> found on People picker page
    When I tap on connected user <Contact> on People picker page
    And I see call action button on People picker page
    And I click call action button on People picker page
    Then I see mute call, end call buttons
    And I see calling to contact <Contact> message

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @staging @id3290
  Scenario Outline: Verify starting a call with action button [LANDSAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    Then I see user <Contact> found on People picker page
    When I tap on connected user <Contact> on People picker page
    And I see call action button on People picker page
    And I click call action button on People picker page
    Then I see mute call, end call buttons
    And I see calling to contact <Contact> message

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @staging @id3291
  Scenario Outline: Verify sharing a photo to a newly created group conversation with action button [PORTRAIT]
    Given There are 4 users where <Name> is me
    Given Myself is connected to all other users
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    Then I tap on first 3 top connections
    When I see Send image action button on People picker page
    And I click Send image action button on People picker page
    And I press Camera Roll button
    And I choose a picture from camera roll on iPad popover
    And I press Confirm button on iPad popover
    Then I see group chat page with 3 users <Contact1> <Contact2> <Contact3>
    And I see new photo in the dialog
    When I navigate back to conversations view
    Then I see in contact list group chat with <Contact1> <Contact2> <Contact3>

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  |
      | user1Name | user2Name | user3Name | user4Name |

  @staging @id3292
  Scenario Outline: Verify sharing a photo to a newly created group conversation with action button [LANDSAPE]
    Given There are 4 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    Then I tap on first 3 top connections
    When I see Send image action button on People picker page
    And I click Send image action button on People picker page
    And I press Camera Roll button
    And I choose a picture from camera roll on iPad popover
    And I press Confirm button on iPad popover
    Then I see group chat page with 3 users <Contact1> <Contact2> <Contact3>
    And I see new photo in the dialog
    And I see in contact list group chat with <Contact1> <Contact2> <Contact3>

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  |
      | user1Name | user2Name | user3Name | user4Name |

  @staging @id3295
  Scenario Outline: Verify action buttons appear after selecting person from Top People [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    And I tap on first 1 top connections
    And I see action buttons appeared on People picker page

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @staging @id3296
  Scenario Outline: Verify action buttons appear after selecting person from Top People [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    And I tap on first 1 top connections
    And I see action buttons appeared on People picker page

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @staging @id3297
  Scenario Outline: Verify action buttons appear after choosing user from search results [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I tap on connected user <Contact> on People picker page
    And I see action buttons appeared on People picker page

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @staging @id3298
  Scenario Outline: Verify action buttons appear after choosing user from search results [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I tap on connected user <Contact> on People picker page
    And I see action buttons appeared on People picker page

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @staging @id3299
  Scenario Outline: Verify button Open is changed on Create after checking second person [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    And I tap on 1st top connection contact
    And I see open conversation action button on People picker page
    And I tap on 2nd top connection contact
    And I see Create Conversation button on People picker page

    Examples: 
      | Name      |
      | user1Name |

  @staging @id3300
  Scenario Outline: Verify button Open is changed on Create after checking second person [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
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

  @staging @id3301
  Scenario Outline: Verify action buttons disappear by unchecking the avatar [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
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

  @staging @id3302
  Scenario Outline: Verify action buttons disappear by unchecking the avatar [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
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

  @staging @id3819
  Scenario Outline: Verify action buttons disappear by deleting token from a search field [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    And I tap on 1st top connection contact
    And I see action buttons appeared on People picker page
    And I tap on 2nd top connection contact
    And I see Create Conversation button on People picker page
    And I press backspace button
    Then I see open conversation action button on People picker page
    And I press backspace button
    Then I see action buttons disappeared on People picker page

    Examples: 
      | Name      |
      | user1Name |

  @staging @id3820
  Scenario Outline: Verify action buttons disappear by deleting token from a search field [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    And I tap on 1st top connection contact
    And I see action buttons appeared on People picker page
    And I tap on 2nd top connection contact
    And I see Create Conversation button on People picker page
    And I press backspace button
    Then I see open conversation action button on People picker page
    And I press backspace button
    Then I see action buttons disappeared on People picker page

    Examples: 
      | Name      |
      | user1Name |

  @staging @id3821
  Scenario Outline: Verify opening conversation with action button [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
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

  @staging @id3822
  Scenario Outline: Verify opening conversation with action button [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
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

  @staging @id2482
  Scenario Outline: Verify label hiding after dismissing all PYMK [PORTRAIT]
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact1> is connected to <Contact2>,<Contact3>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if CONNECT label is not there
    And I see CONNECT label
    And I swipe to reveal hide button for suggested contact <Contact2>
    And I tap hide for suggested contact <Contact2>
    And I swipe to reveal hide button for suggested contact <Contact3>
    And I tap hide for suggested contact <Contact3>
    Then I dont see CONNECT label

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  |
      | user1Name | user2Name | user3Name | user4Name |

  @staging @id3824
  Scenario Outline: Verify label hiding after dismissing all PYMK [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact1> is connected to <Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if CONNECT label is not there
    And I see CONNECT label
    And I swipe to reveal hide button for suggested contact <Contact2>
    And I tap hide for suggested contact <Contact2>
    Then I dont see CONNECT label

    Examples: 
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |
