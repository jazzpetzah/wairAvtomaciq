Feature: Conversation List

  @id324 @smoke
  Scenario Outline: Mute conversation
    Given There are 2 users where <Name> is me
    Given <Contact1> is connected to <Name>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
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
      | Name      | Contact1  |
      | user1Name | user2Name |

  @id1510 @regression
  Scenario Outline: Verify conversation list play/pause controls can change playing media state (SoundCloud) 
    Given There are 2 users where <Name> is me
    Given <Name> is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And I see dialog page
    And I tap on text input
    And I type the message "<SoudCloudLink>" and send it
    # Workaround for bug with autoscroll
    And I scroll to the bottom of conversation view
    And I press PlayPause media item button
    And I navigate back from dialog page
    And I see Contact list
    Then I see PlayPause media content button for conversation <Contact1>

    Examples: 
      | Name      | Contact1  | SoudCloudLink                                              |
      | user1Name | user2Name | https://soundcloud.com/juan_mj_10/led-zeppelin-rock-n-roll |

  @id2177 @regression
  Scenario Outline: I can open and close people picker by diffetent actions
    Given There are 2 users where <Name> is me
    # We need at least 1 user in the convo list, otherwise it will be impossible to swipe
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I open Search by tap
    And I see People picker page
    And I press Clear button
    Then I see Contact list with contacts
    And I do not see TOP PEOPLE
    When I open Search by UI button
    And I see People picker page
    And I swipe down people picker
    Then I see Contact list with contacts
    And I do not see TOP PEOPLE
    When I swipe down contact list
    And I see People picker page

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @id1514 @regression
  Scenario Outline: Verify unsilence the conversation
    Given There are 2 users where <Name> is me
    Given <Contact1> is connected to me
    Given <Contact1> is silenced to user <Name>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
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
      | Name      | Contact1  |
      | user1Name | user2Name |
