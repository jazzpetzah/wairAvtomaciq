Feature: Settings

  @C2905 @regression @fastLogin
  Scenario Outline: Verify user can access settings [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap settings gear button
    Then I see settings page

    Examples:
      | Name      |
      | user1Name |

  @C2906 @regression @rc @fastLogin
  Scenario Outline: Attempt to open About screen in settings [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
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

  @C2907 @regression @fastLogin
  Scenario Outline: Verify reset password page is accessible from settings [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap settings gear button
    And I select settings item Account
    And I select settings item Reset Password
    # Wait until the web page is fully loaded
    And I wait for 8 seconds
    Then I see Reset Password page

    Examples:
      | Name      |
      | user1Name |

  @C2908 @regression
  Scenario Outline: Verify default value for sound settings is all [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap settings gear button
    And I select settings item Options
    Then I verify the value of settings item Sound Alerts equals to "All"

    Examples:
      | Name      |
      | user1Name |

  @C2909 @regression
  Scenario Outline: Verify you can access Help site within the app [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap settings gear button
    And I select settings item Support
    And I select settings item Wire Support Website
    Then I see Support web page

    Examples:
      | Name      |
      | user1Name |

  @C145961 @rc @regression @useSpecialEmail
  Scenario Outline: Verify deleting the account registered by email [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap settings gear button
    And I select settings item Account
    And I start waiting for <Name> account removal notification
    And I select settings item Delete Account
    And I accept alert
    Then I see sign in screen
    And I verify account removal notification is received

    Examples:
      | Name      |
      | user1Name |

  @C2883 @regression @fastLogin
  Scenario Outline: Self profile. Verify max limit in 64 chars [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I tap settings gear button
    Given I select settings item Account
    Given I select settings item Name
    When I clear Name input field on Settings page
    And I set "<NewUsername>" value to Name input field on Settings page
    And I tap Return button on the keyboard
    Then I verify the value of settings item Name equals to "<NewUsername>"
    When I select settings item Name
    And I clear Name input field on Settings page
    And I set "<NewUsername1>" value to Name input field on Settings page
    And I tap Return button on the keyboard
    Then I verify the value of settings item Name equals to "<NewUsername1>"

    Examples:
      | Name      | NewUsername                                                          | NewUsername1                                                     |
      | user1Name | mynewusernamewithmorethan64characters3424245345345354353452345234535 | mynewusernamewithmorethan64characters342424534534535435345234523 |

  @C2869 @regression @fastLogin
  Scenario Outline: Change your profile picture [PORTRAIT]
    Given There is 1 user where <Name> is me
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap settings gear button
    # This is usually enough to have the profile picture loaded
    And I wait for 7 seconds
    And I select settings item Account
    And I select settings item Picture
    And I remember my current profile picture
    And I tap Camera Roll button on Camera page
    And I accept alert
    And I select the first picture from Camera Roll
    And I tap Confirm button on Picture preview page
    Then I wait up to <Timeout> seconds until my profile picture is changed

    Examples:
      | Name      | Timeout |
      | user1Name | 60      |

  @C2875 @rc @regression @fastLogin
  Scenario Outline: Change your profile picture [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap settings gear button
    # This is usually enough to have the profile picture loaded
    And I wait for 7 seconds
    And I select settings item Account
    And I select settings item Picture
    And I remember my current profile picture
    And I tap Camera Roll button on Camera page
    And I accept alert
    And I select the first picture from Camera Roll
    And I tap Confirm button on Picture preview page
    Then I wait up to <Timeout> seconds until my profile picture is changed

    Examples:
      | Name      | Timeout |
      | user1Name | 60      |

  @C2878 @C2886 @regression @fastLogin
  Scenario Outline: Attempt to enter a name with 0/1 chars [PORTRAIT]
    Given There is 1 user where <Name> is me
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I tap settings gear button
    Given I select settings item Account
    Given I select settings item Name
    When I clear Name input field on Settings page
    And I tap Return button on the keyboard
    Then I verify the alert contains text <ExpectedAlertText>
    And I accept alert
    When I clear Name input field on Settings page
    And I set "<OneCharName>" value to Name input field on Settings page
    And I tap Return button on the keyboard
    Then I verify the alert contains text <ExpectedAlertText>

    Examples:
      | Name      | ExpectedAlertText | OneCharName |
      | user1Name | name is too short | c           |

  @C2888 @rc @regression @fastLogin
  Scenario Outline: Verify name change [LANDSCAPE]
    Given There are 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
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
      | user1Name | NewName     |

  @C2856 @regression @fastLogin
  Scenario Outline: Verify changing and applying accent color [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User Myself changes accent color to <Color1>
    Given User Myself removes his avatar picture
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I tap settings gear button
    Given I select settings item Account
    When I select settings item Color
    And I remember the state of Color Picker
    And I set my accent color to <Color2> on Settings page
    Then I verify the state of Color Picker is changed

    Examples:
      | Name      | Color1 | Color2          | Contact   |
      | user1Name | Violet | StrongLimeGreen | user2Name |

  @C2860 @regression
  Scenario Outline: Verify adding phone number to the contact signed up with email [PORTRAIT]
    Given There is 1 users where <Name> is me with email only
    Given I switch to Log In tab
    Given I have entered login <Email>
    Given I have entered password <Password>
    Given I tap Login button
    Given I accept alert
    Given I tap Not Now to not add phone number
    Given I accept First Time overlay
    Given I dismiss settings warning
    Given I see conversations list
    Given I tap settings gear button
    Given I select settings item Account
    When I select settings item Add phone number
    And I enter phone number for Myself
    And I enter registration verification code for Myself
    Then I see conversations list
    When I tap settings gear button
    And I select settings item Account
    Then I verify the value of settings item Phone equals to "<MyPhoneNumber>"

    Examples:
      | Name      | MyPhoneNumber    | Email      | Password      |
      | user1Name | user1PhoneNumber | user1Email | user1Password |

  @C2866 @regression
  Scenario Outline: Verify error message appears in case of registering already taken phone number [LANDSCAPE]
    Given There is 1 users where <Name> is me with email only
    Given I rotate UI to landscape
    Given I switch to Log In tab
    Given I have entered login <Email>
    Given I have entered password <Password>
    Given I tap Login button
    Given I accept alert
    Given I tap Not Now to not add phone number
    Given I accept First Time overlay
    Given I dismiss settings warning
    Given I see conversations list
    Given I tap settings gear button
    Given I select settings item Account
    When I select settings item Add phone number
    And I input phone number <Number> with code <Code>
    Then I verify the alert contains text <ExpectedText>

    Examples:
      | Name      | Number        | Code | ExpectedText                | Email      | Password      |
      | user1Name | 8301652248706 | +0   | has already been registered | user1Email | user1Password |

  @C2855 @rc @regression @fastLogin
  Scenario Outline: Verify theme switcher is not shown on the self profile [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I tap settings gear button
    When I select settings item Options
    Then I do not see settings item <ThemeItemName>

    Examples:
      | Name      | ThemeItemName |
      | user1Name | Dark theme    |
