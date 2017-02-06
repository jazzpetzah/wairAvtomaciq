Feature: Collections

  @C399347 @regression @rc
  Scenario Outline: Verify main overview shows media from all categories (picture, file, link)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User adds the following device: {"<Contact>": [{"name": "<ContactDevice>"}]}
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> sends encrypted image <Picture> to single user conversation <Name>
    Given <Contact> sends <FileSize> file having name "<FileName>" and MIME type "<MIMEType>" via device <ContactDevice> to user Myself
    Given User Myself sends encrypted message "<Link>" to user <Contact>
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    # Wait until animation
    Given I wait for 2 seconds
    When I tap Collections button from top toolbar
    Then I see PICTURES category on Collections page
    And I see FILES category on Collections page
    And I see LINKS category on Collections page

    Examples:
      | Name      | Contact   | Picture        | Link                  | FileName      | FileSize | MIMEType   | ContactDevice |
      | user1Name | user2Name | avatarTest.png | https://www.wire.com/ | qa_random.txt | 1.00MB   | text/plain | device1       |

  @C399351 @regression
  Scenario Outline: Verify message is shown if no media in collection
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    # Wait until animation
    Given I wait for 2 seconds
    When I tap Collections button from top toolbar
    Then I see NO ITEM placeholder on Collections page

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C399353 @regression @rc
  Scenario Outline: Verify opening single picture from all shared media overview
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> sends encrypted image <Picture> to single user conversation Myself
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    Given I see a picture in the conversation view
    # Wait until animation
    Given I wait for 2 seconds
    When I tap Collections button from top toolbar
    And I tap on the 1st picture on Collections page
    Then I see collection image preview
    When I tap on Back button on Collection image preview
    Then I see PICTURES category on Collections page

    Examples:
      | Name      | Contact   | Picture        |
      | user1Name | user2Name | avatarTest.png |

  @C399352 @regression @rc
  Scenario Outline: Verify opening single picture from pictures overview
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> sends 10 image files <Picture> to conversation Myself
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    # Wait until animation
    Given I wait for 2 seconds
    When I tap Collections button from top toolbar
    And I tap Show All label next to category picture on Collection page
    And I tap on the 1st picture on Collections page
    Then I see collection image preview
    And I see timestamp on Collection image preview top toolbar
    And I see sender name "<Contact>" on Collection image preview top toolbar

    Examples:
      | Name      | Contact   | Picture        |
      | user1Name | user2Name | avatarTest.png |

  @C399355 @regression @rc
  Scenario Outline: Verify ephemeral messages are not shown in the collection view
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User adds the following device: {"<Contact>": [{"name": "<ContactDevice>"}]}
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> switches user Myself to ephemeral mode via device <ContactDevice> with 1 minute timeout
    Given User <Contact> sends encrypted image <Picture> to single user conversation <Name>
    Given <Contact> sends <FileSize> file having name "<FileName>" and MIME type "<MIMEType>" via device <ContactDevice> to user Myself
    Given User <Contact> sends encrypted message "<Link>" to user Myself
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    # Wait until animation
    Given I wait for 2 seconds
    When I tap Collections button from top toolbar
    Then I see NO ITEM placeholder on Collections page

    Examples:
      | Name      | Contact   | Picture        | Link                  | FileName      | FileSize | MIMEType   | ContactDevice |
      | user1Name | user2Name | avatarTest.png | https://www.wire.com/ | qa_random.txt | 1.00MB   | text/plain | device1       |

  @C399356 @regression
  Scenario Outline: Verify opening overview of all shared media categories
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I create temporary file <FileSize> in size with name "<FileName>" and extension "<FileExt>"
    Given User <Contact> sends 2 "<Link>" messages to conversation Myself
    Given User Myself sends 2 "<Link>" messages to conversation <Contact>
    Given User Myself sends 4 image files <Picture> to conversation <Contact>
    Given User <Contact> sends 5 image files <Picture> to conversation Myself
    Given User Myself sends 4 temporary files <FileName>.<FileExt> to conversation <Contact>
     # Wait for sync
    Given I wait for 10 seconds
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    # Wait until animation and sync post message
    Given I wait for 10 seconds
    # Wait for sync
    When I tap Collections button from top toolbar
    And I tap Show All label next to category picture on Collection page
    Then I see 9 pictures in <Timeout> seconds on Collection page
    And I tap on Back button on Collection page
    When I tap Show All label next to category link preview on Collection page
    Then I see 4 link previews in <Timeout> seconds on Collection page
    And I tap on Back button on Collection page
    When I swipe up
    And I tap Show All label next to category file sharing on Collection page
    Then I see 4 file sharings in <Timeout> seconds on Collection page

    Examples:
      | Name      | Contact   | Picture        | Link                  | FileName | FileExt | FileSize | Timeout |
      | user1Name | user2Name | avatarTest.png | https://www.wire.com/ | testing  | bin     | 10 KB    | 15      |

  @C399358 @regression @rc
  Scenario Outline: Verify collection is available for a group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact>,<Contact2>
    Given I create temporary file <FileSize> in size with name "<FileName>" and extension "<FileExt>"
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User Myself sends 1 image file <Picture> to conversation <GroupChatName>
    Given User Myself sends 1 temporary file <FileName>.<FileExt> to conversation <GroupChatName>
    Given User <Contact> sends 1 "<Link>" message to conversation <GroupChatName>
    Given I see Conversations list with conversations
    Given I tap on conversation name <GroupChatName>
    # Wait until animation and sync post message
    Given I wait for 2 seconds
    When I tap Collections button from top toolbar
    Then I see PICTURES category on Collections page
    And I see FILES category on Collections page
    And I see LINKS category on Collections page
    And I see sender name "<GroupChatName>" on Collection page

    Examples:
      | Name      | Contact   | Contact2  | GroupChatName   | Picture        | Link                  | FileName | FileExt | FileSize |
      | user1Name | user2Name | user3Name | GroupCollection | avatarTest.png | https://www.wire.com/ | testing  | bin     | 10 KB    |

  @C399362 @regression
  Scenario Outline: Verify you can see AssetsV3 in collection
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given Users add the following devices: {"<Contact>": [{"name": "<ContactDevice>"}]}
    Given User <Contact> switches assets to V3 via device <ContactDevice>
    Given I create temporary file <FileSize> in size with name "<FileName>" and extension "<FileExt>"
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given Users add the following devices: {"Myself": [{"name": "<MySecondDevice>"}]}
    Given User Myself switches assets to V3 via device <MySecondDevice>
    Given User Myself sends encrypted image <Picture> to single user conversation <Contact>
    Given <Contact> sends <FileSize> file having name "<FileName>" and MIME type "<MIMEType>" via device <ContactDevice> to user Myself
    Given User Myself sends encrypted message "<Link>" via device <MySecondDevice> to user <Contact>
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    # Wait until animation and sync post message
    Given I wait for 2 seconds
    When I tap Collections button from top toolbar
    Then I see PICTURES category on Collections page
    And I see FILES category on Collections page
    And I see LINKS category on Collections page

    Examples:
      | Name      | MySecondDevice | Contact   | Picture        | Link                  | FileName      | FileSize | MIMEType   | ContactDevice |
      | user1Name | SecondDev      | user2Name | avatarTest.png | https://www.wire.com/ | qa_random.txt | 1.00MB   | text/plain | device1       |

  @C399363 @regression @rc
  Scenario Outline: Verify collection content is updated after reopen if new media item appears in chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <GroupChatName>
    # Wait until animation and sync post message
    Given I wait for 2 seconds
    When I tap Collections button from top toolbar
    Then I see NO ITEM placeholder on Collections page
    When User <Contact1> sends 1 image file <Picture> to conversation <GroupChatName>
    Then I see NO ITEM placeholder on Collections page
    When I tap on Close button on Collection page
    # Wait for animation
    And I wait for 2 seconds
    And I tap Collections button from top toolbar
    Then I see PICTURES category on Collections page
    When User <Contact2> sends 1 "<Link>" message to conversation <GroupChatName>
    Then I do not see LINKS category on Collections page
    When I tap on Close button on Collection page
    # Wait for animation
    And I wait for 2 seconds
    And I tap Collections button from top toolbar
    Then I see LINKS category on Collections page

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName   | Picture        | Link                  |
      | user1Name | user2Name | user3Name | GroupCollection | avatarTest.png | https://www.wire.com/ |

  @C399365 @regression @rc
  Scenario Outline: Verify you can Reveal collection item
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> sends encrypted image <Picture> to single user conversation Myself
    Given User <Contact> sends <MsgCount> default messages to conversation Myself
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    Given I scroll to the bottom of conversation view
    Given I do not see any pictures in the conversation view
    Given I tap Collections button from top toolbar
    When I tap on the 1st picture on Collections page
    And I see collection image preview
    And I tap on View button on Collection image preview
    Then I see a picture in the conversation view

    Examples:
      | Name      | Contact   | Picture        | MsgCount |
      | user1Name | user2Name | avatarTest.png | 20       |

  @C399367 @regression
  Scenario Outline: Verify no pictures from different conversations are in the overview
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I push local file named "<Picture>" to the device
    And User <Contact1> sends encrypted image <Picture> to single user conversation Myself
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact1>
    When I tap Add picture button from cursor toolbar
    And I tap Gallery button on Extended cursor camera overlay
    And I tap Confirm button on Take Picture view
    And I see 2 images in the conversation view
    And I tap Collections button from top toolbar
    Then I see 2 pictures in <Timeout> seconds on Collection page
    When I tap on Close button on Collection page
    And I navigate back from conversation
    And I tap on conversation name <Contact2>
    And I wait for 2 seconds
    And I tap Collections button from top toolbar
    Then I see NO ITEM placeholder on Collections page

    Examples:
      | Name      | Contact1  | Contact2  | Picture        | Timeout |
      | user1Name | user2Name | user3Name | avatarTest.png | 15      |

  @C399368 @regression
  Scenario Outline: Verify deleted media isn't in collection on other side
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User adds the following device: {"<Contact>": [{"name": "<ContactDevice>"}]}
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> sends encrypted image <PictureName> to single user conversation Myself
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I see 1 image in the conversation view
    # Wait animation
    And I wait for 2 seconds
    And I tap Collections button from top toolbar
    And I see 1 picture in <Timeout> seconds on Collection page
    And I tap on Close button on Collection page
    And User <Contact> delete the recent message everywhere from user Myself via device <ContactDevice>
    Then I do not see any pictures in the conversation view
    When I tap Collections button from top toolbar
    Then I see NO ITEM placeholder on Collections page

    Examples:
      | Name      | Contact   | PictureName    | ContactDevice | Timeout |
      | user1Name | user2Name | avatarTest.png | D1            | 10      |

  @C399370 @regression
  Scenario Outline: Delete image/file/link preivew from collection view by long tap on it
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User adds the following device: {"<Contact>": [{"name": "<ContactDevice>"}]}
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> sends encrypted image <Picture> to single user conversation <Name>
    Given <Contact> sends <FileSize> file having name "<FileName>" and MIME type "<MIMEType>" via device <ContactDevice> to user Myself
    Given User Myself sends encrypted message "<Link>" to user <Contact>
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    # Wait until animation
    Given I wait for 2 seconds
    When I tap Collections button from top toolbar
    And I see 1 picture in <Timeout> seconds on Collection page
    And I see 1 link preview in <Timeout> seconds on Collection page
    And I see 1 file sharing in <Timeout> seconds on Collection page
    And I long tap on the 1st picture on Collections page
    And I tap Delete button on the message bottom menu
    And I tap Delete button on the alert
    And I long tap on the 1st link preview on Collections page
    And I tap Delete button on the message bottom menu
    And I tap Delete only for me button on the message bottom menu
    And I tap Delete button on the alert
    And I long tap on the 1st file sharing on Collections page
    And I tap Delete button on the message bottom menu
    And I tap Delete button on the alert
    Then I see NO ITEM placeholder on Collections page

    Examples:
      | Name      | Contact   | Picture        | Link                  | FileName      | FileSize | MIMEType   | ContactDevice | Timeout |
      | user1Name | user2Name | avatarTest.png | https://www.wire.com/ | qa_random.txt | 1.00MB   | text/plain | device1       | 10      |

  @C399371 @regression @rc
  Scenario Outline: Verify I can like and delete only for me of an image from fullscreen view
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> sends encrypted image <Picture> to single user conversation Myself
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    Given I see a picture in the conversation view
    # Wait until animation
    Given I wait for 2 seconds
    # Like
    When I tap Collections button from top toolbar
    And I tap on the 1st picture on Collections page
    And I see collection image preview
    And I tap on Like button on Collection image preview
    And I tap on Back button on Collection image preview
    And I tap on Close button on Collection page
    And I scroll to the bottom of conversation view
    Then I see Like description with expected text "<Name>" in conversation view
    # Delete only for me
    When I tap Collections button from top toolbar
    And I tap on the 1st picture on Collections page
    And I see collection image preview
    And I tap on Delete button on Collection image preview
    And I tap Delete button on the alert
    Then I see NO ITEM placeholder on Collections page

    Examples:
      | Name      | Contact   | Picture        |
      | user1Name | user2Name | avatarTest.png |

  @C399372 @regression
  Scenario Outline: Verify I can delete for everyone of an image from fullscreen view
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I push local file named "<Picture>" to the device
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    Given I tap Add picture button from cursor toolbar
    Given I tap Gallery button on Extended cursor camera overlay
    Given I tap Confirm button on Take Picture view
    Given I see a picture in the conversation view
    # Wait until animation
    Given I wait for 2 seconds
    When I tap Collections button from top toolbar
    And I tap on the 1st picture on Collections page
    And I see collection image preview
    And I tap on Delete button on Collection image preview
    And I tap Delete for everyone button on the message bottom menu
    And I tap Delete button on the alert
    Then I see NO ITEM placeholder on Collections page

    Examples:
      | Name      | Contact   | Picture        |
      | user1Name | user2Name | avatarTest.png |