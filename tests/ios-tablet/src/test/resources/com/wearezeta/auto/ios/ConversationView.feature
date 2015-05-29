Feature: Conversation View

  @staging @id330
  Scenario Outline: Send Message to contact [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I type the message
    And I send the message
    Then I see message in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @staging @id
  Scenario Outline: Receive message from contact [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    And Contact <Contact> send message to user <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    Then I see message in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @staging @id332 @deployPictures @id1470
  Scenario Outline: Send a camera roll picture to user from contact list [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe the text input cursor
    And I press Add Picture button on iPad
    And I press Camera Roll button on iPad
    And I choose a picture from camera roll on iPad popover
    And I press Confirm button on iPad popover
    Then I see new photo in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @staging @id332 @deployPictures @id1470
  Scenario Outline: Send a camera roll picture to user from contact list [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe the text input cursor
    And I press Add Picture button on iPad
    And I press Camera Roll button on iPad
    And I choose a picture from camera roll on iPad popover
    And I press Confirm button on iPad popover
    Then I see new photo in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @staging @id2429
  Scenario Outline: Verify you can see Ping on the other side - 1:1 conversation [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given User <Contact1> change name to <ContactName>
    Given Myself is connected to <Contact1>
    Given User <Contact1> change accent color to <Color>
    And I Sign in using login <Login> and password <Password>
    When I see Contact list with my name <Name>
    And I tap on contact name <Contact1>
    And User <Contact1> Ping in chat <Name> by BackEnd
    And I wait for 3 seconds
    Then I see User <Contact1> Pinged message in the conversation
    And I see <Action1> icon in conversation
    And User <Contact1> HotPing in chat <Name> by BackEnd
    And I wait for 3 seconds
    And I see User <Contact1> Pinged Again message in the conversation
    And I see <Action2> icon in conversation

    Examples: 
      | Login      | Password      | Name      | Contact1  | Action1 | Action2      | Color        | ContactName |
      | user1Email | user1Password | user1Name | user2Name | PINGED  | PINGED AGAIN | BrightOrange | OtherUser   |

  @staging @id2429
  Scenario Outline: Verify you can see Ping on the other side - 1:1 conversation [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given User <Contact1> change name to <ContactName>
    Given Myself is connected to <Contact1>
    Given User <Contact1> change accent color to <Color>
    Given I rotate UI to landscape
    And I Sign in using login <Login> and password <Password>
    When I see Contact list with my name <Name>
    And I tap on contact name <Contact1>
    And User <Contact1> Ping in chat <Name> by BackEnd
    And I wait for 3 seconds
    Then I see User <Contact1> Pinged message in the conversation
    And I see <Action1> icon in conversation
    And User <Contact1> HotPing in chat <Name> by BackEnd
    And I wait for 3 seconds
    And I see User <Contact1> Pinged Again message in the conversation
    And I see <Action2> icon in conversation

    Examples: 
      | Login      | Password      | Name      | Contact1  | Action1 | Action2      | Color        | ContactName |
      | user1Email | user1Password | user1Name | user2Name | PINGED  | PINGED AGAIN | BrightOrange | OtherUser   |

  @staging @id2427
  Scenario Outline: Verify you can see Ping on the other side - group conversation [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given User <Contact1> change name to <ContactName>
    Given User <Contact1> change accent color to <Color>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    And I Sign in using login <Login> and password <Password>
    When I see Contact list with my name <Name>
    And I tap on group chat with name <GroupChatName>
    And User <Contact1> Ping in chat <GroupChatName> by BackEnd
    And I wait for 3 seconds
    Then I see User <Contact1> Pinged message in the conversation
    And I see <Action1> icon in conversation
    And User <Contact1> HotPing in chat <GroupChatName> by BackEnd
    And I wait for 3 seconds
    And I see User <Contact1> Pinged Again message in the conversation
    And I see <Action2> icon in conversation

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | Action1 | Action2      | GroupChatName        | Color        | ContactName |
      | user1Email | user1Password | user1Name | user2Name | user3Name | PINGED  | PINGED AGAIN | ReceivePingGroupChat | BrightOrange | OtherUser   |

  @staging @id2427
  Scenario Outline: Verify you can see Ping on the other side - group conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given User <Contact1> change name to <ContactName>
    Given User <Contact1> change accent color to <Color>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    And I Sign in using login <Login> and password <Password>
    When I see Contact list with my name <Name>
    And I tap on group chat with name <GroupChatName>
    And User <Contact1> Ping in chat <GroupChatName> by BackEnd
    And I wait for 3 seconds
    Then I see User <Contact1> Pinged message in the conversation
    And I see <Action1> icon in conversation
    And User <Contact1> HotPing in chat <GroupChatName> by BackEnd
    And I wait for 3 seconds
    And I see User <Contact1> Pinged Again message in the conversation
    And I see <Action2> icon in conversation

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | Action1 | Action2      | GroupChatName        | Color        | ContactName |
      | user1Email | user1Password | user1Name | user2Name | user3Name | PINGED  | PINGED AGAIN | ReceivePingGroupChat | BrightOrange | OtherUser   |

  @staging @id2670 @deployPictures
  Scenario Outline: Receive a camera roll picture from user from contact list [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    And Contact <Contact> sends image <Picture> to <ConversationType> conversation <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    Then I see new photo in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   | Picture | ConversationType | 
      | user1Email | user1Password | user1Name | user2Name | testing.jpg | single user | 
      