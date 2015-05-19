Feature: Conversation View

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

  @smoke @id1617 
  Scenario Outline: Verify you can see image on the second end in a group conversation 
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    And I open conversation with <ChatName>
    When I send picture <PictureName> to the current conversation
    Then I see sent picture <PictureName> in the conversation view
    When I open self profile
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And I switch to sign in page
    And I see Sign In page
    And User <Name2> is me
    And I Sign in using login <Login2> and password <Password2>
    Then I see my name on top of Contact list
    And I open conversation with <ChatName>
    Then I see sent picture <PictureName> in the conversation view

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName             | Login2     | Password2     | Name2     | PictureName               |
      | user1Email | user1Password | user1Name | user2Name | user3Name | SendMessageGroupChat | user2Email | user2Password | user2Name | userpicture_landscape.jpg |

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

  @smoke @id1612
  Scenario Outline: Send picture to contact in 1:1
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

  @regression @id2319
  Scenario Outline: Able to send and play youtube link
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    And I open conversation with <Contact>
    When I write message <Youtubelink1>
    And I send message
    Then I see embedded youtube video of <Youtubelink1>
    When I write message <Youtubelink2>
    And I send message
    Then I see embedded youtube video of <Youtubelink2>
    When I write message <Youtubelink3>
    And I send message
    Then I see embedded youtube video of <Youtubelink3>
    When I write message <Youtubelink4>
    And I send message
    Then I see embedded youtube video of <Youtubelink2>

    Examples: 
      | Login      | Password      | Name      | Contact   | Youtubelink1                               | Youtubelink2                                                 | Youtubelink3                          | Youtubelink4                 |
      | user1Email | user1Password | user1Name | user2Name | http://www.youtube.com/watch?v=JOCtdw9FG-s | https://www.youtube.com/watch?v=txqiwrbYGrs&feature=youtu.be | https://www.youtube.com/v/_1w2aASUpWQ | https://youtu.be/QH2-TGUlwu4 |

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

  @regression @id2011
  Scenario Outline: I can see missed messages when rejoining a conversation after leaving it
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given User Myself sent message <Msg1FromUserA> to conversation <ChatName>
    Given User <Contact1> is me
    And I Sign in using login <Contact1Email> and password <Contact1Password>
    And I see Contact list with name <ChatName>
    When I open conversation with <ChatName>
    Then I see text message <Msg1FromUserA>
    And I click People button in group conversation
    And I see Group Participants popover
    When I click Leave button on Group Participants popover
    And I confirm leave group conversation on Group Participants popover
    Then I do not see Contact list with name <ChatName>
    And User <Name> sent message <Msg2FromUserA> to conversation <ChatName>
    When I open archive
    And I unarchive conversation <ChatName>
    When I open conversation with <ChatName>
    Then I do not see text message <Msg2FromUserA>
    When User <Name> added contact <Contact1> to group chat <ChatName>
    Then I see text message <Msg2FromUserA>

    Examples: 
      | Name      | Contact1  | Contact1Email | Contact1Password | Contact2  | ChatName  | Msg1FromUserA | Msg2FromUserA |
      | user1Name | user2Name | user2Email    | user2Password    | user3Name | GroupChat | Message1      | Message2      |
      
  @torun @id1563
  Scenario Outline: Verify you dont receive any messages from blocked person in 1:1 chat
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    And Contact <Contact> sends image <Picture> to single user conversation <Name>
    And Contact <Contact> sends message <Message1> to user <Name>
    And User <Name> blocks user <Contact>
    And Contact <Contact> sends image <Picture2> to single user conversation <Name>
    And Contact <Contact> send message <Message2> to user <Name>
    Then Last message is <Message1>

    And I Sign in using login <ContactEmail> and password <ContactPassword>
    Then I see my name on top of Contact list
    When I open conversation with <Name>
    Then I see text message <Message2>
    When I open self profile
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    
    And User <Name> unblocks user <Contact>

    And I Sign in using login <Login> and password <Password>
    Then I see my name is selected on top of Contact list
    Then I see Contact list with name <Contact>
    When I open conversation with <Contact>
    Then I see text message <Message2>

    Examples: 
      | Name      | Contact  | ContactEmail | ContactPassword | Msg1FromUserA | Msg2FromUserA | Msg3FromUserB |
      | user1Name | user2Name | user2Email    | user2Password | Message1      | Message2      | Message3      |
