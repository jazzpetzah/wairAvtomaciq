Feature: Bring Your Friends

  @C2312 @smoke
  Scenario Outline: Use Gmail contacts import from search UI
    Given There are 1 users where <Name> is me
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    Then I click button to bring friends from Gmail
    And I see Google login popup
    And I sign up at Google with email <Gmail> and password <GmailPassword>
    Then I see Search is opened
    And I see more than 5 suggestions in people picker

    Examples:
      | Login      | Password      | Name      | Gmail                      | GmailPassword |
      | user1Email | user1Password | user1Name | smoketester.wire@gmail.com | aqa123456!    |