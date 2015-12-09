Feature: Conversation View

  @regression @id2380
  Scenario Outline: Tap the cursor to get to the end of the conversation [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I send long message
    And I type the message and send it
    And I scroll to the beginning of the conversation
    And I see plus button is not shown
    And I tap on text input
    Then I see conversation is scrolled to the end
    And I see message in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id3200
  Scenario Outline: Tap the cursor to get to the end of the conversation [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I send long message
    And I type the message and send it
    And I scroll to the beginning of the conversation
    And I see plus button is not shown
    And I tap on text input
    Then I see conversation is scrolled to the end
    And I see message in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id2417
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

  @regression @id3201
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

  @regression @id3202
  Scenario Outline: I can send and play inline youtube link [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I post media link <YouTubeLink>
    And I return to the chat list
    And I tap on contact name <Contact>
    Then I see youtube link <YouTubeLink> and media in dialog
    And I click video container for the first time
    And I see video player page is opened

    Examples: 
      | Name      | Contact   | YouTubeLink                                |
      | user1Name | user2Name | http://www.youtube.com/watch?v=Bb1RhktcugU |

  @regression @id3203
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

  @regression @id2418
  Scenario Outline: Rotate image in fullscreen mode [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given User <Contact> change name to <NewName>
    Given User <Contact> change accent color to <Color>
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
    And I rotate UI to landscape
    Then I see image rotated in fullscreen mode

    Examples: 
      | Name      | Contact   | Picture     | Color        | NewName          |
      | user1Name | user2Name | testing.jpg | BrightOrange | RotateFullscreen |

  @regression @id3206
  Scenario Outline: Rotate image in fullscreen mode [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given User <Contact> change name to <NewName>
    Given User <Contact> change accent color to <Color>
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
    And I rotate UI to portrait
    Then I see image rotated to portrait in fullscreen mode

    Examples: 
      | Name      | Contact   | Picture     | Color        | NewName          |
      | user1Name | user2Name | testing.jpg | BrightOrange | RotateFullscreen |

  @regression @id2451
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
    Then I dont see conversation <Contact> in contact list
    And I open archived conversations on iPad
    Then I see user <Contact> in contact list

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id3192
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

  @regression @rc @id3097 @id3098
  Scenario Outline: Verify opening and closing the cursor by clicking swiping right/left [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I Sign in on tablet using my email
    Given I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I see plus button next to text input
    And I swipe the text input cursor
    Then I see Buttons: Details, Call, Camera, Sketch, Ping
    And I see plus button is not shown
    And I swipe left on options buttons
    And I see Close input options button is not visible
    And I see plus button next to text input
    And I click plus button next to text input
    Then I see Buttons: Details, Call, Camera, Sketch, Ping
    And I click Close input options button
    And I see Close input options button is not visible
    And I see plus button next to text input

    Examples: 
      | Name      | Contact1  |
      | user1Name | user2Name |

  @regression @id3237 @id3238
  Scenario Outline: Verify opening and closing the cursor by clicking swiping right/left [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I see plus button next to text input
    And I swipe the text input cursor
    Then I see Buttons: Details, Call, Camera, Sketch, Ping
    And I see plus button is not shown
    And I swipe left on options buttons
    And I see Close input options button is not visible
    And I see plus button next to text input
    And I click plus button next to text input
    Then I see Buttons: Details, Call, Camera, Sketch, Ping
    And I click Close input options button
    And I see Close input options button is not visible
    And I see plus button next to text input

    Examples: 
      | Name      | Contact1  |
      | user1Name | user2Name |

  @regression @rc @id3100
  Scenario Outline: Verify only people icon exists under the plus in pending/left/removed from conversations [PORTRAIT]
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact2>,<Contact3>
    Given Myself has group chat <GroupChatName> with <Contact2>,<Contact3>
    Given I leave group chat <GroupChatName>
    Given Me sent connection request to <Contact1>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see plus button next to text input
    And I click plus button next to text input
    Then I see only Details button. Call, Camera, Sketch, Ping are not shown
    And I click Close input options button
    And I navigate back to conversations view
    When I tap on group chat with name <GroupChatName>
    And I click plus button next to text input
    Then I see only Details button. Call, Camera, Sketch, Ping are not shown

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName    |
      | user1Name | user2Name | user3Name | user4Name | ArchiveGroupChat |

  @regression @id3267
  Scenario Outline: Verify only people icon exists under the plus in pending/left/removed from conversations [LANDSCAPE]
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact2>,<Contact3>
    Given Myself has group chat <GroupChatName> with <Contact2>,<Contact3>
    Given I leave group chat <GroupChatName>
    Given Me sent connection request to <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see plus button next to text input
    And I click plus button next to text input
    Then I see only Details button. Call, Camera, Sketch, Ping are not shown
    And I click Close input options button
    When I tap on group chat with name <GroupChatName>
    And I click plus button next to text input
    Then I see only Details button. Call, Camera, Sketch, Ping are not shown

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName    |
      | user1Name | user2Name | user3Name | user4Name | ArchiveGroupChat |

  @regression @id3306
  Scenario Outline: Verify player is displayed for vimeo links with video IDs [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User <Name> sent message <VimeoLink> to conversation <Contact1>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    Then I see vimeo link <VimeoLink> and media in dialog

    Examples: 
      | Name      | Contact1  | VimeoLink                   |
      | user1Name | user2Name | https://vimeo.com/129426512 |

  @regression @id3307
  Scenario Outline: Verify player is displayed for vimeo links with video IDs [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User <Name> sent message <VimeoLink> to conversation <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    Then I see vimeo link <VimeoLink> and media in dialog

    Examples: 
      | Name      | Contact1  | VimeoLink                   |
      | user1Name | user2Name | https://vimeo.com/129426512 |

  @regression @id3308
  Scenario Outline: Verify player isn't displayed for vimeo links without video IDs [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Name> sent message <VimeoLink> to conversation <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    Then I see vimeo link <VimeoLink> but NO media player

    Examples: 
      | Name      | Contact   | VimeoLink                    |
      | user1Name | user2Name | https://vimeo.com/categories |

  @regression @id3309
  Scenario Outline: Verify player isn't displayed for vimeo links without video IDs [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Name> sent message <VimeoLink> to conversation <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    Then I see vimeo link <VimeoLink> but NO media player

    Examples: 
      | Name      | Contact   | VimeoLink                    |
      | user1Name | user2Name | https://vimeo.com/categories |

  @regression @id3792
  Scenario Outline: Verify sending link and text in one message and opening the link [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User <Name> sent message <MessageAndLink> to conversation <Contact1>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I see Link <MessageAndLink> in dialog
    And I tap on Link with a message
    Then I see WireWebsitePage

    Examples: 
      | Name      | Contact1  | MessageAndLink                  |
      | user1Name | user2Name | Check https://www.wire.com/ out |

  @regression @id3793
  Scenario Outline: Verify sending link and text in one message and opening the link [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User <Name> sent message <MessageAndLink> to conversation <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I see Link <MessageAndLink> in dialog
    And I tap on Link with a message
    Then I see WireWebsitePage

    Examples: 
      | Name      | Contact1  | MessageAndLink                  |
      | user1Name | user2Name | Check https://www.wire.com/ out |

  @regression @id3790
  Scenario Outline: Verify sending link and opening it [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User <Name> sent message <Link> to conversation <Contact1>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I see Link <Link> in dialog
    And I tap on Link
    Then I see WireWebsitePage

    Examples: 
      | Name      | Contact1  | Link                  |
      | user1Name | user2Name | https://www.wire.com/ |

  @regression @id3791
  Scenario Outline: Verify sending link and opening it [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User <Name> sent message <Link> to conversation <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I see Link <Link> in dialog
    And I tap on Link
    Then I see WireWebsitePage

    Examples: 
      | Name      | Contact1  | Link                  |
      | user1Name | user2Name | https://www.wire.com/ |
      
  @regression @id3799
  Scenario Outline: Verify input field and action buttons are not shown simultaniously [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>, <Contact2>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I type the message
    And I return to the chat list
    When I tap on contact name <Contact2>
    And I see dialog page
    And I return to the chat list
    And I tap on contact name <Contact1>
	And I see dialog page
	Then I see Close input options button is not visible
	And I see controller buttons can not be visible
	And I see the message in input field

    Examples: 
      | Name      | Contact1   | Contact2  |
      | user1Name | user2Name  | user3Name |

  @regression @id3800
  Scenario Outline: Verify input field and action buttons are not shown simultaniously [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>, <Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I type the message
    When I tap on contact name <Contact2>
    And I see dialog page
    And I tap on contact name <Contact1>
    And I see dialog page
    Then I see Close input options button is not visible
    And I see controller buttons can not be visible
    And I see the message in input field

    Examples: 
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @regression @id2393
  Scenario Outline: Verify possibility to copy image in the conversation view [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given Contact <Contact> sends image <Picture> to <ConversationType> conversation <Name>
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I tap on contact name <Contact>
    And I see dialog page
    And I see new photo in the dialog
    And I longpress on image in the conversation
    And I tap on copy badge
    And I tap and hold on message input
    And I click on popup Paste item
    And I press Confirm button
    Then I see new photo in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   | Picture     | ConversationType |
      | user1Email | user1Password | user1Name | user2Name | testing.jpg | single user      |

  @regression @id4008
  Scenario Outline: Verify possibility to copy image in the conversation view [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given Contact <Contact> sends image <Picture> to <ConversationType> conversation <Name>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I tap on contact name <Contact>
    And I see dialog page
    And I see new photo in the dialog
    And I longpress on image in the conversation
    And I tap on copy badge
    And I tap on text input
    And I tap and hold on message input
    And I click on popup Paste item
    And I press Confirm button
    Then I see new photo in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   | Picture     | ConversationType |
      | user1Email | user1Password | user1Name | user2Name | testing.jpg | single user      |

  @regression @id3964
  Scenario Outline: Verify posting in a 1-to-1 conversation without content [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User <Name> sent message <Message> to conversation <Contact1>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I swipe right on a <Contact1>
    And I click delete menu button
    And I confirm delete conversation content
    Then I dont see conversation <GroupChatName> in contact list
    And I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I search for user name <Contact1> and tap on it on People picker page
    And I click open conversation button on People picker page
    Then I see dialog page
    Then I see the only message in dialog is system message CONNECTED TO <Contact1>
    And I type the message
    And I send the message
    And I see message in the dialog

    Examples: 
      | Name      | Contact1  | Message |
      | user1Name | user2Name | testing |

  @regression @id3965
  Scenario Outline: Verify posting in a 1-to-1 conversation without content [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User <Name> sent message <Message> to conversation <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I swipe right on a <Contact1>
    And I click delete menu button
    And I confirm delete conversation content
    Then I dont see conversation <GroupChatName> in contact list
    And I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I search for user name <Contact1> and tap on it on People picker page
    And I click open conversation button on People picker page
    Then I see dialog page
    Then I see the only message in dialog is system message CONNECTED TO <Contact1>
    And I type the message
    And I send the message
    And I see message in the dialog

    Examples: 
      | Name      | Contact1  | Message |
      | user1Name | user2Name | testing |

  @regression @id2409
  Scenario Outline: Verify downloading images in fullscreen [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given Contact <Contact> sends image <Picture> to <ConversationType> conversation <Name>
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I tap on contact name <Contact>
    And I see dialog page
    And I see new photo in the dialog
    And I tap and hold image to open full screen
    And I see Full Screen Page opened
    And I see download button shown on fullscreen page
    And I tap download button on fullscreen page
    And I tap close fullscreen page button
    And I swipe the text input cursor
    And I press Add Picture button
    And I press Camera Roll button
    And I choose last picture from camera roll
    And I press Confirm button
    Then I verify image in dialog is same as template <Picture>

    Examples: 
      | Login      | Password      | Name      | Contact   | Picture     | ConversationType |
      | user1Email | user1Password | user1Name | user2Name | testing.jpg | single user      |

  @regression @id4084
  Scenario Outline: Verify downloading images in fullscreen [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given Contact <Contact> sends image <Picture> to <ConversationType> conversation <Name>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I tap on contact name <Contact>
    And I see dialog page
    And I see new photo in the dialog
    And I tap and hold image to open full screen
    And I see Full Screen Page opened
    And I see download button shown on fullscreen page
    And I tap download button on fullscreen page
    And I tap close fullscreen page button
    And I swipe the text input cursor
    And I press Add Picture button
    And I press Camera Roll button
    And I choose last picture from camera roll
    And I press Confirm button
    And I scroll to the end of the conversation
    Then I verify image in dialog is same as template <Picture>

    Examples: 
      | Login      | Password      | Name      | Contact   | Picture     | ConversationType |
      | user1Email | user1Password | user1Name | user2Name | testing.jpg | single user      |

  @regression @id2334
  Scenario Outline: Verify you still receive messages from blocked person in a group chat [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>, <Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>, <Contact2>
    Given User <Name> blocks user <Contact1>
    Given User <Contact1> sent message <Message> to conversation <GroupChatName>
    Given Contact <Contact1> sends image <Picture> to <ConversationType> conversation <GroupChatName>
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I tap on group chat with name <GroupChatName>
    Then I see only 3 messages

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | GroupChatName | Message                | Picture     | ConversationType |
      | user1Email | user1Password | user1Name | user2Name | user3Name | Caramba!      | He-hey, do you see it? | testing.jpg | group            |

  @regression @id4085
  Scenario Outline: Verify you still receive messages from blocked person in a group chat [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>, <Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>, <Contact2>
    Given User <Name> blocks user <Contact1>
    Given User <Contact1> sent message <Message> to conversation <GroupChatName>
    Given Contact <Contact1> sends image <Picture> to <ConversationType> conversation <GroupChatName>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I tap on group chat with name <GroupChatName>
    Then I see only 3 messages

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | GroupChatName | Message                | Picture     | ConversationType |
      | user1Email | user1Password | user1Name | user2Name | user3Name | Caramba!      | He-hey, do you see it? | testing.jpg | group            |

  @staging @id2381
  Scenario Outline: Verify cursor swiping is disabled when you scroll back into a conversation [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given Contact <Contact> sends image <Picture> to <ConversationType> conversation <Name>
    Given User <Name> sent long message to conversation <Contact>
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I tap on contact name <Contact>
    And I tap on text input
    And I scroll to the beginning of the conversation
    And I swipe the text input cursor
    Then I see controller buttons can not be visible

    Examples: 
      | Login      | Password      | Name      | Contact   | Picture     | ConversationType |
      | user1Email | user1Password | user1Name | user2Name | testing.jpg | single user      |

  @staging @id4086
  Scenario Outline: Verify cursor swiping is disabled when you scroll back into a conversation [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given Contact <Contact> sends image <Picture> to <ConversationType> conversation <Name>
    Given User <Name> sent long message to conversation <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I tap on contact name <Contact>
    And I tap on text input
    And I scroll to the beginning of the conversation
    And I swipe the text input cursor
    Then I see controller buttons can not be visible

    Examples: 
      | Login      | Password      | Name      | Contact   | Picture     | ConversationType |
      | user1Email | user1Password | user1Name | user2Name | testing.jpg | single user      |

  @regression @id2383
  Scenario Outline: Verify people icon is changed on avatar with opening keyboard and back [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I see plus button next to text input
    And I fill in message using script
    And I see plus icon is changed to user avatar icon
    And I clear conversation text input
    Then I see plus button next to text input

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id4087
  Scenario Outline: Verify people icon is changed on avatar with opening keyboard and back [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I see plus button next to text input
    And I fill in message using script
    And I see plus icon is changed to user avatar icon
    And I clear conversation text input
    Then I see plus button next to text input

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |
