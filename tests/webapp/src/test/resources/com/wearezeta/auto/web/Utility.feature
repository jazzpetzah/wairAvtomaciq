Feature: Utility

  @C3257 @utility
  Scenario: Verify buttons on download page
    Given I switch to sign in page
    When I navigate to download page
    Then I see button for iOS
    And I see button for Android
    And I see button for OS X
    And I see button for Windows
    And I see button for Webapp

  @C3283 @utility
  Scenario Outline: Verify that there are no dead links on start page for <Agent>
    Given I switch to sign in page
    When I navigate to start page for <Agent>
    Then I can see no dead links

    Examples:
      | Agent   |
      | iphone  |
      | android |
      | osx     |
      | windows |

  @C77922 @utility
  Scenario Outline: Verify that there are no dead links on privacy page for <Agent>
    Given I switch to sign in page
    When I navigate to privacy page for <Agent>
    Then I can see no dead links

    Examples:
      | Agent   |
      | iphone  |
      | android |
      | osx     |
      | windows |

  @C139985 @utility
  Scenario Outline: Verify that there are no dead links on unsupported browser page for <Agent>
    Given I switch to sign in page
    When I navigate to unsupported page for <Agent>
    Then I can see no dead links

    Examples:
      | Agent   |
      | iphone  |
      | android |
      | osx     |
      | windows |

  @C12086 @utility
  Scenario Outline: Verify that there are no dead links on german start page for <Agent>
    Given I switch to sign in page
    When I navigate to start page for <Agent>
    And I change language to german
    And start page for <Agent> is german
    Then I can see no dead links

    Examples:
      | Agent   |
      | iphone  |
      | android |
      | osx     |
      | windows |

  @C77923 @utility
  Scenario Outline: Verify that there are no dead links on german privacy page for <Agent>
    Given I switch to sign in page
    When I navigate to privacy page for <Agent>
    And I change language to german
    And privacy page for <Agent> is german
    Then I can see no dead links

    Examples:
      | Agent   |
      | iphone  |
      | android |
      | osx     |
      | windows |

  @C139986 @utility
  Scenario Outline: Verify that there are no dead links on german unsupported browser page for <Agent>
    Given I switch to sign in page
    When I navigate to unsupported page for <Agent>
    And I change language to german
    And unsupported page for <Agent> is german
    Then I can see no dead links

    Examples:
      | Agent   |
      | iphone  |
      | android |
      | osx     |
      | windows |

  @C139987 @utility
    Scenario Outline: Verify I get redirected to unsupported browser page
    Given I switch to sign in page
    When I navigate to login page for <Agent>
    Then I see unsupported browser page
    
    Examples:
      | Agent  |
      | safari |

  @C5232 @utility @useSpecialEmail
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
    And I see the history info page
    And I click confirm on history info page
    Then I am signed in properly

    Examples:
      | Email      | OldPassword   | Name      | NewPassword | LoginErr                                  | Agent   |
      | user1Email | user1Password | user1Name | aqa654321#  | Please verify your details and try again. | iphone  |
      | user1Email | user1Password | user1Name | aqa654321#  | Please verify your details and try again. | android |
      | user1Email | user1Password | user1Name | aqa654321#  | Please verify your details and try again. | osx     |
      | user1Email | user1Password | user1Name | aqa654321#  | Please verify your details and try again. | windows |

  @C5233 @utility
  Scenario Outline: Check password reset with unregistered email for all agents
    Given I switch to sign in page
    When I go to Password Change Reset page for <Agent>
    Then I see Password Change Request page
    And I enter unregistered email <UnregisteredMail>
    And I click Change Password button on Password Change Request page
    Then I see unused mail message

    Examples:
      | UnregisteredMail                 | Agent   |
      | smoketester+sm0k3t3st3r@wire.com | iphone  |
      | smoketester+sm0k3t3st3r@wire.com | android |
      | smoketester+sm0k3t3st3r@wire.com | osx     |
      | smoketester+sm0k3t3st3r@wire.com | windows |

  @C5234 @utility @useSpecialEmail
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
      | Email      | Name      | NewPassword | Agent   |
      | user1Email | user1Name | aqa654321#  | ios     |
      | user1Email | user1Name | aqa654321#  | android |
      | user1Email | user1Name | aqa654321#  | osx     |
      | user1Email | user1Name | aqa654321#  | windows |

  @C3275 @C3276
  Scenario Outline: Verify buttons from verfication link for <Agent>
    # Blocked due to limitation of Selenium
    Given There is 1 user where <Name> is me
    Given I switch to sign in page
    When I generate verification code for user <Name>
    And I go to phone verification page for <Agent>
    Then I see 'Open Wire' button with verification code

    Examples:
      | Name      | Agent   |
      | user1Name | iphone  |
      | user1Name | android |

  @C3277 @utility
  Scenario: Verify buttons from verification link for osx
    Given I switch to sign in page
    When I go to verify page for osx
    Then I see download button for osx
    And I see webapp button

  @C3278 @utility
  Scenario: Verify buttons from verification link for windows
    Given I switch to sign in page
    When I go to verify page for windows
    Then I see download button for windows

  @C5236 @utility
  Scenario Outline: Verify error message by broken verification link for <Agent>
    Given I switch to sign in page
    When I go to broken verify page for <Agent>
    Then I see error message

    Examples:
      | Agent   |
      | iphone  |
      | android |
      | osx     |
      | windows |

  @C14580 @utility @useSpecialEmail
  Scenario Outline: Verify you can delete account on <Agent>
    Given There is 1 user where <Name> is me
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    Given I am signed in properly
    Given I open preferences by clicking the gear button
    When I select Settings menu item on self profile page
    And I see Settings dialog
    And I click delete account button on settings page
    And I see email <Email> in delete info text on settings page
    And I click send button to delete account
    And I delete account of user <Name> via email on <Agent>
    And I open Sign In page
    And I see Sign In page
    When I enter email "<Email>"
    And I enter password "<Password>"
    And I press Sign In button
    Then the sign in error message reads <Error>

    Examples:
      | Email      | Password      | Name      | Error                                     | Agent   |
      | user1Email | user1Password | user1Name | Please verify your details and try again. | iphone  |
      | user1Email | user1Password | user1Name | Please verify your details and try again. | android |
      | user1Email | user1Password | user1Name | Please verify your details and try again. | osx     |
      | user1Email | user1Password | user1Name | Please verify your details and try again. | windows |

  @C14581 @utility @useSpecialEmail
  Scenario Outline: Change key checksum on <Agent>
    Given There is 1 user where <Name> is me
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    Given I am signed in properly
    Given I open preferences by clicking the gear button
    When I select Settings menu item on self profile page
    And I see Settings dialog
    And I click delete account button on settings page
    And I see email <Email> in delete info text on settings page
    And I click send button to delete account
    And I navigate to delete account page of user <Name> on <Agent> with changed key checksum
    And I click delete account button
    Then I see error message for wrong key checksum

    Examples:
      | Email      | Password      | Name      | Agent   |
      | user1Email | user1Password | user1Name | iphone  |
      | user1Email | user1Password | user1Name | android |
      | user1Email | user1Password | user1Name | osx     |
      | user1Email | user1Password | user1Name | windows |

  @C14582 @utility @useSpecialEmail
  Scenario Outline: Change code checksum on <Agent>
    Given There is 1 user where <Name> is me
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    Given I am signed in properly
    Given I open preferences by clicking the gear button
    When I select Settings menu item on self profile page
    And I see Settings dialog
    And I click delete account button on settings page
    And I see email <Email> in delete info text on settings page
    And I click send button to delete account
    Then I navigate to delete account page of user <Name> on <Agent> with changed code checksum
    And I click delete account button
    Then I see error message for wrong code checksum

    Examples:
      | Email      | Password      | Name      | Agent   |
      | user1Email | user1Password | user1Name | iphone  |
      | user1Email | user1Password | user1Name | android |
      | user1Email | user1Password | user1Name | osx     |
      | user1Email | user1Password | user1Name | windows |

  @C14583 @utility @useSpecialEmail
  Scenario Outline: Remove checksums on <Agent>
    Given There is 1 user where <Name> is me
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    Given I am signed in properly
    Given I open preferences by clicking the gear button
    When I select Settings menu item on self profile page
    And I see Settings dialog
    And I click delete account button on settings page
    And I see email <Email> in delete info text on settings page
    And I click send button to delete account
    And I remember delete link of user <Name>
    And I use remembered link on <Agent> without key checksum
    Then I see error message for missing checksum
    And I do not see success message for account deleted
    When I use remembered link on <Agent> without code checksum
    Then I see error message for missing checksum
    And I do not see success message for account deleted
    When I use remembered link on <Agent> without both checksum
    Then I see error message for missing checksum
    And I do not see success message for account deleted

    Examples:
      | Email      | Password      | Name      | Agent   |
      | user1Email | user1Password | user1Name | iphone  |
      | user1Email | user1Password | user1Name | android |
      | user1Email | user1Password | user1Name | osx     |
      | user1Email | user1Password | user1Name | windows |

  @C49970 @utility
  Scenario Outline: Verify that language switch works for <Agent>
    Given I switch to sign in page
    When I navigate to <Page> page for <Agent>
    And I can see language switch button for english on <Page> for <Agent>
    Then I change language to german
    And <Page> page for <Agent> is german
    And I can see language switch button for german on <Page> for <Agent>
    Then I change language to english
    Then <Page> page for <Agent> is english

    Examples:
      | Agent   | Page          |
      | iphone  | start         |
      | iphone  | privacy       |
      | iphone  | legal         |
      | iphone  | job           |
      | iphone  | download      |
      | iphone  | forgot        |
      | iphone  | unsupported   |
      | android | start         |
      | android | privacy       |
      | android | legal         |
      | android | job           |
      | android | download      |
      | android | forgot        |
      | android | unsupported   |
      | osx     | start         |
      | osx     | privacy       |
      | osx     | legal         |
      | osx     | job           |
      | osx     | download      |
      | osx     | forgot        |
      | osx     | unsupported   |
      | windows | start         |
      | windows | privacy       |
      | windows | legal         |
      | windows | job           |
      | windows | download      |
      | windows | forgot        |
      | windows | unsupported   |
    