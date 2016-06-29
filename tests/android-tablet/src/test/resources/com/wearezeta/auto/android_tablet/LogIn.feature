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
    Then I see the Conversations list with no conversations

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
    Then I see the Conversations list with no conversations

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

  @C480 @id2823 @regression
  Scenario Outline: (AN-2841) Verify Sign In progress behaviour while there are probelms with internet connectivity
    Given There is 1 user where <Name> is me
    Given I see welcome screen
    Given I rotate UI to landscape
    When I switch to email sign in screen
    And I enter login "<Email>"
    And I enter password "<Password>"
    And I enable Airplane mode on the device
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

  @C162658 @staging
  Scenario Outline: Verify you can remove extra devices and log in successfully if too many devices are registered for your account
    Given There is 1 user where <Name> is me
    Given User <Name> adds new devices Device1,<DeviceToRemove>,<DeviceToRemoveWithoutPassword>,<OtherDevice>,Device5,Device6,Device7
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    When I see Manage Devices overlay
    And I tap Manage Devices button on Manage Devices overlay
    And I select "<DeviceToRemove>" settings menu item
    And I select "Remove device" settings menu item
    And I enter <Password> into the device removal password confirmation dialog
    And I tap OK button on the device removal password confirmation dialog
      # Delete device will take time, should verify at first it already return back to device list view, also the list is already updated
    And I wait for 5 seconds
    And I see "<DeviceToRemoveWithoutPassword>" settings menu item
    And I do not see "<DeviceToRemove>" settings menu item
      # C145960
    And I select "<DeviceToRemoveWithoutPassword>" settings menu item
    And I select "Remove device" settings menu item
    And I see "<OtherDevice>" settings menu item
    And I do not see "<DeviceToRemoveWithoutPassword>" settings menu item
    And I press Back button 2 times
    When I do not see Manage Devices overlay
    Then I see the Conversations list with no conversations

    Examples:
      | Password      | Name      | DeviceToRemoveWithoutPassword | DeviceToRemove | OtherDevice |
      | user1Password | user1Name | Device2                       | Device3        | Device4     |