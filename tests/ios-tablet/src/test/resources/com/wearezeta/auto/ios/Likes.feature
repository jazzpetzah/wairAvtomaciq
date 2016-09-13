Feature: Likes

  @C246217 @staging @fastLogin
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

  @C246224 @C246225 @C246229 @staging @fastLogin @torun
  Scenario Outline: Verify liking image, link and youtube [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given User <Contact> sends encrypted image <Picture> to <ConversationType> conversation <Name>
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I long tap on image in conversation view
    And I tap on Like badge item
    And I long tap on image in conversation view
    And I tap on Unlike badge item
    Then User <Contact> sends encrypted message "<Link>" to user Myself
    When I long tap on link preview in conversation view
    And I tap on Like badge item
    And I long tap on link preview in conversation view
    And I tap on Unlike badge item
    When User <Contact> sends encrypted message "<YouTubeLink>" to user Myself
    And I long tap on media container in conversation view
    And I tap on Like badge item
    And I double tap on media container in conversation view
    Then I tap on Unlike badge item

    Examples:
      | Name      | Contact   | Picture     | ConversationType | Link                                               | YouTubeLink                                |
      | user1Name | user2Name | testing.jpg | single user      | https://twitter.com/wire/status/752800171608535040 | http://www.youtube.com/watch?v=Bb1RhktcugU |