Feature: Conversation View

  @C2632 @regression @rc @id2419
  Scenario Outline: Vefiry sending message [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I type the default message
    And I send the message
    Then I see 1 default message in the dialog

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
    And I type the default message
    And I send the message
    Then I see 1 default message in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2644 @regression @id2695
  Scenario Outline: Receive message from contact [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    Given I see Contact list with my name <Name>
    Given User <Contact> sends 1 encrypted message to user Myself
    And I tap on contact name <Contact>
    And I see dialog page
    Then I see 1 default message in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2644 @regression @id2695
  Scenario Outline: Receive message from contact [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see Contact list with my name <Name>
    Given User <Contact> sends 1 encrypted message to user Myself
    When I tap on contact name <Contact>
    And I see dialog page
    Then I see 1 default message in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2621 @regression @rc @id2413 @deployPictures
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

  @C2615 @regression @rc @id2407 @deployPictures
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

  @C2642 @regression @id2429
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
    Then I see <Action1> icon in conversation

    Examples: 
      | Name      | Contact1  | Action1 | Action2      | Color        | ContactName |
      | user1Name | user2Name | PINGED  | PINGED AGAIN | BrightOrange | OtherUser   |

  @C2642 @regression @id2429 @C3222
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
    Then I see <Action1> icon in conversation

    Examples: 
      | Name      | Contact1  | Action1 | Action2      | Color        | ContactName |
      | user1Name | user2Name | PINGED  | PINGED AGAIN | BrightOrange | OtherUser   |

  @C2640 @regression @id2427 @C3223
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
    Then I see <Action1> icon in conversation

    Examples: 
      | Name      | Contact1  | Contact2  | Action1 | Action2      | GroupChatName        | Color        | ContactName |
      | user1Name | user2Name | user3Name | PINGED  | PINGED AGAIN | ReceivePingGroupChat | BrightOrange | OtherUser   |

  @C2640 @regression @id2427 @C3224
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
    Then I see <Action1> icon in conversation

    Examples: 
      | Name      | Contact1  | Contact2  | Action1 | Action2      | GroupChatName        | Color        | ContactName |
      | user1Name | user2Name | user3Name | PINGED  | PINGED AGAIN | ReceivePingGroupChat | BrightOrange | OtherUser   |

  @C2627 @regression @id2669 @deployPictures
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

  @C2628 @regression @id2670 @deployPictures
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

  @C2646 @regression @id2736
  Scenario Outline: Send Message to contact after navigating away from chat page [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I input more than 200 chars message and send it
    And I type the default message
    And I return to the chat list
    When I tap on my name <Name>
	And I close self profile
    And I tap on text input
    And I send the message
    Then I see 1 default message in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2647 @regression @id2737
  Scenario Outline: Send Message to contact after navigating away from chat page [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I input more than 200 chars message and send it
    And I type the default message
	When I tap on my name <Name>
	And I close self profile
    And I send the message
    Then I see 1 message in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2648 @regression @id2738
  Scenario Outline: Send more than 200 chars message [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I input more than 200 chars message and send it
    Then I see 1 message in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2649 @regression @id2739
  Scenario Outline: Send more than 200 chars message [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I input more than 200 chars message and send it
    Then I see 1 message in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2650 @regression @id2740
  Scenario Outline: Send one line message with lower case and upper case [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I input message with lower case and upper case
    And I send the message
    Then I see 1 message in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2651 @regression @id2741
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
    Then I see 1 message in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2652 @regression @id2742
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

  @C2653 @regression @id2743
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

  @C2654 @regression @id2744
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

  @C2655 @regression @id2745
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

  @C2656 @regression @id2746
  Scenario Outline: Send a text containing spaces on either end of message [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I try to send message with only spaces
    And I see the only message in dialog is system message CONNECTED TO <Contact>
    And I input message with leading empty spaces
    And I send the message
    And I see 1 message in the dialog
    And I input message with trailing emtpy spaces
    And I send the message
    Then I see 2 messages in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2657 @regression @id2747
  Scenario Outline: Send a text containing spaces on either end of message [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I try to send message with only spaces
    And I see the only message in dialog is system message CONNECTED TO <Contact>
    And I input message with leading empty spaces
    And I send the message
    And I see 1 message in the dialog
    And I input message with trailing emtpy spaces
    And I send the message
    Then I see 2 messages in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @obsolete @id2405
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

  @regression @id2404 @C3225
  Scenario Outline: Verify the Media Bar dissapears after playback finishes - SoundCloud [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Name> sent long message to conversation <Contact>
    Given User <Name> sent message <SoundCloudLink> to conversation <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    Then I see dialog page
    And I tap on text input to scroll to the end
    And I see media link <SoundCloudLink> and media in dialog
    When I tap media link
    And I scroll media out of sight until media bar appears
    And I see media bar on dialog page
    And I wait 150 seconds for media to stop playing
    Then I dont see media bar on dialog page

    Examples: 
      | Name      | Contact   | SoundCloudLink                                                                       |
      | user1Name | user2Name | https://soundcloud.com/revealed-recordings/dannic-shermanology-wait-for-you-download |

  @C2676 @regression @id2987
  Scenario Outline: I can send a sketch[PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I Sign in on tablet using my email
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

  @C2677 @regression @id2988
  Scenario Outline: I can send a sketch[LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
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

  @C2633 @regression @id2420
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

  @C2658 @regression @id3193
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

  @C2659 @regression @id3194
  Scenario Outline: Send message to group chat [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I type the default message
    And I send the message
    Then I see 1 default message in the dialog

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | SimpleGroup   |

  @C2660 @regression @id3195
  Scenario Outline: Send message to group chat [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I type the default message
    And I send the message
    Then I see 1 default message in the dialog

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | SimpleGroup   |

  @C2611 @regression @id3196
  Scenario Outline: Play/pause SoundCloud media link from the media bar [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Name> sent long message to conversation <Contact>
    Given User <Name> sent message <SoundCloudLink> to conversation <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input to scroll to the end
    And I return to the chat list
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

  @C2612 @regression @id3197
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
    And I tap on text input to scroll to the end
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

  @C2613 @regression @id3198
  Scenario Outline: Verify the Media Bar disappears when playing media is back in view - SoundCloud [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User <Name> sent long message to conversation <Contact1>
    Given User <Name> sent message <SoundCloudLink> to conversation <Contact1>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I tap on text input to scroll to the end
    And I see media link <SoundCloudLink> and media in dialog
    And I tap media link
    And I scroll media out of sight until media bar appears
    And I tap on text input to scroll to the end
    Then I dont see media bar on dialog page

    Examples: 
      | Name      | Contact1  | SoundCloudLink                                                                       |
      | user1Name | user2Name | https://soundcloud.com/revealed-recordings/dannic-shermanology-wait-for-you-download |

  @C2614 @regression @id3199
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
    And I tap on text input to scroll to the end
    And I see media link <SoundCloudLink> and media in dialog
    And I tap media link
    And I scroll media out of sight until media bar appears
    And I tap on text input to scroll to the end
    Then I dont see media bar on dialog page

    Examples: 
      | Name      | Contact1  | SoundCloudLink                                                                       |
      | user1Name | user2Name | https://soundcloud.com/revealed-recordings/dannic-shermanology-wait-for-you-download |