Feature: Conversation List

  @id324 @smoke
  Scenario Outline: Mute conversation
    Given There are 2 users where <Name> is me
    Given <Contact1> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I swipe up on dialog page
    And I press Right conversation button
    And I press Silence conversartion button
    And I click mute conversation <Contact1>
    Then Contact <Contact1> is muted
    When I swipe right on a <Contact1>
    And I click mute conversation <Contact1>
    Then Contact <Contact1> is not muted

    Examples: 
      | Login      | Password      | Name      | Contact1  |
      | user1Email | user1Password | user1Name | user2Name |

  @id1505 @staging
  Scenario Outline: Verify play/pause controls are visible in the list if there is active media item in other conversation (SoundCloud)
    Given There are 3 users where <Name> is me
    Given <Name> is connected to <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I tap on text input
    And I input <SoudCloudLink> message and send it
    And I tap Dialog page bottom
    And I press PlayPause media item button
    And I navigate back from dialog page
    Then I see PlayPause media content button in Conversations List

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | SoudCloudLink                                              |
      | user1Email | user1Password | user1Name | user2Name | user3Name | https://soundcloud.com/juan_mj_10/led-zeppelin-rock-n-roll |
