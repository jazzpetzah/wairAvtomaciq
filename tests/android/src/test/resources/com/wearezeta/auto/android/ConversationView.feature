Feature: Conversation View

  @id316 @smoke
  Scenario Outline: Send Message to contact
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I type the message "<Message>" and send it
    Then I see my message "<Message>" in the dialog

    Examples: 
      | Name      | Contact   | Message |
      | user1Name | user2Name | Yo      |

  @id317 @smoke
  Scenario Outline: Send Hello and Hey to contact
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe on text input
    And I press Ping button
    Then I see Ping message <Msg> in the dialog

    Examples: 
      | Name      | Contact   | Msg        |
      | user1Name | user2Name | YOU PINGED |

  @id318 @smoke
  Scenario Outline: Send Camera picture to contact
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe on text input
    And I press Add Picture button
    And I press "Take Photo" button
    And I press "Confirm" button
    Then I see new photo in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @id1262 @smoke
  Scenario Outline: Add people to 1:1 chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And I see dialog page
    And I tap conversation details button
    And I see <Contact1> user profile page
    And I press add contact button
    And I see People picker page
    And I tap on Search input on People picker page
    And I enter "<Contact2>" into Search input on People Picker page
    And I see user <Contact2> found on People picker page
    And I tap on user name found on People picker page <Contact2>
    And I see Add to conversation button
    And I click on Add to conversation button
    Then I see group chat page with users <Contact1>,<Contact2>
    And I navigate back from dialog page
    And I see <Contact1> and <Contact2> chat in contact list

    Examples: 
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @id320 @smoke
  Scenario Outline: Send message to group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And I see dialog page
    And I tap on text input
    And I type the message "<Message>" and send it
    Then I see my message "<Message>" in the dialog

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName     | Message |
      | user1Name | user2Name | user3Name | SendMessGroupChat | Yo      |

  @id143 @regression
  Scenario Outline: Send Long Message to contact
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I type the message "LONG_MESSAGE" and send it
    Then I see my message "LONG_MESSAGE" in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @id145 @regression
  Scenario Outline: Send Upper and Lower case to contact
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I type the message "<Message>" and send it
    Then I see my message "<Message>" in the dialog

    Examples: 
      | Name      | Contact   | Message  |
      | user1Name | user2Name | aaaaAAAA |

  @id146 @unicode @regression
  Scenario Outline: Send special chars message to contact
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I type the message "<Message>" and send it
    Then I see my message "<Message>" in the dialog

    Examples: 
      | Name      | Contact   | Message                           |
      | user1Name | user2Name | ÄäÖöÜüß simple message in english |

  @mute @regression @id149
  Scenario Outline: Send emoji message to contact
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I type the message "<Message>" and send it
    Then I see my message "<Message>" in the dialog

    Examples: 
      | Name      | Contact   | Message  |
      | user1Name | user2Name | :) ;) :( |

  @id147 @unicode @regression
  Scenario Outline: Send double byte chars
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I type the message "<Message>" and send it
    Then I see my message "<Message>" in the dialog

    Examples: 
      | Name      | Contact   | Message                     |
      | user1Name | user2Name | 畑 はたけ hatake field of crops |
      
  @id163 @regression
  Scenario Outline: Send image using existing camera rolls (portrait) in 1:1 chat
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
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
      | Name      | Contact   |
      | user1Name | user2Name |

  @id162 @regression
  Scenario Outline: Send image using existing camera rolls (landscape) in 1:1 chat
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
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
      | Name      | Contact   |
      | user1Name | user2Name |

  @id2078 @regression
  Scenario Outline: I want to exit fullscreen view in landscape (rotations)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
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
      | Name      | Contact   |
      | user1Name | user2Name |

  @id1504 @regression
  Scenario Outline: Verify you can play/pause media from the Media Bar (SoundCloud)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And I see dialog page
    And Contact <Contact1> send message to user Myself
    And Contact <Contact1> send message to user Myself
    And Contact <Contact1> send message to user Myself
    And Contact <Contact1> send message to user Myself
    And Contact <Contact1> send message to user Myself
    And Contact <Contact1> send message to user Myself
    And Contact <Contact1> send message to user Myself
    And Contact <Contact1> send message to user Myself
    And Contact <Contact1> send message to user Myself
    And Contact <Contact1> send message to user Myself
    And Contact <Contact1> send message to user Myself
    And Contact <Contact1> send message to user Myself
    And Contact <Contact1> send message to user Myself
    And I tap on text input
    And I type the message "<SoudCloudLink>" and send it
    And I swipe down on dialog page
    And Contact <Contact1> send message to user Myself
    And I tap Dialog page bottom
    And I press PlayPause media item button
    And I swipe down on dialog page
    And I swipe down on dialog page
    Then I see PAUSE on Mediabar
    And I press PlayPause on Mediabar button
    And Contact <Contact1> send message to user Myself
    And I tap Dialog page bottom
    And I see PLAY button in Media

    Examples: 
      | Name      | Contact1  | SoudCloudLink                                              |
      | user1Name | user2Name | https://soundcloud.com/juan_mj_10/led-zeppelin-rock-n-roll |

  @id170 @regression
  Scenario Outline: Verify you can send and play youtube link
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And I see dialog page
    And I tap on text input
    And I type the message "<YoutubeLink>" and send it
    Then I see Play button on Youtube container

    Examples: 
      | Name      | Contact1  | YoutubeLink                                    |
      | user1Name | user2Name | https://www.youtube.com/watch?v=wTcNtgA6gHs    |
      
     
  @id2814 @verification
  Scenario Outline: I can send a sketch
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And I see dialog page
    And I swipe on text input
    And I press Sketch button
    And I draw a sketch with <NumColors> colors
    When I remember what my sketch looks like
    And I send my sketch
    Then I verify that my sketch is the same as what I drew

    Examples: 
      | Name      | Contact1  | NumColors |
      | user1Name | user2Name | 6			|
      
      