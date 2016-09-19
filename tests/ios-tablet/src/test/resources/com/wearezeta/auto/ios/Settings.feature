Feature: Settings

  @C2905 @regression @fastLogin
  Scenario Outline: Verify user can access settings [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap settings gear button
    Then I see settings page

    Examples:
      | Name      |
      | user1Name |

  @C2906 @regression @rc @fastLogin
  Scenario Outline: Attempt to open About screen in settings [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap settings gear button
    And I select settings item About
    Then I see settings item Privacy Policy
    And I see settings item Terms of Use
    And I see settings item License Information
    When I tap Done navigation button on Settings page
    Then I see conversations list

    Examples:
      | Name      |
      | user1Name |

  @C2907 @regression @fastLogin
  Scenario Outline: Verify reset password page is accessible from settings [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap settings gear button
    And I select settings item Account
    And I select settings item Reset Password
    And I wait for 4 seconds
    Then I see reset password page

    Examples:
      | Name      |
      | user1Name |

  @C2908 @regression
  Scenario Outline: Verify default value for sound settings is all [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap settings gear button
    And I select settings item Advanced
    Then I verify sound alerts settings are set to default values

    Examples:
      | Name      |
      | user1Name |

  @C2909 @regression
  Scenario Outline: Verify you can access Help site within the app [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap settings gear button
    And I click on Help button from the options menu
    Then I see Support web page

    Examples:
      | Name      |
      | user1Name |

  @C2911 @regression @fastLogin
  Scenario Outline: Verify about screen contains all the required information [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given User me change accent color to <Color>
    Given I Sign in on tablet using my email
    Given I rotate UI to landscape
    Given I see conversations list
    When I tap settings gear button
    And I select settings item About
    Then I see About page
    And I see WireWebsiteButton
    And I see TermsButton
    And I see PrivacyPolicyButton
    And I see BuildNumberText
    And I open PrivacyPolicyPage
    And I see PrivacyPolicyPage
    And I close legal page
    Then I see About page
    And I open TermsOfUsePage
    And I see TermsOfUsePage
    And I close legal page
    Then I see About page
    And I open WireWebsite
    Then I see WireWebsitePage

    Examples:
      | Name      | Color  |
      | user1Name | Violet |

  @C145961 @rc @regression @useSpecialEmail
  Scenario Outline: Verify deleting the account registered by email [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap settings gear button
    And I select settings item Account
    And I start waiting for <Name> account removal notification
    And I select settings item Delete Account
    # FIXME: Sometimes the alert is not accepted automatically
    And I tap OK button on the alert
    Then I see sign in screen
    And I verify account removal notification is received

    Examples:
      | Name      |
      | user1Name |
