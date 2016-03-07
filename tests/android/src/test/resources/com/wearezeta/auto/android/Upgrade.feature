Feature: Sign In

  @C418 @use_previous_build @staging @torun
  Scenario Outline: Check upgrade from previous build
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I see welcome screen
#    Given I sign in using my email or phone number
#    Given I accept First Time overlay as soon as it is visible
#    Given I see Contact list with contacts
#    Given User <Contact> sends encrypted message <Message> to user Myself
#    Given User <Contact> sends encrypted image <Picture> to single user conversation Myself
    Given I upgrade Wire to the recent version
    And I see welcome screen

#    Given I see Contact list with contacts
#    When I tap on contact name <Contact>
#    Then I see my message "<Message>" in the dialog
#    And I see new photo in the dialog

    Examples:
      | Contact   | Name      | Picture     | Message |
      | user1Name | user2Name | testing.jpg | Test    |
