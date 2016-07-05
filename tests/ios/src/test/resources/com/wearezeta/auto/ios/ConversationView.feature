Feature: Conversation View

  @C3182 @regression @id855
  Scenario Outline: Verify tooltip is shown when cursor area is empty and in/not in focus
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    Then I see input placeholder text
    When I tap on text input
    Then I see input placeholder text
    When I type the default message
    Then I do not see input placeholder text

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
    Then I see 1 default message in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C923 @regression @id331 @fastLogin
  Scenario Outline: Send Hello to contact
    Given There are 2 users where <Name> is me
    Given I prepare Wire to perform fast log in by email as Myself
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Ping button from input tools
    Then I see "<PingMsg>" system message in the conversation view

    Examples:
      | Name      | Contact   | PingMsg    |
      | user1Name | user2Name | YOU PINGED |

  @C909 @regression @IPv6 @id332 @id1470
  Scenario Outline: Send a camera roll picture to user from contact list
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Add Picture button from input tools
    And I press Camera Roll button
    And I choose a picture from camera roll
    And I confirm my choice
    Then I see 1 photo in the conversation view

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
    Then I see 1 default message in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName  |
      | user1Name | user2Name | user3Name | MessageToGroup |

  @C883 @regression @id394
  Scenario Outline: Tap the cursor to get to the end of the conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact> sends 40 encrypted messages to user Myself
    When I tap on contact name <Contact>
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
    Then I see 1 default message in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C878 @regression @id413
  Scenario Outline: Copy and paste to send the message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I see sign in screen
    When I switch to Log In tab
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
    Then I see 0 default messages in the conversation view
    When I type the default message
    And I type the "   " message and send it
    Then I see 1 default message in the conversation view

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
    And I tap Add Picture button from input tools
    And I press Camera Roll button
    And I choose a picture from camera roll
    And I confirm my choice
    And I see 1 photo in the conversation view
    And I tap and hold image to open full screen
    And I see Full Screen Page opened
    And I see sender first name <Name> on fullscreen page
    And I see send date on fullscreen page
    And I see download button shown on fullscreen page
    And I tap on fullscreen page
    And I verify image caption and download button are not shown
    And I tap on fullscreen page
    And I tap close fullscreen page button
    Then I see 1 photo in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C921 @regression @id1480
  Scenario Outline: Rotate image in fullscreen mode
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact> sends encrypted image <Picture> to single user conversation Myself
    And I tap on contact name <Contact>
    And I see 1 photo in the conversation view
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
    And I tap Archive action button
    Then I do not see conversation <Contact> in conversations list
    And I open archived conversations
    Then I see conversation <Contact> in conversations list

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C940 @rc @regression @IPv6 @id2762
  Scenario Outline: Receive message from contact
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact> sends 1 encrypted message to user Myself
    When I tap on contact name <Contact>
    Then I see 1 default message in the conversation view

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
    Then I see 1 photo in the conversation view

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
    And I tap Sketch button from input tools
    And I draw a random sketch
    And I send my sketch
    Then I see 1 photo in the conversation view

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C891 @rc @regression @id3095
  Scenario Outline: Verify only people icon exists under the plus in pending/left/removed from conversations
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact2>,<Contact3>
    Given Myself has group chat <GroupChatName> with <Contact2>,<Contact3>
    Given I leave group chat <GroupChatName>
    Given Me sent connection request to <Contact1>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact1>
    Then I do not see conversation tools buttons
    And I navigate back to conversations list
    When I tap on group chat with name <GroupChatName>
    Then I do not see conversation tools buttons

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
    And I see 1 photo in the conversation view
    And I tap and hold image to open full screen
    And I see Full Screen Page opened
    And I press Sketch button on image fullscreen page
    And I draw a random sketch
    And I send my sketch
    Then I see 2 photos in the conversation view

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
    And I tap Add Picture button from input tools
    And I press Camera Roll button
    And I choose a picture from camera roll
    And I press sketch button on camera roll page
    And I draw a random sketch
    And I send my sketch
    And I confirm my choice
    Then I see 1 photo in the conversation view

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

  @C845 @regression @id3963
  Scenario Outline: Verify posting in a 1-to-1 conversation without content
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Name> sends 1 encrypted message to user <Contact1>
    When I swipe right on a <Contact1>
    And I tap Delete action button
    And I confirm delete conversation content
    Then I do not see conversation <Contact1> in conversations list
    And I wait until <Contact1> exists in backend search results
    And I open search UI
    And I input in People picker search field conversation name <Contact1>
    And I tap on conversation <Contact1> in search result
    And I tap Open conversation action button on People picker page
    Then I see conversation view page
    And I type the default message and send it
    And I see 1 default message in the conversation view

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C879 @regression @id1158 @ZIOS-6517
  Scenario Outline: (BUG-ZIOS-6517) Verify possibility to copy image in the conversation view
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact> sends encrypted image <Picture> to single user conversation Myself
    And I tap on contact name <Contact>
    And I see 1 photo in the conversation view
    And I long tap on image in conversation view
    And I tap on Copy badge item
    And I tap on text input
    And I tap and hold on message input
    And I click on popup Paste item
    And I confirm my choice
    Then I see 2 photo in the conversation view

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
    And I see 1 photo in the conversation view
    And I tap and hold image to open full screen
    And I see Full Screen Page opened
    And I see download button shown on fullscreen page
    And I tap download button on fullscreen page
    And I tap close fullscreen page button
    And I tap Add Picture button from input tools
    And I press Camera Roll button
    And I choose a picture from camera roll
    And I confirm my choice
    And I see 2 photos in the conversation view

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
    Then I see 4 conversation entries

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
    Then I do not see user avatar icon near the conversation input field
    When I tap on text input
    Then I see user avatar icon near the conversation input field
    # This is to hide keyboard
    When I navigate back to conversations list
    And I tap on contact name <Contact>
    Then I do not see user avatar icon near the conversation input field

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C917 @real
  Scenario Outline: Verify sending photo from a back camera
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Add Picture button from input tools
    And I tap Camera Shutter button
    And I confirm my choice
    Then I see 1 photo in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C918 @real
  Scenario Outline: Verify sending photo from a front camera
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Add Picture button from input tools
    And I tap Toggle Camera button
    And I tap Camera Shutter button
    And I confirm my choice
    Then I see 1 photo in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C77924 @regression
  Scenario Outline: Verify an upper toolbar exists in the conversation view
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    Then I see Upper Toolbar on dialog page

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C77968 @regression
  Scenario Outline: Verify upper toolbar for the outgoing connection request is shown
    Given There are 2 users where <Name> is me
    Given I sent connection request to <Contact1>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact1>
    Then I see Upper Toolbar on dialog page
    And I do not see Calling buttons on Upper Toolbar

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C77970 @regression
  Scenario Outline: Verify call icon is not shown in the left group conversation
    Given There are <UsersAmount> users where <Name> is me
    Given Myself is connected to all other
    Given Myself has group chat <GroupChatName> with all other
    Given I sign in using my email or phone number
    Given I see conversations list
    When I leave group chat <GroupChatName>
    And I do not see conversation <GroupChatName> in conversations list
    And I open archived conversations
    And I tap on group chat with name <GroupChatName>
    Then I do not see Audio call button on Upper Toolbar

    Examples:
      | Name      | GroupChatName  | UsersAmount |
      | user1Name | LeaveGROUPCALL | 4           |

  @C78373 @regression
  Scenario Outline: Verify changing name of the user in the upper toolbar
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I see conversation name <Contact> in Upper Toolbar
    And User <Contact> changes name to <NewName>
    Then I see conversation name <NewName> in Upper Toolbar

    Examples:
      | Name      | Contact   | NewName |
      | user1Name | user2Name | NewName |

  @C37374 @regression
  Scenario Outline: Verify changing conversation title in the upper toolbar
    Given There are <UsersAmount> users where <Name> is me
    Given Myself is connected to all other
    Given Myself has group chat <GroupChatName> with all other
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I see conversation name <GroupChatName> in Upper Toolbar
    And User <Contact> renames conversation <GroupChatName> to <NewChatName>
    Then I see conversation name <NewChatName> in Upper Toolbar

    Examples:
      | Name      | Contact   | GroupChatName  | UsersAmount | NewChatName |
      | user1Name | user2Name | RenameChatName | 4           | NewName     |

  @C95637 @regression
  Scenario Outline: Verify opening the image twice in the raw
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User <Contact> sends encrypted image <Picture> to single user conversation Myself
    Given I see conversations list
    When I tap on contact name <Contact>
    Then I see 1 photo in the conversation view
    When I tap and hold image to open full screen
    Then I see Full Screen Page opened
    And I tap close fullscreen page button
    When I tap and hold image to open full screen
    Then I see Full Screen Page opened
    And I tap close fullscreen page button
    When I tap and hold image to open full screen
    Then I see Full Screen Page opened

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @C111318 @regression @rc
  Scenario Outline: Verify cursor and toolbar appear after adding person back
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other
    Given Myself has group chat <GroupChatName> with all other
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I see input placeholder text
    And I see conversation tools buttons
    When <Contact> removed <Name> from group chat <GroupChatName>
    Then I do not see conversation tools buttons
    And I do not see text input in conversation view
    When User <Contact> adds user <Name> to group chat <GroupChatName>
    Then I see conversation tools buttons
    And I see input placeholder text

    Examples:
      | Name      | Contact   | GroupChatName |
      | user1Name | user2Name | CURSORTOOLBAR |
