Feature: Connect

  @id191 @id193 @smoke @rc @rc42
  Scenario Outline: Send connection request from search
    Given There are 2 users where <Name> is me
    Given I sign in using my email or phone number
    Given I see Contact list with no contacts
    And I wait until <Contact> exists in backend search results
    When I open Search by tap
    And I see People picker page
    And I tap on Search input on People picker page
    And I enter "<Contact>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I click Connect button on connect to page
    And I see People picker page
    And I navigate back to Conversations List
    Then I see contact list with name <Contact>

    Examples: 
      | Name      | Contact   | Message       |
      | user1Name | user2Name | Hellow friend |

  @id323 @smoke @rc
  Scenario Outline: Accept incoming connection request from conversation list
    Given There are 2 users where <Name> is me
    Given <Contact> sent connection request to <Name>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <WaitingMess>
    And I see connect to <Contact> dialog
    And I Connect with contact by pressing button
    Then I see contact list with name <Contact>

    Examples: 
      | Name      | Contact   | WaitingMess      |
      | user1Name | user2Name | 1 person waiting |

  @id1411 @regression @rc @rc42
  Scenario Outline: I can see a new inbox for connection when receive new connection request
    Given There are 2 users where <Name> is me
    Given I sign in using my email or phone number
    Given I see Contact list with no contacts
    Given I do not see contact list with name <WaitingMess>
    Given <Contact> sent connection request to <Name>
    When I tap on contact name <WaitingMess>
    And I see connect to <Contact> dialog
    And I press Ignore connect button
    Then I see Contact list
    Then I do not see contact list with name <WaitingMess>

    Examples: 
      | Name      | Contact   | WaitingMess      |
      | user1Name | user2Name | 1 person waiting |

  @id539 @id543 @regression
  Scenario Outline: I can see a inbox count increasing/decreasing correctly + I ignore someone from people picker and clear my inbox
    Given There are 5 users where <Name> is me
    Given <Contact1> sent connection request to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    Given <Contact2> sent connection request to me
    When I wait for 2 seconds
    Then I see contact list with name <WaitingMess2>
    When I tap on contact name <WaitingMess2>
    And I press Ignore connect button
    And I navigate back from connect page
    Then I see contact list with name <WaitingMess1>
    And <Contact3> sent connection request to me
    And <Contact4> sent connection request to me
    And I see contact list with name <WaitingMess3>
    And I wait until <Contact3> exists in backend search results
    And I open Search by tap
    And I see People picker page
    And I tap on Search input on People picker page
    And I enter "<Contact3>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact3>
    And I press Ignore connect button
    And I navigate back from connect page
    And I see contact list with name <WaitingMess2>
    And I tap on contact name <WaitingMess2>
    And I press Ignore connect button
    And I navigate back from connect page
    And I see contact list with name <WaitingMess1>
    And I tap on contact name <WaitingMess1>
    And I press Ignore connect button
    And I do not see contact list with name <WaitingMess1>

    Examples: 
      | Name      | Contact1  | WaitingMess1     | Contact2  | WaitingMess2     | Contact3  | Contact4  | WaitingMess3     |
      | user1Name | user2Name | 1 person waiting | user3Name | 2 people waiting | user4Name | user5Name | 3 people waiting |

  @id544 @regression
  Scenario Outline: I accept someone from people picker and -1 from inbox as well
    Given There are 5 users where <Name> is me
    Given <Contact1> sent connection request to <Name>
    Given <Contact2> sent connection request to <Name>
    Given <Contact4> sent connection request to <Name>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    Given <Contact3> sent connection request to <Name>
    When I see contact list with name <WaitingMess1>
    And I wait until <Contact3> exists in backend search results
    And I open Search by tap
    And I see People picker page
    And I tap on Search input on People picker page
    And I enter "<Contact3>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact3>
    And I see Accept and Ignore buttons
    And I scroll to inbox contact <Contact3>
    And I see connect to <Contact3> dialog
    And I Connect with contact by pressing button
    And I wait for 5 seconds
    Then I navigate back from dialog page
    And I see contact list with name <WaitingMess2>

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | WaitingMess1     | WaitingMess2     |
      | user1Name | user2Name | user3Name | user4Name | user5Name | 4 people waiting | 3 people waiting |

  @id540 @regression
  Scenario Outline: I can ignore a connect request and reconnect later
    Given There are 2 users where <Name> is me
    Given <Contact> sent connection request to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I see contact list with name <WaitingMess>
    And I tap on contact name <WaitingMess>
    And I press Ignore connect button
    And I do not see contact list with name <WaitingMess>
    And I wait until <Contact> exists in backend search results
    And I open Search by tap
    And I see People picker page
    And I tap on Search input on People picker page
    And I enter "<Contact>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I Connect with contact by pressing button
    And I wait for 5 seconds
    Then I see Connect to <Contact> Dialog page

    Examples: 
      | Name      | Contact   | WaitingMess      |
      | user1Name | user2Name | 1 person waiting |

  @id542 @regression
  Scenario Outline: Accept incoming connection request from search
    Given There are 2 users where <Name> is me
    Given <Contact> sent connection request to <Name>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I see contact list with name <WaitingMess>
    And I wait until <Contact> exists in backend search results
    And I open Search by tap
    And I see People picker page
    And I tap on Search input on People picker page
    And I enter "<Contact>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I Connect with contact by pressing button
    Then I see contact list with name <Contact>

    Examples: 
      | Name      | Contact   | WaitingMess      |
      | user1Name | user2Name | 1 person waiting |

  @id547 @regression
  Scenario Outline: I would not know other person has ignored my connection request
    Given There are 2 users where <Name> is me
    Given I sign in using my email or phone number
    Given I see Contact list with no contacts
    And I wait until <Contact> exists in backend search results
    When I open Search by tap
    And I see People picker page
    And I tap on Search input on People picker page
    And I enter "<Contact>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I click Connect button on connect to page
    When <Contact> ignore all requests
    And I press Clear button
    Then I tap on contact name <Contact>
    And I see that connection is pending

    Examples: 
      | Name      | Contact   | Message |
      | user1Name | user2Name | Test    |

  @id541 @regression @rc
  Scenario Outline: I can receive new connection request when app in background
    Given There are 2 users where <Name> is me
    Given I sign in using my email or phone number
    Given I see Contact list with no contacts
    When I minimize the application
    And <Contact> sent connection request to Me
    And I wait for 2 seconds
    And I restore the application
    And I see Contact list
    When I see contact list with name <WaitingMess>
    And I tap on contact name <WaitingMess>
    Then I see connect to <Contact> dialog
    And I see Accept and Ignore buttons
    And I press Ignore connect button

    Examples: 
      | Name      | Contact   | WaitingMess      |
      | user1Name | user2Name | 1 person waiting |

  @id553 @regression @rc
  Scenario Outline: I want to see that the other person has accepted the connect request in the conversation view
    Given There are 2 users where <Name> is me
    Given I sign in using my email or phone number
    Given I see Contact list with no contacts
    And I wait until <Contact> exists in backend search results
    When I open Search by tap
    And I see People picker page
    And I tap on Search input on People picker page
    And I enter "<Contact>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I click Connect button on connect to page
    And I wait for 2 seconds
    When <Contact> accept all requests
    And I wait for 2 seconds
    And I press Clear button
    Then I see contact list with name <Contact>
    And I tap on contact name <Contact>
    And I see Connect to <Contact> Dialog page

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @id552 @regression @rc
  Scenario Outline: I want to discard the new connect request (sending) by returning to the search results after selecting someone I’m not connected to
    Given There are 2 users where <Name> is me
    Given I sign in using my email or phone number
    Given I see Contact list with no contacts
    And I wait until <Contact> exists in backend search results
    When I open Search by tap
    And I see People picker page
    And I tap on Search input on People picker page
    And I enter "<Contact>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact>
    And I see connect to <Contact> dialog
    Then I close Connect To dialog
    And I see People picker page

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @id550 @staging
  Scenario Outline: I want to initiate a connect request by selecting someone from within a group conversation
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to <Name>
    Given <Contact1> is connected to <Contact2>
    Given <Contact1> has group chat <ChatName> with <Name>, <Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <ChatName>
    And I tap conversation details button
    #Sometimes here only one user visible (backend issue)
    And I tap on group chat contact <Contact2>
    And I see connect to <Contact2> dialog
    And I click left Connect button
    And I click Connect button on connect to page
    And I return to group chat page
    And I navigate back from dialog page
    And I see contact list with name <Contact2>

    Examples: 
      | Name      | Contact1  | Contact2  | ChatName         |
      | user1Name | user2Name | user3Name | ContactGroupChat |

  @id676 @regression
  Scenario Outline: I want to block a person from 1:1 conversation
    Given There are 2 users where <Name> is me
    Given <Contact1> is connected to <Name>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And I see dialog page
    And I tap conversation details button
    And I press options menu button
    And I press BLOCK conversation menu button
    And I confirm block
    And I press back button
    And I navigate back from dialog page
    Then I do not see contact list with name <Contact1>
    And I wait until <Contact1> exists in backend search results
    And I wait until <Contact1> is blocked in backend search results
    And I open Search by tap
    And I see People picker page
    And I tap on Search input on People picker page
    And I enter "<Contact1>" into Search input on People Picker page
    And I see user <Contact1> found on People picker page
    And I tap on user name found on People picker page <Contact1>
    Then User info should be shown with Unblock button

    Examples: 
      | Name      | Contact1  |
      | user1Name | user2Name |

  @id680 @regression
  Scenario Outline: I want to see user has been blocked within the Start UI
    Given There are 2 users where <Name> is me
    Given I sign in using my email or phone number
    Given I see Contact list with no contacts
    And I wait until <Contact> exists in backend search results
    When I open Search by tap
    And I see People picker page
    And I tap on Search input on People picker page
    And I enter "<Contact>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I click Connect button on connect to page
    And I press Clear button
    And I see contact list with name <Contact>
    When I tap on contact name <Contact>
    And I see that connection is pending
    And I click Block button on connect to page
    And I confirm block on connect to page
    And I press back button
    And I wait for 5 seconds
    Then I do not see contact list with name <Contact>
    And I wait until <Contact> exists in backend search results
    And I open Search by tap
    And I see People picker page
    And I tap on Search input on People picker page
    And I enter "<Contact>" into Search input on People Picker page
    And I see user <Contact> found on People picker page
    And I tap on user name found on People picker page <Contact>
    Then User info should be shown with Unblock button
    And I click Unblock button

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @id720 @regression
  Scenario Outline: I want to be seen in the search results of someone I blocked
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I sign in using my email
    Given I see Contact list with contacts
    When User <Contact> blocks user Myself
    Then I wait until <Contact> exists in backend search results
    When I open Search by tap
    And I see People picker page
    And I tap on Search input on People picker page
    And I enter "<Contact>" into Search input on People Picker page
    Then I see user <Contact> found on People picker page

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @id723 @regression
  Scenario Outline: I want to unblock someone from their Profile view
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given User <Name> blocks user <Contact1>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    And I wait until <Contact1> exists in backend search results
    And I open Search by tap
    And I see People picker page
    And I tap on Search input on People picker page
    And I enter "<Contact1>" into Search input on People Picker page
    And I see user <Contact1> found on People picker page
    And I tap on user name found on People picker page <Contact1>
    Then User info should be shown with Block button
    And I click Unblock button
    And I see dialog page
    And I navigate back from dialog page
    And I see contact list with name <Contact1>

    Examples: 
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @id1405 @regression @rc
  Scenario Outline: Impossibility of starting 1:1 conversation with pending user (Search)
    Given There are 2 users where <Name> is me
    Given I sign in using my email or phone number
    Given I see Contact list with no contacts
    And I wait until <Contact> exists in backend search results
    When I open Search by tap
    And I see People picker page
    And I tap on Search input on People picker page
    And I enter "<Contact>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I click Connect button on connect to page
    And I see People picker page
    And I see user <Contact> found on People picker page
    And I tap on user name found on People picker page <Contact>
    Then I see that connection is pending

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @id1397 @regression
  Scenario Outline: Verify you do not receive any messages from blocked person in 1:1 chat
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I type the message "<Message>" and send it
    And User <Name> blocks user <Contact>
    And Contact <Contact> sends image <Picture> to single user conversation <Name>
    And Contact <Contact> send message to user <Name>
    And Contact <Contact> ping conversation <Name>
    Then Last message is <Message>

    Examples: 
      | Name      | Contact   | Message          | Picture     |
      | user1Name | user2Name | Hello my friend! | testing.jpg |

  @id2215 @regression @rc @rc42
  Scenario Outline: I can connect to someone from PYMK by clicking +
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to <Contact2>
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I open Search by tap
    And I see People picker page
    And I keep reopening People Picker until PYMK are visible
    And I remember the name of the first PYMK item
    And I click + button on the first PYMK item
    Then I do not see the previously remembered PYMK item
    When I press Clear button
    Then I see contact list with the previously remembered PYMK item
    When I open Search by tap
    Then I do not see the previously remembered PYMK item

    Examples: 
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @id2216 @regression @rc
  Scenario Outline: I can connect to someone from PYMK by tap and typing connect message
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to <Contact2>
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    And I open Search by tap
    And I see People picker page
    And I keep reopening People Picker until PYMK are visible
    And I remember the name of the first PYMK item
    When I tap the first PYMK item
    And I click Connect button on connect to page
    And I see People picker page
    And I press Clear button
    Then I see contact list with the previously remembered PYMK item
    When I open Search by tap
    Then I do not see the previously remembered PYMK item

    Examples: 
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @regression @id2661 @deployAddressBook
  Scenario Outline: Verify you can see People you may know on Wire after uploading your address book
    Given There is 1 user where <Name> is me
    Given I add predefined users to address book
    Given I sign in using my email or phone number
    And I see Contact list with no contacts
    When I open Search by tap
    Then I see recommended user <Contact1> in People Picker

    # disabled step which checks missing contact with phone only
    #    And I see recommended user <Contact2> in People Picker
    Examples: 
      | Name      | Contact1 | Contact2 |
      | user1Name | vb003    | Dorothy  |
