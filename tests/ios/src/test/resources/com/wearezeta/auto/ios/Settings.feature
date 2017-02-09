Feature: Settings

  @C1098 @regression @fastLogin
  Scenario Outline: Verify user can access settings
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap settings gear button
    Then I see settings page

    Examples:
      | Name      |
      | user1Name |

  @C1099 @regression @rc @fastLogin
  Scenario Outline: Attempt to open About screen in settings
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap settings gear button
    And I select settings item About
    Then I see settings item Privacy Policy
    And I see settings item Terms of Use
    And I see settings item License Information
    When I tap Done navigation button on Settings page
    Then I see conversations list

    Examples:
      | Name      |
      | user1Name |

  @C1102 @regression @fastLogin
  Scenario Outline: Verify reset password page is accessible from settings
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap settings gear button
    And I select settings item Account
    And I select settings item Reset Password
    And I wait for 9 seconds
    Then I see Reset Password page

    Examples:
      | Name      |
      | user1Name |

  @C1107 @regression @fastLogin
  Scenario Outline: Verify default value for sound settings is all
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap settings gear button
    And I select settings item Options
    Then I verify the value of settings item Sound Alerts equals to "<ExpectedValue>"

    Examples:
      | Name      | ExpectedValue |
      | user1Name | All           |

  @C1109 @regression @fastLogin
  Scenario Outline: Verify you can access Help site within the app
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap settings gear button
    And I select settings item Support
    And I select settings item Wire Support Website
    And I wait for 4 seconds
    Then I see Support web page

    Examples:
      | Name      |
      | user1Name |

  @C3247 @regression @rc @useSpecialEmail @fastLogin
  Scenario Outline: Verify deleting the account registered by email
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap settings gear button
    And I select settings item Account
    And I start waiting for <Name> account removal notification
    And I select settings item Delete Account
    And I accept alert if visible
    Then I see sign in screen
    And I verify account removal notification is received

    Examples:
      | Name      |
      | user1Name |

  @C3247 @staging
  Scenario Outline: Verify deleting the account registered by phone
    Given I see sign in screen
    Given I enter phone number for <Name>
    Given I enter activation code
    Given I accept terms of service
    Given I input name <Name> and commit it
    Given I accept alert if visible
    Given I tap Keep This One button
    Given I tap Not Now button on Share Contacts overlay
    Given I tap Keep This One button on Unique Username Takeover page
    Given I see conversations list
    Given I tap settings gear button
    Given I select settings item Account
    Given I select settings item Delete Account
    When I see alert contains text <AlertText>
    And I accept alert
    Then I see sign in screen

    Examples:
      | Name      | AlertText |
      | user1Name | delete    |

  @C3211 @rc @regression @fastLogin
  Scenario Outline: Change your profile picture
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap settings gear button
    # Wait until profile picture is fully loaded
    Given I wait for 7 seconds
    Given I select settings item Account
    Given I select settings item Picture
    When I remember my current profile picture
    And I tap Camera Roll button on Camera page
    # Wait for camera toll opening animation
    And I wait for 3 seconds
    And I accept alert if visible
    And I select the first picture from Camera Roll
    And I tap Confirm button on Picture preview page
    Then I wait up to <Timeout> seconds until my profile picture is changed

    Examples:
      | Name      | Timeout |
      | user1Name | 60      |

  @C1092 @C1093 @regression @fastLogin
  Scenario Outline: Attempt to enter a name with 0/1 chars
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap settings gear button
    Given I select settings item Account
    Given I select settings item Name
    When I clear Name input field on Settings page
    And I tap Return button on the keyboard
    And I tap Return button on the keyboard if visible
    Then I see alert contains text <ExpectedAlertText>
    And I accept alert
    And I set "<OneCharName>" value to Name input field on Settings page
    And I tap Return button on the keyboard
    Then I see alert contains text <ExpectedAlertText>

    Examples:
      | Name      | ExpectedAlertText | OneCharName |
      | user1Name | name is too short | c           |

  @C1097 @regression @rc @clumsy @fastLogin
  Scenario Outline: Verify name change
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap settings gear button
    Given I select settings item Account
    Given I select settings item Name
    When I clear Name input field on Settings page
    And I set "<NewUsername>" value to Name input field on Settings page
    And I tap Return button on the keyboard
    And I tap Done navigation button on Settings page
    And I see conversations list
    And I tap settings gear button
    And I select settings item Account
    Then I verify the value of settings item Name equals to "<NewUsername>"

    Examples:
      | Name      | NewUsername |
      | user1Name | New Name    |

  @C1085 @clumsy @rc @regression
  Scenario Outline: Verify adding phone number to the contact signed up with email
    Given There is 1 user where <Name> is me with email only
    Given I switch to Log In tab
    Given I have entered login <Email>
    Given I have entered password <Password>
    Given I tap Login button
    Given I accept alert if visible
    Given I tap Not Now to not add phone number
    Given I accept First Time overlay
    Given I dismiss settings warning if visible
    Given I see conversations list
    Given I tap settings gear button
    Given I select settings item Account
    When I select settings item Add phone number
    And I enter phone number for Myself
    And I enter registration verification code for Myself
    And I tap settings gear button
    And I select settings item Account
    Then I verify the value of settings item Phone equals to "<MyPhoneNumber>"

    Examples:
      | Name      | MyPhoneNumber    | Email      | Password      |
      | user1Name | user1PhoneNumber | user1Email | user1Password |

  @C1087 @regression
  Scenario Outline: Verify error message appears in case of entering a not valid phone number
    Given There is 1 user where <Name> is me with email only
    Given I switch to Log In tab
    Given I have entered login <Email>
    Given I have entered password <Password>
    Given I tap Login button
    Given I accept alert if visible
    Given I tap Not Now to not add phone number
    Given I accept First Time overlay
    Given I dismiss settings warning if visible
    Given I see conversations list
    Given I tap settings gear button
    Given I select settings item Account
    When I select settings item Add phone number
    Then I enter <DigitsCount> digits phone number and expect no commit button

    Examples:
      | Name      | DigitsCount | Email      | Password      |
      | user1Name | 5           | user1Email | user1Password |

  @C1088 @regression
  Scenario Outline: Verify error message appears in case of registering already taken phone number
    Given There is 1 user where <Name> is me with email only
    Given I switch to Log In tab
    Given I have entered login <Email>
    Given I have entered password <Password>
    Given I tap Login button
    Given I accept alert if visible
    Given I tap Not Now to not add phone number
    Given I accept First Time overlay
    Given I dismiss settings warning if visible
    Given I see conversations list
    Given I tap settings gear button
    Given I select settings item Account
    When I select settings item Add phone number
    And I input phone number <Number> with code <Code>
    Then I see alert contains text <ExpectedText>

    Examples:
      | Name      | Number        | Code | ExpectedText                | Email      | Password      |
      | user1Name | 8301652248706 | +0   | has already been registered | user1Email | user1Password |

  @C1081 @regression @rc @fastLogin
  Scenario Outline: Verify theme switcher is shown in settings
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap settings gear button
    When I select settings item Options
    Then I see settings item <ThemeItemName>

    Examples:
      | Name      | ThemeItemName |
      | user1Name | Dark Theme    |

  @C3168 @real @real_rc @fastLogin
  Scenario Outline: Verify changing profile picture using camera
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap settings gear button
    And I select settings item Account
    And I select settings item Picture
    And I remember my current profile picture
    And I tap Take Photo button on Camera page
    And I accept alert if visible
    And I tap Take Photo button on Camera page
    And I tap Confirm button on Picture preview page
    And I accept alert if visible
    Then I wait up to <Timeout> seconds until my profile picture is changed

    Examples:
      | Name      | Timeout |
      | user1Name | 60      |

  @C1086 @regression @useSpecialEmail
  Scenario Outline: Verify adding email to the contact signed up with phone number
    Given I see sign in screen
    Given I enter phone number for <Name>
    Given I enter activation code
    Given I accept terms of service
    Given I input name <Name> and commit it
    Given I accept alert if visible
    Given I tap Keep This One button
    # Wait for sync
    Given I wait for 3 seconds
    Given I accept alert if visible
    Given User <Name> sets the unique username
    Given I tap Share Contacts button on Share Contacts overlay
    Given I accept alert if visible
    Given I dismiss settings warning if visible
    Given I see conversations list
    Given I tap settings gear button
    Given I select settings item Account
    When I select settings item Add email address and password
    And I enter email <Email>
    And I enter password <Password>
    And I start activation email monitoring
    And I tap Create Account button on Registration page
    And I see confirmation page
    And I verify email address <Email> for <Name>
    And User <Name> is me
    And I wait until the UI detects successful email activation
    And I tap settings gear button
    And I select settings item Account
    Then I verify the value of settings item Email equals to "<Email>"

    Examples:
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @C315479 @real @fastLogin
  Scenario Outline: Verify Browser option is shown if Chrome or Firefox is installed
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap settings gear button
    Given I select settings item Options
    When I select settings item Browser
    # This test requires Chrome browser to be already installed on the device under test
    Then I see settings item <AlternativeBrowserName>

    Examples:
      | Name      | AlternativeBrowserName |
      | user1Name | Chrome                 |

  @C404391 @regression @useSpecialEmail @fastLogin
  Scenario Outline: Verify changing email when phone is not assigned
    Given There is 1 user where <Name> is me with email only
    Given I sign in using my email
    Given I see conversations list
    Given I tap settings gear button
    Given I select settings item Account
    Given I select settings item Email
    When I start activation email monitoring on mailbox <NewEmail> with password <Password>
    And I change email address to <NewEmail> on Settings page
    And I tap Save navigation button on Settings page
    # Wait for sync
    And I wait for 3 seconds
    And I verify email address <NewEmail> for Myself
    And I wait until the UI detects successful email activation on Settings page
    Then I verify the value of settings item Email equals to "<NewEmail>"
    And I verify user's Myself email on the backend is equal to <NewEmail>

    Examples:
      | Password      | Name      | NewEmail   |
      | user1Password | user1Name | user2Email |

  @C404408 @regression @fastLogin
  Scenario Outline: Verify impossibility of changing email on already taken email
    Given There are 2 users where <Name> is me
    Given I sign in using my email
    Given I see conversations list
    Given I tap settings gear button
    Given I select settings item Account
    Given I select settings item Email
    When I change email address to <NewEmail> on Settings page
    And I tap Save navigation button on Settings page
    Then I see alert contains text <AlertText>

    Examples:
      | Name      | NewEmail   | AlertText                                                  |
      | user1Name | user2Email | The email address you provided has already been registered |
    