Feature: Connect to user

  @smoke @id473
  Scenario Outline: Receive invitation from user
    Given There are 2 users where <Name> is me
    Given <Contact> has sent connection request to <Name>
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    When I see connect request
    And I open connection requests
    And I accept all connection requests
    Then I see contact <Contact> in Contact list

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @regression @id616
  Scenario Outline: Conversation created on second end after user accept connection request
    Given There are 2 users where <Name> is me
    Given <Contact> has sent connection request to <Name>
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    When I see connect request
    And I open connection requests
    And I accept all connection requests
    And I see contact <Contact> in Contact list
    And I sign out
    And I Sign in using login <Contact> and password <Password>
    And I see my name <Contact> in Contact list
    Then I see contact <Name> in Contact list
    And I open conversation with <Name>
    And I see message CONNECTED TO <Name> in conversation

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @smoke @id1528
  Scenario Outline: Verify sending a connection request to user chosen from people view
    Given There are 2 users where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    When I open People Picker from contact list
    And I wait up to 15 seconds until <ContactEmail> exists in backend search results
    And I search by email for user <Contact>
    And I see user <Contact> in search results
    And I select user <Contact> from search results
    And I send invitation to user
    Then I see contact <Contact> in Contact list

    Examples: 
      | Login      | Password      | Name      | Contact   | ContactEmail |
      | user1Email | user1Password | user1Name | user2Name | user2Email   |

  @staging @id1529
  Scenario Outline: Verify you can unblock someone from search list
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Name> blocks user <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I wait for 10 seconds
    Then I do not see conversation <Contact> in contact list
    When I open People Picker from contact list
    When I wait up to 15 seconds until <Contact> exists in backend search results
    And I search by email for user <Contact>
    And I see user <Contact> in search results
    And I select user <Contact> from search results
    And I unblock user
    Then I open conversation with <Contact>
    And Contact <Contact> sends random message to user <Name>
    And I see random message in conversation

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @regression @id1386
  Scenario Outline: Verify you dont receive any messages from blocked person in 1:1 chat
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Name> blocks user <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    When Contact <Contact> sends random message to user <Name>
    And Contact <Contact> ping conversation <Name>
    And Contact <Contact> sends image testing.jpg to single user conversation <Name>
    And I wait for 10 seconds
    Then I do not see conversation <Contact> in contact list
    When I open People Picker from contact list
    When I wait up to 15 seconds until <Contact> exists in backend search results
    And I search by email for user <Contact>
    And I see user <Contact> in search results
    And I select user <Contact> from search results
    And I unblock user
    Then I open conversation with <Contact>
    And I see random message in conversation
    And I see message <Contact> PINGED in conversation
    Then I see picture in conversation
    And Contact <Contact> sends random message to user <Name>
    And I see random message in conversation

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @regression @id617
  Scenario Outline: Verify 1:1 conversation is not created on the second end after you ignore connection request
    Given There are 2 users where <Name> is me
    Given <Contact> has sent connection request to <Name>
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    When I see connect request
    And I open connection requests
    And I ignore all connection requests
    Then I do not see conversation <Contact> in contact list
    When I sign out
    Given I Sign in using login <Contact> and password <Password>
    And I see my name <Contact> in Contact list
    And I open conversation with <Name>
    And I see message CONNECTING TO <Name> in conversation

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @regression @id1407
  Scenario Outline: Verify impossiibility of starting 1:1 conversation with pending user (Search)
    Given There are 2 users where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    When I open People Picker from contact list
    Given I wait up to 60 seconds until <ContactEmail> exists in backend search results
    And I search by email for user <Contact>
    And I see user <Contact> in search results
    And I select user <Contact> from search results
    And I send invitation to user
    And I open conversation with <Contact>
    Then I can not write a random message

    Examples: 
      | Login      | Password      | Name      | Contact   | ContactEmail |
      | user1Email | user1Password | user1Name | user2Name | user2Email   |

  @staging @id1035
  Scenario Outline: Verify incoming connection requests list disappears after all requests are processed
    Given There are 3 users where <Name> is me
    Given <Contact1> has sent connection request to me
    Given <Contact2> has sent connection request to me
    Given I sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I see 2 connect requests
    And I open connection requests
    When I accept connection request from user <Contact1>
    Then I see 1 connect request
    When I open connection requests
    And I do not see connection request from user <Contact1>
    And I accept connection request from user <Contact2>
    Then I do not see connection requests

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  |
      | user1Email | user1Password | user1Name | user2Name | user3Name |
