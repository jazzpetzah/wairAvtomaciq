Feature: Delete Everywhere

  @C206234 @staging
  Scenario Outline: Verify I can delete my message everywhere (1:1)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And I write message <Message1>
    And I send message
    And I see text message <Message1>
    And I see 2 messages in conversation
    And I open context menu of the latest message
    And I click to delete message for everyone in context menu
    And I click confirm to delete message for everyone
    Then I do not see text message <Message1>
    And I see 1 message in conversation

    Examples:
      | Login      | Password      | Name      | Contact   | Message1 |
      | user1Email | user1Password | user1Name | user2Name | message1 |

  @C206235 @staging
  Scenario Outline: Verify I can delete my message everywhere (group)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <ChatName>
    And I write message <Message1>
    And I send message
    And I see text message <Message1>
    And I see 2 messages in conversation
    And I open context menu of the latest message
    And I click to delete message for everyone in context menu
    And I click confirm to delete message for everyone
    Then I do not see text message <Message1>
    And I see 1 message in conversation

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName | Message1 |
      | user1Email | user1Password | user1Name | user2Name | user3Name | New name | message1 |

  @C206242 @staging
  Scenario Outline: Verify delete everywhere works for images
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And I send picture <PictureName> to the current conversation
    And I see sent picture <PictureName> in the conversation view
    And I see 2 messages in conversation
    And I open context menu of the latest message
    And I click to delete message for everyone in context menu
    And I click confirm to delete message for everyone
    Then I do not see any picture in the conversation view
    And I see 1 message in conversation

    Examples:
      | Login      | Password      | Name      | Contact   | PictureName               |
      | user1Email | user1Password | user1Name | user2Name | userpicture_landscape.jpg |

  @C206244 @staging
  Scenario Outline: Verify delete everywhere works for link preview
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And Contact Myself sends message <Link> via device Device1 to user <Contact>
    And I see a title <LinkTitle> in link preview in the conversation view
    And I see 2 messages in conversation
    And I open context menu of the latest message
    And I click to delete message for everyone in context menu
    And I click confirm to delete message for everyone
    Then I do not see a title <LinkTitle> in link preview in the conversation view
    And I see 1 message in conversation

    Examples:
      | Login      | Password      | Name      | Contact   | Link                           | LinkTitle   |
      | user1Email | user1Password | user1Name | user2Name | http://www.heise.de/newsticker | 7-Tage-News |

  @C206245 @staging
  Scenario Outline: Verify delete everywhere works for location sharing
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <ChatName>
    And User Myself sends location <LocationName> with <Latitude> and <Longitude> to group conversation <ChatName> via device Device1
    And I see location message <LocationName> with <Latitude> and <Longitude> in the conversation view
    And I see 2 messages in conversation
    And I open context menu of the latest message
    And I click to delete message for everyone in context menu
    And I click confirm to delete message for everyone
    Then I do not see location message <LocationName> with <Latitude> and <Longitude> in the conversation view
    And I see 1 message in conversation

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName | Latitude  | Longitude   | LocationName |
      | user1Email | user1Password | user1Name | user2Name | user3Name | New name | 33.747252 | -112.633853 | Triangle     |

  @C206246 @staging
  Scenario Outline: Verify delete everywhere works for file sharing
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And I send <Size> sized file with name <File> to the current conversation
    And I wait until file <File> is uploaded completely
    And I see 2 messages in conversation
    And I open context menu of the latest message
    And I click to delete message for everyone in context menu
    And I click confirm to delete message for everyone
    Then I do not see file transfer for file <File> in the conversation view
    And I see 1 message in conversation

    Examples:
      | Login      | Password      | Name      | Contact   | File        | Size  |
      | user1Email | user1Password | user1Name | user2Name | C206246.zip | 512KB |

  @C206247 @staging
  Scenario Outline: Verify delete everywhere works for audio messages
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And I send audio file with length <Time> and name <File> to the current conversation
    And I wait until audio <File> is uploaded completely
    And I see 2 messages in conversation
    And I open context menu of the latest message
    And I click to delete message for everyone in context menu
    And I click confirm to delete message for everyone
    Then I do not see audio message <File> in the conversation view
    And I see 1 message in conversation

    Examples:
      | Login      | Password      | Name      | Contact   | File        | Time  |
      | user1Email | user1Password | user1Name | user2Name | example.wav | 00:20 |

  @C206248 @staging
  Scenario Outline: Verify delete everywhere works for video messages
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And I send <Size> sized video with name <File> to the current conversation
    And I wait until video <File> is uploaded completely
    And I see 2 messages in conversation
    And I open context menu of the latest message
    And I click to delete message for everyone in context menu
    And I click confirm to delete message for everyone
    Then I do not see video message <File> in the conversation view
    And I see 1 message in conversation

    Examples:
      | Login      | Password      | Name      | Contact   | File        | Size |
      | user1Email | user1Password | user1Name | user2Name | C206248.mp4 | 1MB  |