Feature: Conversation View

  @id316 @regression
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

  @id318 @regression @rc @rc42
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

#@regression @rc @rc42
  @id1262 @staging
  Scenario Outline: Create group conversation from 1:1
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And I see dialog page
    And I tap conversation details button
    And I see <Contact1> user profile page
    And I press add contact button
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

  @id320 @regression @rc
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

  @id143 @regression @rc
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

  @id146 @regression
  Scenario Outline: Send special chars message to contact
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I see dialog page
    And I set unicode input method
    And I tap on text input
    And I type unicode message "<Message>"
    And I set default input method
    And I send the message
    Then I see my message "<Message>" in the dialog

    Examples: 
      | Name      | Contact   | Message                           |
      | user1Name | user2Name | ÄäÖöÜüß simple message in english |

  @id149 @regression
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

  @id147 @regression
  Scenario Outline: Send double byte chars
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I see dialog page
    And I set unicode input method
    And I tap on text input
    And I type unicode message "<Message>"
    And I set default input method
    And I send the message
    Then I see my message "<Message>" in the dialog

    Examples: 
      | Name      | Contact   | Message                     |
      | user1Name | user2Name | 畑 はたけ hatake field of crops |

  @id163 @regression
  Scenario Outline: Send existing image from gallery (portrait) in 1:1 chat
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

  @id162 @regression @rc @rc42
  Scenario Outline: Send existing image from gallery (landscape) in 1:1 chat
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

  @id1504 @regression @rc @rc42
  Scenario Outline: Verify you can play/pause media from the Media Bar (SoundCloud)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And I see dialog page
    And Contact <Contact1> send 18 messages to user Myself
    And I tap on text input
    And I type the message "<SoudCloudLink>" and send it
    And I swipe down on dialog page
    And Contact <Contact1> send message to user Myself
    And I scroll to the bottom of conversation view
    And I press PlayPause media item button
    And I swipe down on dialog page until Mediabar appears
    Then I see PAUSE on Mediabar
    And I press PlayPause on Mediabar button
    And Contact <Contact1> send message to user Myself
    And I scroll to the bottom of conversation view
    And I see PLAY button in Media

    Examples: 
      | Name      | Contact1  | SoudCloudLink                                              |
      | user1Name | user2Name | https://soundcloud.com/juan_mj_10/led-zeppelin-rock-n-roll |

  @id170 @regression @rc @rc42
  Scenario Outline: Verify you can send youtube link
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
      | Name      | Contact1  | YoutubeLink                                 |
      | user1Name | user2Name | https://www.youtube.com/watch?v=wTcNtgA6gHs |

  @id3242 @regression @rc @rc42
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
    And I select last photo in dialog
    And I verify that my sketch in fullscreen is the same as what I drew

    Examples: 
      | Name      | Contact1  | NumColors |
      | user1Name | user2Name | 6         |

  @id3243 @regression @rc @rc42
  Scenario Outline: I can send sketch on image from gallery
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And I see dialog page
    And I swipe on text input
    And I press Add Picture button
    And I press "Gallery" button
    And I rotate UI to portrait
    And I wait for 1 second
    And I select picture for dialog
    And I press "Sketch Image Paint" button
    And I draw a sketch on image with <NumColors> colors
    And I remember what my sketch looks like
    Then I send my sketch
    And I select last photo in dialog
    And I verify that my sketch in fullscreen is the same as what I drew

    Examples: 
      | Name      | Contact1  | NumColors |
      | user1Name | user2Name | 6         |

  @id3244 @regression
  Scenario Outline: I can send sketch on photo
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And I see dialog page
    And I swipe on text input
    And I press Add Picture button
    And I press "Take Photo" button
    And I press "Sketch Image Paint" button
    And I draw a sketch on image with <NumColors> colors
    Then I remember what my sketch looks like
    And I send my sketch
    And I select last photo in dialog
    And I verify that my sketch in fullscreen is the same as what I drew

    Examples: 
      | Name      | Contact1  | NumColors |
      | user1Name | user2Name | 6         |

  @id2990 @regression @rc @rc42
  Scenario Outline: I can send giphy image by typing some massage and clicking GIF button
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I type the message "<Message>"
    And I click on the GIF button
    Then I see giphy preview page
    When I click on the giphy send button
    Then I see dialog page
    And I see new photo in the dialog
    And Last message is <Message> · via giphy.com

    Examples: 
      | Name      | Contact   | Message |
      | user1Name | user2Name | Yo      |

  @id165 @regression @rc
  Scenario Outline: Send GIF format pic
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given Contact <Contact> sends image <GifName> to single user conversation <Name>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I scroll to the bottom of conversation view
    Then I see new picture in the dialog
    And I see the picture in the dialog is animated
    When I select last photo in dialog
    Then I see the picture in the preview is animated

    Examples: 
      | Name      | Contact   | GifName      |
      | user1Name | user2Name | animated.gif |
