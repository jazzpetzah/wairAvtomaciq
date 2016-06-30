Feature: Analytics

  @C165111 @staging @analytics
  Scenario Outline: Verify registration statistics is sent
    When I see welcome screen
    Then I verify that ANALYTICS log contains string "PHONE_REGISTRATION"
    And I verify that ANALYTICS log contains string "appLaunch"
    And I verify that ANALYTICS log contains string "APP_LAUNCH_MECHANISM=direct"
    And I verify that ANALYTICS log contains string "registration"
    And I verify that ANALYTICS log contains string "CONTEXT=phone"
    When I input a new phone number for user <Name>
    Then I verify that ANALYTICS log contains string "registration.entered_phone"
    When I input the verification code
    Then I verify that ANALYTICS log contains string "PHONE_REGISTRATION__VERIFY_CODE"
    And I verify that ANALYTICS log contains string "registration.verified_phone"
    When I input my name
    Then I verify that ANALYTICS log contains string "PHONE_REGISTRATION__ADD_NAME"
    And I verify that ANALYTICS log contains string "registration.entered_name"
    And I verify that ANALYTICS log contains string "registration.succeeded"
    When I select to choose my own picture
    Then I verify that ANALYTICS log contains string "PHONE_REGISTRATION__ADD_PHOTO"
    When I select Camera as picture source
    And I tap Take Photo button on Take Picture view
    When I tap Confirm button on Take Picture view
    Then I verify that ANALYTICS log contains string "registration.added_photo"
    And I verify that ANALYTICS log contains string "session"
    When I see Contact list with no contacts
    Then I verify that ANALYTICS log contains string "CONVERSATION_LIST"

    Examples:
      | Name      |
      | user1Name |
