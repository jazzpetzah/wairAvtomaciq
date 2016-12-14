Feature: Connect

  @C676 @regression @rc @legacy
  Scenario Outline: I can send/cancel sending connection request from Search
    Given There are 3 users where <Name> is me
    Given Myself is connected to <IntermediateContact>
    Given <IntermediateContact> is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    And I wait until <Contact> exists in backend search results
    When I open Search UI
    And I type user name "<Contact>" in search field
    And I tap on user name found on Search page <Contact>
    Then I see user name "<Contact>" on Single unconnected user details page
    And I tap back button
    And I see Search page
    And I tap on user name found on Search page <Contact>
    Then I see user name "<Contact>" on Single unconnected user details page
    When I tap connect button on Single unconnected user details page
    #wait UI animation to finish
    And I wait for 1 second
    Then I see Search page
    When I tap Clear button
    Then I see Conversations list with name <Contact>

    Examples:
      | Name      | Contact   | IntermediateContact |
      | user1Name | user2Name | user3name           |

  @C383 @regression @rc @legacy
  Scenario Outline: I can accept/ignore connection requests from conversation list and inbox updated correctly
    Given There are 3 users where <Name> is me
    Given <Contact1> sent connection request to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Then I see Conversations list with name <WaitingMess1>
    And <Contact2> sent connection request to me
    When I wait for 2 seconds
    Then I see Conversations list with name <WaitingMess2>
    When I tap on conversation name <WaitingMess2>
    And I tap ignore button for user <Contact1> on Single pending incoming connection page
    And I tap back button
    Then I see Conversations list with name <WaitingMess1>
    When I tap on conversation name <WaitingMess1>
    And I tap connect button for user <Contact2> on Single pending incoming connection page
    And I wait for 5 seconds
    And I navigate back from conversation
    Then I see Conversations list with name <Contact2>
    Then I do not see Conversations list with name <WaitingMess1>

    Examples:
      | Name      | Contact1  | Contact2  | WaitingMess1     | WaitingMess2     |
      | user1Name | user2Name | user3Name | 1 person waiting | 2 people waiting |

  @C311220 @regression @rc
  Scenario Outline: I can accept/ignore connection requests from search and inbox updated correctly
    Given There are 3 users where <Name> is me
    Given <Contact1> sent connection request to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given <Contact2> sent connection request to me
    When I wait for 2 seconds
    Then I see Conversations list with name <WaitingMess2>
    And I wait until <Contact1> exists in backend search results
    When I open Search UI
    And I type user name "<Contact1>" in search field
    And I tap on user name found on Search page <Contact1>
    And I tap ignore button for user <Contact1> on Single pending incoming connection page
    And I tap back button
    Then I see Conversations list with name <WaitingMess1>
    When I open Search UI
    And I type user name "<Contact2>" in search field
    And I tap on user name found on Search page <Contact2>
    When I tap connect button for user <Contact2> on Single pending incoming connection page
    And I wait for 5 seconds
    And I navigate back from conversation
    Then I see Conversations list with name <Contact2>
    Then I do not see Conversations list with name <WaitingMess1>

    Examples:
      | Name      | Contact1  | Contact2  | WaitingMess1     | WaitingMess2     |
      | user1Name | user2Name | user3Name | 1 person waiting | 2 people waiting |

  @C384 @regression
  Scenario Outline: I can ignore a connect request and reconnect later
    Given There are 2 users where <Name> is me
    Given <Contact> sent connection request to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Then I see Conversations list with name <WaitingMess>
    When I tap on conversation name <WaitingMess>
    And I tap ignore button on Single pending incoming connection page
    Then I do not see Conversations list with name <WaitingMess>
    And I wait until <Contact> exists in backend search results
    When I open Search UI
    And I type user name "<Contact>" in search field
    And I tap on user name found on Search page <Contact>
    Then I see user name "<Contact>" on Single unconnected user details page
    When I tap connect button on Single pending incoming connection page
    And I wait for 5 seconds
    Then I see conversation view

    Examples:
      | Name      | Contact   | WaitingMess      |
      | user1Name | user2Name | 1 person waiting |

  @C388 @regression
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
    And I tap on user name found on Search page <Contact>
    Then I see user name "<Contact>" on Single unconnected user details page
    When I tap connect button on Single unconnected user details page
    And <Contact> ignore all requests
    And I tap Clear button
    And I tap on conversation name <Contact>
    Then I see avatar on Single pending outgoing connection page
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
    Then I see user name "<Contact>" on Single pending incoming connection page
    And I see connect button on Single pending incoming connection page
    And I see ignore button on Single pending incoming connection page
    And I tap ignore button on Single pending incoming connection page

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
    And I tap on user name found on Search page <Contact>
    Then I see user name "<Contact>" on Single unconnected user details page
    When I tap connect button on Single unconnected user details page
    And I wait for 2 seconds
    And <Contact> accept all requests
    And I wait for 2 seconds
    And I tap Clear button
    Then I see Conversations list with name <Contact>
    When I tap on conversation name <Contact>
    And I see conversation view

    Examples:
      | Name      | Contact   | IntermediateContact |
      | user1Name | user2Name | user3Name           |

  @C389 @regression
  Scenario Outline: I want to initiate a connect request by selecting someone from within a group conversation
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to <Name>,<Contact2>
    Given <Contact1> has group chat <ChatName> with <Name>, <Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <ChatName>
    And I tap conversation name from top toolbar
    #Sometimes here only one user visible (backend issue)
    And I tap on contact <Contact2> on Group info page
    Then I see user name "<Contact2>" on Single unconnected user details page
    When I tap connect button on Single unconnected user details page
    And I tap connect button on Single unconnected user details page
    And I tap back button
    And I navigate back from conversation
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
    And I tap open menu button on Single connected user details page
    And I tap BLOCK button on Conversation options menu overlay page
    And I tap BLOCK button on Confirm overlay page
    Then I do not see Conversations list with name <Contact1>
    And I wait until <Contact1> exists in backend search results
    When I open Search UI
    And I type user name "<Contact1>" in search field
    And I see user <Contact1> in Search result list
    And I tap on user name found on Search page <Contact1>
    Then I see unblock button on Single blocked user details page

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C391 @regression
  Scenario Outline: I can unblock/connect to user from group conversation
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to Myself, <Contact2>
    Given <Contact1> has group chat <ChatName> with Myself, <Contact2>
    Given User Myself blocks user <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Then I do not see Conversations list with name <Contact1>
    Then I do not see Conversations list with name <Contact2>
    #connect
    When I tap on conversation name <ChatName>
    And I tap conversation name from top toolbar
    #Sometimes here only one user visible (backend issue)
    And I tap on contact <Contact2> on Group info page
    Then I see user name "<Contact2>" on Group unconnected user details page
    When I tap on +connect button on Group unconnected user details page
    And I tap on connect button on Group unconnected user details page
    And I tap back button
    And I navigate back from conversation
    Then I see Conversations list with name <Contact2>
    #unblock
    When I tap on conversation name <ChatName>
    And I tap conversation name from top toolbar
    And I tap on contact <Contact1> on Group info page
    When I tap blocked button on Group blocked user details page
    And I tap unblock button on Group blocked user details page
    And I navigate back from conversation
    Then I see Conversations list with name <Contact1>

    Examples:
      | Name      | Contact1  | Contact2  | ChatName         |
      | user1Name | user2Name | user3Name | ContactGroupChat |

  @C392 @regression
  Scenario Outline: I can find user in search even he blocked me
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
  Scenario Outline: (BUG AN-2721) I can find blocked user in Search and unblock
    Given There are 4 users where <Name> is me
      # Having the extra user is a workaround for an app bug
    Given Myself is connected to <Contact1>,<Contact2>
    Given User <Name> blocks user <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact2>
    And I navigate back from conversation
    And I wait until <Contact1> exists in backend search results
    And I open Search UI
    And I type user name "<Contact1>" in search field
    And I see user <Contact1> in Search result list
    And I tap on user name found on Search page <Contact1>
    Then I see unblock button on Single blocked user details page
    Then I see conversation view
    When I navigate back from conversation
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
    And I tap on user name found on Search page <Contact>
    Then I see user name "<Contact>" on Single unconnected user details page
    When I tap connect button on Single unconnected user details page
    Then I see Search page
    And I see user <Contact> in Search result list
    When I tap on user name found on Search page <Contact>
    Then I see avatar on Single pending outgoing connection page

    Examples:
      | Name      | Contact   | Picture                      | IntermediateContact |
      | user1Name | user2Name | aqaPictureContact600_800.jpg | user3Name           |

  @C409 @regression
  Scenario Outline: Verify you do not receive any messages from blocked person in 1:1 chat
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I tap on text input
    And I type the message "<Message>" and send it by cursor Send button
    And User Myself blocks user <Contact>
    And User <Contact> sends encrypted image <Picture> to single user conversation <Name>
    And User <Contact> sends encrypted message to user <Name>
    And User <Contact> securely pings conversation Myself
    Then I see the most recent conversation message is "<Message>"

    Examples:
      | Name      | Contact   | Message          | Picture     |
      | user1Name | user2Name | Hello my friend! | testing.jpg |

  @C82540 @regression
  Scenario Outline: Verify I cannot create a new group conversation with a person who blocked me
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given User <Contact1> blocks user Myself
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact2>
    And I tap conversation name from top toolbar
    And I tap create group button on Single connected user details page
    And I type user name "<Contact1>" in search field
    And I tap on user name found on Search page <Contact1>
    And I tap on Add to conversation button
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
    And I see Unique Username Takeover page
    And I see username on Unique Username Takeover page
    And I tap Keep This One button on Unique Username Takeover page
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
    Given There are 2 users where <UserA> is me
    Given User <UserB> has email <UserAEmail> in address book
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with no conversations
    When I open Search UI
    And I type the first 5 chars of user name "<UserB>" in search field
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
    And I type the first 5 chars of user name "<UserA>" in search field
    Then I see user <UserA> in Search result list

    Examples:
      | UserA     | UserB     | UserAEmail |
      | user1Name | user2Name | user1Email |
