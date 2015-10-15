Feature: Test

  @id316 @regression @torun
  Scenario Outline: Receive Message from contact
    Given There are 3 users where <Name> is me
    Given <Contact> is connected to me
    Given <Contact2> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I see dialog page
    Given Contact <Contact> send message <Message> to user <Name>
    Given Contact <Contact2> send message <Message> to user <Name>
    And I see my message "<Message>" in the dialog
    Given Contact <Contact> sends image testing.jpg to single user conversation <Name>

    Examples:
      | Name      | Contact   | Contact2  | Message |
      | user1Name | user2Name | user3Name | Yo      |
