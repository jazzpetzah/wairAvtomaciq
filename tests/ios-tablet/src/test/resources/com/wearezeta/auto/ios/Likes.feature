Feature: Likes

  @C246217 @regression @fastLogin
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

  @C246224 @C246225 @staging @fastLogin @torun
  Scenario Outline: Verify liking image and link [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given User <Contact> sends encrypted image <Picture> to <ConversationType> conversation <Name>
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I tap at 5% of width and 5% of height of the recent message
    And I remember the state of Like icon in the conversation
    And I tap Like icon in the conversation
    Then I see the state of Like icon is changed in the conversation
    When User <Contact> sends encrypted message "<Link>" to user Myself
    And I long tap on link preview in conversation view
    And I tap on Like badge item
    And I tap toolbox of the recent message
    Then I see user Myself in likers list

    Examples:
      | Name      | Contact   | Picture     | ConversationType | Link                                               |
      | user1Name | user2Name | testing.jpg | single user      | https://twitter.com/wire/status/752800171608535040 |