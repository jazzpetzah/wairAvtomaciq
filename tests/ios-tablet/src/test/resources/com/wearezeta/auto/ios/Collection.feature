Feature: Collection

  @C395995 @regression @fastLogin
  Scenario Outline: Verify you can see collections properly after changing display orientation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User adds the following device: {"<Contact>": [{}]}
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given User <Contact> sends 2 image files <Picture> to conversation Myself
    Given I see conversations list
    Given I tap on contact name <Contact>
    Given I tap Collection button in conversation view
    When I tap the item number 1 in collection category PICTURES
    Then I see full-screen image preview in collection view
    And I rotate UI to portrait
    When I tap X button in collection view
    Then I see 2 photos in the conversation view

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |