Feature: Conversation View

  @C2598 @regression @id3200
  Scenario Outline: Tap the cursor to get to the end of the conversation [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User <Contact> sends 40 encrypted messages to user Myself
    When I tap on contact name <Contact>
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
    And I tap Add Picture button from input tools
    And I press Camera Roll button
    And I choose a picture from camera roll
    And I confirm my choice
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

  @C2629 @regression @id3201
  Scenario Outline: Verify you can see conversation images in fullscreen [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Add Picture button from input tools
    And I press Camera Roll button
    And I choose a picture from camera roll
    And I confirm my choice
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

  @C2661 @regression @id3202
  Scenario Outline: I can send and play inline youtube link [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I post media link <YouTubeLink>
    And I click video container for the first time
    And I wait for 5 seconds
    Then I see video player page is opened

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
    And I click video container for the first time
    And I wait for 5 seconds
    Then I see video player page is opened

    Examples:
      | Name      | Contact   | YouTubeLink                                |
      | user1Name | user2Name | http://www.youtube.com/watch?v=Bb1RhktcugU |

  @C2630 @regression @id3206
  Scenario Outline: Rotate image in fullscreen mode [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Add Picture button from input tools
    And I press Camera Roll button
    And I choose a picture from camera roll
    And I confirm my choice
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
    And I tap Archive action button
    Then I do not see conversation <Contact> in conversations list
    And I open archived conversations
    Then I see conversation <Contact> in conversations list

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2733 @rc @regression @id3192
  Scenario Outline: Verify archiving conversation from ellipsis menu [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I open conversation details
    And I open ellipsis menu
    And I tap Archive action button
    Then I do not see conversation <Contact> in conversations list
    And I open archived conversations
    Then I see conversation <Contact> in conversations list

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2596 @regressionF2601 @id3100
  Scenario Outline: Verify only people icon exists under the plus in pending/left/removed from conversations [PORTRAIT]
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact2>,<Contact3>
    Given Myself has group chat <GroupChatName> with <Contact2>,<Contact3>
    Given I leave group chat <GroupChatName>
    Given Me sent connection request to <Contact1>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    Then I do not see conversation tools buttons
    And I navigate back to conversations list
    When I tap on group chat with name <GroupChatName>
    Then I do not see conversation tools buttons

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
    Then I do not see conversation tools buttons
    When I tap on group chat with name <GroupChatName>
    Then I do not see conversation tools buttons

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
    Then I see the default message in input field

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2583 @regression @id2393
  Scenario Outline: Verify possibility to copy image in the conversation view [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User <Contact> sends encrypted image <Picture> to single user conversation <Name>
    When I tap on contact name <Contact>
    And I see 1 photo in the dialog
    # Wait for polka dots to disappear
    And I wait for 7 seconds
    And I long tap on image in the conversation
    And I tap on Copy badge item
    And I tap on text input
    And I tap and hold on message input
    And I click on popup Paste item
    And I confirm my choice
    Then I see 2 photos in the dialog

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

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
    # Wait for polka dots to disappear
    And I wait for 7 seconds
    And I long tap on image in the conversation
    And I tap on Copy badge item
    And I tap on text input
    And I tap and hold on message input
    And I click on popup Paste item
    And I confirm my choice
    Then I see 2 photos in the dialog

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @C2549 @regression @id3965 @ZIOS-5063
  Scenario Outline: Verify posting in a 1-to-1 conversation without content [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User Myself sends 1 encrypted message to user <Contact1>
    When I swipe right on a <Contact1>
    And I tap Delete action button
    And I confirm delete conversation content
    Then I do not see conversation <GroupChatName> in conversations list
    And I open search UI
    And I input in People picker search field conversation name <Contact1>
    And I tap on conversation <Contact1> in search result
    And I tap Open conversation action button on People picker page
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
    And I tap Add Picture button from input tools
    And I press Camera Roll button
    And I choose a picture from camera roll
    And I confirm my choice
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
    And I see 1 photo in the dialog
    And I tap and hold image to open full screen
    And I see Full Screen Page opened
    And I see download button shown on fullscreen page
    And I tap download button on fullscreen page
    And I tap close fullscreen page button
    And I tap Add Picture button from input tools
    And I press Camera Roll button
    And I choose a picture from camera roll
    When I confirm my choice
    Then I see 2 photos in the dialog

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

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

  @C2603 @regression @id4087
  Scenario Outline: Verify people icon is changed on avatar with opening keyboard and back [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    Then I do not see user avatar icon near the conversation input field
    When I tap on text input
    Then I see user avatar icon near the conversation input field
    # This is to hide keyboard
    When I tap on contact name <Contact1>
    And I tap on contact name <Contact>
    Then I do not see user avatar icon near the conversation input field

    Examples:
      | Name      | Contact   | Contact1  |
      | user1Name | user2Name | user3Name |

  @C2624 @regression @rc
  Scenario Outline: Verify sending GIF format pic [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User <Contact> sends encrypted image <GifPicture> to single user conversation Myself
    When I tap on contact name <Contact>
    Then I see 1 photo in the dialog
    And I see the picture in the conversation view is animated
    When I tap and hold image to open full screen
    Then I see the picture on image fullscreen page is animated

    Examples:
      | Name      | Contact   | GifPicture   |
      | user1Name | user2Name | animated.gif |

  @C2592 @regression
  Scenario Outline: Verify cursor tooltip is shown
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    Then I see input placeholder text
    When I tap on text input
    Then I see input placeholder text
    When I type the default message
    Then I do not see input placeholder text
    When I send the message
    Then I see input placeholder text

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |