Feature: Conversation View

  @C2632 @regression @rc @id2419
  Scenario Outline: Verify sending message [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see conversations list
    When I tap on contact name <Contact>
    And I type the default message and send it
    Then I see 1 default message in the dialog

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2645 @regression @id2375
  Scenario Outline: Verify sending message [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see conversations list
    When I tap on contact name <Contact>
    And I type the default message and send it
    Then I see 1 default message in the dialog

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2644 @regression @id2695
  Scenario Outline: Receive message from contact [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User <Contact> sends 1 encrypted message to user Myself
    And I tap on contact name <Contact>
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
    Given I see conversations list
    Given User <Contact> sends 1 encrypted message to user Myself
    When I tap on contact name <Contact>
    Then I see 1 default message in the dialog

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2621 @regression @rc @id2413 @deployPictures
  Scenario Outline: Verify sending image [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see conversations list
    When I tap on contact name <Contact>
    And I click plus button next to text input
    And I press Add Picture button on iPad
    And I press Camera Roll button
    And I choose a picture from camera roll
    And I press Confirm button
    Then I see 1 photo in the dialog

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2615 @regression @rc @id2407 @deployPictures
  Scenario Outline: Verify sending image [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see conversations list
    When I tap on contact name <Contact>
    And I click plus button next to text input
    And I press Add Picture button on iPad
    And I press Camera Roll button
    And I choose a picture from camera roll
    And I press Confirm button
    Then I see 1 photo in the dialog

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2642 @regression @id2429
  Scenario Outline: Verify you can see Ping on the other side - 1:1 conversation [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given User <Contact1> change name to <ContactName>
    Given Myself is connected to <Contact1>
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I tap on contact name <Contact1>
    Given User <Contact1> securely pings conversation <Name>
    When I wait for 3 seconds
    Then I see User <Contact1> Pinged message in the conversation

    Examples:
      | Name      | Contact1  | ContactName |
      | user1Name | user2Name | OtherUser   |

  @C2642 @regression @id2429 @C3222
  Scenario Outline: Verify you can see Ping on the other side - 1:1 conversation [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given User <Contact1> change name to <ContactName>
    Given Myself is connected to <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I tap on contact name <Contact1>
    Given User <Contact1> securely pings conversation <Name>
    When I wait for 3 seconds
    Then I see User <Contact1> Pinged message in the conversation

    Examples:
      | Name      | Contact1  | ContactName |
      | user1Name | user2Name | OtherUser   |

  @C2640 @regression @id2427 @C3223
  Scenario Outline: Verify you can see Ping on the other side - group conversation [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I tap on group chat with name <GroupChatName>
    Given User <Contact1> securely pings conversation <GroupChatName>
    When I wait for 3 seconds
    Then I see User <Contact1> Pinged message in the conversation

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName        |
      | user1Name | user2Name | user3Name | ReceivePingGroupChat |

  @C2640 @regression @id2427 @C3224
  Scenario Outline: Verify you can see Ping on the other side - group conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I tap on group chat with name <GroupChatName>
    Given User <Contact1> securely pings conversation <GroupChatName>
    When I wait for 3 seconds
    Then I see User <Contact1> Pinged message in the conversation

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName        |
      | user1Name | user2Name | user3Name | ReceivePingGroupChat |

  @C2627 @regression @id2669 @deployPictures
  Scenario Outline: Receive a camera roll picture from user from contact list [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User <Contact> sends encrypted image <Picture> to <ConversationType> conversation <Name>
    When I tap on contact name <Contact>
    Then I see 1 photo in the dialog

    Examples:
      | Name      | Contact   | Picture     | ConversationType |
      | user1Name | user2Name | testing.jpg | single user      |

  @C2628 @regression @id2670 @deployPictures
  Scenario Outline: Receive a camera roll picture from user from contact list [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User <Contact> sends encrypted image <Picture> to single user conversation <Name>
    When I tap on contact name <Contact>
    Then I see 1 photo in the dialog

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @C2646 @regression @id2736
  Scenario Outline: Send Message to contact after navigating away from chat page [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see conversations list
    When I tap on contact name <Contact>
    And I type the default message
    And I navigate back to conversations list
    When I tap my avatar
    And I close self profile
    And I tap on text input
    And I press Enter key in Simulator window
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
    And I see conversations list
    When I tap on contact name <Contact>
    And I type the default message
    And I type the default message
    When I tap my avatar
    And I close self profile
    And I tap on text input
    And I press Enter key in Simulator window
    Then I see 1 default message in the dialog

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2654 @regression @id2744
  Scenario Outline: Copy and paste to send the message [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I see sign in screen
    When I tap I HAVE AN ACCOUNT button
    And I have entered login <Text>
    And I tap and hold on Email input
    And I click on popup SelectAll item
    And I click on popup Copy item
    And I have entered login <Login>
    And I have entered password <Password>
    And I press Login button
    And I see conversations list
    And I tap on contact name <Contact>
    And I tap on text input
    And I tap and hold on message input
    And I click on popup Paste item
    And I press Enter key in Simulator window
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
    When I tap I HAVE AN ACCOUNT button
    And I have entered login <Text>
    And I tap and hold on Email input
    And I click on popup SelectAll item
    And I click on popup Copy item
    And I have entered login <Login>
    And I have entered password <Password>
    And I press Login button
    And I see conversations list
    And I tap on contact name <Contact>
    And I tap on text input
    And I tap and hold on message input
    And I click on popup Paste item
    And I press Enter key in Simulator window
    Then I see last message in dialog is expected message <Text>

    Examples:
      | Login      | Password      | Name      | Contact   | Text       |
      | user1Email | user1Password | user1Name | user2Name | TextToCopy |

  @C2656 @regression @id2746
  Scenario Outline: Send a text containing spaces on either end of message [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the "   " message and send it
    Then I see 0 default messages in the dialog
    When I type the default message
    And I type the "   " message and send it
    Then I see 1 default message in the dialog

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2657 @regression @id2747
  Scenario Outline: Send a text containing spaces on either end of message [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the "   " message and send it
    Then I see 0 default messages in the dialog
    When I type the default message
    And I type the "   " message and send it
    Then I see 1 default message in the dialog

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C5237 @regression @id2403
  Scenario Outline: Conversation gets scrolled back to playing media when clicking on media bar [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User Myself sends 40 encrypted messages to user <Contact>
    Given User Myself sends encrypted message "<SoundCloudLink>" to user <Contact>
    When I tap on contact name <Contact>
    And I scroll to the end of the conversation
    Then I see media link <SoundCloudLink> and media in dialog
    And I tap media link
    And I scroll media out of sight until media bar appears
    And I tap on the media bar
    Then I see conversation view is scrolled back to the playing media link <SoundCloudLink>

    Examples:
      | Name      | Contact   | SoundCloudLink                                                   |
      | user1Name | user2Name | https://soundcloud.com/tiffaniafifa2/overdose-exo-short-acoustic |

  @regression @id2404 @C3225
  Scenario Outline: (MediaBar disappears on Simulator) Verify the Media Bar dissapears after playback finishes - SoundCloud [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User Myself sends 40 encrypted messages to user <Contact>
    Given User Myself sends encrypted message "<SoundCloudLink>" to user <Contact>
    When I tap on contact name <Contact>
    And I tap on text input to scroll to the end
    And I see media link <SoundCloudLink> and media in dialog
    When I tap media link
    And I scroll media out of sight until media bar appears
    Then I wait up to 35 seconds for media bar to disappear

    Examples:
      | Name      | Contact   | SoundCloudLink                                                   |
      | user1Name | user2Name | https://soundcloud.com/tiffaniafifa2/overdose-exo-short-acoustic |

  @C2676 @regression @id2987
  Scenario Outline: I can send a sketch[PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I click plus button next to text input
    And I tap on sketch button in cursor
    And I draw a random sketch
    And I send my sketch
    Then I see 1 photo in the dialog

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2677 @regression @id2988
  Scenario Outline: I can send a sketch[LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I click plus button next to text input
    And I tap on sketch button in cursor
    And I draw a random sketch
    And I send my sketch
    Then I see 1 photo in the dialog

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2633 @regression @id2420
  Scenario Outline: Verify sending ping in 1-to-1 conversation [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see conversations list
    When I tap on contact name <Contact>
    And I click plus button next to text input
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
    And I see conversations list
    When I tap on contact name <Contact>
    And I click plus button next to text input
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
    And I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I type the default message and send it
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
    And I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I type the default message and send it
    Then I see 1 default message in the dialog

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | SimpleGroup   |

  @C2611 @regression @id3196
  Scenario Outline: Play/pause SoundCloud media link from the media bar [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User Myself sends 40 encrypted messages to user <Contact>
    Given User Myself sends encrypted message "<SoundCloudLink>" to user <Contact>
    When I tap on contact name <Contact>
    And I tap on text input to scroll to the end
    And I navigate back to conversations list
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
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User Myself sends 40 encrypted messages to user <Contact>
    Given User Myself sends encrypted message "<SoundCloudLink>" to user <Contact>
    When I tap on contact name <Contact>
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
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User Myself sends 40 encrypted messages to user <Contact>
    Given User Myself sends encrypted message "<SoundCloudLink>" to user <Contact>
    When I tap on contact name <Contact1>
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
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User Myself sends 40 encrypted messages to user <Contact1>
    Given User Myself sends encrypted message "<SoundCloudLink>" to user <Contact1>
    When I tap on contact name <Contact1>
    And I tap on text input to scroll to the end
    And I see media link <SoundCloudLink> and media in dialog
    And I tap media link
    And I scroll media out of sight until media bar appears
    And I tap on text input to scroll to the end
    Then I dont see media bar on dialog page

    Examples:
      | Name      | Contact1  | SoundCloudLink                                                                       |
      | user1Name | user2Name | https://soundcloud.com/revealed-recordings/dannic-shermanology-wait-for-you-download |