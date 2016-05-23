Feature: Connect

  @C676 @C677 @id191 @id193 @regression @rc @rc42
  Scenario Outline: Send connection request from search
    Given There are 3 users where <Name> is me
    Given Myself is connected to <IntermediateContact>
    Given <IntermediateContact> is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    And I wait until <Contact> exists in backend search results
    When I open Search UI
    And I enter "<Contact>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I click Connect button on connect to page
    And I see People picker page
    And I press Clear button
    Then I see contact list with name <Contact>

    Examples:
      | Name      | Contact   | IntermediateContact |
      | user1Name | user2Name | user3name           |

  @C687 @id323 @regression @rc @rc42
  Scenario Outline: Accept incoming connection request from conversation list
    Given There are 2 users where <Name> is me
    Given <Contact> sent connection request to <Name>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <WaitingMess>
    And I see connect to <Contact> dialog
    And I Connect with contact by pressing button
    Then I see contact list with name <Contact>

    Examples:
      | Name      | Contact   | WaitingMess      |
      | user1Name | user2Name | 1 person waiting |

  @C706 @id1411 @regression @rc @rc42
  Scenario Outline: I can see a new inbox for connection when receive new connection request
    Given There are 2 users where <Name> is me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
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

  @C383 @C386 @id539 @id543 @regression
  Scenario Outline: I can see a inbox count increasing/decreasing correctly + I ignore someone from people picker and clear my inbox
    Given There are 5 users where <Name> is me
    Given <Contact1> sent connection request to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
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
    And I open Search UI
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

  @C387 @id544 @regression
  Scenario Outline: I accept someone from people picker and -1 from inbox as well
    Given There are 5 users where <Name> is me
    Given <Contact1> sent connection request to <Name>
    Given <Contact2> sent connection request to <Name>
    Given <Contact4> sent connection request to <Name>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given <Contact3> sent connection request to <Name>
    When I see contact list with name <WaitingMess1>
    And I wait until <Contact3> exists in backend search results
    And I open Search UI
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

  @C384 @id540 @regression
  Scenario Outline: I can ignore a connect request and reconnect later
    Given There are 2 users where <Name> is me
    Given <Contact> sent connection request to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I see contact list with name <WaitingMess>
    And I tap on contact name <WaitingMess>
    And I press Ignore connect button
    And I do not see contact list with name <WaitingMess>
    And I wait until <Contact> exists in backend search results
    And I open Search UI
    And I enter "<Contact>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I Connect with contact by pressing button
    And I wait for 5 seconds
    Then I see conversation view

    Examples:
      | Name      | Contact   | WaitingMess      |
      | user1Name | user2Name | 1 person waiting |

  @C385 @id542 @regression @C111633
  Scenario Outline: Accept incoming connection request from search
    Given There are 2 users where <Name> is me
    Given <Contact> sent connection request to <Name>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I see contact list with name <WaitingMess>
    And I wait until <Contact> exists in backend search results
    And I open Search UI
    And I enter "<Contact>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I do not see text input
    And I do not see cursor toolbar
    And I Connect with contact by pressing button
    Then I see contact list with name <Contact>

    Examples:
      | Name      | Contact   | WaitingMess      |
      | user1Name | user2Name | 1 person waiting |

  @C388 @id547 @regression @C111632
  Scenario Outline: I would not know other person has ignored my connection request
    Given There are 3 users where <Name> is me
    Given Myself is connected to <IntermediateContact>
    Given <IntermediateContact> is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    And I wait until <Contact> exists in backend search results
    When I open Search UI
    And I enter "<Contact>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I click Connect button on connect to page
    When <Contact> ignore all requests
    And I press Clear button
    Then I tap on contact name <Contact>
    And I see that connection is pending
    And I do not see cursor toolbar
    And I do not see text input

    Examples:
      | Name      | Contact   | IntermediateContact |
      | user1Name | user2Name | user3Name           |

  @C694 @id541 @regression @rc
  Scenario Outline: I can receive new connection request when app in background
    Given There are 2 users where <Name> is me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
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

  @C696 @id553 @regression @rc
  Scenario Outline: I want to see that the other person has accepted the connect request in the conversation view
    Given There are 3 users where <Name> is me
    Given Myself is connected to <IntermediateContact>
    Given <IntermediateContact> is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    And I wait until <Contact> exists in backend search results
    When I open Search UI
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
    And I see conversation view

    Examples:
      | Name      | Contact   | IntermediateContact |
      | user1Name | user2Name | user3Name           |

  @C695 @id552 @regression @rc
  Scenario Outline: I want to discard the new connect request (sending) by returning to the search results after selecting someone I’m not connected to
    Given There are 3 users where <Name> is me
    Given Myself is connected to <IntermediateContact>
    Given <IntermediateContact> is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    And I wait until <Contact> exists in backend search results
    When I open Search UI
    And I enter "<Contact>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact>
    And I see connect to <Contact> dialog
    Then I close Connect To dialog
    And I see People picker page

    Examples:
      | Name      | Contact   | IntermediateContact |
      | user1Name | user2Name | user3Name           |

  @C389 @id550 @regression
  Scenario Outline: I want to initiate a connect request by selecting someone from within a group conversation
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to <Name>
    Given <Contact1> is connected to <Contact2>
    Given <Contact1> has group chat <ChatName> with <Name>, <Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <ChatName>
    And I tap conversation name from top toolbar
    #Sometimes here only one user visible (backend issue)
    And I tap on group chat contact <Contact2>
    And I see connect to <Contact2> dialog
    And I click left Connect button
    And I click Connect button on connect to page
    And I close participant page by UI button
    And I navigate back from dialog page
    And I see contact list with name <Contact2>

    Examples:
      | Name      | Contact1  | Contact2  | ChatName         |
      | user1Name | user2Name | user3Name | ContactGroupChat |

  @C390 @id676 @regression
  Scenario Outline: I want to block a person from 1:1 conversation
    Given There are 3 users where <Name> is me
    # Having the extra user is a workaround for an app bug
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And I tap conversation name from top toolbar
    And I press options menu button
    And I press BLOCK conversation menu button
    And I confirm block
    Then I do not see contact list with name <Contact1>
    And I wait until <Contact1> exists in backend search results
    And I open Search UI
    And I enter "<Contact1>" into Search input on People Picker page
    And I see user <Contact1> found on People picker page
    And I tap on user name found on People picker page <Contact1>
    Then User info should be shown with Unblock button

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C391 @id680 @regression
  Scenario Outline: I want to see user has been blocked within the Start UI
    Given There are 3 users where <Name> is me
    # Having the extra user is a workaround for an app bug
    Given Myself is connected to <Contact1>
    # if Contact2 doesn't have any contacts, it cannot be found by Myself
    Given <Contact1> is connected to <Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    And I wait until <Contact2> exists in backend search results
    When I open Search UI
    And I enter "<Contact2>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact2>
    And I see connect to <Contact2> dialog
    And I click Connect button on connect to page
    And I press Clear button
    And I see contact list with name <Contact2>
    When I tap on contact name <Contact2>
    And I see that connection is pending
    And I click ellipsis button
    And I click Block button
    And I confirm block on connect to page
    Then I see Contact list with contacts
    Then I do not see contact list with name <Contact2>
    And I wait until <Contact2> exists in backend search results
    And I open Search UI
    And I enter "<Contact2>" into Search input on People Picker page
    And I see user <Contact2> found on People picker page
    And I tap on user name found on People picker page <Contact2>
    And User info should be shown with Unblock button
    When I click Unblock button
    And I navigate back from dialog page
    Then I see contact list with name <Contact2>

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C392 @id720 @regression
  Scenario Outline: I want to be seen in the search results of someone I blocked
    Given There are 3 users where <Name> is me
    # Having the extra user is a workaround for an app bug
    Given Myself is connected to <Contact1>,<Contact2>
    Given User <Contact1> blocks user Myself
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    And I see contact list with name <Contact1>
    And I see contact list with name <Contact2>
    And I wait until <Contact1> exists in backend search results
    When I open Search UI
    And I enter "<Contact1>" into Search input on People Picker page
    Then I see user <Contact1> found on People picker page

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C407 @id723 @regression
  Scenario Outline: (BUG AN-2721) I want to unblock someone from their Profile view
    Given There are 4 users where <Name> is me
      # Having the extra user is a workaround for an app bug
    Given Myself is connected to <Contact1>,<Contact2>
    Given User <Name> blocks user <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact2>
    And I navigate back from dialog page
    And I wait until <Contact1> exists in backend search results
    And I open Search UI
    And I enter "<Contact1>" into Search input on People Picker page
    And I see user <Contact1> found on People picker page
    And I tap on user name found on People picker page <Contact1>
    And User info should be shown with Unblock button
    When I click Unblock button
    Then I see conversation view
    And I navigate back from dialog page
    And I see contact list with name <Contact1>

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C705 @id1405 @regression @rc
  Scenario Outline: Impossibility of starting 1:1 conversation with pending user (Search)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <IntermediateContact>
    Given <IntermediateContact> is connected to <Contact>
    Given <Contact> has an avatar picture from file <Picture>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    And I wait until <Contact> exists in backend search results
    When I open Search UI
    And I enter "<Contact>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I click Connect button on connect to page
    And I see People picker page
    And I see user <Contact> found on People picker page
    And I tap on user name found on People picker page <Contact>
    Then I see that connection is pending

    Examples:
      | Name      | Contact   | Picture                      | IntermediateContact |
      | user1Name | user2Name | aqaPictureContact600_800.jpg | user3Name           |

  @C409 @id1397 @regression
  Scenario Outline: Verify you do not receive any messages from blocked person in 1:1 chat
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I tap on text input
    And I type the message "<Message>" and send it
    And User <Name> blocks user <Contact>
    And User <Contact> sends encrypted image <Picture> to single user conversation <Name>
    And User <Contact> sends encrypted message to user <Name>
    And User <Contact> securely pings conversation Myself
    Then I see the most recent conversation message is "<Message>"

    Examples:
      | Name      | Contact   | Message          | Picture     |
      | user1Name | user2Name | Hello my friend! | testing.jpg |

  @C82540 @regression
  Scenario Outline: Verify you cannot create a new group conversation with a person who blocked you
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given User <Contact1> blocks user Myself
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact2>
    And I tap conversation name from top toolbar
    And I press create group button
    And I enter "<Contact1>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact1>
    And I click on Add to conversation button
    Then I see Unable to Create Group Conversation overlay
    When I accept Unable to Create Group Conversation overlay
    Then I see Contact list with contacts
    And I do not see group conversation with <Contact1>,<Contact2> in conversations list

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |
