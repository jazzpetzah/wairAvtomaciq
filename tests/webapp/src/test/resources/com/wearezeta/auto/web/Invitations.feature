Feature: Invitations

  @id
  Scenario: Verify buttons on invitation page for iphone
    When I use generic invitation link for invitation for iphone
    Then I see button that sends me to App Store
    And I see button to connect for iphone including invitation code

  @id
  Scenario: Verify buttons on invitation page for android
    When I use generic invitation link for invitation for android
    Then I see button that sends me to Play Store
    And I see button to connect for android including invitation code
