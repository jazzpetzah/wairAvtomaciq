Feature: Settings

  @C1098 @regression @fastLogin
  Scenario Outline: Verify user can access settings
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap settings gear button
    Then I see settings page

    Examples:
      | Name      |
      | user1Name |

  @C1099 @regression @rc @fastLogin
  Scenario Outline: Attempt to open About screen in settings
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
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

  @C1102 @regression @fastLogin
  Scenario Outline: Verify reset password page is accessible from settings
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap settings gear button
    And I select settings item Account
    And I select settings item Reset Password
    And I wait for 4 seconds
    Then I see Reset Password page

    Examples:
      | Name      |
      | user1Name |

  @C1107 @regression @fastLogin
  Scenario Outline: Verify default value for sound settings is all
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap settings gear button
    And I select settings item Privacy & Security
    Then I verify the value of settings item Sound Alerts equals to All

    Examples:
      | Name      |
      | user1Name |

  @C1109 @regression @fastLogin
  Scenario Outline: Verify you can access Help site within the app
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap settings gear button
    And I select settings item Support
    And I select settings item Wire Support Website
    And I wait for 4 seconds
    Then I see Support web page

    Examples:
      | Name      |
      | user1Name |

  @C3247 @regression @rc @useSpecialEmail @fastLogin
  Scenario Outline: Verify deleting the account registered by email
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
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