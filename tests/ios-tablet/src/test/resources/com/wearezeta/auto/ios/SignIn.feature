Feature: Sign In

  @C3119 @regression @id2607
  Scenario Outline: Sign in to ZClient [PORTRAIT]
    Given There is 1 user where <Name> is me
    Given I see sign in screen
    When I switch to Log In tab
    And I have entered login <Login>
    And I have entered password <Password>
    And I press Login button
    And I accept First Time overlay
    And I dismiss settings warning
    Then I see conversations list

    Examples:
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @C3133 @rc @regression @id2925
  Scenario Outline: Sign in to ZClient [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I see sign in screen
    When I switch to Log In tab
    And I have entered login <Login>
    And I have entered password <Password>
    And I press Login button
    And I accept First Time overlay
    And I dismiss settings warning
    Then I see conversations list

    Examples:
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @C3132 @regression @id2924 @noAcceptAlert
  Scenario Outline: Notification if SignIn credentials are wrong [LANDSCAPE]
    Given I see sign in screen
    Given I rotate UI to landscape
    When I switch to Log In tab
    And I enter wrong email <WrongMail>
    And I enter wrong password <WrongPassword>
    And I attempt to press Login button
    Then I see wrong credentials notification

    Examples:
      | WrongMail  | WrongPassword |
      | wrongwrong | wrong         |

  @C3136 @regression @id3818
  Scenario Outline: Verify phone sign in when email is assigned [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I see sign in screen
    Given I switch to Log In tab
    Given I switch to Phone Log In tab
    When I enter phone number for Myself
    When I enter login verification code for Myself
    And I accept First Time overlay
    And I dismiss settings warning
    Then I see conversations list

    Examples:
      | Name      |
      | user1Name |

  @C3134 @regression @id3787 @useSpecialEmail
  Scenario Outline: Verify first time phone sign in when email is not assigned [LANDSCAPE]
    Given There is 1 user where <Name> is me with phone number only
    Given I rotate UI to landscape
    Given I see sign in screen
    Given I switch to Log In tab
    Given I switch to Phone Log In tab
    When I enter phone number for Myself
    When I enter login verification code for Myself
    Then I see set email/password suggesstion page
    When I have entered login <Email>
    And I start activation email monitoring
    And I have entered password <Password>
    When I click DONE keyboard button
    Then I see email verification reminder
    When I verify registration address
    And I dismiss settings warning
    Then I see conversations list

    Examples:
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @C3137 @regression @id3836 @noAcceptAlert
  Scenario Outline: Verify impossibility to login with the wrong code [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I see sign in screen
    Given I switch to Log In tab
    Given I switch to Phone Log In tab
    And I enter phone number for Myself
    When I enter random verification code for Myself
    Then I see wrong credentials notification

    Examples:
      | Name      |
      | user1Name |

  @C3140 @regression @id3840 @noAcceptAlert
  Scenario Outline: Verify impossibility to resend code within 10 min [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I see sign in screen
    Given I switch to Log In tab
    Given I switch to Phone Log In tab
    And I enter phone number for Myself
    When I tap RESEND code button
    Then I see Resend will be possible after 10 min alert

    Examples:
      | Name      |
      | user1Name |

  @C3141 @regression @id3843 @noAcceptAlert
  Scenario Outline: Verify impossibility to login with unregistered phone number [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I see sign in screen
    Given I switch to Log In tab
    Given I switch to Phone Log In tab
    And I enter 10 digits phone number
    Then I verify the alert contains text <ExpectedText>

    Examples:
      | Name      | ExpectedText               |
      | user1Name | enter a valid phone number |

  @C2861 @regression @id3853 @useSpecialEmail
  Scenario Outline: Verify adding email to the contact signed up with phone number [LANDSCAPE]
    Given There is 1 user where <Name> is me with phone number only
    Given I rotate UI to landscape
    Given I see sign in screen
    Given I switch to Log In tab
    Given I switch to Phone Log In tab
    And I enter phone number for Myself
    And I enter login verification code for Myself
    And I see set email/password suggesstion page
    And I have entered login <Email>
    And I start activation email monitoring
    And I have entered password <Password>
    And I click DONE keyboard button
    And I see email verification reminder
    And I verify registration address
    And I dismiss settings warning
    Then I see conversations list
    When I tap settings gear button
    Then I see email <Email> on Personal page

    Examples:
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @C2868 @regression @id3865 @noAcceptAlert
  Scenario Outline: Verify error message appears in case of registering already taken email [LANDSCAPE]
    Given There is 1 user where <Name> is me with phone number only
    Given I rotate UI to landscape
    Given I see sign in screen
    Given I switch to Log In tab
    Given I switch to Phone Log In tab
    When I enter phone number for Myself
    When I enter login verification code for Myself
    And I accept alert
    And I see set email/password suggesstion page
    When I have entered login <Email>
    And I have entered password <Password>
    When I click DONE keyboard button
    Then I see already registered email alert

    Examples:
      | Email                     | Password      | Name      |
      | smoketester@wearezeta.com | user1Password | user1Name |