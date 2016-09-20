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
    Then I see Reset Password page

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
    And I select settings item Privacy & Security
    Then I verify the value of setting item Sound Alerts equals to All

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
    And I select settings item Support
    And I select settings item Wire Support Website
    Then I see Support web page

    Examples:
      | Name      |
      | user1Name |

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
