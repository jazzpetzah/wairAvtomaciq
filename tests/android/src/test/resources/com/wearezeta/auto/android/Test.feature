Feature: Test

  @id666 @regression @torun
  Scenario Outline: I want to make sure that my changes are working
    Given There are 2 users where <Name> is me
    Given <Contact1> is connected to <Name>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And I see dialog page
    Given Contact <Contact> send message to user <Name>

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |