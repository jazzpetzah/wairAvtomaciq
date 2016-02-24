Feature: Conversation View

  @C3182 @regression @id855
  Scenario Outline: Verify swipe right tutorial appearance
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    Then I see TAPORSLIDE text

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C3181 @rc @regression @clumsy @IPv6 @id330
  Scenario Outline: Send Message to contact
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the default message and send it
    Then I see 1 default message in the dialog

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C923 @regression @id331
  Scenario Outline: Send Hello to contact
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I click plus button next to text input
    And I click Ping button
    Then I see You Pinged message in the dialog

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C909 @C3176 @rc @regression @IPv6 @id332 @id1470
  Scenario Outline: Send a camera roll picture to user from contact list
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I click plus button next to text input
    And I press Add Picture button
    And I press Camera Roll button
    And I choose a picture from camera roll
    And I press Confirm button
    Then I see 1 photo in the dialog

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C924 @regression @id334
  Scenario Outline: Send message to group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I type the default message and send it
    Then I see 1 default message in the dialog

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName  |
      | user1Name | user2Name | user3Name | MessageToGroup |

  @C3210 @regression @IPv6 @id1468
  Scenario Outline: (MediaBar disappears on Simulator) Play/pause SoundCloud media link from the media bar
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User Myself sends 40 encrypted messages to user <Contact>
    Given User Myself sends encrypted message "<SoundCloudLink>" to user <Contact>
    When I tap on contact name <Contact>
    And I tap on text input
    And I tap media container
    And I scroll media out of sight until media bar appears
    And I pause playing the media in media bar
    Then I see media is paused on Media Bar
    And I press play in media bar
    Then I see media is playing on Media Bar
    And I stop media in media bar
    Then I see media is stopped on Media Bar

    Examples:
      | Name      | Contact   | SoundCloudLink                                                   |
      | user1Name | user2Name | https://soundcloud.com/tiffaniafifa2/overdose-exo-short-acoustic |

  @C3205 @regression @id384
  Scenario Outline: (MediaBar disappears on Simulator) Conversation gets scrolled back to playing media when clicking on media bar
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User Myself sends 40 encrypted messages to user <Contact>
    Given User Myself sends encrypted message "<SoundCloudLink>" to user <Contact>
    When I tap on contact name <Contact>
    And I tap on text input to scroll to the end
    And I tap media container
    And I scroll media out of sight until media bar appears
    And I tap on the media bar
    Then I see conversation view is scrolled back to the playing media link <SoundCloudLink>

    Examples:
      | Name      | Contact   | SoundCloudLink                                   |
      | user1Name | user2Name | https://soundcloud.com/sodab/256-ra-robag-wruhme |

  @C3206 @regression @id385
  Scenario Outline: (MediaBar disappears on Simulator) Verify the Media Bar dissapears after playback finishes - SoundCloud
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User Myself sends 40 encrypted messages to user <Contact>
    Given User Myself sends encrypted message "<SoundCloudLink>" to user <Contact>
    And I tap on contact name <Contact>
    And I tap on text input to scroll to the end
    When I tap media container
    And I scroll media out of sight until media bar appears
    Then I wait up to 35 seconds for media bar to disappear

    Examples:
      | Name      | Contact   | SoundCloudLink                                                   |
      | user1Name | user2Name | https://soundcloud.com/tiffaniafifa2/overdose-exo-short-acoustic |

  @C3207 @regression @id386
  Scenario Outline: (MediaBar disappears on Simulator) Verify the Media Bar disappears when playing media is back in view - SoundCloud
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Name> sends 40 encrypted messages to user <Contact1>
    Given User <Name> sends encrypted message "<SoundCloudLink>" to user <Contact1>
    When I tap on contact name <Contact1>
    And I tap on text input to scroll to the end
    And I tap media container
    When I scroll media out of sight until media bar appears
    And I tap on text input to scroll to the end
    Then I dont see media bar on dialog page

    Examples:
      | Name      | Contact1  | SoundCloudLink                                                   |
      | user1Name | user2Name | https://soundcloud.com/tiffaniafifa2/overdose-exo-short-acoustic |

  @C883 @regression @id394
  Scenario Outline: (ZIOS-5920) Tap the cursor to get to the end of the conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I navigate back to conversations list
    Given User <Contact> sends 40 encrypted messages to user Myself
    When I tap on contact name <Contact>
    And I see plus button is not shown
    And I tap on text input to scroll to the end
    Then I see conversation is scrolled to the end

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C932 @regression @id415
  Scenario Outline: Send Message to contact after navigating away from chat page
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the default message
    And I navigate back to conversations list
    And I tap on contact name <Contact>
    And I tap on text input
    And I press Enter key in Simulator window
    Then I see 1 default message in the dialog

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C878 @regression @id413
  Scenario Outline: Copy and paste to send the message
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
    And I accept First Time overlay if it is visible
    And I dismiss settings warning
    And I see conversations list
    And I tap on contact name <Contact>
    And I tap on text input
    And I tap and hold on message input
    And I paste and commit the text
    Then I see last message in dialog is expected message <Text>

    Examples:
      | Login      | Password      | Name      | Contact   | Text       |
      | user1Email | user1Password | user1Name | user2Name | TextToCopy |

  @C931 @regression @id414
  Scenario Outline: Send a text containing spaces
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    And I tap on contact name <Contact>
    When I type the "   " message and send it
    Then I see 0 default messages in the dialog
    When I type the default message
    And I type the "   " message and send it
    Then I see 1 default message in the dialog

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C920 @regression @id1474
  Scenario Outline: Verify you can see conversation images in fullscreen
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I click plus button next to text input
    And I press Add Picture button
    And I press Camera Roll button
    And I choose a picture from camera roll
    And I press Confirm button
    And I see 1 photo in the dialog
    And I tap and hold image to open full screen
    And I see Full Screen Page opened
    And I see sender first name <Name> on fullscreen page
    And I see send date on fullscreen page
    And I see download button shown on fullscreen page
    And I tap on fullscreen page
    And I verify image caption and download button are not shown
    And I tap on fullscreen page
    And I tap close fullscreen page button
    Then I see 1 photo in the dialog

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C3183 @rc @regression @IPv6 @id526
  Scenario Outline: I can send and play inline youtube link
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I post media link <YouTubeLink>
    And I navigate back to conversations list
    And I tap on contact name <Contact>
    And I click video container for the first time
    # Wait until web page is loaded
    And I wait for 5 seconds
    Then I see video player page is opened

    Examples:
      | Name      | Contact   | YouTubeLink                                |
      | user1Name | user2Name | http://www.youtube.com/watch?v=Bb1RhktcugU |

  @C140 @regression @id1388
  Scenario Outline: Verify play/pause controls are visible in the list if there is active media item in other conversation - SoundCloud
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User Myself sends 40 encrypted messages to user <Contact1>
    Given User Myself sends encrypted message "<SoundCloudLink>" to user <Contact1>
    Given User Myself sends 40 encrypted messages to user <Contact2>
    Given User Myself sends encrypted message "<SoundCloudLink>" to user <Contact2>
    When I tap on contact name <Contact1>
    And I tap media container
    And I navigate back to conversations list
    And I see play/pause button next to username <Contact1> in contact list
    And I tap play/pause button in contact list next to username <Contact1>
    And I tap on contact name <Contact2>
    And I tap media container
    And I navigate back to conversations list
    And I see play/pause button next to username <Contact2> in contact list
    And I tap play/pause button in contact list next to username <Contact2>
    And I tap on contact name <Contact2>
    And I scroll media out of sight until media bar appears
    Then I see media is paused on Media Bar

    Examples:
      | Name      | Contact1  | Contact2  | SoundCloudLink                                                                       |
      | user1Name | user2Name | user3Name | https://soundcloud.com/revealed-recordings/dannic-shermanology-wait-for-you-download |

  @C921 @regression @id1480
  Scenario Outline: Rotate image in fullscreen mode
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact> sends encrypted image <Picture> to single user conversation Myself
    And I tap on contact name <Contact>
    And I see 1 photo in the dialog
    And I tap and hold image to open full screen
    And I see Full Screen Page opened
    When I rotate UI to landscape
    Then I see Full Screen Page opened

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @C1826 @regression @id2124
  Scenario Outline: Verify archiving conversation from ellipsis menu
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I open conversation details
    And I open ellipsis menu
    And I click archive menu button
    Then I dont see conversation <Contact> in contact list
    And I open archived conversations
    Then I see user <Contact> in contact list

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C141 @rc @regression @id1476
  Scenario Outline: Play/pause controls can change playing media state (SoundCloud)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact> sends encrypted message "<SoundCloudLink>" to user Myself
    When I tap on contact name <Contact>
    And I remember media container state
    And I tap media container
    And I navigate back to conversations list
    And I wait for 1 second
    And I tap play/pause button in contact list next to username <Contact>
    And I tap on contact name <Contact>
    Then I see media container state is not changed
    When I navigate back to conversations list
    And I wait for 1 second
    And I tap play/pause button in contact list next to username <Contact>
    And I tap on contact name <Contact>
    Then I see media container state is changed

    Examples:
      | Name      | Contact   | SoundCloudLink                                                            |
      | user1Name | user2Name | https://soundcloud.com/isabella-emanuelsson/david-guetta-she-wolf-falling |

  @C940 @regression @IPv6 @id2762
  Scenario Outline: Receive message from contact
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact> sends 1 encrypted message to user Myself
    When I tap on contact name <Contact>
    Then I see 1 default message in the dialog

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C922 @regression @IPv6 @id2763
  Scenario Outline: Receive a camera roll picture from user from contact list
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact> sends encrypted image <Picture> to single user conversation <Name>
    When I tap on contact name <Contact>
    Then I see 1 photo in the dialog

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @C951 @rc @regression @id2976
  Scenario Outline: I can send a sketch
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
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

  @C888 @C889 @rc @regression @id3093 @id3092
  Scenario Outline: Verify opening and closing input options by buttons click and swiping right/left
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I swipe right text input to reveal option buttons
    Then I see conversation tools buttons
    And I see plus button is not shown
    And I swipe left on options buttons
    And I see Close input options button is not visible
    And I see plus button next to text input
    And I click plus button next to text input
    Then I see conversation tools buttons
    And I click Close input options button
    And I see Close input options button is not visible
    And I see plus button next to text input

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C891 @regression @id3095
  Scenario Outline: Verify only people icon exists under the plus in pending/left/removed from conversations
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact2>,<Contact3>
    Given Myself has group chat <GroupChatName> with <Contact2>,<Contact3>
    Given I leave group chat <GroupChatName>
    Given Me sent connection request to <Contact1>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I see plus button next to text input
    And I click plus button next to text input
    Then I see no other conversation tools buttons except of Details
    And I click Close input options button
    And I navigate back to conversations list
    When I tap on group chat with name <GroupChatName>
    And I see plus button next to text input
    And I click plus button next to text input
    Then I see no other conversation tools buttons except of Details

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName    |
      | user1Name | user2Name | user3Name | user4Name | ArchiveGroupChat |

  @C954 @regression @id3265
  Scenario Outline: Verify drawing on image from single view
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact> sends encrypted image <Picture> to single user conversation Myself
    When I tap on contact name <Contact>
    And I see 1 photo in the dialog
    And I tap and hold image to open full screen
    And I see Full Screen Page opened
    And I press Sketch button on image fullscreen page
    And I draw a random sketch
    And I send my sketch
    Then I see 2 photos in the dialog

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @C952 @rc @regression @id3263
  Scenario Outline: Verify drawing on the image from gallery
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I click plus button next to text input
    And I press Add Picture button
    And I press Camera Roll button
    And I choose a picture from camera roll
    And I press sketch button on camera roll page
    And I draw a random sketch
    And I send my sketch
    And I press Confirm button
    Then I see 1 photo in the dialog

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C908 @regression @id2781
  Scenario Outline: Verify player isn't displayed for vimeo links without video IDs
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Name> sends encrypted message "<VimeoLink>" to user <Contact>
    When I tap on contact name <Contact>
    Then I see vimeo link <VimeoLink> but NO media player

    Examples:
      | Name      | Contact   | VimeoLink                    |
      | user1Name | user2Name | https://vimeo.com/categories |

  @C907 @regression @id2780
  Scenario Outline: Verify player is displayed for vimeo links with video IDs
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Name> sends encrypted message "<VimeoLink>" to user <Contact1>
    When I tap on contact name <Contact1>
    Then I see vimeo link <VimeoLink> and media in dialog

    Examples:
      | Name      | Contact1  | VimeoLink                   |
      | user1Name | user2Name | https://vimeo.com/129426512 |

  @C941 @regression @id3788
  Scenario Outline: Verify sending link and opening it
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User Myself sends encrypted message "<Link>" to user <Contact1>
    When I tap on contact name <Contact1>
    And I tap on message "<Link>"
    Then I see WireWebsitePage

    Examples:
      | Name      | Contact1  | Link                  |
      | user1Name | user2Name | https://www.wire.com/ |

  @C942 @regression @id3789
  Scenario Outline: Verify sending link and text in one message and opening the link
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User Myself sends encrypted message "<MessageAndLink>" to user <Contact1>
    When I tap on contact name <Contact1>
    And I tap on message "<MessageAndLink>"
    Then I see WireWebsitePage

    Examples:
      | Name      | Contact1  | MessageAndLink                                |
      | user1Name | user2Name | https://www.wire.com/ is the best of the best |

  @C943 @regression @id3798
  Scenario Outline: Verify input field and action buttons are not shown simultaneously
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>, <Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I type the default message
    And I navigate back to conversations list
    When I tap on contact name <Contact2>
    And I navigate back to conversations list
    And I tap on contact name <Contact1>
    Then I see Close input options button is not visible
    And I see the default message in input field

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C845 @regression @id3963
  Scenario Outline: Verify posting in a 1-to-1 conversation without content
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Name> sends 1 encrypted message to user <Contact1>
    When I swipe right on a <Contact1>
    And I click delete menu button
    And I confirm delete conversation content
    Then I dont see conversation <GroupChatName> in contact list
    And I open search by taping on it
    And I search for user name <Contact1> and tap on it on People picker page
    And I tap Open conversation action button on People picker page
    Then I see dialog page
    And I type the default message and send it
    And I see 1 default message in the dialog

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C879 @regression @id1158
  Scenario Outline: Verify possibility to copy image in the conversation view
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact> sends encrypted image <Picture> to single user conversation Myself
    And I tap on contact name <Contact>
    And I see 1 photo in the dialog
    And I longpress on image in the conversation
    And I tap on copy badge
    And I tap on text input
    And I tap and hold on message input
    And I click on popup Paste item
    And I press Confirm button
    Then I see 2 photo in the dialog

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @C911 @regression @id562
  Scenario Outline: Verify downloading images in fullscreen
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact> sends encrypted image <Picture> to single user conversation Myself
    When I tap on contact name <Contact>
    And I see 1 photo in the dialog
    And I tap and hold image to open full screen
    And I see Full Screen Page opened
    And I see download button shown on fullscreen page
    And I tap download button on fullscreen page
    And I tap close fullscreen page button
    And I click plus button next to text input
    And I press Add Picture button
    And I press Camera Roll button
    And I choose last picture from camera roll
    And I press Confirm button
    And I see 2 photos in the dialog

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @C27 @regression @id715
  Scenario Outline: Verify you still receive messages from blocked person in a group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>, <Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>, <Contact2>
    Given User <Name> blocks user <Contact1>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact1> sends 1 encrypted message to group conversation <GroupChatName>
    Given User <Contact1> sends encrypted image <Picture> to group conversation <GroupChatName>
    When I tap on group chat with name <GroupChatName>
    Then I see 3 conversation entries

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Picture     |
      | user1Name | user2Name | user3Name | Caramba!      | testing.jpg |

  @C886 @regression @id2019
  Scenario Outline: Verify people icon is changed on avatar with opening keyboard and back
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I see plus button next to text input
    And I type the default message
    And I see plus icon is changed to user avatar icon
    And I clear conversation text input
    Then I see plus button next to text input

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |