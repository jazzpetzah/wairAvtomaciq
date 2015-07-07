Feature: Conversation View

  @smoke @id1626
  Scenario Outline: Send message in 1on1
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
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
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <ChatName>
    When I send picture <PictureName> to the current conversation
    Then I see sent picture <PictureName> in the conversation view
    And I see only 1 picture in the conversation
    When I open self profile
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And I see Sign In page
    And User <Name2> is me
    And I Sign in using login <Login2> and password <Password2>
    Then I see my avatar on top of Contact list
    And I open conversation with <ChatName>
    Then I see sent picture <PictureName> in the conversation view
    And I see only 1 picture in the conversation

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName             | Login2     | Password2     | Name2     | PictureName               |
      | user1Email | user1Password | user1Name | user2Name | user3Name | SendMessageGroupChat | user2Email | user2Password | user2Name | userpicture_landscape.jpg |

  @regression @id1628
  Scenario Outline: Send message to group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <ChatName>
    When I write random message
    And I send message
    Then I see random message in conversation

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName             |
      | user1Email | user1Password | user1Name | user2Name | user3Name | SendMessageGroupChat |

  @regression @id1612
  Scenario Outline: Send picture to contact in 1:1
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    And I send picture <PictureName> to the current conversation
    Then I see sent picture <PictureName> in the conversation view
    And I see only 1 picture in the conversation

    Examples: 
      | Login      | Password      | Name      | Contact   | PictureName               |
      | user1Email | user1Password | user1Name | user2Name | userpicture_landscape.jpg |

  @regression @id2319
  Scenario Outline: Able to send and play youtube link
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
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

  @regression @id1934
  Scenario Outline: Send Camera picture to group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
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
    Given I switch to Sign In page
    Given I Sign in using login <Contact1Email> and password <Contact1Password>
    And I see Contact list with name <ChatName>
    When I open conversation with <ChatName>
    Then I see text message <Msg1FromUserA>
    And I click People button in group conversation
    And I see Group Participants popover
    When I click Leave button on Group Participants popover
    And I click confirm leave group conversation on Group Participants popover
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

  @regression @id1688
  Scenario Outline: Verify you can add maximum+1 number of participants into group conversation
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I see my avatar on top of Contact list
    And I open People Picker from Contact List
    And I type <Contact1> in search field of People Picker
    And I select <Contact1> from People Picker results
    And I type <Contact2> in search field of People Picker
    And I select <Contact2> from People Picker results
    And I choose to create conversation from People Picker
    And I open conversation with <Contact1>, <Contact2>
    And I click People button in group conversation
    When I click Add People button on Group Participants popover
    And I see Add People message on Group Participants popover
    And I confirm add to chat on Group Participants popover
    And I select the first 125 participants from Group Participants popover search results
    And I choose to create group conversation from Group Participants popover
    When I click People button in group conversation
    Then I see 128 participants in the Group Participants popover
    When I click Add People button on Group Participants popover
    And I see Add People message on Group Participants popover
    And I confirm add to chat on Group Participants popover
    And I select the first 1 participants from Group Participants popover search results
    And I choose to create group conversation from Group Participants popover
    When I click People button in group conversation
    Then I see 128 participants in the Group Participants popover

    Examples: 
      | Login                       | Password   | Contact1   | Contact2   |
      | smoketester+id1688@wire.com | aqa123456! | perf.200.1 | perf.200.2 |

  @id2279 @regression
  Scenario Outline: Send a long message containing new lines in 1on1
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    When I write 10 new lines
    And I write message aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    And I write 10 new lines
    And I write message bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb
    And I write 10 new lines
    And I send message
    Then I verify the last text message equals to <ExpectedMessage>

    Examples: 
      | Login      | Password      | Name      | Contact   | ExpectedMessage                   |
      | user1Email | user1Password | user1Name | user2Name | ('a' * 100)('LF' * 10)('b' * 100) |

  @staging @id1624
  Scenario Outline: Verify you can see conversation images in fullscreen
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    And I send picture <PictureName> to the current conversation
    And I see sent picture <PictureName> in the conversation view
    When I click on picture
    Then I see picture in fullscreen
    When I click x button to close fullscreen mode
    Then I do not see picture in fullscreen
    When I click on picture
    Then I see picture in fullscreen
    When I click on black border to close fullscreen mode
    Then I do not see picture in fullscreen

    Examples: 
      | Login      | Password      | Name      | Contact   | PictureName               |
      | user1Email | user1Password | user1Name | user2Name | userpicture_landscape.jpg |

  @id2891 @staging
  Scenario Outline: Verify you can send not-random gif with giphy button
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    When I write message <Message>
    And I click GIF button
    Then I see Giphy popup
    And I verify that the search of the Giphy popup contains <Message>
    And I see gif image in Giphy popup
    And I see more button in Giphy popup
    When I click send button in Giphy popup
    Then I see sent gif in the conversation view
    And I verify the second last text message equals to <ExpectedMessage>

    Examples: 
      | Login      | Password      | Name      | Contact   | Message | ExpectedMessage     |
      | user1Email | user1Password | user1Name | user2Name | cat     | cat • via giphy.com |
