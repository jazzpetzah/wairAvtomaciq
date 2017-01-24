Feature: Collections

  @C368979 @regression @fastLogin
  Scenario Outline: Verify main overview shows media from all categories (picture, file, link)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given Users add the following devices: {"Myself": [{}], "<Contact>": [{"name": "<ContactDevice>"}]}
    Given I create temporary file <FileSize> in size with name "<FileName>" and extension "<FileExt>"
    Given I sign in using my email or phone number
    Given User <Contact> sends encrypted image <Picture> to single user conversation <Name>
    Given User <Contact> sends file <FileNameVideo> having MIME type <MIMEType> to single user conversation <Name> using device <ContactDevice>
    Given User <Contact> sends temporary file <FileName>.<FileExt> having MIME type <FileMIME> to single user conversation <Name> using device <ContactDevice>
    Given User Myself sends encrypted message "<Link>" to user <Contact>
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I tap Collection button in conversation view
    Then I see collection category PICTURES
    And I see collection category VIDEOS
    And I see collection category FILES
    And I see collection category LINKS

    Examples:
      | Name      | Contact   | Picture     | Link                  | FileName | FileExt | FileSize | FileMIME                 | ContactDevice | FileNameVideo | MIMEType  |
      | user1Name | user2Name | testing.jpg | https://www.wire.com/ | testing  | bin     | 240 KB   | application/octet-stream | device1       | testing.mp4   | video/mp4 |

  @C368980 @regression @fastLogin
  Scenario Outline: Verify message is shown if no media in collection
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I tap Collection button in conversation view
    Then I see "No Items" placeholder in collection view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C375779 @regression @fastLogin
  Scenario Outline: Verify GIF pictures are not presented in Collection
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given Users add the following devices: {"Myself": [{}], "<Contact>": [{}]}
    Given I sign in using my email or phone number
    Given User Myself sends Giphy animation with tag "<GiphyTag1>" to single user conversation <Contact>
    Given User <Contact> sends Giphy animation with tag "<GiphyTag2>" to single user conversation Myself
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I tap Collection button in conversation view
    Then I see "No Items" placeholder in collection view

    Examples:
      | Name      | Contact   | GiphyTag1 | GiphyTag2 |
      | user1Name | user2Name | happy     | hello     |

  @C368984 @regression @fastLogin
  Scenario Outline: Opening single picture from pictures overview
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given Users add the following devices: {"<Contact>": [{}]}
    Given I sign in using my email or phone number
    Given User <Contact> sends encrypted image <Picture> to single user conversation Myself
    Given I see conversations list
    Given I tap on contact name <Contact>
    Given I tap Collection button in conversation view
    When I tap the item number 1 in collection category PICTURES
    Then I see full-screen image preview in collection view
    When I tap Back button in collection view
    Then I see collection category PICTURES

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @C368983 @regression @fastLogin
  Scenario Outline: Opening single picture from media overview
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given Users add the following devices: {"<Contact>": [{}]}
    Given I sign in using my email or phone number
    Given User <Contact> sends 10 image files <Picture> to conversation Myself
    Given I see conversations list
    Given I tap on contact name <Contact>
    Given I tap Collection button in conversation view
    Given I tap Show All label next to collection category PICTURES
    When I tap the item number 1 in collection category PICTURES
    Then I see full-screen image preview in collection view

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |


  @C368982 @regression @fastLogin
  Scenario Outline: Verify ephemeral messages are not shown in the collection view
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User adds the following device: {"<Contact>": [{"name": "<ContactDevice>"}]}
    Given I create temporary file <FileSize> in size with name "<FileName>" and extension "<FileExt>"
    Given I sign in using my email or phone number
    Given User <Contact> switches user Myself to ephemeral mode with 1 minute timeout
    Given User <Contact> sends encrypted image <Picture> to single user conversation <Name>
    Given User <Contact> sends file <FileNameVideo> having MIME type <MIMEType> to single user conversation <Name> using device <ContactDevice>
    Given User <Contact> sends temporary file <FileName>.<FileExt> having MIME type <FileMIME> to single user conversation <Name> using device <ContactDevice>
    Given User <Contact> sends encrypted message "<Link>" to user Myself
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I tap Collection button in conversation view
    Then I see "No Items" placeholder in collection view

    Examples:
      | Name      | Contact   | Picture     | Link                  | FileName | FileExt | FileSize | FileMIME                 | ContactDevice | FileNameVideo | MIMEType  |
      | user1Name | user2Name | testing.jpg | https://www.wire.com/ | testing  | bin     | 240 KB   | application/octet-stream | device1       | testing.mp4   | video/mp4 |

  @C368981 @regression @fastLogin
  Scenario Outline: Verify opening overview of all shared media categories
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given Users add the following devices: {"Myself": [{}], "<Contact>": [{}]}
    Given I create temporary file <FileSize> in size with name "<FileName>" and extension "<FileExt>"
    Given I sign in using my email or phone number
    Given User <Contact> sends 5 video files <VideoFileName> to conversation Myself
    Given User Myself sends 5 video files <VideoFileName> to conversation <Contact>
    Given User Myself sends 7 image files <Picture> to conversation <Contact>
    Given User <Contact> sends 7 image files <Picture> to conversation Myself
    Given User Myself sends 5 temporary files <FileName>.<FileExt> to conversation <Contact>
    Given User <Contact> sends 5 audio files <AudioFileName> to conversation Myself
    Given User <Contact> sends 5 "<Link>" messages to conversation Myself
    Given User Myself sends 5 "<Link>" messages to conversation <Contact>
    # Wait for sync
    Given I wait for 10 seconds
    Given I see conversations list
    Given I tap on contact name <Contact>
    Given I tap Collection button in conversation view
    # Wait for sync
    Given I wait for 5 seconds
    When I tap Show All label next to collection category PICTURES
    Then I see the count of tiles in collection category equals to 14
    When I tap Back button in collection view
    And I tap Show All label next to collection category VIDEOS
    Then I see the count of tiles in collection category equals to 10
    When I tap Back button in collection view
    And I tap Show All label next to collection category LINKS
    # Temporarily disabled, because of unstable SE, which does not generate previews for all the sent links
    # Then I see the count of tiles in collection category equals to 10
    When I tap Back button in collection view
    And I scroll collection view down
    And I tap Show All label next to collection category FILES
    Then I see the count of tiles in collection category equals to 10

    Examples:
      | Name      | Contact   | Picture     | Link                  | FileName | FileExt | FileSize | VideoFileName | AudioFileName |
      | user1Name | user2Name | testing.jpg | https://www.wire.com/ | testing  | bin     | 240 KB   | testing.mp4   | test.m4a      |

  @C368988 @regression @fastLogin
  Scenario Outline: (ZIOS-7911) Verify swiping between the pictures in collection view
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given Users add the following devices: {"<Contact>": [{}]}
    Given I sign in using my email or phone number
    Given User <Contact> sends encrypted image <Picture1> to single user conversation Myself
    Given User Myself sends encrypted image <Picture2> to single user conversation <Contact>
    Given I see conversations list
    Given I tap on contact name <Contact>
    Given I tap Collection button in conversation view
    Given I tap the item number 1 in collection category PICTURES
    Given I see full-screen image preview in collection view
    Given I remember current picture preview state
    When I swipe left on full-screen image preview in collection view
    Then I verify that current picture preview similarity score is less than 0.2 within 1 second
    When I swipe right on full-screen image preview in collection view
    Then I verify that current picture preview similarity score is more than 0.8 within 1 second

    Examples:
      | Name      | Contact   | Picture1    | Picture2                 |
      | user1Name | user2Name | testing.jpg | userpicture_portrait.jpg |

  @C368985 @regression @fastLogin
  Scenario Outline: Share picture, link, file into one different conversation
    Given There are 6 users where <Name> is me
    Given Myself is connected to all other users
    Given User add the following device: {"<Contact1Sender>": [{}]}
    Given I sign in using my email or phone number
    Given User <Contact1Sender> sends 1 video file <VideoFileName> to conversation Myself
    Given User <Contact1Sender> sends 1 image file <Picture> to conversation Myself
    Given User <Contact1Sender> sends 1 audio file <AudioFileName> to conversation Myself
    Given User <Contact1Sender> sends 1 "<Link>" message to conversation Myself
    Given I see conversations list
    Given I tap on contact name <Contact1Sender>
    Given I tap Collection button in conversation view
    When I long tap the item number 1 in collection category PICTURES
    And I tap on Share badge item
    And I select <Contact2> conversation on Forward page
    Then I tap Send button on Forward page
    When I tap Collection button in conversation view
    And I long tap the item number 1 in collection category VIDEOS
    Then I do not see Share badge item
    When I long tap the item number 1 in collection category LINKS
    And I tap on Share badge item
    And I select <Contact4> conversation on Forward page
    Then I tap Send button on Forward page
    When I tap Collection button in conversation view
    And I long tap the item number 1 in collection category FILES
    And I do not see Share badge item
    And I tap X button in collection view
    Then I see conversation with user <Contact1Sender>
    When I navigate back to conversations list
    And I tap on contact name <Contact2>
    Then I see 1 photo in the conversation view
    When I navigate back to conversations list
    And I tap on contact name <Contact4>
    Then I see link preview container in the conversation view

    Examples:
      | Name      | Contact1Sender | Contact2  | Contact4  | Picture     | Link                  | VideoFileName | AudioFileName |
      | user1Name | user2Name      | user3Name | user5Name | testing.jpg | https://www.wire.com/ | testing.mp4   | test.m4a      |

  @C395991 @regression @fastLogin
  Scenario Outline: Verify collection is available for a group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact>,<Contact2>
    Given Users add the following devices: {"Myself": [{}], "<Contact>": [{}]}
    Given I create temporary file <FileSize> in size with name "<FileName>" and extension "<FileExt>"
    Given I sign in using my email or phone number
    Given User <Contact> sends 1 video file <VideoFileName> to conversation <GroupChatName>
    Given User Myself sends 1 image file <Picture> to conversation <GroupChatName>
    Given User Myself sends 1 temporary file <FileName>.<FileExt> to conversation <GroupChatName>
    Given User <Contact> sends 1 audio file <AudioFileName> to conversation <GroupChatName>
    Given User <Contact> sends 1 "<Link>" message to conversation <GroupChatName>
    Given I see conversations list
    Given I tap on group chat with name <GroupChatName>
    When I tap Collection button in conversation view
    Then I see collection category PICTURES
    And I see collection category VIDEOS
    And I see collection category FILES
    And I see collection category LINKS

    Examples:
      | Name      | Contact   | Contact2  | GroupChatName   | Picture     | Link                  | FileName | FileExt | FileSize | VideoFileName | AudioFileName |
      | user1Name | user2Name | user3Name | GroupCollection | testing.jpg | https://www.wire.com/ | testing  | bin     | 240 KB   | testing.mp4   | test.m4a      |

  @C395993 @regression @fastLogin
  Scenario Outline: Verify you can see AssetsV3 in collection
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given Users add the following devices: {"Myself": [{"name": "<MySecondDevice>"}], "<Contact>": [{"name": "<ContactDevice>"}]}
    Given User Myself switches assets to V3 protocol via device <MySecondDevice>
    Given User <Contact> switches assets to V3 protocol via device <ContactDevice>
    Given I create temporary file <FileSize> in size with name "<FileName>" and extension "<FileExt>"
    Given I sign in using my email or phone number
    Given User Myself sends encrypted image <Picture> to single user conversation <Contact>
    Given User <Contact> sends file <FileNameVideo> having MIME type <MIMEType> to single user conversation <Name> using device <ContactDevice>
    Given User <Contact> sends temporary file <FileName>.<FileExt> having MIME type <FileMIME> to single user conversation <Name> using device <ContactDevice>
    Given User Myself sends encrypted message "<Link>" to user <Contact>
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I tap Collection button in conversation view
    Then I see collection category PICTURES
    And I see collection category VIDEOS
    And I see collection category FILES
    And I see collection category LINKS

    Examples:
      | Name      | MySecondDevice | Contact   | Picture     | Link                  | FileName | FileExt | FileSize | FileMIME                 | ContactDevice | FileNameVideo | MIMEType  |
      | user1Name | SecondDev      | user2Name | testing.jpg | https://www.wire.com/ | testing  | bin     | 240 KB   | application/octet-stream | device1       | testing.mp4   | video/mp4 |

  @C395992 @regression @fastLogin
  Scenario Outline: Verify collection content is updated after reopen if new media item appears in chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given Users add the following devices: {"<Contact1>": [{}], "<Contact2>": [{}]}
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on group chat with name <GroupChatName>
    Given I tap Collection button in conversation view
    Given I see "No Items" placeholder in collection view
    When User <Contact1> sends 1 image file <Picture> to conversation <GroupChatName>
    Then I see "No Items" placeholder in collection view
    When I tap X button in collection view
    And I tap Collection button in conversation view
    Then I see collection category PICTURES
    When User <Contact2> sends 1 "<Link>" message to conversation <GroupChatName>
    Then I do not see collection category LINKS
    When I tap X button in collection view
    And I tap Collection button in conversation view
    Then I see collection category LINKS

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName   | Picture     | Link                  |
      | user1Name | user2Name | user3Name | GroupCollection | testing.jpg | https://www.wire.com/ |

  @C395994 @regression @fastLogin
  Scenario Outline: Verify you can Reveal collection item
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given Users add the following devices: {"<Contact>": [{}]}
    Given I sign in using my email or phone number
    Given User <Contact> sends encrypted image <Picture> to single user conversation Myself
    # Wait for sync
    Given I wait for 5 seconds
    Given User <Contact> sends <MsgCount> default messages to conversation Myself
    Given I see conversations list
    Given I tap on contact name <Contact>
    Given I see 0 photos in the conversation view
    Given I tap Collection button in conversation view
    When I long tap the item number 1 in collection category PICTURES
    And I tap on Reveal badge item
    Then I see 1 photo in the conversation view
    When I tap on text input
    Then I see 0 photos in the conversation view
    When I tap Collection button in conversation view
    And I tap the item number 1 in collection category PICTURES
    And I tap Reveal button in collection view
    Then I see 1 photo in the conversation view

    Examples:
      | Name      | Contact   | Picture     | MsgCount |
      | user1Name | user2Name | testing.jpg | 20       |
