Feature: Forward Message

  @C345370 @staging @fastLogin
  Scenario Outline: Verify forwarding own picture
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given User Myself sends encrypted image <Picture> to single user conversation <Contact1>
    Given I see conversations list
    Given I tap on contact name <Contact1>
    Given I long tap on image in conversation view
    When I tap on Forward badge item
    And I select <Contact2> conversation on Forward page
    And I tap Send button on Forward page
    Then I see conversation with user <Contact1>
    When I navigate back to conversations list
    And I tap on contact name <Contact2>
    Then I see 1 photo in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | Picture     |
      | user1Name | user2Name | user3name | testing.jpg |

  @C345383 @staging @fastLogin
  Scenario Outline: ZIOS-7673 Verify outgoing/incoming connection requests/ left conversations are not in a forward list
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact3>
    Given <Contact1> sent connection request to Me
    Given Myself sent connection request to <Contact2>
    Given I sign in using my email or phone number
    Given User <Contact3> sends 1 encrypted message to user Myself
    Given I see conversations list
    Given I tap on contact name <Contact3>
    Given I long tap default message in conversation view
    When I tap on Forward badge item
    Then I do not see <Contact2> conversation on Forward page
    And I do not see <Contact1> conversation on Forward page

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  |
      | user1Name | user2Name | user3name | user4name |
