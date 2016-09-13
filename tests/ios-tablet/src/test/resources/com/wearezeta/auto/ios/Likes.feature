Feature: Likes

  @C246217 @staging @fastLogin @torun
  Scenario Outline: Verify liking/unliking a message by tapping on like icon
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given User <Contact> sends 1 encrypted message to user Myself
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I tap default message in conversation view
    And I remember the state of Like icon in the conversation
    And I tap Like icon in the conversation
    Then I see the state of Like icon is changed in the conversation
    When I tap Unlike icon in the conversation
    Then I see the state of Like icon is not changed in the conversation

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C246224 @C246225 @C246229 @staging @fastLogin
  Scenario Outline: Verify liking image, link and youtube [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given User <Contact> sends encrypted image <Picture> to <ConversationType> conversation <Name>
    Given I see conversations list
    Given I tap on contact name <Contact>
    Then I see 1 photo in the conversation view
    When I tap on image in conversation view
    And I remember the state of Like icon in the conversation
    And I double tap default message in conversation view
    Then I see the state of Like icon is changed in the conversation

    Examples:
      | Name      | Contact   | Picture     | ConversationType |
      | user1Name | user2Name | testing.jpg | single user      |