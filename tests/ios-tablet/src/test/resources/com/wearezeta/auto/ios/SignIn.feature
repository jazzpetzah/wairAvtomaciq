Feature: Sign In

  @C3119 @regression
  Scenario Outline: Sign in to ZClient [PORTRAIT]
    Given There is 1 user where <Name> is me
    Given I rotate UI to portrait
    Given I see sign in screen
    When I switch to Log In tab
    And I have entered login <Login>
    And I have entered password <Password>
    And I tap Login button
    And I accept alert if visible
    And I accept First Time overlay
    And I dismiss settings warning if visible
    Then I see conversations list

    Examples:
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @C3133 @rc @regression
  Scenario Outline: Sign in to ZClient [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I see sign in screen
    When I switch to Log In tab
    And I have entered login <Login>
    And I have entered password <Password>
    And I tap Login button
    And I accept alert if visible
    And I accept First Time overlay
    And I dismiss settings warning if visible
    Then I see conversations list

    Examples:
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @C3132 @regression
  Scenario Outline: Notification if SignIn credentials are wrong [LANDSCAPE]
    Given I see sign in screen
    Given I rotate UI to landscape
    When I switch to Log In tab
    And I enter wrong email <WrongMail>
    And I enter wrong password <WrongPassword>
    And I attempt to tap Login button
    Then I see wrong credentials notification

    Examples:
      | WrongMail  | WrongPassword |
      | wrongwrong | wrong         |

  @C3136 @regression
  Scenario Outline: Verify phone sign in when email is assigned [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I see sign in screen
    Given I switch to Log In tab
    Given I switch to Phone Log In tab
    Given I enter phone number for Myself
    When I enter login verification code for Myself
    And I accept alert if visible
    And I accept First Time overlay
    And I dismiss settings warning if visible
    Then I see conversations list

    Examples:
      | Name      |
      | user1Name |

  @C3134 @regression @useSpecialEmail
  Scenario Outline: Verify first time phone sign in when email is not assigned [LANDSCAPE]
    Given There is 1 user where <Name> is me with phone number only
    Given I rotate UI to landscape
    Given I see sign in screen
    Given I switch to Log In tab
    Given I switch to Phone Log In tab
    When I enter phone number for Myself
    And I enter login verification code for Myself
    And I accept alert if visible
    And I have entered login <Email>
    And I start activation email monitoring
    And I have entered password <Password>
    When I tap Login button
    Then I see email verification reminder
    And I verify email address <Email> for Myself
    And I wait until the UI detects successful email activation
    And I accept First Time overlay
    And I dismiss settings warning if visible
    Then I see conversations list

    Examples:
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @C3137 @regression
  Scenario Outline: Verify impossibility to login with the wrong code [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I see sign in screen
    Given I switch to Log In tab
    Given I switch to Phone Log In tab
    Given I enter phone number for Myself
    When I enter random verification code for Myself
    Then I see wrong credentials notification

    Examples:
      | Name      |
      | user1Name |

  @C3141 @regression
  Scenario Outline: Verify impossibility to login with unregistered phone number [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I see sign in screen
    Given I switch to Log In tab
    Given I switch to Phone Log In tab
    When I enter 10 digits phone number
    Then I see alert contains text <ExpectedText>

    Examples:
      | Name      | ExpectedText               |
      | user1Name | enter a valid phone number |

  @C2861 @regression @useSpecialEmail
  Scenario Outline: Verify adding email to the contact signed up with phone number [LANDSCAPE]
    Given There is 1 user where <Name> is me with phone number only
    Given I rotate UI to landscape
    Given I see sign in screen
    Given I switch to Log In tab
    Given I switch to Phone Log In tab
    When I enter phone number for Myself
    And I enter login verification code for Myself
    And I accept alert if visible
    And I have entered login <Email>
    And I start activation email monitoring
    And I have entered password <Password>
    And I tap Login button
    And I see email verification reminder
    And I verify email address <Email> for Myself
    And I wait until the UI detects successful email activation
    And I accept First Time overlay
    And I dismiss settings warning if visible
    Then I see conversations list
    When I tap settings gear button
    And I select settings item Account
    Then I verify the value of settings item Email equals to "<Email>"

    Examples:
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @C2868 @regression
  Scenario Outline: Verify error message appears in case of registering already taken email [LANDSCAPE]
    Given There is 1 user where <Name> is me with phone number only
    Given I rotate UI to landscape
    Given I see sign in screen
    Given I switch to Log In tab
    Given I switch to Phone Log In tab
    When I enter phone number for Myself
    And I enter login verification code for Myself
    And I accept alert if visible
    And I have entered login <Email>
    And I have entered password <Password>
    And I tap Login button
    Then I see already registered email alert

    Examples:
      | Email                     | Password      | Name      |
      | smoketester@wearezeta.com | user1Password | user1Name |