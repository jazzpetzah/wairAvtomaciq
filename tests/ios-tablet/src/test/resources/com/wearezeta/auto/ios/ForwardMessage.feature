Feature: Forward Message

  @C345391 @staging @fastLogin
  Scenario Outline: Verify forwarding own picture
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given User Myself sends encrypted image <Picture> to single user conversation <Contact1>
    Given I see conversations list
    Given I tap on contact name <Contact1>
    Given I long tap on image in conversation view
    When I tap on Forward badge item
    And I select <Contact2> conversation on Forward page
    And I tap Send button on Forward page
    Then I see conversation with user <Contact1>
    When I tap on contact name <Contact2>
    Then I see 1 photo in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | Picture     |
      | user1Name | user2Name | user3name | testing.jpg |

  @C345388 @staging @fastLogin
  Scenario Outline: Verify forwarding own text message
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I tap on contact name <Contact1>
    Given I type the default message and send it
    Given I tap Hide keyboard button
    Given I long tap default message in conversation view
    When I tap on Forward badge item
    And I select <Contact2> conversation on Forward page
    And I tap Send button on Forward page
    Then I see conversation with user <Contact1>
    When I tap on contact name <Contact2>
    Then I see 1 default message in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3name |
