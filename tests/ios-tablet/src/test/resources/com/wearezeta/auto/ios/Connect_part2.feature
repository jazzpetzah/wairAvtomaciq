Feature: Connect

  @C2461 @regression @id3223
  Scenario Outline: Verify possibility of disconnecting from conversation list  [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Me sent connection request to <Contact1>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I see plus button next to text input
    And I click plus button next to text input
    And I open conversation details
    And I click Cancel request button
    Then I see Cancel request confirmation page
    And I confirm Cancel request by click on Yes button
    And I dismiss popover on iPad
    When I return to the chat list
    Then I dont see conversation <Contact> in contact list

    Examples: 
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2465 @regression @id3268
  Scenario Outline: Verify possibility of disconnecting from conversation list  [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Me sent connection request to <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I see plus button next to text input
    And I click plus button next to text input
    And I open conversation details
    And I click Cancel request button
    Then I see Cancel request confirmation page
    And I confirm Cancel request by click on Yes button
    Then I dont see conversation <Contact> in contact list

    Examples: 
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2462 @regression @rc @id3228
  Scenario Outline: Verify sending connection request after disconnecting [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Me sent connection request to <Contact1>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I click plus button next to text input
    And I open conversation details
    And I click Cancel request button
    And I confirm Cancel request by click on Yes button
    And I dismiss popover on iPad
    And I return to the chat list
    And I open search by taping on it
    And I input in People picker search field user name <Contact1>
    And I see user <Contact1> found on People picker page
    And I tap on NOT connected user name on People picker page <Contact1>
    And I see connect to <Contact1> dialog
    And I click Connect button on connect to dialog
    And I see Connect dialog is closed
    And I click close button to dismiss people view
    Then I see first item in contact list named <Contact1>

    Examples: 
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2466 @regression @id3273
  Scenario Outline: Verify sending connection request after disconnecting [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Me sent connection request to <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I click plus button next to text input
    And I open conversation details
    And I click Cancel request button
    And I confirm Cancel request by click on Yes button
    And I dismiss popover on iPad
    And I open search by taping on it
    And I input in People picker search field user name <Contact1>
    And I see user <Contact1> found on People picker page
    And I tap on NOT connected user name on People picker page <Contact1>
    And I see connect to <Contact1> dialog
    And I click Connect button on connect to dialog
    And I see Connect dialog is closed
    And I click close button to dismiss people view
    Then I see first item in contact list named <Contact1>

    Examples: 
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2463 @regression @id3229
  Scenario Outline: Verify possibility of disconnecting from Search UI [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Me sent connection request to <Contact1>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact1>
    And I see user <Contact1> found on People picker page
    And I see the user <Contact1> avatar with a clock
    And I tap on NOT connected user name on People picker page <Contact1>
    And I click Cancel request button
    And I see Cancel request confirmation page
    And I confirm Cancel request by click on Yes button
    And I see user <Contact1> found on People picker page
    Then I see the user <Contact1> avatar without the pending clock

    Examples: 
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2467 @regression @id3304
  Scenario Outline: Verify possibility of disconnecting from Search UI [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Me sent connection request to <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact1>
    And I see user <Contact1> found on People picker page
    And I see the user <Contact1> avatar with a clock
    And I tap on NOT connected user name on People picker page <Contact1>
    And I click Cancel request button
    And I see Cancel request confirmation page
    And I confirm Cancel request by click on Yes button
    And I see user <Contact1> found on People picker page
    Then I see the user <Contact1> avatar without the pending clock

    Examples: 
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2437 @staging @id2332 @ZIOS-4985
  Scenario Outline: Verify ignoring a connection request from another person (People view) [PORTRAIT]
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact3> sent connection request to me
    Given <Contact1> is connected to <Contact2>,<Contact3>
    Given <Contact1> has group chat <GroupChatName> with <Name>,<Contact2>,<Contact3>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I see Pending request link in contact list
    And I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    And I select user on iPad group popover <Contact3>
    And I see incoming pending popover from user <Contact3> on iPad
    And I tap on start dialog button on other user profile page
    And I click on Ignore button on Pending requests page
    And I exit the group info iPad popover
    And I return to the chat list
    Then I dont see Pending request link in contact list

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName |
      | user1Name | user2Name | user3Name | user4Name | IGNORECONNECT |

  @C2442 @staging @id3305 @ZIOS-4985
  Scenario Outline: Verify ignoring a connection request from another person (People view) [LANDSCAPE]
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact3> sent connection request to me
    Given <Contact1> is connected to <Contact2>,<Contact3>
    Given <Contact1> has group chat <GroupChatName> with <Name>,<Contact2>,<Contact3>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I see Pending request link in contact list
    And I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    And I select user on iPad group popover <Contact3>
    And I see incoming pending popover from user <Contact3> on iPad
    And I tap on start dialog button on other user profile page
    And I click on Ignore button on Pending requests page
    And I exit the group info iPad popover
    Then I dont see Pending request link in contact list

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName |
      | user1Name | user2Name | user3Name | user4Name | IGNORECONNECT |

  @C2428 @regression @id3795
  Scenario Outline: Verify common connections are not tappable [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact2> sent connection request to me
    Given <Contact1> is connected to <Contact2>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I see Pending request link in contact list
    And I click on Pending request link in contact list
    And I see Pending request page
    And I see YOU BOTH KNOW people section
    And I click person in YOU BOTH KNOW section
    Then I see Pending request page

    Examples: 
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2429 @regression @id3796
  Scenario Outline: Verify common connections are not tappable [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact2> sent connection request to me
    Given <Contact1> is connected to <Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I see Pending request link in contact list
    And I click on Pending request link in contact list
    And I see Pending request page
    And I see YOU BOTH KNOW people section
    And I click person in YOU BOTH KNOW section
    Then I see Pending request page

    Examples: 
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2512 @regression @id3903
  Scenario Outline: Verify inbox is highlighted and opened in the list [PORTRAIT]
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact3>
    Given <Contact> sent connection request to Me
    Given <Contact2> sent connection request to Me
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I see Pending request link in contact list
    And I click on Pending request link in contact list
    Then I see Pending request page
    When I swipe right in current window
    And I see conversation 2 people waiting is selected in list
    And I swipe left in current window
    Then I see Pending request page

    Examples: 
      | Name      | Contact   | Contact2  | Contact3  |
      | user1Name | user2Name | user3Name | user4Name |

  @C2513 @regression @id3904
  Scenario Outline: Verify inbox is highlighted and opened in the list [LANDSCAPE]
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact3>
    Given <Contact> sent connection request to Me
    Given <Contact2> sent connection request to Me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I see Pending request link in contact list
    And I click on Pending request link in contact list
    Then I see Pending request page
    When I swipe right in current window
    And I see conversation 2 people waiting is selected in list

    Examples: 
      | Name      | Contact   | Contact2  | Contact3  |
      | user1Name | user2Name | user3Name | user4Name |

  @C2430 @regression @id3997
  Scenario Outline: Verify displaying first and last names for the incoming connection request [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given <Contact> sent connection request to Me
    Given User <Contact> changes name to <NewName>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I see Pending request link in contact list
    And I click on Pending request link in contact list
    And I see Pending request page
    Then I see user <NewName> found on Pending request page

    Examples: 
      | Name      | Contact   | NewName  |
      | user1Name | user2Name | New Name |

  @C2431 @regression @id3998
  Scenario Outline: Verify displaying first and last names for the incoming connection request [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given <Contact> sent connection request to Me
    Given User <Contact> changes name to <NewName>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I see Pending request link in contact list
    And I click on Pending request link in contact list
    And I see Pending request page
    Then I see user <NewName> found on Pending request page

    Examples: 
      | Name      | Contact   | NewName  |
      | user1Name | user2Name | New Name |

  @C2468 @staging @id4001
  Scenario Outline: Verify connection request is deleted from the inbox of the addresser [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given <Contact> sent connection request to Me
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I see Pending request link in contact list
    And <Contact> cancel all outgoing connection requests
    Then I dont see Pending request link in contact list

    Examples: 
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2469 @staging @id4002
  Scenario Outline: Verify connection request is deleted from the inbox of the addresser [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given <Contact> sent connection request to Me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I see Pending request link in contact list
    And <Contact> cancel all outgoing connection requests
    Then I dont see Pending request link in contact list

    Examples: 
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |