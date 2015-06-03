Feature: Sign In

  @id2262 @smoke
  Scenario Outline: Sign in to ZClient in portrait mode
    Given There is 1 user where <Name> is me
    Given I see welcome screen
    Given I rotate UI to portrait
    When I switch to email sign in screen
    And I enter login "<Login>"
    And I enter password "<Password>"
    And I click Sign In button
    And I see the Conversations list
    Then I see my name on Self Profile page

    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @id2284 @smoke
  Scenario Outline: Negative case for sign in portrait mode
    Given I see welcome screen
    Given I rotate UI to portrait
    When I switch to email sign in screen
    And I enter login "<Login>"
    And I enter password "<Password>"
    And I click Sign In button
    Then I see error message "<ErrMessage>"

    Examples: 
      | Login | Password | ErrMessage                          |
      | aaa   | aaa      | Please enter a valid email address. |

 @id2248 @smoke
  Scenario Outline: Sign in to ZClient in landscape mode
    Given There is 1 user where <Name> is me
    Given I see welcome screen
    And I rotate UI to landscape
    When I switch to email sign in screen
    And I enter login "<Login>"
    And I enter password "<Password>"
    And I click Sign In button
    And I see the Conversations list
    Then I see my name on Self Profile page

    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @id2285 @smoke
  Scenario Outline: Negative case for sign in landscape mode
    Given I see welcome screen
    And I rotate UI to landscape
    When I switch to email sign in screen
    And I enter login "<Login>"
    And I enter password "<Password>"
    And I click Sign In button
    Then I see error message "<ErrMessage>"

    Examples: 
      | Login | Password | ErrMessage                          |
      | aaa   | aaa      | Please enter a valid email address. |
