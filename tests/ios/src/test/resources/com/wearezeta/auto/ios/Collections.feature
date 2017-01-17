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
      | Name      | Contact   | Picture     | Link                  | FileName | FileExt | FileSize | FileMIME                 | ContactDevice | FileNameVideo  | MIMEType  |
      | user1Name | user2Name | testing.jpg | https://www.wire.com/ | testing  | bin     | 240 KB   | application/octet-stream | device1       | testing.mp4    | video/mp4 |

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
