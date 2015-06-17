Feature: Conversation View

  @regression @id855
  Scenario Outline: Verify swipe right tutorial appearance
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    When I see Contact list with my name <Name>
    And I tap on contact name <Contact>
    And I see dialog page
    Then I see TAPORSLIDE text
    
    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @smoke @id330
  Scenario Outline: Send Message to contact
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I type the message
    And I send the message
    Then I see message in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  #https://wearezeta.atlassian.net/browse/ZIOS-3269
  @regression @id331
  Scenario Outline: Send Hello to contact
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe the text input cursor
    And I click Ping button
    Then I see You Pinged message in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @smoke @id332 @deployPictures @id1470
  Scenario Outline: Send a camera roll picture to user from contact list
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe the text input cursor
    And I press Add Picture button
    And I press Camera Roll button
    And I choose a picture from camera roll
    And I press Confirm button
    Then I see new photo in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id334
  Scenario Outline: Send message to group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I create group chat with <Contact1> and <Contact2>
    And I type the message
    And I send the message
    Then I see message in the dialog

    Examples: 
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @regression @id1468
  Scenario Outline: Play/pause SoundCloud media link from the media bar
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I type and send long message and media link <SoundCloudLink>
    And I wait for 5 seconds
    And I swipe right on Dialog page
    And I tap on contact name <Contact>
    And I scroll to the end of the conversation
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

  @regression @id384
  Scenario Outline: Conversation gets scrolled back to playing media when clicking on media bar
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I type and send long message and media link <SoundCloudLink>
    And I swipe right on Dialog page
    And I tap on contact name <Contact>
    And I scroll to the end of the conversation
    Then I see media link <SoundCloudLink> and media in dialog
    And I tap media link
    And I scroll media out of sight until media bar appears
    And I tap on the media bar
    Then I see conversation view is scrolled back to the playing media link <SoundCloudLink>

    Examples: 
      | Name      | Contact   | SoundCloudLink                                                                       |
      | user1Name | user2Name | https://soundcloud.com/revealed-recordings/dannic-shermanology-wait-for-you-download |

  @staging @id385
  Scenario Outline: Verify the Media Bar dissapears after playback finishes - SoundCloud
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I type and send long message and media link <SoundCloudLink>
    And I swipe right on Dialog page
    And I tap on contact name <Contact>
    And I scroll to the end of the conversation
    And I see media link <SoundCloudLink> and media in dialog
    And I tap media link
    And I scroll media out of sight until media bar appears
    And I see media bar on dialog page
    And I wait 150 seconds for media to stop playing
    Then I dont see media bar on dialog page

    Examples: 
      | Name      | Contact   | SoundCloudLink                                                                       |
      | user1Name | user2Name | https://soundcloud.com/revealed-recordings/dannic-shermanology-wait-for-you-download |

  @staging @id386
  Scenario Outline: Verify the Media Bar disappears when playing media is back in view - SoundCloud
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I type and send long message and media link <SoundCloudLink>
    And I swipe right on Dialog page
    And I tap on contact name <Contact1>
    And I scroll to the end of the conversation
    And I see media link <SoundCloudLink> and media in dialog
    And I tap media link
    And I scroll media out of sight until media bar appears
    And I scroll to the end of the conversation
    Then I dont see media bar on dialog page

    Examples: 
      | Name      | Contact1  | SoundCloudLink                                                                       |
      | user1Name | user2Name | https://soundcloud.com/revealed-recordings/dannic-shermanology-wait-for-you-download |

  @regression @id394
  Scenario Outline: Tap the cursor to get to the end of the conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
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

  @regression @id415
  Scenario Outline: Send Message to contact after navigating away from chat page
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
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

  @regression @id407
  Scenario Outline: Send more than 200 chars message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I input more than 200 chars message and send it
    Then I see message in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id408
  Scenario Outline: Send one line message with lower case and upper case
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I input message with lower case and upper case
    And I send the message
    Then I see message in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id409
  Scenario Outline: Send special chars (German)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    And I tap on contact name <Contact>
    And I see dialog page
    And I send using script predefined message <Text>
    Then I see last message in dialog is expected message <Text>

    Examples: 
      | Name      | Contact   | Text                  |
      | user1Name | user2Name | ÄäÖöÜüß & latin chars |

  @regression @id413
  Scenario Outline: Copy and paste to send the message
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

  @regression @id414
  Scenario Outline: Send a text containing spaces
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
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

  @regression @id416
  Scenario Outline: Keyboard up and navigate to main convo list
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I see keyboard
    And I scroll away the keyboard
    And I dont see keyboard

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id1474
  Scenario Outline: Verify you can see conversation images in fullscreen
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe the text input cursor
    And I press Add Picture button
    And I press Camera Roll button
    And I choose a picture from camera roll
    And I press Confirm button
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

  @regression @id526
  Scenario Outline: I can send and play inline youtube link
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I post media link <YouTubeLink>
    Then I see media link <YouTubeLink> and media in dialog
    And I click video container for the first time
    And I see video player page is opened

    Examples: 
      | Name      | Contact   | YouTubeLink                                |
      | user1Name | user2Name | http://www.youtube.com/watch?v=Bb1RhktcugU |

  @regression @id1387
  Scenario Outline: Verify you can play/pause media from the Media Bar - YouTube
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I type and send long message and media link <YouTubeLink>
    And I see media link <YouTubeLink> and media in dialog
    And I swipe right on Dialog page
    And I tap on contact name <Contact>
    And I click video container for the first time
    And I see video player page is opened
    And I tap Pause button on Video player page
    And I tap on Done button on Video player page
    And I scroll media out of sight until media bar appears
    And I press play in media bar
    And I see video player page is opened
    And I tap on Done button on Video player page
    And I stop media in media bar
    Then The media stops playing

    Examples: 
      | Name      | Contact   | YouTubeLink                                |
      | user1Name | user2Name | http://www.youtube.com/watch?v=Bb1RhktcugU |

  @staging @id1388
  Scenario Outline: Verify play/pause controls are visible in the list if there is active media item in other conversation - SoundCloud
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I type and send long message and media link <SoundCloudLink>
    And I see media link <SoundCloudLink> and media in dialog
    And I swipe right on Dialog page
    And I tap on contact name <Contact1>
    And I tap media link
    And I swipe right on Dialog page
    And I see play/pause button next to username <Contact1> in contact list
    And I tap play/pause button in contact list next to username <Contact1>
    And I tap on contact name <Contact2>
    And I type and send long message and media link <SoundCloudLink>
    And I see media link <SoundCloudLink> and media in dialog
    And I swipe right on Dialog page
    And I tap on contact name <Contact2>
    And I tap media link
    And I swipe right on Dialog page
    And I see play/pause button next to username <Contact2> in contact list
    And I tap play/pause button in contact list next to username <Contact2>
    And I tap on contact name <Contact2>
    And I scroll media out of sight until media bar appears
    Then I see playing media is paused

    Examples: 
      | Name      | Contact1  | Contact2  | SoundCloudLink                                                                       |
      | user1Name | user2Name | user3Name | https://soundcloud.com/revealed-recordings/dannic-shermanology-wait-for-you-download |

  @regression @id1137
  Scenario Outline: Verify appearance of title bar for conversation, restored from background
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I close the app for <CloseAppTime> seconds
    Then I see title bar in conversation name <Contact>

    Examples: 
      | Name      | Contact   | CloseAppTime |
      | user1Name | user2Name | 2            |
      
  @staging @id1480
  Scenario Outline: Rotate image in fullscreen mode
  	Given There are 2 users where <Name> is me
  	Given User <Contact> change name to <NewName>
    Given User <Contact> change accent color to <Color>
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    And Contact <Contact> sends image <Picture> to single user conversation <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I see new photo in the dialog
    And I memorize message send time
    And I tap and hold image to open full screen
    And I see Full Screen Page opened
    Then I rotate image in fullscreen mode
    
    Examples: 
      | Name      | Contact   | Picture     | Color       | NewName          |
      | user1Name | user2Name | testing.jpg | BrightOrange| RotateFullscreen |
      
  @regression @id2124
  Scenario Outline: Verify archiving conversation from ellipsis menu
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I open conversation details
    And I open ellipsis menu
    And I click archive menu button
    Then I dont see conversation <Contact> in contact list
    And I open archived conversations
    Then I see user <Contact> in contact list

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |
      
  @regression @id2132
  Scenario Outline: Verify displaying chathead when another conversation is opened
    Given There are 3 users where <Name> is me
    Given User <Contact2> change avatar picture to <Picture>
    Given User <Contact2> change name to <NewName>
    Given Myself is connected to <Contact>,<Contact2>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And Contact <Contact2> sends random message to user <Name>
    Then I see chathead of contact <Contact2> 
    And I wait for 5 seconds
    Then I do not see chathead of contact <Contact2>

    
    Examples: 
      | Name      | Contact   | Contact2  | NewName  | Picture                      |
      | user1Name | user2Name | user3Name | CHATHEAD | aqaPictureContact600_800.jpg |
 

  @regression @id1476
  Scenario Outline: Play/pause controls can change playing media state (SoundCloud)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I type and send long message and media link <SoundCloudLink>
    And I swipe right on Dialog page
    And I tap on contact name <Contact>
    And I see media link <SoundCloudLink> and media in dialog
    And I tap media link
    And I swipe right on Dialog page
    And I see play/pause button next to username <Contact> in contact list
    And I tap play/pause button in contact list next to username <Contact>
    And I tap on contact name <Contact>
    And I scroll media out of sight until media bar appears
    Then I see playing media is paused
    And I swipe right on Dialog page
    And I tap play/pause button in contact list next to username <Contact>
    And I tap on contact name <Contact>
    Then I see media is playing
    
    Examples: 
      | Name      | Contact   | SoundCloudLink |
      | user1Name | user2Name | https://soundcloud.com/isabella-emanuelsson/david-guetta-she-wolf-falling |
      
  @smoke @id2762
  Scenario Outline: Receive message from contact
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    And Contact <Contact> send message to user <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    Then I see message in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @smoke @id2763 @deployPictures
  Scenario Outline: Receive a camera roll picture from user from contact list
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    And Contact <Contact> sends image <Picture> to <ConversationType> conversation <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    Then I see new photo in the dialog

    Examples: 
      | Name      | Contact   | Picture | ConversationType | 
      | user1Name | user2Name | testing.jpg | single user | 
  
