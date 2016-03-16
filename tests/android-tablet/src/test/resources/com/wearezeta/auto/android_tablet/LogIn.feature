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
    And I see First Time overlay
    And I tap Got It button on First Time overlay
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
    And I see First Time overlay
    And I tap Got It button on First Time overlay
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
    Then I see alert message containing "<ErrMessage>"

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
    Then I see alert message containing "<ErrMessage>"

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

  @C43811 @staging
  Scenario Outline: Verify you can sign in with phone number only
    Given There is 1 user with phone number only where <Name> is me
    Given I see welcome screen
    Given I rotate UI to landscape
    And I switch to Email sign in screen
    And I switch to Phone Login screen
    And I select Wire country on Phone Login page
    And I enter my phone number on Phone Login page
    And I tap Login button on Phone Login page
    And I enter my code on Phone Number Verification page
    And I tap Commit button on Phone Number Verification page
    And I enter login "<Email>"
    And I enter password "<Password>"
    And I start listening for registration email
    And I tap Sign In button
    When I verify my registration via email
    And I accept First Time overlay as soon as it is visible
    And I see the Conversations list with no conversations
    Then I see my name on Self Profile page

    Examples:
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @C480 @id2823 @regression
  Scenario Outline: (AN-2841) Verify Sign In progress behaviour while there are probelms with internet connectivity
    Given There is 1 user where <Name> is me
    Given I see welcome screen
    Given I rotate UI to landscape
    When I switch to email sign in screen
    And I enter login "<Email>"
    And I enter password "<Password>"
    When I enable Airplane mode on the device
    And I tap Sign In button
    Then I see alert message containing "<ErrMessage>"
    When I tap OK button on the error message
    And I disable Airplane mode on the device
    And I tap Sign In button
    And I accept First Time overlay as soon as it is visible
    Then I see the Conversations list with no conversations

    Examples:
      | Email      | Password      | ErrMessage                                           | Name      |
      | user1Email | user1Password | Please check your Internet connection and try again. | user1Name |