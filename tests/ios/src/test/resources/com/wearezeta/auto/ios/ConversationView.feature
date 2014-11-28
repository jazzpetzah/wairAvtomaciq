Feature: Conversation View

  @smoke @id330
  Scenario Outline: Send Message to contact
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I type the message
    And I send the message
    Then I see my message in the dialog

    Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |

  @smoke @id331
  Scenario Outline: Send Hello to contact
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe the text input cursor
    And I click Ping button
    Then I see Hello message in the dialog
    And I click Ping button
    Then I see Hey message in the dialog

    Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |

  @smoke @id332
  Scenario Outline: Send a camera roll picture to user from contact list
    Given I Sign in using login <Login> and password <Password>
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
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |

  @smoke @id334
  Scenario Outline: Send message to group chat
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I create group chat with <Contact1> and <Contact2>
    And I type the message
    And I send the message
    Then I see my message in the dialog

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 |

  @staging @id383
  Scenario Outline: Play/pause SoundCloud media link from the media bar
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I type and send long message and media link <SoundCloudLink>
    Then I see media link <SoundCloudLink> and media in dialog
    And I tap media link
    And I scroll media out of sight until media bar appears
    And I pause playing the media in media bar
    Then I see playing media is paused
    And I press play in media bar
    Then I see media is playing
    And I stop media in media bar
    Then The media stopps playing

    Examples: 
      | Login   | Password    | Name    | Contact1    | SoundCloudLink                                                                       |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | https://soundcloud.com/revealed-recordings/dannic-shermanology-wait-for-you-download |

  @regression @id384
  Scenario Outline: Conversation gets scrolled back to playing media when clicking on media bar
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I type and send long message and media link <SoundCloudLink>
    Then I see media link <SoundCloudLink> and media in dialog
    And I tap media link
    And I scroll media out of sight until media bar appears
    And I tap on the media bar
    Then I see conversation view is scrolled back to the playing media link <SoundCloudLink>

    Examples: 
      | Login   | Password    | Name    | Contact1    | SoundCloudLink                                                                       |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | https://soundcloud.com/revealed-recordings/dannic-shermanology-wait-for-you-download |

  @regression @id385
  Scenario Outline: Verify the Media Bar dissapears after playback finishes (SoundCloud)
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I type and send long message and media link <SoundCloudLink>
    And I see media link <SoundCloudLink> and media in dialog
    And I tap media link
    And I scroll media out of sight until media bar appears
    And I see media bar on dialog page
    And I wait <time> seconds for media to stop playing
    Then I dont see media bar on dialog page

    Examples: 
      | Login   | Password    | Name    | Contact1    | SoundCloudLink                                                                       | time |
      | aqaUser | aqaPassword | aqaUser | aqaContact3 | https://soundcloud.com/revealed-recordings/dannic-shermanology-wait-for-you-download | 129  |

  @regression @id386
  Scenario Outline: Verify the Media Bar disappears when playing media is back in view (SoundCloud)
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I type and send long message and media link <SoundCloudLink>
    And I see media link <SoundCloudLink> and media in dialog
    And I tap media link
    And I scroll media out of sight until media bar appears
    And I tap on text input
    Then I dont see media bar on dialog page

    Examples: 
      | Login   | Password    | Name    | Contact1    | SoundCloudLink                                                                       |
      | aqaUser | aqaPassword | aqaUser | aqaContact2 | https://soundcloud.com/revealed-recordings/dannic-shermanology-wait-for-you-download |

  @staging @id394
  Scenario Outline: Tap the cursor to get to the end of the conversation
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I send long message
    And I scroll to the beginning of the conversation
    And I tap on text input
    Then I see last message in the dialog

    Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |

  @staging @id415
  Scenario Outline: Send Message to contact after navigating away from chat page
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I type the message
    And I scroll away the keyboard
    And I navigate back to conversations view
    Then I tap on contact name <Contact>
    And I send the message
    Then I see my message in the dialog

    Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |

  @staging @id407
  Scenario Outline: Send more than 200 chars message
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I input more than 200 chars message and send it
    And I see my message in the dialog
    And I swipe right on Dialog page
    And I tap on my name <Name>
    And I click on Settings button on personal page
    And I click Sign out button from personal page
    And I Sign in using login <Contact> and password <Password>
    And I see Personal page
    And I swipe right on the personal page
    And I tap on contact name <Name>
    Then I see my message in the dialog

    Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |

  @regression @id408
  Scenario Outline: Send one line message with lower case and upper case
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I input message with lower case and upper case
    And I send the message
    Then I see my message in the dialog

    Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |

  #Muted due to special chars verification problems on ios
  @mute @staging @id409
  Scenario Outline: Send special chars (German)
    Given I press Sign in button
    And I fill in email input <Text>
    And I copy email input field content
    And I have entered login <Login>
    And I have entered password <Password>
    And I press Login button
    And I see Contact list with my name <Name>
    And I tap on contact name <Contact>
    And I see dialog page
    And I tap and hold on message input
    And I click on popup Paste item
    And I send the message
    Then I see last message in dialog is expected message <Text>

    Examples: 
      | Login   | Password    | Name    | Contact     | Text                  |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | ÄäÖöÜüß & latin chars |

  @regression @id413
  Scenario Outline: Copy and paste to send the message
    Given I see sign in screen
    When I press Sign in button
    And I have entered login <text>
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
    Then I see last message in dialog is expected message <text>

    Examples: 
      | Login   | Password    | Name    | Contact     | text       |
      | aqaUser | aqaPassword | aqaUser | aqaContact2 | TextToCopy |

  @regression @id414
  Scenario Outline: Send a text containing spaces
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I try to send message with only spaces
    And I see message with only spaces is not send
    And I input message with leading empty spaces
    And I send the message
    And I see my message in the dialog
    And I input message with trailing emtpy spaces
    And I send the message
    Then I see my message in the dialog

    Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |

  @regression @id416
  Scenario Outline: Keyboard up and navigate to main convo list
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I see keyboard
    And I scroll away the keyboard
    And I dont see keyboard

    Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | aqaContact2 |

  @regression @id488
  Scenario Outline: Verify you can see conversation images in fullscreen
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
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
      | Login   | Password    | Name    | Contact1    |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |

  @regression @id526
  Scenario Outline: I can send and play inline youtube link
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I post media link <YouTubeLink>
    Then I see media link <YouTubeLink> and media in dialog
    And I click video container for the first time
    And I see video player page is opened

    Examples: 
      | Login   | Password    | Name    | Contact     | YouTubeLink                                |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | http://www.youtube.com/watch?v=Bb1RhktcugU |

  @regression @id555
  Scenario Outline: Verify you can add people from 1:1 people view (view functionality)
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I swipe up on dialog page to open other user personal page
    And I see <Contact1> user profile page
    And I press Add button
    And I see People picker page
    And I dont see keyboard
    And I tap on connected user <Contact2> on People picker page
    And I see user <Contact2> on People picker page is selected
    And I tap on connected user <Contact2> on People picker page
    And I see user <Contact2> on People picker page is NOT selected
    And I tap on connected user <Contact2> on People picker page
    And I tap on Search input on People picker page
    And I see keyboard
    And I don't see Add to conversation button
    And I press keyboard Delete button
    And I see user <Contact2> on People picker page is NOT selected

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 |

  @staging @id556
  Scenario Outline: Verify you can add people from 1:1 people view (via Add to Conversation button)
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I swipe up on dialog page to open other user personal page
    And I see <Contact1> user profile page
    And I press Add button
    And I see People picker page
    And I scroll up page a bit
    And I tap on connected user <Contact2> on People picker page
    And I tap on connected user <Contact3> on People picker page
    And I see Add to conversation button
    And I click on Add to conversation button
    And I see group chat page with 3 users <Contact1> <Contact2> <Contact3>
    And I swipe right on group chat page
    And I see Contact list with my name <Name>
    And I see in contact list group chat with <Contact1> <Contact2> <Contact3>

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | Contact3    |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | aqaContact3 |

  @staging @id557
  Scenario Outline: Verify you can add people from 1:1 people view (via keyboard button)
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I swipe up on dialog page to open other user personal page
    And I see <Contact1> user profile page
    And I press Add button
    And I see People picker page
    And I input in People picker search field user name <Contact2>
    And I see user <Contact2> found on People picker page
    And I don't see Add to conversation button
    And I click on connected user <Contact2> avatar on People picker page
    And I click on connected user <Contact3> avatar on People picker page
    And I click on Go button
    And I see group chat page with 3 users <Contact1> <Contact2> <Contact3>
    And I swipe right on group chat page
    And I see Contact list with my name <Name>
    And I see in contact list group chat with <Contact1> <Contact2> <Contact3>

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | Contact3    |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | aqaContact3 |

  @staging @id559
  Scenario Outline: Verify you can add people from 1:1 people view (cancel view)
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I swipe up on dialog page to open other user personal page
    And I see <Contact1> user profile page
    And I press Add button
    And I see People picker page
    And I scroll up page a bit
    And I dont see keyboard
    And I see Add to conversation button
    And I tap on connected user <Contact2> on People picker page
    And I tap on connected user <Contact3> on People picker page
    And I click close button to dismiss people view
    And I see <Contact1> user profile page
    And I press Add button
    And I see People picker page
    And I see user <Contact2> on People picker page is NOT selected
    And I see user <Contact3> on People picker page is NOT selected
    And I click close button to dismiss people view
    And I see <Contact1> user profile page
    And I swipe down on other user profile page
    And I see dialog page
    And I swipe right on Dialog page
    And I see Contact list with my name <Name>
    And I don't see in contact list group chat with <Contact1> <Contact2> <Contact3>

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | Contact3    |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | aqaContact3 |

  #Muted due to app quit on logout workaround
  @staging @id606 @mute
  Scenario Outline: Verify you can see text message, which was sent into a group conversation, on the second end
    Given I have group chat with name <ChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    And I tap on group chat with name <ChatName>
    And I send predefined message <message>
    And I see message in group chat <message>
    And I swipe right on Dialog page
    And I tap on my name <Name>
    And I click on Settings button on personal page
    And I click Sign out button from personal page
    And I Sign in using login <Contact1> and password <Password>
    And I see Personal page
    And I swipe right on the personal page
    And I see in contact list group chat named <ChatName>
    And I tap on group chat with name <ChatName>
    And I see message in group chat <message>
    And I swipe right on Dialog page
    And I tap on my name <Contact1>
    And I click on Settings button on personal page
    And I click Sign out button from personal page
    And I Sign in using login <Contact2> and password <Password>
    And I see Personal page
    And I swipe right on the personal page
    And I see in contact list group chat named <ChatName>
    And I tap on group chat with name <ChatName>
    And I see message in group chat <message>

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | ChatName   | message      |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | QAtestChat | Test Message |

  #Muted due to app quit on logout workaround
  @staging @id607 @mute
  Scenario Outline: Verify you can see multimedia message, which was sent into a group conversation, on the second end
    Given I have group chat with name <ChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    And I tap on group chat with name <ChatName>
    And I post media link <YouTubeLink>
    And I tap on dialog window
    And I swipe right on Dialog page
    And I tap on my name <Name>
    And I click on Settings button on personal page
    And I click Sign out button from personal page
    And I Sign in using login <Contact1> and password <Password>
    And I see Personal page
    And I swipe right on the personal page
    And I see in contact list group chat named <ChatName>
    And I tap on group chat with name <ChatName>
    And I see media link <YouTubeLink> and media in dialog
    And I click video container for the first time
    And I see video player page is opened
    And I tap on Done button on Video player page
    And I swipe right on Dialog page
    And I tap on my name <Contact1>
    And I click on Settings button on personal page
    And I click Sign out button from personal page
    And I Sign in using login <Contact2> and password <Password>
    And I see Personal page
    And I swipe right on the personal page
    And I see in contact list group chat named <ChatName>
    And I tap on group chat with name <ChatName>
    And I see media link <YouTubeLink> and media in dialog
    And I click video container for the first time
    And I see video player page is opened
    And I tap on Done button on Video player page

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | ChatName   | YouTubeLink                                |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | QAtestChat | http://www.youtube.com/watch?v=Bb1RhktcugU |

  #Muted due to app quit on logout workaround
  @staging @id608 @mute
  Scenario Outline: Verify you can see image, which was sent into a group conversation, on the second end
    Given I have group chat with name <ChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    And I tap on group chat with name <ChatName>
    And I swipe the text input cursor
    And I press Add Picture button
    And I press Camera Roll button
    And I choose a picture from camera roll
    And I press Confirm button
    And I see new photo in the dialog
    And I verify image in dialog is same as template <Picture>
    And I swipe right on Dialog page
    And I tap on my name <Name>
    And I click on Settings button on personal page
    And I click Sign out button from personal page
    And I Sign in using login <Contact1> and password <Password>
    And I see Personal page
    And I swipe right on the personal page
    And I see in contact list group chat named <ChatName>
    And I tap on group chat with name <ChatName>
    And I scroll to image in dialog
    And I verify image in dialog is same as template <Picture>
    And I swipe right on Dialog page
    And I tap on my name <Contact1>
    And I click on Settings button on personal page
    And I click Sign out button from personal page
    And I Sign in using login <Contact2> and password <Password>
    And I see Personal page
    And I swipe right on the personal page
    And I see in contact list group chat named <ChatName>
    And I tap on group chat with name <ChatName>
    And I scroll to image in dialog
    And I verify image in dialog is same as template <Picture>

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | ChatName   | Picture                   |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | QAtestChat | userpicture_landscape.jpg |

  #Muted due relogin issue
  @mute @staging @id614
  Scenario Outline: I am able to play inline YouTube link poster by others
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I post media link <YouTubeLink>
    And I tap on dialog window
    And I swipe right on Dialog page
    And I tap on my name <Name>
    And I click on Settings button on personal page
    And I click Sign out button from personal page
    And I see sign in screen
    And I Sign in using login <Contact> and password <Password>
    And I see Personal page
    And I swipe right on the personal page
    And I tap on contact name <Name>
    Then I see media link <YouTubeLink> and media in dialog
    And I click video container for the first time
    And I see video player page is opened

    Examples: 
      | Login   | Password    | Name    | Contact     | YouTubeLink                                |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | http://www.youtube.com/watch?v=Bb1RhktcugU |

  @staging @id1387
  Scenario Outline: Verify you can play/pause media from the Media Bar (YouTube)
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I type and send long message and media link <YouTubeLink>
    And I see media link <YouTubeLink> and media in dialog
    And I click video container for the first time
    And I see video player page is opened
    And I tap Pause button on Video player page
    And I tap on Done button on Video player page
    And I scroll media out of sight until media bar appears
    And I press play in media bar
    And I see video player page is opened
    And I tap on Done button on Video player page
    And I stop media in media bar
    Then The media stopps playing

    Examples: 
      | Login   | Password    | Name    | Contact     | YouTubeLink                                |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | http://www.youtube.com/watch?v=Bb1RhktcugU |

  @staging @id1388
  Scenario Outline: Verify play/pause controls are visible in the list if there is active media item in other conversation (SoundCloud)
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I type and send long message and media link <SoundCloudLink>
    And I see media link <SoundCloudLink> and media in dialog
    And I tap media link
    And I scroll away the keyboard
    And I swipe right on Dialog page
    And I see play/pause button next to username <Contact1> in contact list
    And I tap on contact name <Contact2>
    And I type and send long message and media link <SoundCloudLink>
    And I see media link <SoundCloudLink> and media in dialog
    And I tap media link
    And I swipe right on Dialog page
    And I dont see play/pause button next to username <Contact1> in contact list
    And I see play/pause button next to username <Contact2> in contact list
    And I tap on play/pause button in contact list
    And I tap on contact name <Contact2>
    And I scroll media out of sight until media bar appears
    Then I see playing media is paused

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | SoundCloudLink                                 |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | https://soundcloud.com/carl-cox/carl-cox-nexus |
