Feature: Upgrade

  @C58608 @upgrade
  Scenario Outline: Upgrade
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact> sends 1 encrypted message to user Myself
    Given User <Contact> sends encrypted image <Picture> to single user conversation <Name>
    # To let the content to be synchronized
    Given I wait for 5 seconds
    Given I upgrade Wire to the recent version
    And I see conversations list
    When I tap on contact name <Contact>
    Then I see 1 photo in the conversation view
    And I see 1 default message in the conversation view
    When I type the default message and send it
    # This is to make the keyboard invisible
    And I navigate back to conversations list
    When I tap on contact name <Contact>
    Then I see 2 default messages in the conversation view
    When I tap Add Picture button from input tools
    And I select the first picture from Keyboard Gallery
    And I tap Confirm button on Picture preview page
    Then I see 2 photos in the conversation view
    When I restart Wire
    Then I see 2 photos in the conversation view

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |
