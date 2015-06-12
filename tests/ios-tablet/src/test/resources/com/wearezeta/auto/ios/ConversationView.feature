Feature: Conversation View

  @staging @id2419
  Scenario Outline: Vefiry sending message [PORTRAIT]
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
      
  @staging @id2375
  Scenario Outline: Vefiry sending message [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
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

  @staging @id2695
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
      
  @staging @id2695
  Scenario Outline: Receive message from contact [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    And Contact <Contact> send message to user <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    Then I see message in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
      
  @staging @id2413 @deployPictures 
  Scenario Outline: Verify sending image [PORTRAIT]
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
      
  @staging @id2407 @deployPictures 
  Scenario Outline: Verify sending image [LANDSCAPE]
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
      
  @staging @id2669 @deployPictures
  Scenario Outline: Receive a camera roll picture from user from contact list [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    And Contact <Contact> sends image <Picture> to <ConversationType> conversation <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    Then I see new photo in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   | Picture | ConversationType | 
      | user1Email | user1Password | user1Name | user2Name | testing.jpg | single user | 

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
      
  @staging @id2736
  Scenario Outline: Send Message to contact after navigating away from chat page [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using phone number or login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I input more than 200 chars message and send it
    And I type the message
    And I swipe right on Dialog page
    And I tap on contact name <Contact>
    And I tap on text input
    And I send the message
    Then I see message in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
      
  @staging @id2737
  Scenario Outline: Send Message to contact after navigating away from chat page [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in using phone number or login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I input more than 200 chars message and send it
    And I type the message
    And I swipe right on Dialog page
    And I tap on contact name <Contact>
    And I tap on text input
    And I send the message
    Then I see message in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @staging @id2738
  Scenario Outline: Send more than 200 chars message [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using phone number or login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I input more than 200 chars message and send it
    Then I see message in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
      
  @staging @id2739
  Scenario Outline: Send more than 200 chars message [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in using phone number or login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I input more than 200 chars message and send it
    Then I see message in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @staging @id2740
  Scenario Outline: Send one line message with lower case and upper case [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using phone number or login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I input message with lower case and upper case
    And I send the message
    Then I see message in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
      
  @staging @id2741
  Scenario Outline: Send one line message with lower case and upper case [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in using phone number or login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I input message with lower case and upper case
    And I send the message
    Then I see message in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @staging @id2742
  Scenario Outline: Send special chars (German) [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using phone number or login <Login> and password <Password>
    And I see Contact list with my name <Name>
    And I tap on contact name <Contact>
    And I see dialog page
    And I send using script predefined message <Text>
    Then I see last message in dialog is expected message <Text>

    Examples: 
      | Login      | Password      | Name      | Contact   | Text                  |
      | user1Email | user1Password | user1Name | user2Name | ÄäÖöÜüß & latin chars |
      
  @staging @id2743
  Scenario Outline: Send special chars (German) [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in using phone number or login <Login> and password <Password>
    And I see Contact list with my name <Name>
    And I tap on contact name <Contact>
    And I see dialog page
    And I send using script predefined message <Text>
    Then I see last message in dialog is expected message <Text>

    Examples: 
      | Login      | Password      | Name      | Contact   | Text                  |
      | user1Email | user1Password | user1Name | user2Name | ÄäÖöÜüß & latin chars |

  @staging @id2744
  Scenario Outline: Copy and paste to send the message [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I see sign in screen
    When I press Sign in button
    And I have entered login <Text>
    And I tap and hold on Email input
    And I click on popup SelectAll item
    And I click on popup Copy item
    And I have entered login <Login>
    And I have entered password <Password>
    And I press Login button
    And I see Contact list with my name <Name>
    And I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I tap and hold on message input
    And I click on popup Paste item
    And I send the message
    Then I see last message in dialog is expected message <Text>

    Examples: 
      | Login      | Password      | Name      | Contact   | Text       |
      | user1Email | user1Password | user1Name | user2Name | TextToCopy |
      
  @staging @id2745
  Scenario Outline: Copy and paste to send the message [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I see sign in screen
    When I press Sign in button
    And I have entered login <Text>
    And I tap and hold on Email input
    And I click on popup SelectAll item
    And I click on popup Copy item
    And I have entered login <Login>
    And I have entered password <Password>
    And I press Login button
    And I see Contact list with my name <Name>
    And I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I tap and hold on message input
    And I click on popup Paste item
    And I send the message
    Then I see last message in dialog is expected message <Text>

    Examples: 
      | Login      | Password      | Name      | Contact   | Text       |
      | user1Email | user1Password | user1Name | user2Name | TextToCopy |

  @staging @id2746
  Scenario Outline: Send a text containing spaces on either end of message [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using phone number or login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I try to send message with only spaces
    And I see message with only spaces is not send
    And I input message with leading empty spaces
    And I send the message
    And I see message in the dialog
    And I input message with trailing emtpy spaces
    And I send the message
    Then I see message in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
      
  @staging @id2747
  Scenario Outline: Send a text containing spaces on either end of message [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in using phone number or login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I try to send message with only spaces
    And I see message with only spaces is not send
    And I input message with leading empty spaces
    And I send the message
    And I see message in the dialog
    And I input message with trailing emtpy spaces
    And I send the message
    Then I see message in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
      
      