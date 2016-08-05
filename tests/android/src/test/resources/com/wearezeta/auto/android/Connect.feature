Feature: Connect

  @C676 @C677 @regression @rc @rc42
  Scenario Outline: Send connection request from search
    Given There are 3 users where <Name> is me
    Given Myself is connected to <IntermediateContact>
    Given <IntermediateContact> is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    And I wait until <Contact> exists in backend search results
    When I open Search UI
    And I type user name "<Contact>" in search field
    And I tap on user name found on People picker page <Contact>
    Then I see connect to <Contact> dialog
    When I click Connect button on connect to page
    Then I see People picker page
    When I press Clear button
    Then I see Conversations list with name <Contact>

    Examples:
      | Name      | Contact   | IntermediateContact |
      | user1Name | user2Name | user3name           |

  @C687 @regression @rc @rc42
  Scenario Outline: Accept incoming connection request from Conversations list
    Given There are 2 users where <Name> is me
    Given <Contact> sent connection request to <Name>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <WaitingMess>
    Then I see connect to <Contact> dialog
    When I Connect with contact by pressing button
    Then I see Conversations list with name <Contact>

    Examples:
      | Name      | Contact   | WaitingMess      |
      | user1Name | user2Name | 1 person waiting |

  @C706 @regression @rc @rc42
  Scenario Outline: I can see a new inbox for connection when receive new connection request
    Given There are 2 users where <Name> is me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with no conversations
    Given I do not see Conversations list with name <WaitingMess>
    Given <Contact> sent connection request to <Name>
    When I tap on conversation name <WaitingMess>
    Then I see connect to <Contact> dialog
    When I press Ignore connect button
    Then I see Conversations list
    And I do not see Conversations list with name <WaitingMess>

    Examples:
      | Name      | Contact   | WaitingMess      |
      | user1Name | user2Name | 1 person waiting |

  @C383 @C386 @regression
  Scenario Outline: I can see a inbox count increasing/decreasing correctly + I ignore someone from people picker and clear my inbox
    Given There are 5 users where <Name> is me
    Given <Contact1> sent connection request to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given <Contact2> sent connection request to me
    When I wait for 2 seconds
    Then I see Conversations list with name <WaitingMess2>
    When I tap on conversation name <WaitingMess2>
    And I press Ignore connect button
    And I navigate back from connect page
    Then I see Conversations list with name <WaitingMess1>
    And <Contact3> sent connection request to me
    And <Contact4> sent connection request to me
    And I see Conversations list with name <WaitingMess3>
    And I wait until <Contact3> exists in backend search results
    When I open Search UI
    And I type user name "<Contact3>" in search field
    And I tap on user name found on People picker page <Contact3>
    And I press Ignore connect button
    And I navigate back from connect page
    Then I see Conversations list with name <WaitingMess2>
    When I tap on conversation name <WaitingMess2>
    And I press Ignore connect button
    And I navigate back from connect page
    Then I see Conversations list with name <WaitingMess1>
    When I tap on conversation name <WaitingMess1>
    And I press Ignore connect button
    Then I do not see Conversations list with name <WaitingMess1>

    Examples:
      | Name      | Contact1  | WaitingMess1     | Contact2  | WaitingMess2     | Contact3  | Contact4  | WaitingMess3     |
      | user1Name | user2Name | 1 person waiting | user3Name | 2 people waiting | user4Name | user5Name | 3 people waiting |

  @C387 @regression
  Scenario Outline: I accept someone from people picker and -1 from inbox as well
    Given There are 5 users where <Name> is me
    Given <Contact1> sent connection request to <Name>
    Given <Contact2> sent connection request to <Name>
    Given <Contact4> sent connection request to <Name>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given <Contact3> sent connection request to <Name>
    Then I see Conversations list with name <WaitingMess1>
    And I wait until <Contact3> exists in backend search results
    When I open Search UI
    And I type user name "<Contact3>" in search field
    And I tap on user name found on People picker page <Contact3>
    Then I see Accept and Ignore buttons
    When I scroll to inbox contact <Contact3>
    Then I see connect to <Contact3> dialog
    When I Connect with contact by pressing button
    And I wait for 5 seconds
    And I navigate back from dialog page
    Then I see Conversations list with name <WaitingMess2>

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | WaitingMess1     | WaitingMess2     |
      | user1Name | user2Name | user3Name | user4Name | user5Name | 4 people waiting | 3 people waiting |

  @C384 @regression
  Scenario Outline: I can ignore a connect request and reconnect later
    Given There are 2 users where <Name> is me
    Given <Contact> sent connection request to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Then I see Conversations list with name <WaitingMess>
    When I tap on conversation name <WaitingMess>
    And I press Ignore connect button
    Then I do not see Conversations list with name <WaitingMess>
    And I wait until <Contact> exists in backend search results
    When I open Search UI
    And I type user name "<Contact>" in search field
    And I tap on user name found on People picker page <Contact>
    Then I see connect to <Contact> dialog
    When I Connect with contact by pressing button
    And I wait for 5 seconds
    Then I see conversation view

    Examples:
      | Name      | Contact   | WaitingMess      |
      | user1Name | user2Name | 1 person waiting |

  @C385 @C111633 @regression @rc
  Scenario Outline: Accept incoming connection request from search
    Given There are 2 users where <Name> is me
    Given <Contact> sent connection request to <Name>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Then I see Conversations list with name <WaitingMess>
    And I wait until <Contact> exists in backend search results
    When I open Search UI
    And I type user name "<Contact>" in search field
    And I tap on user name found on People picker page <Contact>
    Then I see connect to <Contact> dialog
    And I do not see text input
    And I do not see cursor toolbar
    When I Connect with contact by pressing button
    Then I see Conversations list with name <Contact>

    Examples:
      | Name      | Contact   | WaitingMess      |
      | user1Name | user2Name | 1 person waiting |

  @C388  @C111632 @regression @rc
  Scenario Outline: I would not know other person has ignored my connection request
    Given There are 3 users where <Name> is me
    Given Myself is connected to <IntermediateContact>
    Given <IntermediateContact> is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    And I wait until <Contact> exists in backend search results
    When I open Search UI
    And I type user name "<Contact>" in search field
    And I tap on user name found on People picker page <Contact>
    Then I see connect to <Contact> dialog
    When I click Connect button on connect to page
    And <Contact> ignore all requests
    And I press Clear button
    And I tap on conversation name <Contact>
    Then I see that connection is pending
    And I do not see cursor toolbar
    And I do not see text input

    Examples:
      | Name      | Contact   | IntermediateContact |
      | user1Name | user2Name | user3Name           |

  @C694 @regression @rc
  Scenario Outline: I can receive new connection request when app in background
    Given There are 2 users where <Name> is me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with no conversations
    When I minimize the application
    And <Contact> sent connection request to Me
    And I wait for 2 seconds
    And I restore the application
    And I see Conversations list
    Then I see Conversations list with name <WaitingMess>
    When I tap on conversation name <WaitingMess>
    Then I see connect to <Contact> dialog
    And I see Accept and Ignore buttons
    And I press Ignore connect button

    Examples:
      | Name      | Contact   | WaitingMess      |
      | user1Name | user2Name | 1 person waiting |

  @C696 @regression @rc
  Scenario Outline: I want to see that the other person has accepted the connect request in the conversation view
    Given There are 3 users where <Name> is me
    Given Myself is connected to <IntermediateContact>
    Given <IntermediateContact> is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I open Search UI
    And I wait until <Contact> exists in backend search results
    And I type user name "<Contact>" in search field
    And I tap on user name found on People picker page <Contact>
    Then I see connect to <Contact> dialog
    When I click Connect button on connect to page
    And I wait for 2 seconds
    And <Contact> accept all requests
    And I wait for 2 seconds
    And I press Clear button
    Then I see Conversations list with name <Contact>
    When I tap on conversation name <Contact>
    And I see conversation view

    Examples:
      | Name      | Contact   | IntermediateContact |
      | user1Name | user2Name | user3Name           |

  @C695 @regression @rc
  Scenario Outline: I want to discard the new connect request (sending) by returning to the search results after selecting someone I’m not connected to
    Given There are 3 users where <Name> is me
    Given Myself is connected to <IntermediateContact>
    Given <IntermediateContact> is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I open Search UI
    And I wait until <Contact> exists in backend search results
    And I type user name "<Contact>" in search field
    And I tap on user name found on People picker page <Contact>
    Then I see connect to <Contact> dialog
    And I close Connect To dialog
    And I see People picker page

    Examples:
      | Name      | Contact   | IntermediateContact |
      | user1Name | user2Name | user3Name           |

  @C389 @regression
  Scenario Outline: I want to initiate a connect request by selecting someone from within a group conversation
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to <Name>
    Given <Contact1> is connected to <Contact2>
    Given <Contact1> has group chat <ChatName> with <Name>, <Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <ChatName>
    And I tap conversation name from top toolbar
    #Sometimes here only one user visible (backend issue)
    And I tap on group chat contact <Contact2>
    Then I see connect to <Contact2> dialog
    When I click left Connect button
    And I click Connect button on connect to page
    Then I close participant page by UI button
    When I navigate back from dialog page
    Then I see Conversations list with name <Contact2>

    Examples:
      | Name      | Contact1  | Contact2  | ChatName         |
      | user1Name | user2Name | user3Name | ContactGroupChat |

  @C390 @regression
  Scenario Outline: I want to block a person from 1:1 conversation
    Given There are 3 users where <Name> is me
    # Having the extra user is a workaround for an app bug
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And I tap conversation name from top toolbar
    And I press options menu button
    And I press BLOCK conversation menu button
    And I confirm block
    Then I do not see Conversations list with name <Contact1>
    And I wait until <Contact1> exists in backend search results
    When I open Search UI
    And I type user name "<Contact1>" in search field
    And I see user <Contact1> in Search result list
    And I tap on user name found on People picker page <Contact1>
    Then User info should be shown with Unblock button

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C391 @regression
  Scenario Outline: I want to see user has been blocked within the Start UI
    Given There are 3 users where <Name> is me
    # Having the extra user is a workaround for an app bug
    Given Myself is connected to <Contact1>
    # if Contact2 doesn't have any contacts, it cannot be found by Myself
    Given <Contact1> is connected to <Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I open Search UI
    And I wait until <Contact2> exists in backend search results
    And I type user name "<Contact2>" in search field
    And I tap on user name found on People picker page <Contact2>
    Then I see connect to <Contact2> dialog
    When I click Connect button on connect to page
    And I press Clear button
    Then I see Conversations list with name <Contact2>
    When I tap on conversation name <Contact2>
    Then I see that connection is pending
    When I click ellipsis button
    And I click Block button
    And I confirm block on connect to page
    Then I see Conversations list with conversations
    And I do not see Conversations list with name <Contact2>
    And I wait until <Contact2> exists in backend search results
    When I open Search UI
    And I type user name "<Contact2>" in search field
    And I see user <Contact2> in Search result list
    And I tap on user name found on People picker page <Contact2>
    Then User info should be shown with Unblock button
    When I click Unblock button
    And I navigate back from dialog page
    Then I see Conversations list with name <Contact2>

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C392 @regression
  Scenario Outline: I want to be seen in the search results of someone I blocked
    Given There are 3 users where <Name> is me
    # Having the extra user is a workaround for an app bug
    Given Myself is connected to <Contact1>,<Contact2>
    Given User <Contact1> blocks user Myself
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Then I see Conversations list with name <Contact1>
    And I see Conversations list with name <Contact2>
    And I wait until <Contact1> exists in backend search results
    When I open Search UI
    And I type user name "<Contact1>" in search field
    Then I see user <Contact1> in Search result list

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C407 @regression
  Scenario Outline: (BUG AN-2721) I want to unblock someone from their Profile view
    Given There are 4 users where <Name> is me
      # Having the extra user is a workaround for an app bug
    Given Myself is connected to <Contact1>,<Contact2>
    Given User <Name> blocks user <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact2>
    And I navigate back from dialog page
    And I wait until <Contact1> exists in backend search results
    And I open Search UI
    And I type user name "<Contact1>" in search field
    And I see user <Contact1> in Search result list
    And I tap on user name found on People picker page <Contact1>
    Then User info should be shown with Unblock button
    When I click Unblock button
    Then I see conversation view
    When I navigate back from dialog page
    Then I see Conversations list with name <Contact1>

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C705 @regression @rc
  Scenario Outline: Impossibility of starting 1:1 conversation with pending user (Search)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <IntermediateContact>
    Given <IntermediateContact> is connected to <Contact>
    Given <Contact> has an avatar picture from file <Picture>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I open Search UI
    And I wait until <Contact> exists in backend search results
    And I type user name "<Contact>" in search field
    And I tap on user name found on People picker page <Contact>
    Then I see connect to <Contact> dialog
    When I click Connect button on connect to page
    Then I see People picker page
    And I see user <Contact> in Search result list
    When I tap on user name found on People picker page <Contact>
    Then I see that connection is pending

    Examples:
      | Name      | Contact   | Picture                      | IntermediateContact |
      | user1Name | user2Name | aqaPictureContact600_800.jpg | user3Name           |

  @C409 @regression
  Scenario Outline: Verify you do not receive any messages from blocked person in 1:1 chat
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
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
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact2>
    And I tap conversation name from top toolbar
    And I press create group button
    And I type user name "<Contact1>" in search field
    And I tap on user name found on People picker page <Contact1>
    And I click on Add to conversation button
    Then I see Unable to Create Group Conversation overlay
    When I accept Unable to Create Group Conversation overlay
    Then I see Conversations list with conversations
    And I do not see group conversation with <Contact1>,<Contact2> in conversations list

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C183892 @C183893 @regression
  Scenario Outline: Verify autoconnect users by phones
    Given I delete all contacts from Address Book
    Given I see welcome screen
  # Add A into B's address book
    Given I add name <AName> and phone <APhone> with prefix <PhonePrefix> to Address Book
    Given I add name <A2Name> and phone <A2Phone> with prefix <PhonePrefix> to Address Book
    Given I see welcome screen
  # Here is B
    When I input a new phone number for user <Contact>
    And I input the verification code
    And I input my name
    And I wait for 3 seconds
    And I select to keep the current picture
    Then I see Conversations list with name <AName>
    And I see Conversations list with name <A2Name>

    Examples:
      | Contact   | AName            | APhone     | PhonePrefix | A2Name           | A2Phone    |
      | user1Name | AutoconnectUser2 | 1722036230 | +49         | AutoconnectUser3 | 1622360109 |


  @C192699 @regression @rc
  Scenario Outline: Verify autoconnect users by phone - Delay
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    When User Myself has phone numbers <PhonePrefix><APhone>,<PhonePrefix><A2Phone> in address book
    And I accept First Time overlay as soon as it is visible
    And I wait for 3 seconds
    Then I see Conversations list with name <AName>
    And I see Conversations list with name <A2Name>

    Examples:
      | Name      | APhone     | PhonePrefix | A2Phone    | AName            | A2Name           |
      | user1Name | 1722036230 | +49         | 1622360109 | AutoconnectUser2 | AutoconnectUser3 |

  @C194553 @regression
  Scenario Outline: Direct matching emails -  delayed
    # Given I am on Android 4.4 or better
    Given There are 2 users where <UserA> is me
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with no conversations
    Given User <UserB> has email <UserA> in address book
    When I wait for 10 seconds
    # Then I see the message "<Message>" in push notifications list
    When I open Search UI
    And I type user name "<UserB>" in search field
    Then I see user <UserB> in Search result list
    When I clear search result by tap clear button or back button
    And I tap conversations list settings button
    And I select "Account" settings menu item
    And I select "Log out" settings menu item
    And I confirm sign out
    And User <UserB> is me
    And I sign in using my email
    And I accept First Time overlay as soon as it is visible
    And I see Conversations list with no conversations
    And I open Search UI
    And I type user name "<UserA>" in search field
    Then I see user <UserA> in Search result list

    Examples:
      | UserA     | UserB     | Message          |
      | user1Name | user2Name | No spec for that |