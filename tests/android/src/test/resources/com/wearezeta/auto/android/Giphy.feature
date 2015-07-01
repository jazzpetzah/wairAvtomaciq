Feature: Connect

 @id2809 @torun
  Scenario Outline: Pressing send from the preview screen should post the gif from the preview to the conversation
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I type the message "<Message>"
    And I click on the GIF button
    And I click on the giphy send button
    And I see dialog page
    Then I see new photo in the dialog
    And I see the via giphy dotcom message

    Examples: 
      | Name      | Contact   | Message |
      | user1Name | user2Name | Yo      |