Feature: Connect

  @C2461 @regression @id3223
  Scenario Outline: Verify possibility of disconnecting from conversation list [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Me sent connection request to <Contact1>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I open conversation details
    And I tap Cancel Request button on pending profile page
    And I confirm Cancel Request action on pending profile page
    When I navigate back to conversations list
    Then I do not see conversation <Contact1> in conversations list

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2465 @rc @regression @id3268
  Scenario Outline: Verify possibility of disconnecting from conversation list [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Me sent connection request to <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I open conversation details
    And I tap Cancel Request button on pending profile page
    And I confirm Cancel Request action on pending profile page
    Then I do not see conversation <Contact1> in conversations list

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2462 @regression @id3228
  Scenario Outline: (ZIOS-6323) Verify sending connection request after disconnecting [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Me sent connection request to <Contact1>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I open conversation details
    And I tap Cancel Request button on pending profile page
    And I confirm Cancel Request action on pending profile page
    And I navigate back to conversations list
    And I open search UI
    And I input in People picker search field user name <Contact1>
    And I tap on conversation <Contact1> in search result
    And I see connect to <Contact1> dialog
    And I click Connect button on connect to dialog
    And I see Connect dialog is closed
    And I click close button to dismiss people view
    Then I see first item in contact list named <Contact1>

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2466 @rc @regression @id3273
  Scenario Outline: Verify sending connection request after disconnecting [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Me sent connection request to <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I open conversation details
    And I tap Cancel Request button on pending profile page
    And I confirm Cancel Request action on pending profile page
    And I open search UI
    And I input in People picker search field user name <Contact1>
    And I tap on conversation <Contact1> in search result
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
    When I open search UI
    And I input in People picker search field user name <Contact1>
    And I tap on conversation <Contact1> in search result
    And I tap Cancel Request button on pending profile page
    And I confirm Cancel Request action on pending profile page
    Then I see the conversation "<Contact1>" exists in Search results

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
    When I open search UI
    And I input in People picker search field user name <Contact1>
    And I tap on conversation <Contact1> in search result
    And I tap Cancel Request button on pending profile page
    And I confirm Cancel Request action on pending profile page
    Then I see the conversation "<Contact1>" exists in Search results

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2437 @regression @id2332 @ZIOS-4985
  Scenario Outline: Verify ignoring a connection request from another person (People view) [PORTRAIT]
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact3> sent connection request to me
    Given <Contact1> is connected to <Contact2>,<Contact3>
    Given <Contact1> has group chat <GroupChatName> with <Name>,<Contact2>,<Contact3>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I see Pending request link in conversations list
    And I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I select user on iPad group popover <Contact3>
    And I see incoming pending popover from user <Contact3> on iPad
    And I tap on start dialog button on other user profile page
    And I click on Ignore button on Pending requests page
    And I dismiss popover on iPad
    And I navigate back to conversations list
    Then I do not see Pending request link in conversations list

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName |
      | user1Name | user2Name | user3Name | user4Name | IGNORECONNECT |

  @C2442 @rc @regression @id3305 @ZIOS-4985
  Scenario Outline: Verify ignoring a connection request from another person (People view) [LANDSCAPE]
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact3> sent connection request to me
    Given <Contact1> is connected to <Contact2>,<Contact3>
    Given <Contact1> has group chat <GroupChatName> with <Name>,<Contact2>,<Contact3>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I see Pending request link in conversations list
    And I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I select user on iPad group popover <Contact3>
    And I see incoming pending popover from user <Contact3> on iPad
    And I tap on start dialog button on other user profile page
    And I click on Ignore button on Pending requests page
    And I dismiss popover on iPad
    Then I do not see Pending request link in conversations list

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName |
      | user1Name | user2Name | user3Name | user4Name | IGNORECONNECT |

  @C2513 @rc @regression @id3904
  Scenario Outline: Verify inbox is highlighted and opened in the list [LANDSCAPE]
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact3>
    Given <Contact> sent connection request to Me
    Given <Contact2> sent connection request to Me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I click on Pending request link in conversations list
    Then I see Pending request page

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
    When I click on Pending request link in conversations list
    And I see Pending request page
    Then I see Hello connect message from user <NewName> on Pending request page

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
    When I click on Pending request link in conversations list
    And I see Pending request page
    Then I see Hello connect message from user <NewName> on Pending request page

    Examples:
      | Name      | Contact   | NewName  |
      | user1Name | user2Name | New Name |

  @C2468 @regression @id4001
  Scenario Outline: Verify connection request is deleted from the inbox of the addresser [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given <Contact> sent connection request to Me
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I see Pending request link in conversations list
    And <Contact> cancel all outgoing connection requests
    Then I do not see Pending request link in conversations list

    Examples:
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2469 @regression @id4002
  Scenario Outline: Verify connection request is deleted from the inbox of the addresser [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given <Contact> sent connection request to Me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I see Pending request link in conversations list
    And <Contact> cancel all outgoing connection requests
    Then I do not see Pending request link in conversations list

    Examples:
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2436 @regression @rc
  Scenario Outline: Verify accepting a connection request from another person (People view) [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to <Contact2>,Me
    Given <Contact1> has group chat <GroupChatName> with <Contact2>,Me
    Given <Contact2> sent connection request to Me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I see Pending request link in conversations list
    And I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I select participant <Contact2>
    And I tap Connect button on pending profile page
    And I confirm Connect action on pending profile page
    And I dismiss popover on iPad
    Then I see conversation <Contact2> in conversations list
    And I do not see Pending request link in conversations list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | TESTCHAT      |

  @C2470 @regression
  Scenario Outline: Verify copying invitation to the clipboard [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search UI
    And I press the send an invite button
    And I press invite others button
    And I press the copy button
    And I click close Invite list button
    And I click clear button
    And I tap on contact name <Contact>
    And I tap on text input
    And I tap and hold on message input
    And I paste and commit the text
    Then I verify that pasted message contains MyEmail

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |