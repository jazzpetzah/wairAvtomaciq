Feature: Conversation View

  @id2047 @staging
    Scenario Outline: See one-to-one pop-over
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in to self profile using login <Login> and password <Password>
    When I swipe right to tablet contact list
    And I see conversation list loaded with my name <Name>
    And I tap on tablet contact name <Contact>
    And I see tablet dialog page
    And I tap on profile button
    Then I see participant pop-over
    And I see <Contact> name and email in pop-over
    And I can close pop-over by close button
    When I tap on profile button
    Then I see participant pop-over
    And I see <Contact> name and email in pop-over
    And I can close pop-over by tapping outside

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |