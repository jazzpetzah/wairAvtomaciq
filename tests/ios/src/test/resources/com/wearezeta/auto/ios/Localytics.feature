Feature: Localytics

  @C375780 @staging @enableLocalyticsLogs @torun
  Scenario Outline: Verify key tracking events
    Given There is 1 user
    Given I see sign in screen
    Given I enter phone number for <Name>
    Given I enter activation code
    Given I accept terms of service
    Given I input name <Name> and commit it
    Given I accept alert
    Given I tap Keep This One button
    # Wait for sync
    Given I wait for 3 seconds
    Given I accept alert if visible
    Given I tap Share Contacts button on Share Contacts overlay
    Given User <Name> is me
    Given Myself is connected to <Contact1>
    Given User Myself sets the unique username
    Given I accept alert if visible
    Given I dismiss settings warning if visible
    Given I see conversation <Contact1> in conversations list
    Then I see "registration.succeeded" event is sent to Localytics at least 1 time

    Examples:
      | Contact1  | Name      |
      | user1Name | user2Name |

