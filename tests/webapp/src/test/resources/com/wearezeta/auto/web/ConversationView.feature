Feature: Conversation

  @smoke @id1626
  Scenario Outline: Send message in 1on1
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see my name in Contact list
    And I open conversation with <Contact>
    When I write random message
    And I send message
    Then I see random message in conversation

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

	@torun @id1617 
 Scenario Outline: Verify you can see image on the second end in a group conversation 
	Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see my name in Contact list
    And I open conversation with <ChatName>
	When I send picture <PictureName> to group conversation
	Then I see sent picture <PictureName> in the conversation view
	When I open self profile
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And I switch to sign in page
    And I see Sign In page
    And I Sign in using login <Login2> and password <Password2>
    And User <Name2> is me
    Then I see my name in Contact list
    And I open conversation with <ChatName>
    Then I see sent picture <PictureName> in the conversation view

	Examples: 
| Login      | Password      | Name      | Contact1  | Contact2  | ChatName             | Login2     | Password2     | Name2     |PictureName                |
| user1Email | user1Password | user1Name | user2Name | user3Name | SendMessageGroupChat | user2Email | user2Password | user2Name | userpicture_landscape.jpg |
    
      

  @smoke @id1628
  Scenario Outline: Send message to group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see my name in Contact list
    And I open conversation with <ChatName>
    When I write random message
    And I send message
    Then I see random message in conversation

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName             |
      | user1Email | user1Password | user1Name | user2Name | user3Name | SendMessageGroupChat |

  @smoke @id1545
  Scenario Outline: Archive and unarchive conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see my name in Contact list
    When I archive conversation <Contact>
    Then I do not see Contact list with name <Contact>
    When I open archive
    And I unarchive conversation <Contact>
    Then I see Contact list with name <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @staging @id1612
  Scenario Outline: Send Camera picture to contact in 1:1
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see my name in Contact list
    And I open conversation with <Contact>
    And I send picture <PictureName> to single conversation
    Then I see sent picture <PictureName> in the conversation view

    Examples: 
      | Login      | Password      | Name      | Contact   | PictureName               |
      | user1Email | user1Password | user1Name | user2Name | userpicture_landscape.jpg |

  @staging @id1934
  Scenario Outline: Send Camera picture to group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see my name in Contact list
    And I open conversation with <ChatName>
    When I send picture <PictureName> to group conversation
    Then I see sent picture <PictureName> in the conversation view

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName             | PictureName               |
      | user1Email | user1Password | user1Name | user2Name | user3Name | SendPictureGroupChat | userpicture_landscape.jpg |

  @smoke @id1918
  Scenario Outline: Mute 1on1 conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see my name in Contact list
    When I set muted state for conversation <Contact>
    And I open self profile
    Then I see that conversation <Contact> is muted

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
      
  @staging @id1919
  Scenario Outline: Unmute 1on1 conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Name> muted conversation with <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see my name in Contact list
    And I see that conversation <Contact> is muted
    When I set unmuted state for conversation <Contact>
    And I open self profile
    Then I see that conversation <Contact> is not muted

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @smoke @id1706
  Scenario Outline: Verify you cannot Ping several times in a row
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see my name in Contact list
    And I open conversation with <Contact>
	When I click ping button
	Then I see ping message <PING>
	When I click ping button
	Then I see ping message <PING_AGAIN>
	Then I see only one ping message

    Examples: 
      | Login      | Password      | Name      | Contact   | PING   | PING_AGAIN   |
      | user1Email | user1Password | user1Name | user2Name | pinged | pinged again |
