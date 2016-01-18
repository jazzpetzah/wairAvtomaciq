Feature: Utility

  @C3262
  Scenario: Verify buttons on invitation page for iphone
    When I use generic invitation link for invitation for iphone
    And I see You are invited page with agent
    Then I see button that sends me to App Store
    And I see button to connect for iphone including invitation code

  @C3263
  Scenario: Verify buttons on invitation page for android
    When I use generic invitation link for invitation for android
    And I see You are invited page with agent
    Then I see button that sends me to Play Store
    And I see button to connect for android including invitation code

  @C3264 @utility
  Scenario: Verify buttons on invitation page for osx
    When I use generic invitation link for invitation for osx
    And I see You are invited page
    Then I see 'Open Wire' button

  @C3265 @utility
  Scenario: Verify buttons on invitation page for windows
    When I use generic invitation link for invitation for windows
    And I see You are invited page
    Then I see 'Open Wire' button

  @C3257 @utility
  Scenario: Verify buttons on download page
    When I navigate to download page
    Then I see button for iOS
    And I see button for Android
    And I see button for OS X
    And I see button for Windows
    And I see button for Webapp

  @C5232 @utility
  Scenario Outline: Check password reset utility page for all agents
    Given There is 1 user where <Name> is me
    Given I switch to sign in page
    When I click Change Password button
    Then I see Password Change Request page
    When I enter email <Email> on Password Change Request page
    And Myself starts listening for password change confirmation
    And I click Change Password button on Password Change Request page
    Then I see Password Change Request Succeeded page
    When I open Password Change link from the received email for <Agent>
    Then I see Password Change page
    When I enter password <NewPassword> on Password Change page
    And I click Change Password button on Password Change page
    Then I see Password Change Succeeded page
    Given I open Sign In page
    When I see Sign In page
    When I enter email "<Email>"
    And I enter password "<OldPassword>"
    And I press Sign In button
    Then I see login error "<LoginErr>"
    When I enter email "<Email>"
    And I enter password "<NewPassword>"
    And I press Sign In button
    Then I am signed in properly

    Examples: 
      | Email      | OldPassword   | Name      | NewPassword | LoginErr                                  | Agent   |
      | user1Email | user1Password | user1Name | aqa654321#  | Please verify your details and try again. | iphone  |
      | user1Email | user1Password | user1Name | aqa654321#  | Please verify your details and try again. | android |
      | user1Email | user1Password | user1Name | aqa654321#  | Please verify your details and try again. | osx     |
      | user1Email | user1Password | user1Name | aqa654321#  | Please verify your details and try again. | windows |

  @C5233 @utility
  Scenario Outline: Check password reset with unregistered email for all agents
    When I navigate to Password Change Reset page for <Agent>
    Then I see Password Change Request page
    And I enter unregistered email <UnregisteredMail>
    And I click Change Password button on Password Change Request page
    Then I dont see Password Change Request Succeeded page

    Examples: 
      | UnregisteredMail                 | Agent   |
      | smoketester+sm0k3t3st3r@wire.com | iphone  |
      | smoketester+sm0k3t3st3r@wire.com | android |
      | smoketester+sm0k3t3st3r@wire.com | osx     |
      | smoketester+sm0k3t3st3r@wire.com | windows |

  @C5234 @utility
  Scenario Outline: Check password reset with wrong checksum for all agents
    Given There is 1 user where <Name> is me
    Given I switch to sign in page
    When I click Change Password button
    Then I see Password Change Request page
    When I enter email <Email> on Password Change Request page
    And Myself starts listening for password change confirmation
    And I click Change Password button on Password Change Request page
    Then I see Password Change Request Succeeded page
    When I open Password Change link from the received email with wrong checksum for <Agent>
    Then I see Password Change page
    When I enter password <NewPassword> on Password Change page
    And I click Change Password button on Password Change page
    Then I dont see Password Change Succeeded page

    Examples: 
      | Email      | OldPassword   | Name      | NewPassword | Agent   |
      | user1Email | user1Password | user1Name | aqa654321#  | ios     |
      | user1Email | user1Password | user1Name | aqa654321#  | android |
      | user1Email | user1Password | user1Name | aqa654321#  | osx     |
      | user1Email | user1Password | user1Name | aqa654321#  | windows |
