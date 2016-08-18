Feature: Edit Message

  @C202349 @staging @fastLogin
  Scenario Outline: Verify I cannot edit another users message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User <Contact> sends 1 encrypted message to user Myself
    Given I see conversations list
    When I tap on contact name <Contact>
    Then I see 1 default message in the conversation view
    When I long tap default message in conversation view
    Then I do not see Edit badge item

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |
