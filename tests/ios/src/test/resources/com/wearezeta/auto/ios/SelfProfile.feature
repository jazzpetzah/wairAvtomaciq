Feature: Self Profile

  @C3211 @regression
  Scenario Outline: Change your profile picture
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap settings gear button
    # This is usually enough to have the profile picture loaded
    And I wait for 10 seconds
    And I remember my current profile picture
    And I tap on personal screen
    And I tap Camera Roll button on personal screen
    And I select the first picture from Camera Roll
    And I tap Confirm button on Picture preview page
    Then I wait up to <Timeout> seconds until my profile picture is changed

    Examples:
      | Name      | Timeout |
      | user1Name | 60      |

  @C1092 @regression
  Scenario Outline: Attempt to enter a name with 0 chars
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap settings gear button
    And I tap to edit my name
    And I attempt to input an empty name and press return
    And I see error message asking for more characters
    And I attempt to input an empty name and tap the screen
    And I see error message asking for more characters

    Examples:
      | Name      |
      | user1Name |

  @C1093 @regression
  Scenario Outline: Attempt to enter a name with 1 char
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap settings gear button
    And I tap to edit my name
    And I attempt to enter <username> and press return
    And I see error message asking for more characters
    And I attempt to enter <username> and tap the screen
    And I see error message asking for more characters

    Examples:
      | Name      | username |
      | user1Name | c        |

  @C1097 @regression @rc @clumsy
  Scenario Outline: Verify name change
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap settings gear button
    And I tap to edit my name
    And I attempt to input an empty name and press return
    And I see error message asking for more characters
    And I change my name to <NewUsername>
    And I close self profile
    And I see conversations list
    And I tap settings gear button
    Then I see my new name <NewUsername>

    Examples:
      | Name      | NewUsername |
      | user1Name | New Name    |

  @C1085 @clumsy @rc @regression
  Scenario Outline: Verify adding phone number to the contact signed up with email
    Given There is 1 user where <Name> is me with email only
    Given I sign in using my email
    Given I click Not Now to not add phone number
    Given I accept First Time overlay if it is visible
    Given I dismiss settings warning
    Given I see conversations list
    When I tap settings gear button
    And I tap to add my phone number
    And I enter phone number for Myself
    And I enter registration verification code for Myself
    Then I see phone number attached to profile

    Examples:
      | Name      |
      | user1Name |

  @C1087 @regression
  Scenario Outline: Verify error message appears in case of entering a not valid phone number
    Given There is 1 user where <Name> is me with email only
    Given I sign in using my email
    Given I accept alert
    Given I click Not Now to not add phone number
    Given I accept First Time overlay if it is visible
    Given I dismiss settings warning
    Given I see conversations list
    When I tap settings gear button
    And I tap to add my phone number
    Then I enter 5 digits phone number and expect no commit button

    Examples:
      | Name      |
      | user1Name |

  @C1088 @regression @noAcceptAlert
  Scenario Outline: Verify error message appears in case of registering already taken phone number
    Given There is 1 user where <Name> is me with email only
    Given I sign in using my email
    Given I accept alert
    Given I click Not Now to not add phone number
    Given I accept alert
    Given I accept First Time overlay if it is visible
    Given I accept alert
    Given I dismiss settings warning
    Given I see conversations list
    When I tap settings gear button
    And I tap to add my phone number
    And I input phone number <Number> with code <Code>
    Then I verify the alert contains text <ExpectedText>

    Examples:
      | Name      | Number        | Code | ExpectedText                |
      | user1Name | 8301652248706 | +0   | has already been registered |

  @C1081 @regression @rc
  Scenario Outline: Verify theme switcher is shown on the self profile
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap settings gear button
    Then I see theme switcher button on self profile page

    Examples:
      | Name      |
      | user1Name |

  @C3168 @real
  Scenario Outline: Verify changing profile picture using camera
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap settings gear button
    And I remember my current profile picture
    And I tap on personal screen
    And I tap Camera button on personal screen
    And I tap Take Photo button on Camera page
    And I tap Use Photo button on Picture preview page
    Then I wait up to <Timeout> seconds until my profile picture is changed

    Examples:
      | Name      | Timeout |
      | user1Name | 60      |

  @C1086 @staging
  Scenario Outline: Verify adding email to the contact signed up with phone number
    Given I see sign in screen
    Given I enter phone number for <Name>
    Given I enter activation code
    Given I accept terms of service
    Given I input name <Name> and hit Enter
    Given I press Keep This One button
    Given I tap Share Contacts button on Share Contacts overlay
    Given I see conversations list
    When I tap settings gear button
    And I tap ADD EMAIL ADDRESS AND PASSWORD
    And I enter email <Email>
    And I enter password <Password>
    And I start activation email monitoring
    And I click Create Account Button
    And I see confirmation page
    And I verify registration address
    Then I see email <Email> on Personal page

    Examples:
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |