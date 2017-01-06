Feature: Sign In

  @C1134 @regression @rc @clumsy
  Scenario Outline: Sign in to ZClient
    Given There is 1 user where <Name> is me
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

  @C1133 @regression @rc @clumsy
  Scenario Outline: Notification if SignIn credentials are wrong
    Given I see sign in screen
    When I switch to Log In tab
    And I enter wrong email <WrongMail>
    And I enter wrong password <WrongPassword>
    And I attempt to tap Login button
    Then I see wrong credentials notification

    Examples:
      | WrongMail  | WrongPassword |
      | wrongwrong | wrong         |

  @C1138 @regression
  Scenario Outline: Verify phone sign in when email is assigned
    Given There is 1 user where <Name> is me
    Given I see sign in screen
    Given I switch to Log In tab
    Given I switch to Phone Log In tab
    When I enter phone number for Myself
    And I enter login verification code for Myself
    And I accept alert if visible
    And I accept First Time overlay
    And I dismiss settings warning if visible
    Then I see conversations list

    Examples:
      | Name      |
      | user1Name |

  @C1145 @regression
  Scenario Outline: Verify impossibility to login with the wrong code
    Given There is 1 user where <Name> is me
    Given I see sign in screen
    Given I switch to Log In tab
    Given I switch to Phone Log In tab
    And I enter phone number for Myself
    When I enter random verification code for Myself
    Then I see wrong credentials notification

    Examples:
      | Name      |
      | user1Name |

  @C1143 @regression
  Scenario Outline: Verify impossibility to login with unregistered phone number
    Given I see sign in screen
    Given I switch to Log In tab
    Given I switch to Phone Log In tab
    When I enter <Count> digits phone number
    Then I see alert contains text <ExpectedText>

    Examples:
      | Count | ExpectedText               |
      | 16    | enter a valid phone number |

  @C1136 @rc @regression @useSpecialEmail
  Scenario Outline: Verify first time phone sign in when email is not assigned
    Given There is 1 user where <Name> is me with phone number only
    Given I see sign in screen
    Given I switch to Log In tab
    Given I switch to Phone Log In tab
    When I enter phone number for Myself
    And I enter login verification code for Myself
    And I accept alert if visible
    And I have entered login <Email>
    And I start activation email monitoring
    And I have entered password <Password>
    And I tap Done keyboard button
    And I see email verification reminder
    And I verify registration address
    And I accept First Time overlay
    And I dismiss settings warning if visible
    Then I see conversations list
    When I tap settings gear button
    And I select settings item Account
    Then I verify the value of settings item Email equals to "<Email>"

    Examples:
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @C3505 @regression
  Scenario Outline: Verify error message appears in case of registering already taken email
    Given There is 1 user where <Name> is me with phone number only
    Given I see sign in screen
    Given I switch to Log In tab
    Given I switch to Phone Log In tab
    When I enter phone number for Myself
    And I enter login verification code for Myself
    And I accept alert if visible
    And I have entered login <Email>
    And I have entered password <Password>
    And I tap Done keyboard button
    Then I see already registered email alert

    Examples:
      | Email                     | Password      | Name      |
      | smoketester@wearezeta.com | user1Password | user1Name |

  @C1147 @regression
  Scenario Outline: Verify error message appears in case of entering not valid email address
    Given There is 1 user where <Name> is me with phone number only
    Given I see sign in screen
    Given I switch to Log In tab
    Given I switch to Phone Log In tab
    When I enter phone number for Myself
    And I enter login verification code for Myself
    And I accept alert if visible
    And I have entered login <Email>
    And I have entered password <Password>
    And I tap Done keyboard button
    Then I see invalid email alert

    Examples:
      | Email        | Password      | Name      |
      | invalidemail | user1Password | user1Name |
