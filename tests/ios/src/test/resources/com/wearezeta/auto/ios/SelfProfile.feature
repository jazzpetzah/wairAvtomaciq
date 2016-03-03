Feature: Self Profile

  @C3211 @regression @id344
  Scenario Outline: Change your profile picture
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap my avatar
    # This is usually enough to have the profile picture loaded
    And I wait for 10 seconds
    And I remember my current profile picture
    And I tap on personal screen
    And I press Camera button
    And I choose a picture from camera roll
    And I press Confirm button
    Then I wait up to <Timeout> seconds until my profile picture is changed

    Examples:
      | Name      | Timeout |
      | user1Name | 60      |

  @C1092 @regression @id1055
  Scenario Outline: Attempt to enter a name with 0 chars
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap my avatar
    And I tap to edit my name
    And I attempt to input an empty name and press return
    And I see error message asking for more characters
    And I attempt to input an empty name and tap the screen
    And I see error message asking for more characters

    Examples:
      | Name      |
      | user1Name |

  @C1093 @regression @id1056
  Scenario Outline: Attempt to enter a name with 1 char
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap my avatar
    And I tap to edit my name
    And I attempt to enter <username> and press return
    And I see error message asking for more characters
    And I attempt to enter <username> and tap the screen
    And I see error message asking for more characters

    Examples:
      | Name      | username |
      | user1Name | c        |

  @C1097 @regression @rc @clumsy @id1463
  Scenario Outline: Verify name change
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap my avatar
    And I tap to edit my name
    And I attempt to input an empty name and press return
    And I see error message asking for more characters
    And I change my name to <NewUsername>
    And I close self profile
    And I see conversations list
    And I tap my avatar
    Then I see my new name <NewUsername>

    Examples:
      | Name      | NewUsername |
      | user1Name | New Name    |

  @C1085 @clumsy @rc @regression @id3849
  Scenario Outline: Verify adding phone number to the contact signed up with email
    Given There is 1 user where <Name> is me with email only
    Given I sign in using my email
    Given I click Not Now to not add phone number
    Given I accept First Time overlay if it is visible
    Given I dismiss settings warning
    Given I see conversations list
    When I tap my avatar
    And I tap to add my phone number
    And I see country picker button on Sign in screen
    And I enter phone number and verification code
    Then I see phone number attached to profile

    Examples:
      | Name      |
      | user1Name |

  @C1087 @regression @noAcceptAlert @id3854
  Scenario Outline: Verify error message appears in case of entering a not valid phone number
    Given There is 1 user where <Name> is me with email only
    Given I sign in using my email
    Given I accept alert
    Given I click Not Now to not add phone number
    Given I accept alert
    Given I accept First Time overlay if it is visible
    Given I accept alert
    Given I dismiss settings warning
    Given I see conversations list
    When I tap my avatar
    And I tap to add my phone number
    And I see country picker button on Sign in screen
    And I enter invalid phone number
    Then I see invalid phone number alert

    Examples:
      | Name      |
      | user1Name |

  @C1088 @regression @noAcceptAlert @id3860 @ZIOS-5836
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
    When I tap my avatar
    And I tap to add my phone number
    And I see country picker button on Sign in screen
    And I input phone number <Number> with code <Code>
    Then I see already registered phone number alert

    Examples:
      | Name      | Number        | Code |
      | user1Name | 8301652248706 | +0   |

  @C1081 @regression @rc @id3990
  Scenario Outline: Verify theme switcher is shown on the self profile
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap my avatar
    Then I see theme switcher button on self profile page

    Examples:
      | Name      |
      | user1Name |