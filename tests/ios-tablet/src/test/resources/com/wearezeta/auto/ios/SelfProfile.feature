Feature: Self Profile

  @C2883 @regression @fastLogin
  Scenario Outline: Self profile. Verify max limit in 64 chars [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap settings gear button
    And I select settings item Account
    And I tap to edit my name
    And I change my name to <NewUsername>
    Then I verify the value of settings item Name equals to "<NewUsername>"
    When I close self profile
    And I tap settings gear button
    And I tap to edit my name
    And I change my name to <NewUsername1>
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
    And I tap OK button on the alert
    When I clear Name input field on Settings page
    And I set "<OneCharName>" value to Name input field on Settings page
    And I tap Return button on the keyboard
    Then I verify the alert contains text <ExpectedAlertText>

    Examples:
      | Name      | ExpectedAlertText     | OneCharName |
      | user1Name | AT LEAST 2 CHARACTERS | c           |

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
    When I tap settings gear button
    And I remember the state of color picker
    And I set my accent color to <Color2>
    Then I verify the state of color picker is changed

    Examples:
      | Name      | Color1 | Color2          | Contact   |
      | user1Name | Violet | StrongLimeGreen | user2Name |

  @C2860 @regression
  Scenario Outline: Verify adding phone number to the contact signed up with email [PORTRAIT]
    Given There is 1 users where <Name> is me with email only
    Given I Sign in on tablet using my email
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

  @C2866 @regression @noAcceptAlert
  Scenario Outline: Verify error message appears in case of registering already taken phone number [LANDSCAPE]
    Given There is 1 users where <Name> is me with email only
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
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

  @C2855 @rc @regression @fastLogin
  Scenario Outline: Verify theme switcher is not shown on the self profile [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap settings gear button
    Then I dont see theme switcher button on self profile page

    Examples:
      | Name      |
      | user1Name |