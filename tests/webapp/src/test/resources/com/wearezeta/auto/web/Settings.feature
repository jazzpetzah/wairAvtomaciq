Feature: Settings

  @C1706 @regression
  Scenario Outline: Check Preferences opening
    Given There is 1 user where <Name> is me
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    Given I am signed in properly
    And I open preferences by clicking the gear button
    Then I see name <Name> in account preferences

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @C1773 @regression
  Scenario Outline: Verify sound settings are saved after re-login
    Given There is 1 user where <Name> is me
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    Given I am signed in properly
    And I open preferences by clicking the gear button
    And I open options in preferences
    When I set sound alerts setting to none
    Then I see Sound Alerts setting is set to none
    And I close preferences
    And I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click logout button on clear data dialog
    Given I see Sign In page
    Given I Sign in using login <Email> and password <Password>
    And I am signed in properly
    And I open preferences by clicking the gear button
    And I open options in preferences
    Then I see Sound Alerts setting is set to none
    When I set sound alerts setting to some
    Then I see Sound Alerts setting is set to some
    And I close preferences
    And I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click logout button on clear data dialog
    Given I see Sign In page
    Given I Sign in using login <Email> and password <Password>
    And I am signed in properly
    And I open preferences by clicking the gear button
    And I open options in preferences
    Then I see Sound Alerts setting is set to some
    When I set sound alerts setting to all
    Then I see Sound Alerts setting is set to all

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @C352087 @regression
  Scenario Outline: Verify I can set sound alert settings
    Given There is 2 user where <Name> is me
    Given user <Contact> adds a new device Device1 with label Label1
    Given <Contact> is connected to Myself
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    Given I am signed in properly
    And I open preferences by clicking the gear button
    And I open options in preferences
    When I set sound alerts setting to none
    Then I see Sound Alerts setting is set to none
    Then Soundfile new_message did not start playing
    When Contact <Contact> sends message Hello1 to user Myself
    Then Soundfile new_message did not start playing
    Then Soundfile ping_from_them did not start playing
    And User <Contact> pinged in the conversation with me
    Then Soundfile ping_from_them did not start playing
    Then I see Sound Alerts setting is set to none
    When I set sound alerts setting to some
    Then I see Sound Alerts setting is set to some
    Then Soundfile new_message did not start playing
    When Contact <Contact> sends message Hello2 to user Myself
    Then Soundfile new_message did not start playing
    Then Soundfile ping_from_them did not start playing
    And User <Contact> pinged in the conversation with me
    Then Soundfile ping_from_them did start playing
    Then I see Sound Alerts setting is set to some
    When I set sound alerts setting to all
    Then I see Sound Alerts setting is set to all
    Then Soundfile new_message did not start playing
    When Contact <Contact> sends message Hello4 to user Myself
    Then Soundfile new_message did start playing
    Then Soundfile ping_from_them did not start playing
    And User <Contact> pinged in the conversation with me
    Then Soundfile ping_from_them did start playing

    Examples: 
      | Email      | Password      | Name      | Contact    |
      | user1Email | user1Password | user1Name | user2Name  |

  @C12064 @regression @useSpecialEmail
  Scenario Outline: Verify you can delete account
    Given There is 1 user where <Name> is me
    Given I switch to Sign In page
    Given I remember current page
    Given I Sign in using login <Email> and password <Password>
    Given I am signed in properly
    Given I open preferences by clicking the gear button
    When I open account in preferences
    And I click delete account button on settings page
    And I click cancel deletion button on settings page
    And I click delete account button on settings page
    And I click send button to delete account
    And I delete account of user <Name> via email
    And I navigate to previously remembered page
    And I see Sign In page
    When I enter email "<Email>"
    And I enter password "<Password>"
    And I press Sign In button
    Then the sign in error message reads <Error>

    Examples:
      | Email      | Password      | Name      | Error                                     |
      | user1Email | user1Password | user1Name | Please verify your details and try again. |

  @C345367 @localytics @regression
  Scenario Outline: Verify tracking data is never uploaded if user opted out
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I enable localytics via URL parameter
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    Given I am signed in properly
    When I see Contact list with name <Contact>
    Then I open preferences by clicking the gear button
    And I open options in preferences
    When I see option to send reports is checked
    And I remember number of localytics events
    And I click on option to send reports
    Then I see option to send reports is unchecked
    When I close preferences
    And I see Contact list with name <Contact>
    And I open conversation with <Contact>
    And I write random message
    And I send message
    And I see random message in conversation
    And I wait for 10 seconds
    Then There are no added localytics events

    Examples:
      | Email      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @C352073 @localytics @staging
  Scenario Outline: Verify data is never uploaded to Raygun if user opted out
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I enable localytics via URL parameter
    Given I switch to Sign In page
    Given user <Name> adds a new device Device1 with label Label1
    Given I Sign in using login <Email> and password <Password>
    Given I see the history info page
    Given I click confirm on history info page
    Given I am signed in properly
    When I see Contact list with name <Contact>
    Then I open preferences by clicking the gear button
    And I open options in preferences
    When I see option to send reports is checked
    And I click on option to send reports
    Then I see option to send reports is unchecked
    And I wait for 2 seconds
    And I remember number of raygun events
    When I close preferences
    And I see Contact list with name <Contact>
    And I open conversation with <Contact>
    And I write random message
    And I send message
    And I see random message in conversation
    When I break the session with device Device1 of user <Name>
    And Contact <Name> sends message TEST via device Device1 to user <Contact>
    Then I see <UNABLE_TO_DECRYPT> action in conversation
    And I wait for 10 seconds
    Then There are no added raygun events

    Examples:
      | Email      | Password      | Name      | Contact   | UNABLE_TO_DECRYPT |
      | user1Email | user1Password | user1Name | user2Name | WAS NOT RECEIVED  |