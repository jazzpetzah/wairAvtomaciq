Feature: Conversation View

  @C119438 @regression
  Scenario Outline: Verify group conversation history is loaded properly
    Given There are 3 users where <Name> is me
    Given user <Contact1> adds a new device Device1 with label Label1
    Given user <Name> adds a new device Device1 with label Label1
    Given <Contact1> is connected to Myself,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I see the history info page
    Then I click confirm on history info page
    When I am signed in properly
    Then I open self profile
    When I click gear button on self profile page
    And I select Log out menu item on self profile page
    And I see the clear data dialog
    And I click Logout button on clear data dialog
    Then I see Sign In page
    And <Contact1> has group chat <ChatName> with Myself,<Contact2>
    And Contact <Name> sends 35 messages with prefix <OtherDeviceMsg> via device Device1 to group conversation <ChatName>
    And I wait for 10 seconds
    And Contact <Contact1> sends 100 messages with prefix <OfflineMsg> via device Device1 to group conversation <ChatName>
    When I Sign in using login <Login> and password <Password>
    And I wait for 10 seconds
    And I am signed in properly
    And I open conversation with <ChatName>
    And Contact <Contact1> sends 10 messages with prefix <OnlineMsg> via device Device1 to group conversation <ChatName>
    And I wait for 5 seconds
    Then I verify all remembered messages are present in conversation <ChatName>

    Examples: 
      | Login      | Password      | Name      | Contact1   | Contact2  | ChatName                | OfflineMsg | OnlineMsg | OtherDeviceMsg |
      | user1Email | user1Password | user1Name | user2Name  | user3Name | HistoryOfflineGroupChat | OFFLINE    | ONLINE    | OTHERDEVICE    |

  @C1703 @smoke
  Scenario Outline: Send message in 1on1
    Given There are 2 users where <Name> is me
    Given user <Contact> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    When I write random message
    And I send message
    Then I see random message in conversation

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @C1701 @smoke
  Scenario Outline: Verify you can see image on the second end in a group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login2> and password <Password2>
    Given I am signed in properly
    Given I see Welcome page
    Given I confirm keeping picture on Welcome page
    Given I see Contact list with name <ChatName>
    Given I open self profile
    Given I click gear button on self profile page
    Given I select Log out menu item on self profile page
    Given I see the clear data dialog
    Given I click Logout button on clear data dialog
    Given I see Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <ChatName>
    When I send picture <PictureName> to the current conversation
    Then I see sent picture <PictureName> in the conversation view
    And I see only 1 picture in the conversation
    When I open self profile
    And I click gear button on self profile page
    And I select Log out menu item on self profile page
    And I see the clear data dialog
    And I click Logout button on clear data dialog
    And I see Sign In page
    And User <Name2> is me
    And I Sign in using login <Login2> and password <Password2>
    Then I am signed in properly
    And I open conversation with <ChatName>
    Then I see sent picture <PictureName> in the conversation view
    And I see only 1 picture in the conversation

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName             | Login2     | Password2     | Name2     | PictureName               |
      | user1Email | user1Password | user1Name | user2Name | user3Name | SendMessageGroupChat | user2Email | user2Password | user2Name | userpicture_landscape.jpg |

  @C1704 @regression
  Scenario Outline: Send message to group chat
    Given There are 3 users where <Name> is me
    Given user <Contact1> adds a new device Device1 with label Label1
    Given user <Contact2> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <ChatName>
    When I write random message
    And I send message
    Then I see random message in conversation

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName             |
      | user1Email | user1Password | user1Name | user2Name | user3Name | SendMessageGroupChat |

  @C1700 @regression
  Scenario Outline: Send picture to contact in 1:1
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    And I send picture <PictureName> to the current conversation
    Then I see sent picture <PictureName> in the conversation view
    And I see only 1 picture in the conversation

    Examples: 
      | Login      | Password      | Name      | Contact   | PictureName               |
      | user1Email | user1Password | user1Name | user2Name | userpicture_landscape.jpg |

  @C1784 @regression
  Scenario Outline: Able to send and play youtube link
    Given There are 2 users where <Name> is me
    Given user <Contact> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
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
    Then I see embedded youtube video of <Youtubelink4>

    Examples: 
      | Login      | Password      | Name      | Contact   | Youtubelink1                               | Youtubelink2                                                 | Youtubelink3                          | Youtubelink4                 |
      | user1Email | user1Password | user1Name | user2Name | http://www.youtube.com/watch?v=JOCtdw9FG-s | https://www.youtube.com/watch?v=txqiwrbYGrs&feature=youtu.be | https://www.youtube.com/v/_1w2aASUpWQ | https://youtu.be/QH2-TGUlwu4 |

  @C1759 @regression
  Scenario Outline: Send Camera picture to group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <ChatName>
    When I send picture <PictureName> to the current conversation
    Then I see sent picture <PictureName> in the conversation view

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName             | PictureName               |
      | user1Email | user1Password | user1Name | user2Name | user3Name | SendPictureGroupChat | userpicture_landscape.jpg |

  @C1764 @regression @e2ee
  Scenario Outline: I cannot see missed messages when rejoining a conversation after leaving it
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    And I see Contact list with name <ChatName>
    When I open conversation with <ChatName>
    And Contact <Contact1> sends message <Msg1FromUserA> to group conversation <ChatName>
    Then I see text message <Msg1FromUserA>
    And I click People button in group conversation
    And I see Group Participants popover
    When I click Leave button on Group Participants popover
    And I click confirm leave group conversation on Group Participants popover
    Then I do not see Contact list with name <ChatName>
    And Contact <Contact1> sends message <Msg2FromUserA> to group conversation <ChatName>
    When I open archive
    And I unarchive conversation <ChatName>
    When I open conversation with <ChatName>
    Then I do not see text message <Msg2FromUserA>
    When User <Contact1> added contact <Name> to group chat <ChatName>
    Then I do not see text message <Msg2FromUserA>

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName             | Msg1FromUserA | Msg2FromUserA |
      | user1Email | user1Password | user1Name | user2Name | user3Name | SendPictureGroupChat | Message1      | Message2      |

  @C1710 @regression
  Scenario Outline: Verify you can add maximum+1 number of participants into group conversation
    Given I switch to sign in page
    Given I enter email "<Login>"
    Given I enter password "<Password>"
    Given I press Sign In button
    Given I see the history info page
    Given I click confirm on history info page
    Given I wait for 20 seconds
    And I am signed in properly
    And I open People Picker from Contact List
    And I type <Contact1> in search field of People Picker
    And I select <Contact1> from People Picker results
    And I wait for the search field of People Picker to be empty
    And I type <Contact2> in search field of People Picker
    And I select <Contact2> from People Picker results
    And I choose to create conversation from People Picker
    And I open conversation with <Contact1>, <Contact2>
    And I click People button in group conversation
    And I see Group Participants popover
    When I click Add People button on Group Participants popover
    And I select the first 125 participants from Group Participants popover search results
    And I choose to create group conversation from Group Participants popover
    When I click People button in group conversation
    And I see Group Participants popover
    Then I see 127 participants in the Group Participants popover
    When I click Add People button on Group Participants popover
    And I select the first 1 participants from Group Participants popover search results
    And I choose to create group conversation from Group Participants popover
    And I see full house warning modal
    And I click on close button in full house warning modal
    And I do not see full house warning modal
    When I click People button in group conversation
    And I see Group Participants popover
    Then I see 127 participants in the Group Participants popover

    Examples: 
      | Login                       | Password   | Contact1   | Contact2   |
      | smoketester+id1688@wire.com | aqa123456! | perf.200.1 | perf.200.2 |

  @C1781 @regression
  Scenario Outline: Send a long message containing new lines in 1on1
    Given There are 2 users where <Name> is me
    Given user <Contact> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
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

  @C49977 @regression
  Scenario Outline: Send a really long message to group conversation
    Given I switch to Sign In page
    Given I Sign in temporary using login <Login> and password <Password>
    Given I see the history info page
    Given I click confirm on history info page
    Given I am signed in properly
    When I open conversation with <ChatName>
    And I paste message from file <File>
    And I send message
    Then I verify the last text message equals file <File>

    Examples:
      | Login                         | Password   | ChatName    | File           |
      | smoketester+68b16b1c@wire.com | aqa123456! | Lorem ipsum | loremipsum.txt |

  @C131207 @regression
  Scenario Outline: Receive a really long message to group conversation
    Given There is a known user <Contact> with email <ContactEmail> and password <Password>
    Given User <Contact> removes all his registered OTR clients
    Given I switch to Sign In page
    Given I Sign in temporary using login <Login> and password <Password>
    Given I see the history info page
    Given I click confirm on history info page
    Given I am signed in properly
    When I open conversation with <ChatName>
    And Contact <Contact> sends long message from file <File> to group conversation <ChatName>
    Then I verify the last text message equals file <File>

    Examples:
      | Login                         | Password   | ChatName       | File           | Contact  | ContactEmail                  |
      | smoketester+68b16b1c@wire.com | aqa123456! | ReceiveLongMsg | loremipsum.txt | db76e9c3 | smoketester+db76e9c3@wire.com |

  @C1702 @regression
  Scenario Outline: Verify you can see conversation images in fullscreen
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    And I send picture <PictureName> to the current conversation
    And I see sent picture <PictureName> in the conversation view
    When I click on picture
    Then I see picture <PictureName> in fullscreen
    When I click x button to close fullscreen mode
    Then I do not see picture <PictureName> in fullscreen
    When I click on picture
    Then I see picture <PictureName> in fullscreen
    When I click on black border to close fullscreen mode
    Then I do not see picture <PictureName> in fullscreen

    Examples: 
      | Login      | Password      | Name      | Contact   | PictureName               |
      | user1Email | user1Password | user1Name | user2Name | userpicture_landscape.jpg |

  @C1792 @regression
  Scenario Outline: Verify you can send not-random gif with giphy button
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
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

  @C1797 @regression
  Scenario Outline: Verify that typed-in messages are not lost
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    When I write message <Message>
    And I refresh page
    And I wait for 5 seconds
    Then I verify that message <Message> was cached

    Examples: 
      | Login      | Password      | Name      | Contact   | Message                                  |
      | user1Email | user1Password | user1Name | user2Name | All of these Candlejack jokes aren’t fu- |

  @C1793 @regression
  Scenario Outline: Verify Start (Search) is opened when you press ⌥ ⌘ N (Mac) or alt + ctrl + N (Win)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    When I type shortcut combination to open search
    Then I see Search is opened

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @C1794 @regression
  Scenario Outline: Verify you ping in a conversation when you press alt + ctrl + K (Win)
    Given There are 2 users where <Name> is me
    Given user <Contact> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    When I hover ping button
    Then I see correct ping button tooltip
    When I type shortcut combination to ping
    Then I see <PING> action in conversation

    Examples: 
      | Login      | Password      | Name      | Contact   | PING       |
      | user1Email | user1Password | user1Name | user2Name | you pinged |

  @C1795 @regression
  Scenario Outline: Verify you start a call in a conversation when you press ⌥ ⌘ R (Mac) or alt + ctrl + R (Win)
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    And I see calling button
    When I hover call button
    Then I see correct call button tooltip
    When I type shortcut combination to start a call
    Then I see the outgoing call controls for conversation <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @C111934 @regression
  Scenario Outline: Verify conversation scrolls to first unread message while being online
    Given There are 2 users where <Name> is me
    Given user <Contact> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I disable ad banner
    And I am signed in properly
    When I open conversation with <Contact>
    And Contact <Contact> sends 35 messages with prefix <READ> via device Device1 to user <Name>
    And I wait for 5 seconds
    Then I really see text message <READ>34
    When I open self profile
    And Contact <Contact> sends 35 messages with prefix <UNREAD> via device Device1 to user <Name>
    And I wait for 5 seconds
    Then I see unread dot in conversation <Contact>
    When I open conversation with <Contact>
    Then I do not see text message <READ>33
    And I do not see text message <READ>0
    And I do not see text message <UNREAD>34
    And I really see text message <UNREAD>0

    Examples: 
      | Login      | Password      | Name      | Contact   | READ | UNREAD |
      | user1Email | user1Password | user1Name | user2Name | Read | Unread |

  @C149662 @staging
  Scenario Outline: Verify maximum character limit dialog is shown when want to send a very long text message to group conversation
    Given I switch to Sign In page
    Given I Sign in temporary using login <Login> and password <Password>
    Given I see the history info page
    Given I click confirm on history info page
    Given I am signed in properly
    When I open conversation with <ChatName>
    And I paste message from file <File1>
    And I send message
    Then I see long message warning dialog
    #And I click OK on long message warning dialog
    Then I do not see long message warning dialog
    And I do not see text message <File2>
    When I open conversation with <ChatName>
    And I paste message from file <File2>
    And I send message
    Then I do not see long message warning dialog
    Then I verify the last text message equals file <File2>

    Examples:
      | Login                         | Password   | ChatName    | File1          | File2              |
      | smoketester+68b16b1c@wire.com | aqa123456! | Lorem ipsum | over8000ch.txt | lessThan8000ch.txt |
