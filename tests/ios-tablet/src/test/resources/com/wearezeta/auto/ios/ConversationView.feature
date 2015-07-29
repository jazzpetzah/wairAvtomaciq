Feature: Conversation View

  @regression @id2419
  Scenario Outline: Vefiry sending message [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I type the message
    And I send the message
    Then I see message in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id2375
  Scenario Outline: Vefiry sending message [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I type the message
    And I send the message
    Then I see message in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id2695
  Scenario Outline: Receive message from contact [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    And Contact <Contact> send message to user <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    Then I see message in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id2695
  Scenario Outline: Receive message from contact [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    And Contact <Contact> send message to user <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    Then I see message in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id2413 @deployPictures
  Scenario Outline: Verify sending image [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
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
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id2407 @deployPictures
  Scenario Outline: Verify sending image [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
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
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id2429
  Scenario Outline: Verify you can see Ping on the other side - 1:1 conversation [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given User <Contact1> change name to <ContactName>
    Given Myself is connected to <Contact1>
    Given User <Contact1> change accent color to <Color>
    Given I Sign in on tablet using my email
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
      | Name      | Contact1  | Action1 | Action2      | Color        | ContactName |
      | user1Name | user2Name | PINGED  | PINGED AGAIN | BrightOrange | OtherUser   |

  @regression @id2429
  Scenario Outline: Verify you can see Ping on the other side - 1:1 conversation [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given User <Contact1> change name to <ContactName>
    Given Myself is connected to <Contact1>
    Given User <Contact1> change accent color to <Color>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
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
      | Name      | Contact1  | Action1 | Action2      | Color        | ContactName |
      | user1Name | user2Name | PINGED  | PINGED AGAIN | BrightOrange | OtherUser   |

  @regression @id2427
  Scenario Outline: Verify you can see Ping on the other side - group conversation [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given User <Contact1> change name to <ContactName>
    Given User <Contact1> change accent color to <Color>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in on tablet using my email
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
      | Name      | Contact1  | Contact2  | Action1 | Action2      | GroupChatName        | Color        | ContactName |
      | user1Name | user2Name | user3Name | PINGED  | PINGED AGAIN | ReceivePingGroupChat | BrightOrange | OtherUser   |

  @regression @id2427
  Scenario Outline: Verify you can see Ping on the other side - group conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given User <Contact1> change name to <ContactName>
    Given User <Contact1> change accent color to <Color>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
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
      | Name      | Contact1  | Contact2  | Action1 | Action2      | GroupChatName        | Color        | ContactName |
      | user1Name | user2Name | user3Name | PINGED  | PINGED AGAIN | ReceivePingGroupChat | BrightOrange | OtherUser   |

  @regression @id2669 @deployPictures
  Scenario Outline: Receive a camera roll picture from user from contact list [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    And Contact <Contact> sends image <Picture> to <ConversationType> conversation <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    Then I see new photo in the dialog

    Examples: 
      | Name      | Contact   | Picture     | ConversationType |
      | user1Name | user2Name | testing.jpg | single user      |

  @regression @id2670 @deployPictures
  Scenario Outline: Receive a camera roll picture from user from contact list [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    And Contact <Contact> sends image <Picture> to <ConversationType> conversation <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    Then I see new photo in the dialog

    Examples: 
      | Name      | Contact   | Picture     | ConversationType |
      | user1Name | user2Name | testing.jpg | single user      |

  @regression @id2736
  Scenario Outline: Send Message to contact after navigating away from chat page [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
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
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id2737
  Scenario Outline: Send Message to contact after navigating away from chat page [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
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
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id2738
  Scenario Outline: Send more than 200 chars message [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I input more than 200 chars message and send it
    Then I see message in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id2739
  Scenario Outline: Send more than 200 chars message [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I input more than 200 chars message and send it
    Then I see message in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id2740
  Scenario Outline: Send one line message with lower case and upper case [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I input message with lower case and upper case
    And I send the message
    Then I see message in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id2741
  Scenario Outline: Send one line message with lower case and upper case [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I input message with lower case and upper case
    And I send the message
    Then I see message in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id2742
  Scenario Outline: Send special chars (German) [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    And I tap on contact name <Contact>
    And I see dialog page
    And I send using script predefined message <Text>
    Then I see last message in dialog is expected message <Text>

    Examples: 
      | Name      | Contact   | Text                  |
      | user1Name | user2Name | ÄäÖöÜüß & latin chars |

  @regression @id2743
  Scenario Outline: Send special chars (German) [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    And I tap on contact name <Contact>
    And I see dialog page
    And I send using script predefined message <Text>
    Then I see last message in dialog is expected message <Text>

    Examples: 
      | Name      | Contact   | Text                  |
      | user1Name | user2Name | ÄäÖöÜüß & latin chars |

  @regression @id2744
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

  @regression @id2745
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

  @regression @id2746
  Scenario Outline: Send a text containing spaces on either end of message [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
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
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id2747
  Scenario Outline: Send a text containing spaces on either end of message [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
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
      | Name      | Contact   |
      | user1Name | user2Name |

  @staging @id2405
  Scenario Outline: Play/pause Youtube media link from the media bar [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I type and send long message and media link <YouTubeLink>
    And I see media link <YouTubeLink> and media in dialog
    And I click play video button
    And I scroll media out of sight until media bar appears
    And I pause playing the media in media bar
    Then I see playing media is paused
    And I press play in media bar
    Then I see media is playing
    And I stop media in media bar
    Then The media stops playing

    Examples: 
      | Name      | Contact   | YouTubeLink                                 |
      | user1Name | user2Name | https://www.youtube.com/watch?v=gywGBuMUiI4 |

  @regression @id2403
  Scenario Outline: Conversation gets scrolled back to playing media when clicking on media bar [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Name> sent long message to conversation <Contact>
    Given User <Name> sent message <SoundCloudLink> to conversation <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I scroll to the end of the conversation
    Then I see media link <SoundCloudLink> and media in dialog
    And I tap media link
    And I scroll media out of sight until media bar appears
    And I tap on the media bar
    Then I see conversation view is scrolled back to the playing media link <SoundCloudLink>

    Examples: 
      | Name      | Contact   | SoundCloudLink                                                                       |
      | user1Name | user2Name | https://soundcloud.com/revealed-recordings/dannic-shermanology-wait-for-you-download |

  @regression @id2404
  Scenario Outline: Verify the Media Bar dissapears after playback finishes - SoundCloud [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I type and send long message and media link <SoundCloudLink>
    And I tap media link
    And I scroll media out of sight until media bar appears
    And I see media bar on dialog page
    And I wait 150 seconds for media to stop playing
    Then I dont see media bar on dialog page

    Examples: 
      | Name      | Contact   | SoundCloudLink                                                                       |
      | user1Name | user2Name | https://soundcloud.com/revealed-recordings/dannic-shermanology-wait-for-you-download |

  @regression @id2978
  Scenario Outline: Verify I can send gif from preview [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I type tag for giphy preview <GiphyTag> and open preview overlay
    And I wait for 5 seconds
    And I send gif from giphy preview page
    And I wait for 5 seconds
    And I see dialog page
    Then I see new photo in the dialog

    Examples: 
      | Name      | Contact   | GiphyTag |
      | user1Name | user2Name | Happy    |

  @regression @id2979
  Scenario Outline: Verify I can send gif from preview [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I type tag for giphy preview <GiphyTag> and open preview overlay
    And I wait for 5 seconds
    And I send gif from giphy preview page
    And I wait for 5 seconds
    And I see dialog page
    Then I see new photo in the dialog

    Examples: 
      | Name      | Contact   | GiphyTag |
      | user1Name | user2Name | Happy    |

  @staging @id2987
  Scenario Outline: I can send a sketch[PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I swipe the text input cursor
    And I tap on sketch button in cursor
    And I draw a random sketch
    And I send my sketch
    Then I see new photo in the dialog

    Examples: 
      | Name      | Contact1  |
      | user1Name | user2Name |

  @staging @id2988
  Scenario Outline: I can send a sketch[LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I rotate UI to landscape
    Given I sign in using my email or phone number
    Given I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I swipe the text input cursor
    And I tap on sketch button in cursor
    And I draw a random sketch
    And I send my sketch
    Then I see new photo in the dialog

    Examples: 
      | Name      | Contact1  |
      | user1Name | user2Name |

  @staging @id2420
  Scenario Outline: Verify sending ping in 1-to-1 conversation [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe the text input cursor
    And I click Ping button
    Then I see You Pinged message in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @staging @id3193
  Scenario Outline: Verify sending ping in 1-to-1 conversation [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe the text input cursor
    And I click Ping button
    Then I see You Pinged message in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @staging @id3194
  Scenario Outline: Send message to group chat [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I type the message
    And I send the message
    Then I see message in the dialog

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | SimpleGroup   |

  @staging @id3195
  Scenario Outline: Send message to group chat [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I type the message
    And I send the message
    Then I see message in the dialog

    Examples: 
      @staging @id3196

      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | SimpleGroup   |

  Scenario Outline: Play/pause SoundCloud media link from the media bar [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Name> sent long message to conversation <Contact>
    Given User <Name> sent message <SoundCloudLink> to conversation <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I swipe right on Dialog page
    And I tap on contact name <Contact>
    Then I see media link <SoundCloudLink> and media in dialog
    And I tap media link
    And I scroll media out of sight until media bar appears
    And I pause playing the media in media bar
    Then I see playing media is paused
    And I press play in media bar
    Then I see media is playing
    And I stop media in media bar
    Then The media stops playing

    Examples: 
      | Name      | Contact   | SoundCloudLink                                                                       |
      | user1Name | user2Name | https://soundcloud.com/revealed-recordings/dannic-shermanology-wait-for-you-download |

  @staging @id3197
  Scenario Outline: Play/pause SoundCloud media link from the media bar [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Name> sent long message to conversation <Contact>
    Given User <Name> sent message <SoundCloudLink> to conversation <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I tap on contact name <Contact>
    Then I see media link <SoundCloudLink> and media in dialog
    And I tap media link
    And I scroll media out of sight until media bar appears
    And I pause playing the media in media bar
    Then I see playing media is paused
    And I press play in media bar
    Then I see media is playing
    And I stop media in media bar
    Then The media stops playing

    Examples: 
      | Name      | Contact   | SoundCloudLink                                                                       |
      | user1Name | user2Name | https://soundcloud.com/revealed-recordings/dannic-shermanology-wait-for-you-download |

  @staging @id3198
  Scenario Outline: Verify the Media Bar disappears when playing media is back in view - SoundCloud [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User <Name> sent long message to conversation <Contact1>
    Given User <Name> sent message <SoundCloudLink> to conversation <Contact1>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I tap on text input
    And I see media link <SoundCloudLink> and media in dialog
    And I tap media link
    And I scroll media out of sight until media bar appears
    And I tap on text input
    Then I dont see media bar on dialog page

    Examples: 
      | Name      | Contact1  | SoundCloudLink                                                                       |
      | user1Name | user2Name | https://soundcloud.com/revealed-recordings/dannic-shermanology-wait-for-you-download |

  @staging @id3199
  Scenario Outline: Verify the Media Bar disappears when playing media is back in view - SoundCloud [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User <Name> sent long message to conversation <Contact1>
    Given User <Name> sent message <SoundCloudLink> to conversation <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I tap on text input
    And I see media link <SoundCloudLink> and media in dialog
    And I tap media link
    And I scroll media out of sight until media bar appears
    And I tap on text input
    Then I dont see media bar on dialog page

    Examples: 
      | Name      | Contact1  | SoundCloudLink                                                                       |
      | user1Name | user2Name | https://soundcloud.com/revealed-recordings/dannic-shermanology-wait-for-you-download |

  @staging @id2380
  Scenario Outline: Tap the cursor to get to the end of the conversation [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I send long message
    And I scroll to the beginning of the conversation
    And I tap on text input
    Then I see last message in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @staging @id3200
  Scenario Outline: Tap the cursor to get to the end of the conversation [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I send long message
    And I scroll to the beginning of the conversation
    And I tap on text input
    Then I see last message in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @staging @id2417
  Scenario Outline: Verify you can see conversation images in fullscreen [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe the text input cursor
    And I press Add Picture button
    And I press Camera Roll button
    And I choose a picture from camera roll on iPad popover
    And I press Confirm button on iPad popover
    And I see new photo in the dialog
    And I memorize message send time
    And I tap and hold image to open full screen
    And I see Full Screen Page opened
    And I see sender first name <Name> on fullscreen page
    And I see send date on fullscreen page
    And I see download button shown on fullscreen page
    And I tap on fullscreen page
    And I verify image caption and download button are not shown
    And I tap on fullscreen page
    And I tap close fullscreen page button
    Then I see new photo in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @staging @id3201
  Scenario Outline: Verify you can see conversation images in fullscreen [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe the text input cursor
    And I press Add Picture button
    And I press Camera Roll button
    And I choose a picture from camera roll on iPad popover
    And I press Confirm button on iPad popover
    And I see new photo in the dialog
    And I memorize message send time
    And I tap and hold image to open full screen
    And I see Full Screen Page opened
    And I see sender first name <Name> on fullscreen page
    And I see send date on fullscreen page
    And I see download button shown on fullscreen page
    And I tap on fullscreen page
    And I verify image caption and download button are not shown
    And I tap on fullscreen page
    And I tap close fullscreen page button
    Then I see new photo in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @staging @id3202
  Scenario Outline: I can send and play inline youtube link [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I post media link <YouTubeLink>
    And I swipe right on Dialog page
    And I tap on contact name <Contact>
    Then I see youtube link <YouTubeLink> and media in dialog
    And I click video container for the first time
    And I see video player page is opened

    Examples: 
      | Name      | Contact   | YouTubeLink                                |
      | user1Name | user2Name | http://www.youtube.com/watch?v=Bb1RhktcugU |

  @staging @id3203
  Scenario Outline: I can send and play inline youtube link [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I post media link <YouTubeLink>
    Then I see youtube link <YouTubeLink> and media in dialog
    And I click video container for the first time
    And I see video player page is opened

    Examples: 
      | Name      | Contact   | YouTubeLink                                |
      | user1Name | user2Name | http://www.youtube.com/watch?v=Bb1RhktcugU |

  @staging @id3204
  Scenario Outline: Verify appearance of title bar for conversation, restored from background [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I close the app for <CloseAppTime> seconds
    Then I see title bar in conversation name <Contact>

    Examples: 
      | Name      | Contact   | CloseAppTime |
      | user1Name | user2Name | 2            |

  @staging @id3205
  Scenario Outline: Verify appearance of title bar for conversation, restored from background [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I close the app for <CloseAppTime> seconds
    Then I see title bar in conversation name <Contact>

    Examples: 
      | Name      | Contact   | CloseAppTime |
      | user1Name | user2Name | 2            |

  @staging @id2418
  Scenario Outline: Rotate image in fullscreen mode [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given User <Contact> change name to <NewName>
    Given User <Contact> change accent color to <Color>
    Given Myself is connected to <Contact>
    Given Contact <Contact> sends image <Picture> to single user conversation <Name>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I see new photo in the dialog
    And I memorize message send time
    And I tap and hold image to open full screen
    And I see Full Screen Page opened
    And I rotate UI to landscape
    Then I see image rotated in fullscreen mode

    Examples: 
      | Name      | Contact   | Picture     | Color        | NewName          |
      | user1Name | user2Name | testing.jpg | BrightOrange | RotateFullscreen |

  @staging @id3206
  Scenario Outline: Rotate image in fullscreen mode [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given User <Contact> change name to <NewName>
    Given User <Contact> change accent color to <Color>
    Given Myself is connected to <Contact>
    Given Contact <Contact> sends image <Picture> to single user conversation <Name>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I see new photo in the dialog
    And I memorize message send time
    And I tap and hold image to open full screen
    And I see Full Screen Page opened
    And I rotate UI to portrait
    Then I see image rotated in fullscreen mode

    Examples: 
      | Name      | Contact   | Picture     | Color        | NewName          |
      | user1Name | user2Name | testing.jpg | BrightOrange | RotateFullscreen |

  @staging @id2451 
  Scenario Outline: Verify archiving conversation from ellipsis menu [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>    
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I open conversation details
    And I open ellipsis menu
    And I click archive menu button
    And I swipe right on the personal page
    Then I dont see conversation <Contact> in contact list
    And I open archived conversations on iPad
    Then I see user <Contact> in contact list

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @staging @id3192
  Scenario Outline: Verify archiving conversation from ellipsis menu [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I open conversation details
    And I open ellipsis menu
    And I click archive menu button
    Then I dont see conversation <Contact> in contact list
    And I open archived conversations on iPad
    Then I see user <Contact> in contact list

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |
