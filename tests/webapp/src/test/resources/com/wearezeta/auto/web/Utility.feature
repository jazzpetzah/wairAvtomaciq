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

  @C3258 @utility
  Scenario Outline: Check password reset utility page for iphone
    Given There is 1 user where <Name> is me
    Given I switch to sign in page
    When I click Change Password button
    Then I see Password Change Request page
    When I enter email <Email> on Password Change Request page
    And Myself starts listening for password change confirmation
    And I click Change Password button on Password Change Request page
    Then I see Password Change Request Succeeded page
    When I open Password Change link from the received email for iphone
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
      | Email      | OldPassword   | Name      | NewPassword | LoginErr                                  |
      | user1Email | user1Password | user1Name | aqa654321#  | Please verify your details and try again. |

  @C3259 @utility
  Scenario Outline: Check password reset utility page for android
    Given There is 1 user where <Name> is me
    Given I switch to sign in page
    When I click Change Password button
    Then I see Password Change Request page
    When I enter email <Email> on Password Change Request page
    And Myself starts listening for password change confirmation
    And I click Change Password button on Password Change Request page
    Then I see Password Change Request Succeeded page
    When I open Password Change link from the received email for android
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
      | Email      | OldPassword   | Name      | NewPassword | LoginErr                                  |
      | user1Email | user1Password | user1Name | aqa654321#  | Please verify your details and try again. |

  @C3260 @utility
  Scenario Outline: Check password reset utility page for mac
    Given There is 1 user where <Name> is me
    Given I switch to sign in page
    When I click Change Password button
    Then I see Password Change Request page
    When I enter email <Email> on Password Change Request page
    And Myself starts listening for password change confirmation
    And I click Change Password button on Password Change Request page
    Then I see Password Change Request Succeeded page
    When I open Password Change link from the received email for mac
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
      | Email      | OldPassword   | Name      | NewPassword | LoginErr                                  |
      | user1Email | user1Password | user1Name | aqa654321#  | Please verify your details and try again. |

  @C3261 @utility
  Scenario Outline: Check password reset utility page for windows
    Given There is 1 user where <Name> is me
    Given I switch to sign in page
    When I click Change Password button
    Then I see Password Change Request page
    When I enter email <Email> on Password Change Request page
    And Myself starts listening for password change confirmation
    And I click Change Password button on Password Change Request page
    Then I see Password Change Request Succeeded page
    When I open Password Change link from the received email for windows
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
      | Email      | OldPassword   | Name      | NewPassword | LoginErr                                  |
      | user1Email | user1Password | user1Name | aqa654321#  | Please verify your details and try again. |

  @C3267 @utility
  Scenario: Check password reset with unregistered email for iphone
    When I navigate to Password Change Reset page for iphone
    Then I see Password Change Request page
    And I enter unregistered email
    And I click Change Password button on Password Change Request page
    Then I dont see Password Change Request Succeeded page

  @C3268 @utility
  Scenario: Check password reset with unregistered email for android
    When I navigate to Password Change Reset page for android
    Then I see Password Change Request page
    And I enter unregistered email
    And I click Change Password button on Password Change Request page
    Then I dont see Password Change Request Succeeded page

  @C3269 @utility
  Scenario: Check password reset with unregistered email for mac
    When I navigate to Password Change Reset page for mac
    Then I see Password Change Request page
    And I enter unregistered email
    And I click Change Password button on Password Change Request page
    Then I dont see Password Change Request Succeeded page

  @C3270 @utility
  Scenario: Check password reset with unregistered email for windows
    When I navigate to Password Change Reset page for windows
    Then I see Password Change Request page
    And I enter unregistered email
    And I click Change Password button on Password Change Request page
    Then I dont see Password Change Request Succeeded page

  @C3271 @utility
  Scenario Outline: Check password reset with wrong checksum for iphone
    Given There is 1 user where <Name> is me
    Given I switch to sign in page
    When I click Change Password button
    Then I see Password Change Request page
    When I enter email <Email> on Password Change Request page
    And Myself starts listening for password change confirmation
    And I click Change Password button on Password Change Request page
    Then I see Password Change Request Succeeded page
    When I open Password Change link from the received email with wrong checksum for iphone
    Then I see Password Change page
    When I enter password <NewPassword> on Password Change page
    And I click Change Password button on Password Change page
    Then I dont see Password Change Succeeded page

    Examples: 
      | Email      | OldPassword   | Name      | NewPassword |
      | user1Email | user1Password | user1Name | aqa654321#  |

  @C3272 @utility
  Scenario Outline: Check password reset with wrong checksum for android
    Given There is 1 user where <Name> is me
    Given I switch to sign in page
    When I click Change Password button
    Then I see Password Change Request page
    When I enter email <Email> on Password Change Request page
    And Myself starts listening for password change confirmation
    And I click Change Password button on Password Change Request page
    Then I see Password Change Request Succeeded page
    When I open Password Change link from the received email with wrong checksum for android
    Then I see Password Change page
    When I enter password <NewPassword> on Password Change page
    And I click Change Password button on Password Change page
    Then I dont see Password Change Succeeded page

    Examples: 
      | Email      | OldPassword   | Name      | NewPassword |
      | user1Email | user1Password | user1Name | aqa654321#  |

  @C3273 @utility
  Scenario Outline: Check password reset with wrong checksum for mac
    Given There is 1 user where <Name> is me
    Given I switch to sign in page
    When I click Change Password button
    Then I see Password Change Request page
    When I enter email <Email> on Password Change Request page
    And Myself starts listening for password change confirmation
    And I click Change Password button on Password Change Request page
    Then I see Password Change Request Succeeded page
    When I open Password Change link from the received email with wrong checksum for mac
    Then I see Password Change page
    When I enter password <NewPassword> on Password Change page
    And I click Change Password button on Password Change page
    Then I dont see Password Change Succeeded page

    Examples: 
      | Email      | OldPassword   | Name      | NewPassword |
      | user1Email | user1Password | user1Name | aqa654321#  |

  @C3274 @utility
  Scenario Outline: Check password reset with wrong checksum for windows
    Given There is 1 user where <Name> is me
    Given I switch to sign in page
    When I click Change Password button
    Then I see Password Change Request page
    When I enter email <Email> on Password Change Request page
    And Myself starts listening for password change confirmation
    And I click Change Password button on Password Change Request page
    Then I see Password Change Request Succeeded page
    When I open Password Change link from the received email with wrong checksum for windows
    Then I see Password Change page
    When I enter password <NewPassword> on Password Change page
    And I click Change Password button on Password Change page
    Then I dont see Password Change Succeeded page

    Examples: 
      | Email      | OldPassword   | Name      | NewPassword |
      | user1Email | user1Password | user1Name | aqa654321#  |
