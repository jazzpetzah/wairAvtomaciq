Feature: Conversation

  @smoke @id1626
  Scenario Outline: Send message in 1on1
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    And I open conversation with <Contact>
    When I write random message
    And I send message
    Then I see random message in conversation

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @smoke @id1628
  Scenario Outline: Send message to group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
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
    And I see my name on top of Contact list
    When I archive conversation <Contact>
    Then I do not see Contact list with name <Contact>
    When I open archive
    And I unarchive conversation <Contact>
    Then I see Contact list with name <Contact>

    Examples:
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @smoke @id1612
  Scenario Outline: Send Camera picture to contact in 1:1
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    And I open conversation with <Contact>
    And I send picture <PictureName> to the current conversation
    Then I see sent picture <PictureName> in the conversation view

    Examples: 
      | Login      | Password      | Name      | Contact   | PictureName               |
      | user1Email | user1Password | user1Name | user2Name | userpicture_landscape.jpg |

  @smoke @id1934
  Scenario Outline: Send Camera picture to group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    And I open conversation with <ChatName>
    When I send picture <PictureName> to the current conversation
    Then I see sent picture <PictureName> in the conversation view

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName             | PictureName               |
      | user1Email | user1Password | user1Name | user2Name | user3Name | SendPictureGroupChat | userpicture_landscape.jpg |

  @smoke @id1918
  Scenario Outline: Mute 1on1 conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
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
    And I see my name on top of Contact list
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
    And I see my name on top of Contact list
    And I open conversation with <Contact>
	When I click ping button
	Then I see ping message <PING>
	When I click ping button
	Then I see ping message <PING_AGAIN>
	Then I see only one ping message

    Examples: 
      | Login      | Password      | Name      | Contact   | PING   | PING_AGAIN   |
      | user1Email | user1Password | user1Name | user2Name | pinged | pinged again |

  @torun @id2011
  Scenario Outline: I can see missed messages when rejoining a conversation after leaving it
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given User Myself sent message <Msg1FromUserA> to conversation <ChatName>
    Given User <Contact1> is me
    And I Sign in using login <Contact1Email> and password <Contact1Password>
    And I see Contact list with name <ChatName>
    When I open conversation with <ChatName>
    Then I see <Msg1FromUserA> message
    And I click People button in group conversation
    And I see Group Participants popover
    When I click Leave button on Group Participants popover
    And I confirm leave group conversation on Group Participants popover
    Then I do not see Contact list with name <ChatName>
    And User <Name> sent message <Msg2FromUserA> to conversation <ChatName>
    When I open archive
    And I unarchive conversation <ChatName>
    When I open conversation with <ChatName>
    Then I do not see <Msg2FromUserA> message
    When User <Name> added contact <Contact1> to group chat <ChatName>
    Then I see <Msg2FromUserA> message

    Examples: 
      | Name      | Contact1  | Contact1Email | Contact1Password | Contact2  | ChatName  | Msg1FromUserA | Msg2FromUserA |
      | user1Name | user2Name | user2Email    | user2Password    | user3Name | GroupChat | Message1      | Message2      |
