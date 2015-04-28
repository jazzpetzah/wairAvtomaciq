Feature: Sign In

 @id2262 @staging
  Scenario Outline: Sign in to ZClient in portrait mode
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    And I rotate UI to portrait
    Given I see sign in screen
    When I press Sign in button
    And I have entered login <Login>
    And I have entered password <Password>
    And I attempt to press Login button
    Then I see personal info page loaded with my name <Name>

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @staging
  Scenario Outline: Negative case for sign in portrait mode
    Given I see sign in screen
    And I rotate UI to portrait
    When I press Sign in button
    And I have entered login <Login>
    And I have entered password <Password>
    And I attempt to press Login button
    Then Login error message appears
    And Contains wrong name or password text

    Examples: 
      | Login | Password |
      | aaa   | aaa      |

 @id2248 @staging
  Scenario Outline: Sign in to ZClient in landscape mode
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    And I rotate UI to landscape
    Given I see sign in screen
    When I press Sign in button
    And I have entered login <Login>
    And I have entered password <Password>
    And I attempt to press Login button
    Then I see personal info page loaded with my name <Name>

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @staging
  Scenario Outline: Negative case for sign in landscape mode
    Given I see sign in screen
    And I rotate UI to landscape
    When I press Sign in button
    And I have entered login <Login>
    And I have entered password <Password>
    And I attempt to press Login button
    Then Login error message appears
    And Contains wrong name or password text

    Examples: 
      | Login | Password |
      | aaa   | aaa      |
