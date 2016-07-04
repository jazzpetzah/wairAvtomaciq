Feature: Analytics

  @C165111 @regression @rc @analytics
  Scenario Outline: Verify registration statistics is sent
    When I see welcome screen
    And I verify that <LogType> log contains string "registration.opened_phone_signup"
    When I input a new phone number for user <Name>
    Then I verify that <LogType> log contains string "registration.entered_phone"
    When I input the verification code
    And I verify that <LogType> log contains string "registration.verified_phone"
    When I input my name
    Then I verify that <LogType> log contains string "registration.entered_name"
    And I verify that <LogType> log contains string "registration.succeeded"
    When I select to choose my own picture
    And I select Camera as picture source
    And I tap Take Photo button on Take Picture view
    And I tap Confirm button on Take Picture view
    Then I verify that <LogType> log contains string "registration.added_photo"
    And I verify that <LogType> log contains string "session"
    And I see Contact list with no contacts

    Examples:
      | Name      | LogType   |
      | user1Name | ANALYTICS |
