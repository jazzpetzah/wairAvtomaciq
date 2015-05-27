Feature: Conversation List

  @id324 @smoke
  Scenario Outline: Mute conversation
    Given There are 2 users where <Name> is me
    Given <Contact1> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I tap on contact name <Contact1>
    And I see dialog page
    And I tap conversation details button
    And I press options menu button
    And I press Silence conversation button
    #And I return to group chat page
    #Some elements seem to be missing (e.g. "X" button) so
    #Instead of searching for elements, it works perfectly fine (and faster) just to press back 3 times
    And I press back button
    And I press back button
    And I press back button
    #And I navigate back from dialog page
    Then Contact <Contact1> is muted

    Examples: 
      | Login      | Password      | Name      | Contact1  |
      | user1Email | user1Password | user1Name | user2Name |

  @id1510 @regression
  Scenario Outline: Verify conversation list play/pause controls can change playing media state (SoundCloud) 
    Given There are 2 users where <Name> is me
    Given <Name> is connected to <Contact1>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I tap on contact name <Contact1>
    And I see dialog page
    And I tap on text input
    And I type the message "<SoudCloudLink>" and send it
    And I type the message "Test" and send it
    And I press PlayPause media item button
    And I navigate back from dialog page
    And I see Contact list
    Then I see PlayPause media content button for conversation <Contact1>

    Examples: 
      | Login      | Password      | Name      | Contact1  | SoudCloudLink                                              |
      | user1Email | user1Password | user1Name | user2Name | https://soundcloud.com/juan_mj_10/led-zeppelin-rock-n-roll |

  @id2177 @regression 
  Scenario Outline: I can open and close people picker by UI button or swipe
    Given There are 2 users where <Name> is me
    Given <Contact1> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I press Open StartUI
    And I see People picker page
    And I press Clear button
    Then I see Contact list
    And I do not see TOP PEOPLE
    When I press Open StartUI
    And I see People picker page
    And I swipe down people picker
    Then I see Contact list
    And I do not see TOP PEOPLE

    Examples: 
      | Login      | Password      | Name      | Contact1  |
      | user1Email | user1Password | user1Name | user2Name |

  @id1514 @staging
  Scenario Outline: Verify unsilince the conversation
    Given There are 2 users where <Name> is me
    Given <Contact1> is connected to <Name>
    Given <Contact1> is silenced to user <Name>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    Given Contact <Contact1> is muted
    When I tap on contact name <Contact1>
    And I see dialog page
    And I tap conversation details button
    And I press options menu button
    And I press Notify conversation button
    And I press back button
    And I press back button
    And I navigate back from dialog page
    Then Contact <Contact1> is not muted

    Examples: 
      | Login      | Password      | Name      | Contact1  |
      | user1Email | user1Password | user1Name | user2Name |
