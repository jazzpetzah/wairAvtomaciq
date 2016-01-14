Feature: Log In

  @C743 @id2262 @regression @rc @rc44
  Scenario Outline: Sign in to Wire in portrait mode
    Given There is 1 user where <Name> is me
    Given I see welcome screen
    Given I rotate UI to portrait
    When I switch to email sign in screen
    And I enter login "<Login>"
    And I enter password "<Password>"
    And I tap Sign In button
    And I accept First Time overlay as soon as it is visible
    And I see the Conversations list with no conversations
    Then I see my name on Self Profile page

    Examples:
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @C733 @id2248 @regression @rc @rc44
  Scenario Outline: Sign in to Wire in landscape mode
    Given There is 1 user where <Name> is me
    Given I see welcome screen
    Given I rotate UI to landscape
    When I switch to email sign in screen
    And I enter login "<Login>"
    And I enter password "<Password>"
    And I tap Sign In button
    And I accept First Time overlay as soon as it is visible
    And I see the Conversations list with no conversations
    Then I see my name on Self Profile page

    Examples:
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @C750 @id2284 @regression @rc
  Scenario Outline: Negative case for sign in portrait mode
    Given I see welcome screen
    Given I rotate UI to portrait
    When I switch to email sign in screen
    And I enter login "<Login>"
    And I enter password "<Password>"
    And I tap Sign In button
    Then I see error message "<ErrMessage>"

    Examples:
      | Login | Password  | ErrMessage                          |
      | aaa   | aaabbbccc | Please enter a valid email address. |

  @C751 @id2285 @regression @rc
  Scenario Outline: Negative case for sign in landscape mode
    Given I see welcome screen
    Given I rotate UI to landscape
    When I switch to email sign in screen
    And I enter login "<Login>"
    And I enter password "<Password>"
    And I tap Sign In button
    Then I see error message "<ErrMessage>"

    Examples:
      | Login | Password  | ErrMessage                          |
      | aaa   | aaabbbccc | Please enter a valid email address. |

  @C781 @id2906 @regression @rc
  Scenario Outline: Verify reset password button works from sign-in page
    Given I see Welcome screen
    Given I rotate UI to landscape
    When I switch to email sign in screen
    Then I see "<LinkText>" link on Welcome screen
    When I tap "<LinkText>" link on Welcome screen
    Then I see the Wire app is not in foreground

    Examples:
      | LinkText         |
      | Forgot Password? |