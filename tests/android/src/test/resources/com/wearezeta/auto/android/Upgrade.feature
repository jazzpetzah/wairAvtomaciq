Feature: Upgrade

  @C418 @upgrade
  Scenario Outline: Check upgrade from previous build
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given User <Contact> sends encrypted message <Message> to user Myself
    Given User <Contact> sends encrypted image <Picture> to single user conversation Myself
    # Let the content to be delivered
    Given I wait for 5 seconds
    Given I upgrade Wire to the recent version
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    Then I see my message "<Message>" in the dialog
    And I see 1 image in the conversation view
    When I swipe on text input
    And I tap Add Picture button from input tools
    And I press "Gallery" button
    And I press "Confirm" button
    Then I see 2 images in the conversation view
    When I tap on text input
    And I type the message "<Message2>" and send it
    Then I see my message "<Message2>" in the dialog

    Examples:
      | Contact   | Name      | Picture     | Message | Message2 |
      | user1Name | user2Name | testing.jpg | Test    | Message2 |
