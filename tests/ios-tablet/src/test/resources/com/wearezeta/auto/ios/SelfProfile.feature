Feature: Self Profile

  @C2883 @regression @id3157
  Scenario Outline: Self profile. Verify max limit in 64 chars [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap my avatar
    And I tap to edit my name
    And I change my name to <NewUsername>
    Then I see my new name <NewUsername1>
    When I close self profile
    And I tap my avatar
    And I tap to edit my name
    And I change my name to <NewUsername>
    Then I see my new name <NewUsername1>

    Examples:
      | Name      | NewUsername                                                          | NewUsername1                                                     |
      | user1Name | mynewusernamewithmorethan64characters3424245345345354353452345234535 | mynewusernamewithmorethan64characters342424534534535435345234523 |

  @C2869 @regression @rc @id2574
  Scenario Outline: Change your profile picture [PORTRAIT]
    Given There is 1 user where <Name> is me
    Given I Sign in on tablet using my email
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

  @C2875 @regression @id3159
  Scenario Outline: Change your profile picture [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
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

  @C2878 @regression @rc @id2582
  Scenario Outline: Attempt to enter a name with 0 chars [PORTRAIT]
    Given There is 1 user where <Name> is me
    Given I Sign in on tablet using my email
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

  @C2886 @regression @id3161
  Scenario Outline: Verify 2 chars limit [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap my avatar
    And I tap to edit my name
    And I attempt to enter <username1char> and press return
    Then I see error message asking for more characters
    And I attempt to enter <username1char> and tap the screen
    And I see error message asking for more characters
    And I attempt to enter <username2chars> and press return
    Then I see my new name <username2chars>

    Examples:
      | Name      | username1char | username2chars |
      | user1Name | c             | AB             |

  @C2888 @regression @id3163
  Scenario Outline: Verify name change [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User <Name> sends 1 encrypted message to user <Contact>
    When I tap my avatar
    And I tap to edit my name
    And I change my name to <NewUsername>
    And I close self profile
    And I see conversations list
    And I tap on contact name <Contact>
    Then I see my user name <NewUsername> in conversation

    Examples:
      | Name      | NewUsername | Contact   |
      | user1Name | NewName     | user2Name |

  @C2856 @regression @rc @id2571
  Scenario Outline: Verify changing and applying accent color [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Name> change accent color to <Color1>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap my avatar
    And I slide my accent color via the colorpicker from <Color1> to <Color2>
    And I close self profile

    Examples:
      | Name      | Color1 | Color2          | Contact   |
      | user1Name | Violet | StrongLimeGreen | user2Name |

  @C2860 @rc @regression @id3850
  Scenario Outline: Verify adding phone number to the contact signed up with email [PORTRAIT]
    Given There is 1 users where <Name> is me with email only
    Given I Sign in on tablet using my email
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

  @C2864 @regression @noAcceptAlert @id3856
  Scenario Outline: Verify error message appears in case of entering a not valid phone number [LANDSCAPE]
    Given There is 1 users where <Name> is me with email only
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
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

  @C2866 @regression @noAcceptAlert @id3862 @ZIOS-5836
  Scenario Outline: Verify error message appears in case of registering already taken phone number [LANDSCAPE]
    Given There is 1 users where <Name> is me with email only
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
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

  @C2854 @regression @rc @id3986
  Scenario Outline: Verify theme switcher is not shown on the self profile [PORTRAIT]
    Given There is 1 user where <Name> is me
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap my avatar
    Then I dont see theme switcher button on self profile page

    Examples:
      | Name      |
      | user1Name |