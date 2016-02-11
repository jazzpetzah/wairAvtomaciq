Feature: Conversation View

  @C2588 @regression @id2380
  Scenario Outline: Tap the cursor to get to the end of the conversation [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User <Contact> sends 50 encrypted messages to user Myself
    When I tap on contact name <Contact>
    And I see plus button is not shown
    And I tap on text input to scroll to the end
    Then I see conversation is scrolled to the end

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2598 @regression @id3200
  Scenario Outline: Tap the cursor to get to the end of the conversation [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User <Contact> sends 40 encrypted messages to user Myself
    When I tap on contact name <Contact>
    And I see plus button is not shown
    And I tap on text input to scroll to the end
    Then I see conversation is scrolled to the end

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2625 @regression @id2417
  Scenario Outline: Verify you can see conversation images in fullscreen [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
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
    Then I see 2 photos in the dialog

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2629 @regression @id3201
  Scenario Outline: Verify you can see conversation images in fullscreen [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
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
    Then I see 2 photos in the dialog

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2661 @regression @id3202
  Scenario Outline: I can send and play inline youtube link [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I post media link <YouTubeLink>
    And I navigate back to conversations list
    And I tap on contact name <Contact>
    Then I see youtube link <YouTubeLink> and media in dialog
    And I click video container for the first time
    And I see video player page is opened

    Examples:
      | Name      | Contact   | YouTubeLink                                |
      | user1Name | user2Name | http://www.youtube.com/watch?v=Bb1RhktcugU |

  @C2662 @regression @id3203
  Scenario Outline: I can send and play inline youtube link [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I post media link <YouTubeLink>
    Then I see youtube link <YouTubeLink> and media in dialog
    And I click video container for the first time
    And I see video player page is opened

    Examples:
      | Name      | Contact   | YouTubeLink                                |
      | user1Name | user2Name | http://www.youtube.com/watch?v=Bb1RhktcugU |

  @C2579 @staging @id3204
  Scenario Outline: Verify appearance of title bar for conversation, restored from background [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see conversations list
    When I tap on contact name <Contact>
    And I close the app for <CloseAppTime> seconds
    Then I see title bar in conversation name <Contact>

    Examples:
      | Name      | Contact   | CloseAppTime |
      | user1Name | user2Name | 2            |

  @C2580 @staging @id3205
  Scenario Outline: Verify appearance of title bar for conversation, restored from background [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I close the app for <CloseAppTime> seconds
    Then I see title bar in conversation name <Contact>

    Examples:
      | Name      | Contact   | CloseAppTime |
      | user1Name | user2Name | 2            |

  @C2626 @regression @id2418
  Scenario Outline: Rotate image in fullscreen mode [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    Given I see conversations list
    And I tap on contact name <Contact>
    And I click plus button next to text input
    And I press Add Picture button
    And I press Camera Roll button
    And I choose a picture from camera roll
    And I press Confirm button
    And I see 1 photo in the dialog
    And I tap and hold image to open full screen
    And I see Full Screen Page opened
    When I rotate UI to landscape
    Then I see Full Screen Page opened

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2630 @regression @id3206
  Scenario Outline: Rotate image in fullscreen mode [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
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
    And I rotate UI to portrait
    Then I see Full Screen Page opened

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2729 @regression @id2451
  Scenario Outline: Verify archiving conversation from ellipsis menu [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I open conversation details
    And I open ellipsis menu
    And I click archive menu button
    Then I dont see conversation <Contact> in contact list
    And I open archived conversations on iPad
    Then I see user <Contact> in contact list

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2733 @regression @id3192
  Scenario Outline: Verify archiving conversation from ellipsis menu [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I open conversation details
    And I open ellipsis menu
    And I click archive menu button
    Then I dont see conversation <Contact> in contact list
    And I open archived conversations on iPad
    Then I see user <Contact> in contact list

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2594 @regression @rc @id3097 @id3098
  Scenario Outline: Verify opening and closing the cursor by clicking swiping right/left [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I click plus button next to text input
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

  @C2599 @C2600 @regression @id3237 @id3238
  Scenario Outline: Verify opening and closing the cursor by clicking swiping right/left [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I click plus button next to text input
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

  @C2596 @regression @rc @id3100
  Scenario Outline: Verify only people icon exists under the plus in pending/left/removed from conversations [PORTRAIT]
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact2>,<Contact3>
    Given Myself has group chat <GroupChatName> with <Contact2>,<Contact3>
    Given I leave group chat <GroupChatName>
    Given Me sent connection request to <Contact1>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I see plus button next to text input
    And I click plus button next to text input
    Then I see only Details button. Call, Camera, Sketch, Ping are not shown
    And I click Close input options button
    And I navigate back to conversations list
    When I tap on group chat with name <GroupChatName>
    And I click plus button next to text input
    Then I see only Details button. Call, Camera, Sketch, Ping are not shown

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName    |
      | user1Name | user2Name | user3Name | user4Name | ArchiveGroupChat |

  @C2601 @regression @id3267
  Scenario Outline: Verify only people icon exists under the plus in pending/left/removed from conversations [LANDSCAPE]
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact2>,<Contact3>
    Given Myself has group chat <GroupChatName> with <Contact2>,<Contact3>
    Given I leave group chat <GroupChatName>
    Given Me sent connection request to <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
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

  @C2564 @regression @id3306
  Scenario Outline: Verify player is displayed for vimeo links with video IDs [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User Myself sends encrypted message "<VimeoLink>" to user <Contact1>
    When I tap on contact name <Contact1>
    Then I see vimeo link <VimeoLink> and media in dialog

    Examples:
      | Name      | Contact1  | VimeoLink                   |
      | user1Name | user2Name | https://vimeo.com/129426512 |

  @C2565 @regression @id3307
  Scenario Outline: Verify player is displayed for vimeo links with video IDs [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User Myself sends encrypted message "<VimeoLink>" to user <Contact1>
    When I tap on contact name <Contact1>
    Then I see vimeo link <VimeoLink> and media in dialog

    Examples:
      | Name      | Contact1  | VimeoLink                   |
      | user1Name | user2Name | https://vimeo.com/129426512 |

  @C2566 @regression @id3308
  Scenario Outline: Verify player isn't displayed for vimeo links without video IDs [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User Myself sends encrypted message "<VimeoLink>" to user <Contact>
    When I tap on contact name <Contact>
    Then I see vimeo link <VimeoLink> but NO media player

    Examples:
      | Name      | Contact   | VimeoLink                    |
      | user1Name | user2Name | https://vimeo.com/categories |

  @C2567 @regression @id3309
  Scenario Outline: Verify player isn't displayed for vimeo links without video IDs [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User Myself sends encrypted message "<VimeoLink>" to user <Contact>
    When I tap on contact name <Contact>
    Then I see vimeo link <VimeoLink> but NO media player

    Examples:
      | Name      | Contact   | VimeoLink                    |
      | user1Name | user2Name | https://vimeo.com/categories |

  @C2665 @regression @id3792
  Scenario Outline: Verify sending link and text in one message and opening the link [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User Myself sends encrypted message "<MessageAndLink>" to user <Contact1>
    When I tap on contact name <Contact1>
    And I tap on message "<MessageAndLink>"
    Then I see WireWebsitePage

    Examples:
      | Name      | Contact1  | MessageAndLink                                |
      | user1Name | user2Name | https://www.wire.com/ is the best of the best |

  @C2666 @regression @id3793
  Scenario Outline: Verify sending link and text in one message and opening the link [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User Myself sends encrypted message "<MessageAndLink>" to user <Contact1>
    When I tap on contact name <Contact1>
    And I tap on message "<MessageAndLink>"
    Then I see WireWebsitePage

    Examples:
      | Name      | Contact1  | MessageAndLink                                |
      | user1Name | user2Name | https://www.wire.com/ is the best of the best |

  @C2663 @regression @id3790
  Scenario Outline: Verify sending link and opening it [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User Myself sends encrypted message "<Link>" to user <Contact1>
    When I tap on contact name <Contact1>
    And I tap on message "<Link>"
    Then I see WireWebsitePage

    Examples:
      | Name      | Contact1  | Link                  |
      | user1Name | user2Name | https://www.wire.com/ |

  @C2664 @regression @id3791
  Scenario Outline: Verify sending link and opening it [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User Myself sends encrypted message "<Link>" to user <Contact1>
    When I tap on contact name <Contact1>
    And I tap on link <Link>
    Then I see WireWebsitePage

    Examples:
      | Name      | Contact1  | Link                  |
      | user1Name | user2Name | https://www.wire.com/ |

  @C2667 @regression @id3799
  Scenario Outline: Verify input field and action buttons are not shown simultaniously [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>, <Contact2>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I type the default message
    And I navigate back to conversations list
    When I tap on contact name <Contact2>
    And I navigate back to conversations list
    And I tap on contact name <Contact1>
    Then I see Close input options button is not visible
    And I see controller buttons can not be visible
    And I see the default message in input field

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2668 @regression @id3800
  Scenario Outline: Verify input field and action buttons are not shown simultaniously [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>, <Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I type the default message
    When I tap on contact name <Contact2>
    And I tap on contact name <Contact1>
    Then I see Close input options button is not visible
    And I see controller buttons can not be visible
    And I see the default message in input field

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2583 @regression @id2393
  Scenario Outline: Verify possibility to copy image in the conversation view [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> sends encrypted image <Picture> to <ConversationType> conversation <Name>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I see 1 photo in the dialog
    And I longpress on image in the conversation
    And I tap on copy badge
    And I tap and hold on message input
    And I click on popup Paste item
    And I press Confirm button
    Then I see 2 photos in the dialog

    Examples:
      | Name      | Contact   | Picture     | ConversationType |
      | user1Name | user2Name | testing.jpg | single user      |

  @C2587 @regression @id4008
  Scenario Outline: Verify possibility to copy image in the conversation view [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User <Contact> sends encrypted image <Picture> to single user conversation <Name>
    And I tap on contact name <Contact>
    And I see 1 photo in the dialog
    And I longpress on image in the conversation
    And I tap on copy badge
    And I tap on text input
    And I tap and hold on message input
    And I click on popup Paste item
    And I press Confirm button
    Then I see 2 photos in the dialog

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @C2548 @regression @id3964 @ZIOS-5063
  Scenario Outline: Verify posting in a 1-to-1 conversation without content [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User Myself sends 1 encrypted message to user <Contact1>
    When I swipe right on a <Contact1>
    And I click delete menu button
    And I confirm delete conversation content
    Then I dont see conversation <GroupChatName> in contact list
    And I open search by taping on it
    And I tap on Search input on People picker page
    And I search for user name <Contact1> and tap on it on People picker page
    And I click open conversation button on People picker page
    When I type the default message and send it
    Then I see 1 default message in the dialog

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2549 @regression @id3965 @ZIOS-5063
  Scenario Outline: Verify posting in a 1-to-1 conversation without content [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User Myself sends 1 encrypted message to user <Contact1>
    When I swipe right on a <Contact1>
    And I click delete menu button
    And I confirm delete conversation content
    Then I dont see conversation <GroupChatName> in contact list
    And I open search by taping on it
    And I tap on Search input on People picker page
    And I search for user name <Contact1> and tap on it on People picker page
    And I click open conversation button on People picker page
    When I type the default message and send it
    Then I see 1 default message in the dialog

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2617 @regression @id2409
  Scenario Outline: Verify downloading images in fullscreen [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User <Contact> sends encrypted image <Picture> to single user conversation <Name>
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

  @C2631 @regression @id4084
  Scenario Outline: Verify downloading images in fullscreen [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User <Contact> sends encrypted image <Picture> to single user conversation <Name>
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

  @C2448 @regression @id2334
  Scenario Outline: Verify you still receive messages from blocked person in a group chat [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>, <Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>, <Contact2>
    Given User <Name> blocks user <Contact1>
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User <Contact1> sends 1 encrypted message to group conversation <GroupChatName>
    Given User <Contact1> sends image <Picture> to group conversation <GroupChatName>
    When I tap on group chat with name <GroupChatName>
    Then I see 3 conversation entries

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Picture     |
      | user1Name | user2Name | user3Name | Caramba!      | testing.jpg |

  @C2460 @regression @id4085
  Scenario Outline: Verify you still receive messages from blocked person in a group chat [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>, <Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>, <Contact2>
    Given User <Name> blocks user <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User <Contact1> sends 1 encrypted message to group conversation <GroupChatName>
    Given User <Contact1> sends encrypted image <Picture> to group conversation <GroupChatName>
    When I tap on group chat with name <GroupChatName>
    Then I see 3 conversation entries

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Picture     |
      | user1Name | user2Name | user3Name | Caramba!      | testing.jpg |

  @C2589 @staging @id2381
  Scenario Outline: Verify cursor swiping is disabled when you scroll back into a conversation [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User Myself sends 40 encrypted messages to user <Contact>
    Given User <Contact> sends encrypted image <Picture> to single user conversation Myself
    When I tap on contact name <Contact>
    And I tap on text input to scroll to the end
    And I scroll to the beginning of the conversation
    And I click plus button next to text input
    Then I see controller buttons can not be visible

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @C2602 @staging @id4086
  Scenario Outline: Verify cursor swiping is disabled when you scroll back into a conversation [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User Myself sends 40 encrypted messages to user <Contact>
    Given User <Contact> sends encrypted image <Picture> to single user conversation Myself
    When I tap on contact name <Contact>
    And I tap on text input to scroll to the end
    And I scroll to the beginning of the conversation
    And I click plus button next to text input
    Then I see controller buttons can not be visible

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @C2591 @regression @id2383
  Scenario Outline: Verify people icon is changed on avatar with opening keyboard and back [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
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

  @C2603 @regression @id4087
  Scenario Outline: Verify people icon is changed on avatar with opening keyboard and back [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
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