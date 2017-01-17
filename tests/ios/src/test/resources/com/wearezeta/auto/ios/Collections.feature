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
      | Name      | Contact   | Picture     | Link                  | FileName | FileExt | FileSize | FileMIME                 | ContactDevice | FileNameVideo  | MIMEType |
      | user1Name | user2Name | testing.jpg | https://www.wire.com/ | testing  | bin     | 240 KB   | application/octet-stream | device1       | testing.mp4    | video/mp4|