Feature: Rich Media

  @id3241 @staging
  Scenario Outline: I can send GIF image from giphy grid
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I type the message "<Message>"
    And I click on the GIF button
    Then I see giphy preview page
    When I click Show giphy grid button
    And I select the first item in giphy grid
    And I see giphy preview page
    And I click on the giphy send button
    Then I see dialog page
    And I see new photo in the dialog
    And Last message is <Message> · via giphy.com

    Examples:
      | Name      | Contact   | Message |
      | user1Name | user2Name | Yo      |

