Feature: Collections

  @staging @torun
  Scenario Outline: test open
    Given There are 2 users where <User1> is me
    Given Myself is connected to <User2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with name <User2>
    When I tap on conversation name <User2>
    Then I see conversation view
    And I tap Collection button from top toolbar
    And I see Collection page
    And I see "<User2>" title on Conversation page

    Examples:
      | User1     | User2     |
      | user1Name | user2Name |