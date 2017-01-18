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

  @C368980 @staging @fastLogin
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

  @C375779 @staging @fastLogin
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

  @C368983 @staging @fastLogin
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

  @C368982 @staging @fastLogin
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

  @C368981 @staging @fastLogin
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
    Then I see the count of tiles in collection category equals to 10
    When I tap Back button in collection view
    And I scroll collection view down
    And I tap Show All label next to collection category FILES
    Then I see the count of tiles in collection category equals to 10

    Examples:
      | Name      | Contact   | Picture     | Link                  | FileName | FileExt | FileSize | VideoFileName | AudioFileName |
      | user1Name | user2Name | testing.jpg | https://www.wire.com/ | testing  | bin     | 240 KB   | testing.mp4   | test.m4a      |

  @C395991 @staging @fastLogin
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
    Given User <Contact> sends 1 "<Link>" messages to conversation <GroupChatName>
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