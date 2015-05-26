Feature: Conversation View

  @id316 @smoke
  Scenario Outline: Send Message to contact
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I type the message "<Message>" and send it
    Then I see my message "<Message>" in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   | Message |
      | user1Email | user1Password | user1Name | user2Name | Yo      |

  @id317 @smoke
  Scenario Outline: Send Hello and Hey to contact
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe on text input
    And I press Ping button
    Then I see Ping message <Msg> in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   | Msg        |
      | user1Email | user1Password | user1Name | user2Name | YOU PINGED |

  @id318 @smoke
  Scenario Outline: Send Camera picture to contact
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe on text input
    And I press Add Picture button
    And I press "Take Photo" button
    And I press "Confirm" button
    Then I see new photo in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @id1262 @smoke
  Scenario Outline: Add people to 1:1 chat
    Given There are 3 users where <Name> is me
    Given <Name> is connected to <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I tap on contact name <Contact1>
    And I see dialog page
    And I tap conversation details button
    And I see <Contact1> user profile page
    And I press add contact button
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact2>
    And I see user <Contact2> found on People picker page
    And I tap on user name found on People picker page <Contact2>
    And I see Add to conversation button
    And I click on Add to conversation button
    Then I see group chat page with users <Contact1>,<Contact2>
    And I navigate back from dialog page
    And I see <Contact1> and <Contact2> chat in contact list

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  |
      | user1Email | user1Password | user1Name | user2Name | user3Name |

  @id320 @smoke
  Scenario Outline: Send message to group chat
    Given There are 3 users where <Name> is me
    Given <Name> is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I tap on contact name <GroupChatName>
    And I see dialog page
    And I tap on text input
    And I type the message "<Message>" and send it
    Then I see my message "<Message>" in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | GroupChatName     | Message |
      | user1Email | user1Password | user1Name | user2Name | user3Name | SendMessGroupChat | Yo      |

  @id143 @regression
  Scenario Outline: Send Long Message to contact
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I type the message "LONG_MESSAGE" and send it
    Then I see my message "LONG_MESSAGE" in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @id145 @regression
  Scenario Outline: Send Upper and Lower case to contact
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I type the message "<Message>" and send it
    Then I see my message "<Message>" in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   | Message  |
      | user1Email | user1Password | user1Name | user2Name | aaaaAAAA |

  @id146 @unicode @regression
  Scenario Outline: Send special chars message to contact
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I type the message "<Message>" and send it
    Then I see my message "<Message>" in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   | Message                           |
      | user1Email | user1Password | user1Name | user2Name | ÄäÖöÜüß simple message in english |

  @mute @regression @id149
  Scenario Outline: Send emoji message to contact
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I type the message "<Message>" and send it
    Then I see my message "<Message>" in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   | Message  |
      | user1Email | user1Password | user1Name | user2Name | :) ;) :( |

  @id147 @unicode @regression
  Scenario Outline: Send double byte chars
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I type the message "<Message>" and send it
    Then I see my message "<Message>" in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   | Message                     |
      | user1Email | user1Password | user1Name | user2Name | 畑 はたけ hatake field of crops |
      
  @id163 @regression
  Scenario Outline: Send image using existing camera rolls (portrait) in 1:1 chat
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe on text input
    And I press Add Picture button
    And I press "Gallery" button
    And I rotate UI to portrait
    And I wait for 1 second
    And I select picture for dialog
    And I press "Confirm" button
    Then I see new photo in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @id162 @regression
  Scenario Outline: Send image using existing camera rolls (landscape) in 1:1 chat
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe on text input
    And I press Add Picture button
    And I press "Gallery" button
    When I rotate UI to landscape
    And I wait for 1 second
    And I select picture for dialog
    And I press "Confirm" button
    Then I see new photo in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
      
  @id2078 @staging
  Scenario Outline: I want to exit fullscreen view in landscape (rotations)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe on text input
    And I press Add Picture button
    And I press "Gallery" button
    And I select picture for dialog
    And I press "Confirm" button
    Then I see new photo in the dialog
    And I select last photo in dialog
    And I rotate UI to landscape
    And I tap conversation details button
    And I rotate UI to portrait
    Then I select last photo in dialog
    And I rotate UI to landscape
    And I swipe down on dialog page
    And I rotate UI to portrait
    Then I select last photo in dialog
    And I rotate UI to landscape
    And I tap on center of screen
    And I press "Image Close" button
    Then I rotate UI to portrait
    And I navigate back from dialog page
    And I see Contact list
    
    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @id1504 @regression
  Scenario Outline: Verify you can play/pause media from the Media Bar (SoundCloud)
    Given There are 3 users where <Name> is me
    Given <Name> is connected to <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I tap on contact name <Contact1>
    And I see dialog page
    And Contact <Contact1> send message to user <Name>
    And Contact <Contact1> send message to user <Name>
    And Contact <Contact1> send message to user <Name>
    And Contact <Contact1> send message to user <Name>
    And Contact <Contact1> send message to user <Name>
    And Contact <Contact1> send message to user <Name>
    And Contact <Contact1> send message to user <Name>
    And Contact <Contact1> send message to user <Name>
    And Contact <Contact1> send message to user <Name>
    And Contact <Contact1> send message to user <Name>
    And Contact <Contact1> send message to user <Name>
    And Contact <Contact1> send message to user <Name>
    And Contact <Contact1> send message to user <Name>
    And I tap on text input
    And I type the message "<SoudCloudLink>" and send it
    And I swipe down on dialog page
    And Contact <Contact1> send message to user <Name>
    And I tap Dialog page bottom
    And I press PlayPause media item button
    And I swipe down on dialog page
    And I swipe down on dialog page
    Then I see PAUSE on Mediabar
    And I press PlayPause on Mediabar button
    And Contact <Contact1> send message to user <Name>
    And I tap Dialog page bottom
    And I see PLAY button in Media

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | SoudCloudLink                                              |
      | user1Email | user1Password | user1Name | user2Name | user3Name | https://soundcloud.com/juan_mj_10/led-zeppelin-rock-n-roll |
      
@id2085 @staging
  Scenario Outline: I want to share a picture to wire
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I tap on contact name <Contact>
    And I open the gallery application
    And I share image from Gallery to Wire
    Then I see new photo in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
      
 @id2084 @staging
  Scenario Outline: I want to share a URL on Wire
    Given There is 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    And I tap on contact name <Contact>
    And I share URL from native browser app to Wire with contact <Contact>
    And I see URL in the dialog
    
    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
      
 @id170 @staging
  Scenario Outline: Verify you can send and play youtube link
    Given There are 3 users where <Name> is me
    Given <Name> is connected to <Contact1>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I tap on contact name <Contact1>
    And I see dialog page
    And I tap on text input
    And I type the message "<YoutubeLink>" and send it
    And I press play on youtube container
    Then I am taken out of Wire and into the native browser app

    Examples: 
      | Login      | Password      | Name      | Contact1  | YoutubeLink                                    |
      | user1Email | user1Password | user1Name | user2Name | https://www.youtube.com/watch?v=wTcNtgA6gHs    |
      