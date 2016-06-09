Feature: Self Profile

  @C2883 @regression @id3157
  Scenario Outline: Self profile. Verify max limit in 64 chars [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap settings gear button
    And I tap to edit my name
    And I change my name to <NewUsername>
    Then I see my new name <NewUsername1>
    When I close self profile
    And I tap settings gear button
    And I tap to edit my name
    And I change my name to <NewUsername>
    Then I see my new name <NewUsername1>

    Examples:
      | Name      | NewUsername                                                          | NewUsername1                                                     |
      | user1Name | mynewusernamewithmorethan64characters3424245345345354353452345234535 | mynewusernamewithmorethan64characters342424534534535435345234523 |

  @C2869 @regression @id2574
  Scenario Outline: Change your profile picture [PORTRAIT]
    Given There is 1 user where <Name> is me
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap settings gear button
    # This is usually enough to have the profile picture loaded
    And I wait for 10 seconds
    And I remember my current profile picture
    And I tap on personal screen
    And I press Camera Roll button
    And I choose a picture from camera roll
    And I confirm my choice
    Then I wait up to <Timeout> seconds until my profile picture is changed

    Examples:
      | Name      | Timeout |
      | user1Name | 60      |

  @C2875 @rc @regression @id3159
  Scenario Outline: Change your profile picture [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap settings gear button
    # This is usually enough to have the profile picture loaded
    And I wait for 10 seconds
    And I remember my current profile picture
    And I tap on personal screen
    And I press Camera Roll button
    And I choose a picture from camera roll
    And I confirm my choice
    Then I wait up to <Timeout> seconds until my profile picture is changed

    Examples:
      | Name      | Timeout |
      | user1Name | 60      |

  @C2878 @regression @id2582
  Scenario Outline: Attempt to enter a name with 0 chars [PORTRAIT]
    Given There is 1 user where <Name> is me
    Given I Sign in on tablet using my email
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

  @C2886 @regression @id3161
  Scenario Outline: Verify 2 chars limit [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap settings gear button
    # This alert is not accepted automatically
    And I accept alert
    And I tap to edit my name
    And I attempt to enter <username1char> and press return
    Then I see error message asking for more characters

    Examples:
      | Name      | username1char |
      | user1Name | c             |

  @C2888 @rc @regression @id3163
  Scenario Outline: Verify name change [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User <Name> sends 1 encrypted message to user <Contact>
    When I tap settings gear button
    And I tap to edit my name
    And I change my name to <NewUsername>
    And I close self profile
    And I tap on contact name <Contact>
    Then I see my user name <NewUsername> in conversation

    Examples:
      | Name      | NewUsername | Contact   |
      | user1Name | NewName     | user2Name |

  @C2856 @regression @id2571
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

  @C2860 @regression @id3850
  Scenario Outline: Verify adding phone number to the contact signed up with email [PORTRAIT]
    Given There is 1 users where <Name> is me with email only
    Given I Sign in on tablet using my email
    Given I click Not Now to not add phone number
    Given I accept First Time overlay if it is visible
    Given I dismiss settings warning
    Given I see conversations list
    When I tap settings gear button
    And I tap to add my phone number
    And I enter phone number and verification code
    Then I see phone number attached to profile

    Examples:
      | Name      |
      | user1Name |

  @C2866 @regression @noAcceptAlert @id3862
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

  @C2855 @rc @regression @id3986
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