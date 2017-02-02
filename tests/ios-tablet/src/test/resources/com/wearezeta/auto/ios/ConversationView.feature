Feature: Conversation View

  @C2645 @rc @regression @fastLogin
  Scenario Outline: Verify sending message [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see conversations list
    When I tap on contact name <Contact>
    And I type the default message and send it
    Then I see 1 default message in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2644 @regression @fastLogin
  Scenario Outline: Receive message from contact [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User adds the following device: {"<Contact>": [{}]}
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User <Contact> sends 1 default message to conversation Myself
    When I tap on contact name <Contact>
    Then I see 1 default message in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2621 @regression @fastLogin
  Scenario Outline: Verify sending image [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Add Picture button from input tools
    And I accept alert if visible
    And I accept alert if visible
    And I select the first picture from Keyboard Gallery
    And I tap Confirm button on Picture preview page
    Then I see 1 photo in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2615 @regression @rc @fastLogin
  Scenario Outline: Verify sending image [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see conversations list
    When I tap on contact name <Contact>
    And I tap Add Picture button from input tools
    And I accept alert if visible
    And I accept alert if visible
    And I select the first picture from Keyboard Gallery
    And I tap Confirm button on Picture preview page
    Then I see 1 photo in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2642 @regression @C3222 @fastLogin
  Scenario Outline: Verify you can see Ping on the other side - 1:1 conversation [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given User <Contact1> change name to <ContactName>
    Given Myself is connected to <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I tap on contact name <Contact1>
    Given User <Contact1> pings conversation <Name>
    When I wait for 3 seconds
    Then I see "<ContactName> PINGED" system message in the conversation view

    Examples:
      | Name      | Contact1  | ContactName |
      | user1Name | user2Name | OtherUser   |

  @C2640 @regression @C3224 @fastLogin
  Scenario Outline: Verify you can see Ping on the other side - group conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I tap on group chat with name <GroupChatName>
    When User <Contact1> pings conversation <GroupChatName>
    Then I see "<Contact1> PINGED" system message in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName        |
      | user1Name | user2Name | user3Name | ReceivePingGroupChat |

  @C2627 @regression @fastLogin
  Scenario Outline: Receive a camera roll picture from user from contact list [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User adds the following device: {"<Contact>": [{}]}
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given User <Contact> sends 1 image file <Picture> to conversation Myself
    Given I see conversations list
    When I tap on contact name <Contact>
    Then I see 1 photo in the conversation view

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @C2628 @regression @fastLogin
  Scenario Outline: Receive a camera roll picture from user from contact list [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User adds the following device: {"<Contact>": [{}]}
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given User <Contact> sends 1 image file <Picture> to conversation Myself
    Given I see conversations list
    When I tap on contact name <Contact>
    Then I see 1 photo in the conversation view

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @C2647 @regression @fastLogin
  Scenario Outline: Send Message to contact after navigating away from chat page [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Me is connected to <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I type the default message
    And I tap on contact name <Contact2>
    And I tap on contact name <Contact1>
    And I tap on text input
    And I tap Send Message button in conversation view
    Then I see 1 default message in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2655 @regression
  Scenario Outline: Copy and paste to send the message [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I see sign in screen
    When I switch to Log In tab
    And I have entered login <Text>
    And I tap and hold on Email input
    And I tap on Select All badge item
    And I tap on Copy badge item
    And I have entered login <Login>
    And I have entered password <Password>
    And I tap Login button
    And I accept alert if visible
    And I accept First Time overlay
    And I dismiss settings warning if visible
    And I see conversations list
    And I tap on contact name <Contact>
    And I tap on text input
    And I long tap on text input
    And I tap on Paste badge item
    And I tap Send Message button in conversation view
    Then I see last message in the conversation view is expected message <Text>

    Examples:
      | Login      | Password      | Name      | Contact   | Text       |
      | user1Email | user1Password | user1Name | user2Name | TextToCopy |

  @C2657 @regression @fastLogin
  Scenario Outline: Send a text containing spaces on either end of message [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the "   " message
    Then I do not see Send Message button in conversation view
    When I type the default message
    And I type the "   " message and send it
    Then I see 1 default message in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2676 @regression @fastLogin
  Scenario Outline: I can send a sketch[PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I tap Sketch button from input tools
    And I draw a random sketch
    And I tap Send button on Sketch page
    Then I see 1 photo in the conversation view

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2677 @regression @fastLogin
  Scenario Outline: I can send a sketch[LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I tap Sketch button from input tools
    And I draw a random sketch
    And I tap Send button on Sketch page
    Then I see 1 photo in the conversation view

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2658 @regression @fastLogin
  Scenario Outline: Verify sending ping in 1-to-1 conversation [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see conversations list
    When I tap on contact name <Contact>
    And I tap Ping button from input tools
    Then I see "<PingMsg>" system message in the conversation view

    Examples:
      | Name      | Contact   | PingMsg    |
      | user1Name | user2Name | YOU PINGED |

  @C2660 @regression @fastLogin
  Scenario Outline: Send message to group chat [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I type the default message and send it
    Then I see 1 default message in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | SimpleGroup   |


  @C2598 @regression @fastLogin
  Scenario Outline: Tap the cursor to get to the end of the conversation [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given User <Contact> sends 40 default messages to conversation Myself
    Given I see conversations list
    When I tap on contact name <Contact>
    Then I see conversation is scrolled to the end

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2625 @regression @fastLogin
  Scenario Outline: Verify you can see conversation images in fullscreen [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Add Picture button from input tools
    And I accept alert if visible
    And I accept alert if visible
    And I select the first picture from Keyboard Gallery
    And I tap Confirm button on Picture preview page
    And I see 1 photo in the conversation view
    And I tap on image in conversation view
    And I tap Fullscreen button on image
    And I see Full Screen Page opened
    And I tap close fullscreen page button
    Then I see 1 photo in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2629 @regression @fastLogin
  Scenario Outline: Verify you can see conversation images in fullscreen [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Add Picture button from input tools
    And I accept alert if visible
    And I accept alert if visible
    And I select the first picture from Keyboard Gallery
    And I tap Confirm button on Picture preview page
    And I see 1 photo in the conversation view
    And I tap on image in conversation view
    And I tap Fullscreen button on image
    And I see Full Screen Page opened
    And I tap close fullscreen page button
    Then I see 1 photo in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2661 @regression @fastLogin
  Scenario Outline: I can send and play inline youtube link [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the "<YouTubeLink>" message and send it
    And I tap on media container in conversation view
    And I wait for 5 seconds
    Then I see video player page is opened

    Examples:
      | Name      | Contact   | YouTubeLink                                |
      | user1Name | user2Name | http://www.youtube.com/watch?v=Bb1RhktcugU |

  @C2662 @regression @fastLogin
  Scenario Outline: I can send and play inline youtube link [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the "<YouTubeLink>" message and send it
    And I tap Hide keyboard button
    And I tap on media container in conversation view
    And I wait for 5 seconds
    Then I see video player page is opened

    Examples:
      | Name      | Contact   | YouTubeLink                                |
      | user1Name | user2Name | http://www.youtube.com/watch?v=Bb1RhktcugU |

  @C2630 @regression @fastLogin
  Scenario Outline: Rotate image in fullscreen mode [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Add Picture button from input tools
    And I accept alert if visible
    And I accept alert if visible
    And I select the first picture from Keyboard Gallery
    And I tap Confirm button on Picture preview page
    And I see 1 photo in the conversation view
    And I tap on image in conversation view
    And I tap Fullscreen button on image
    And I see Full Screen Page opened
    And I rotate UI to portrait
    Then I see Full Screen Page opened

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2729 @regression @fastLogin
  Scenario Outline: Verify archiving conversation from ellipsis menu [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I open conversation details
    And I tap Open Menu button on Single user profile page
    And I tap Archive conversation action button
    Then I do not see conversation <Contact> in conversations list
    And I open archived conversations
    Then I see conversation <Contact> in conversations list

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2733 @rc @regression @fastLogin
  Scenario Outline: Verify archiving conversation from ellipsis menu [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I open conversation details
    And I tap Open Menu button on Single user profile page
    And I tap Archive conversation action button
    Then I do not see conversation <Contact> in conversations list
    And I open archived conversations
    Then I see conversation <Contact> in conversations list

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2596 @regression @fastLogin
  Scenario Outline: Verify only people icon exists under the plus in pending/left/removed from conversations [PORTRAIT]
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact2>,<Contact3>
    Given Myself has group chat <GroupChatName> with <Contact2>,<Contact3>
    Given User Myself leaves group chat <GroupChatName>
    Given Me sent connection request to <Contact1>
    Given I rotate UI to portrait
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

  @C2601 @regression @fastLogin
  Scenario Outline: Verify only people icon exists under the plus in pending/left/removed from conversations [LANDSCAPE]
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact2>,<Contact3>
    Given Myself has group chat <GroupChatName> with <Contact2>,<Contact3>
    Given User Myself leaves group chat <GroupChatName>
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

  @C2564 @regression @fastLogin
  Scenario Outline: Verify player is displayed for vimeo links with video IDs [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User adds the following device: {"Myself": [{}]}
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given User Myself sends 1 "<VimeoLink>" message to conversation <Contact1>
    Given I see conversations list
    When I tap on contact name <Contact1>
    Then I see vimeo link <VimeoLink> with media preview in the conversation view

    Examples:
      | Name      | Contact1  | VimeoLink                   |
      | user1Name | user2Name | https://vimeo.com/129426512 |

  @C2565 @regression @fastLogin
  Scenario Outline: Verify player is displayed for vimeo links with video IDs [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User adds the following device: {"Myself": [{}]}
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given User Myself sends 1 "<VimeoLink>" message to conversation <Contact1>
    When I see conversations list
    Then I see vimeo link <VimeoLink> with media preview in the conversation view

    Examples:
      | Name      | Contact1  | VimeoLink                   |
      | user1Name | user2Name | https://vimeo.com/129426512 |

  @C2567 @regression @fastLogin
  Scenario Outline: Verify player isn't displayed for vimeo links without video IDs [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User adds the following device: {"Myself": [{}]}
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User Myself sends 1 "<VimeoLink>" message to conversation <Contact>
    When I tap on contact name <Contact>
    Then I see vimeo link <VimeoLink> without media preview in the conversation view

    Examples:
      | Name      | Contact   | VimeoLink                    |
      | user1Name | user2Name | https://vimeo.com/categories |

  @C2668 @regression @fastLogin
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
    Then I see the default message in the conversation input

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2583 @regression @fastLogin
  Scenario Outline: Verify possibility to copy image in the conversation view [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User adds the following device: {"<Contact>": [{}]}
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given User <Contact> sends 1 image file <Picture> to conversation Myself
    Given I see conversations list
    When I tap on contact name <Contact>
    And I see 1 photo in the conversation view
    # Wait for polka dots to disappear
    And I wait for 7 seconds
    And I long tap on image in conversation view
    And I tap on Copy badge item
    And I tap on text input
    And I long tap on text input
    And I tap on Paste badge item
    And I confirm my choice
    # Wait for convo list to update
    And I wait for 3 seconds
    Then I see 2 photos in the conversation view

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @C2587 @regression @fastLogin
  Scenario Outline: Verify possibility to copy image in the conversation view [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User adds the following device: {"<Contact>": [{}]}
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given User <Contact> sends 1 image file <Picture> to conversation Myself
    Given I see conversations list
    Given I tap on contact name <Contact>
    Given I see 1 photo in the conversation view
    # Wait for polka dots to disappear
    Given I wait for 7 seconds
    When I long tap on image in conversation view
    And I tap on Copy badge item
    And I tap on text input
    And I long tap on text input
    And I tap on Paste badge item
    And I confirm my choice
    # Wait for animation
    And I wait for 2 seconds
    Then I see 2 photos in the conversation view

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @C2549 @regression @fastLogin
  Scenario Outline: Verify posting in a 1-to-1 conversation without content [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User adds the following device: {"Myself": [{}]}
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given User Myself sends 1 default message to conversation <Contact1>
    Given I see conversations list
    When I swipe right on conversation <Contact1>
    And I tap Delete conversation action button
    And I confirm Delete conversation action
    Then I do not see conversation <Contact1> in conversations list
    When I wait until <Contact1> exists in backend search results
    And I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<Contact1>" in Search UI input field
    And I tap on conversation <Contact1> in search result
    And I tap Open conversation action button on Search UI page
    When I type the default message and send it
    Then I see 1 default message in the conversation view

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2617 @regression @fastLogin
  Scenario Outline: Verify downloading images in fullscreen [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User adds the following device: {"<Contact>": [{}]}
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given User <Contact> sends 1 image file <Picture> to conversation Myself
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I long tap on image in conversation view
    Then I see Save badge item
    When I tap on Save badge item
    And I accept alert if visible
    Then I do not see Save badge item
    And I see 1 photo in the conversation view

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @C2631 @regression @fastLogin
  Scenario Outline: Verify downloading images in fullscreen [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User adds the following device: {"<Contact>": [{}]}
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given User <Contact> sends 1 image file <Picture> to conversation Myself
    Given I see conversations list
    When I long tap on image in conversation view
    Then I see Save badge item
    When I tap on Save badge item
    And I accept alert if visible
    Then I do not see Save badge item
    And I see 1 photo in the conversation view

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @C2460 @regression @fastLogin
  Scenario Outline: Verify you still receive messages from blocked person in a group chat [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>, <Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>, <Contact2>
    Given User <Name> blocks user <Contact1>
    Given User adds the following device: {"<Contact1>": [{}]}
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User <Contact1> sends 1 default message to conversation <GroupChatName>
    Given User <Contact1> sends 1 image file <Picture> to conversation <GroupChatName>
    When I tap on group chat with name <GroupChatName>
    Then I see 1 default message in the conversation view
    And I see 1 photo in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Picture     |
      | user1Name | user2Name | user3Name | Caramba!      | testing.jpg |

  @C2624 @regression @rc @fastLogin
  Scenario Outline: ZIOS-7986 Verify sending GIF format pic [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User adds the following device: {"<Contact>": [{}]}
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given User <Contact> sends 1 image file <GifPicture> to conversation Myself
    Given I see conversations list
    Given I tap on contact name <Contact>
    # Wait for the picture to be loaded
    When I wait for 5 seconds
    Then I see the picture in the conversation view is animated
    When I tap on image in conversation view
    # Wait for animation
    And I wait for 2 seconds
    And I tap Fullscreen button on image
    Then I see the picture on image fullscreen page is animated

    Examples:
      | Name      | Contact   | GifPicture   |
      | user1Name | user2Name | animated.gif |

  @C2592 @regression @fastLogin
  Scenario Outline: Verify cursor tooltip is shown
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    Then I see Standard input placeholder text
    When I tap on text input
    Then I see Standard input placeholder text
    When I type the default message
    Then I do not see Standard input placeholder text
    When I tap Send Message button in conversation view
    Then I see Standard input placeholder text

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |