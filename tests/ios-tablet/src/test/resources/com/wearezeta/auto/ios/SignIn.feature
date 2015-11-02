Feature: Sign In

  @regression @rc @id2607
  Scenario Outline: Sign in to ZClient [PORTRAIT]
    Given There is 1 user where <Name> is me
    Given I see sign in screen
    When I press Sign in button
    And I have entered login <Login>
    And I have entered password <Password>
    And I press Login button
    Then I see Contact list with my name <Name>

    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @regression @id2925
  Scenario Outline: Sign in to ZClient [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I see sign in screen
    When I press Sign in button
    And I have entered login <Login>
    And I have entered password <Password>
    And I press Login button
    Then I see Contact list with my name <Name>

    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @regression @id2749 @noAcceptAlert
  Scenario Outline: Notification if SignIn credentials are wrong [PORTRAIT]
    Given I see sign in screen
    When I press Sign in button
    And I enter wrong email <WrongMail>
    And I enter wrong password <WrongPassword>
    And I attempt to press Login button
    Then I see wrong credentials notification

    Examples: 
      | WrongMail  | WrongPassword |
      | wrongwrong | wrong         |

  @regression @id2924 @noAcceptAlert
  Scenario Outline: Notification if SignIn credentials are wrong [LANDSCAPE]
    Given I see sign in screen
    Given I rotate UI to landscape
    When I press Sign in button
    And I enter wrong email <WrongMail>
    And I enter wrong password <WrongPassword>
    And I attempt to press Login button
    Then I see wrong credentials notification

    Examples: 
      | WrongMail  | WrongPassword |
      | wrongwrong | wrong         |

  @regression @rc @id2608
  Scenario Outline: Verify possibility of reseting password (welcome page) [PORTRAIT]
    Given I see sign in screen
    And I press Sign in button
    And I click on Change Password button on SignIn
    Then I see reset password page
    And I change URL to staging
    And I type in email <Login> to change password
    And I press Change Password button in browser
    And I copy link from email and past it into Safari
    And I type in new password <NewPassword>
    And I press Change Password button in browser
    And Return to Wire app
    And I sign in using my email
    Then I see Contact list with my name <Name>

    Examples: 
      | Login      | Password      | Name      | NewPassword  |
      | user1Email | user1Password | user1Name | aqa123456789 |

  @regression @id2923
  Scenario Outline: Verify possibility of reseting password (welcome page) [LANDSCAPE]
    Given I see sign in screen
    Given I rotate UI to landscape
    And I press Sign in button
    And I click on Change Password button on SignIn
    Then I see reset password page
    And I change URL to staging
    And I type in email <Login> to change password
    And I press Change Password button in browser
    And I copy link from email and past it into Safari
    And I type in new password <NewPassword>
    And I press Change Password button in browser
    And Return to Wire app
    And I sign in using my email
    Then I see Contact list with my name <Name>

    Examples: 
      | Login      | Password      | Name      | NewPassword  |
      | user1Email | user1Password | user1Name | aqa123456789 |

  @regression @id3817
  Scenario Outline: Verify phone sign in when email is assigned [PORTRAIT]
    Given There is 1 user where <Name> is me
    Given I see sign in screen
    When I tap I HAVE AN ACCOUNT button
    Then I see PHONE SIGN IN button
    When I tap on PHONE SIGN IN button
    Then I see country picker button on Sign in screen
    When I enter phone number for user <Name>
    Then I see verification code page
    When I enter verification code for user <Name>
    Then I see Contact list with my name <Name>

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @regression @id3818
  Scenario Outline: Verify phone sign in when email is assigned [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I see sign in screen
    When I tap I HAVE AN ACCOUNT button
    Then I see PHONE SIGN IN button
    When I tap on PHONE SIGN IN button
    Then I see country picker button on Sign in screen
    When I enter phone number for user <Name>
    Then I see verification code page
    When I enter verification code for user <Name>
    Then I see Contact list with my name <Name>

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @regression @id2726
  Scenario Outline: Verify first time phone sign in when email is not assigned [PORTRAIT]
    Given There is 1 user where <Name> is me with phone number only
    Given I see sign in screen
    When I tap I HAVE AN ACCOUNT button
    Then I see PHONE SIGN IN button
    When I tap on PHONE SIGN IN button
    Then I see country picker button on Sign in screen
    When I enter phone number for user <Name>
    Then I see verification code page
    When I enter verification code for user <Name>
    Then I see set email/password suggesstion page
    When I have entered login <Email>
    And I start activation email monitoring
    And I have entered password <Password>
    When I click DONE keyboard button
    Then I see email verification reminder
    When I verify registration address
    Then I see Contact list with my name <Name>

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @regression @id3787
  Scenario Outline: Verify first time phone sign in when email is not assigned [LANDSCAPE]
    Given There is 1 user where <Name> is me with phone number only
    Given I rotate UI to landscape
    Given I see sign in screen
    When I tap I HAVE AN ACCOUNT button
    Then I see PHONE SIGN IN button
    When I tap on PHONE SIGN IN button
    Then I see country picker button on Sign in screen
    When I enter phone number for user <Name>
    Then I see verification code page
    When I enter verification code for user <Name>
    Then I see set email/password suggesstion page
    When I have entered login <Email>
    And I start activation email monitoring
    And I have entered password <Password>
    When I click DONE keyboard button
    Then I see email verification reminder
    When I verify registration address
    Then I see Contact list with my name <Name>

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @regression @id3836 @noAcceptAlert
  Scenario Outline: Verify impossibility to login with the wrong code [PORTRAIT]
    Given There is 1 user where <Name> is me
    Given I see sign in screen
    When I tap I HAVE AN ACCOUNT button
    Then I see PHONE SIGN IN button
    And I tap on PHONE SIGN IN button
    And I see country picker button on Sign in screen
    And I enter phone number for user <Name>
    And I see verification code page
    When I enter random verification code
    Then I see wrong credentials notification

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @regression @id3836 @noAcceptAlert
  Scenario Outline: Verify impossibility to login with the wrong code [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I see sign in screen
    When I tap I HAVE AN ACCOUNT button
    Then I see PHONE SIGN IN button
    And I tap on PHONE SIGN IN button
    And I see country picker button on Sign in screen
    And I enter phone number for user <Name>
    And I see verification code page
    When I enter random verification code
    Then I see wrong credentials notification

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @regression @id3839 @noAcceptAlert
  Scenario Outline: Verify impossibility to resend code within 10 min [PORTRAIT]
    Given There is 1 user where <Name> is me
    Given I see sign in screen
    When I tap I HAVE AN ACCOUNT button
    Then I see PHONE SIGN IN button
    And I tap on PHONE SIGN IN button
    And I see country picker button on Sign in screen
    And I enter phone number for user <Name>
    And I see verification code page
    When I tap RESEND code button
    Then I see Resend will be possible after 10 min aleart

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @regression @id3840 @noAcceptAlert
  Scenario Outline: Verify impossibility to resend code within 10 min [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I see sign in screen
    When I tap I HAVE AN ACCOUNT button
    Then I see PHONE SIGN IN button
    And I tap on PHONE SIGN IN button
    And I see country picker button on Sign in screen
    And I enter phone number for user <Name>
    And I see verification code page
    When I tap RESEND code button
    Then I see Resend will be possible after 10 min aleart

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @regression @id2733 @noAcceptAlert
  Scenario Outline: Verify impossibility to login with unregistered phone number [PORTRAIT]
    Given There is 1 user where <Name> is me
    Given I see sign in screen
    When I tap I HAVE AN ACCOUNT button
    And I see PHONE SIGN IN button
    And I tap on PHONE SIGN IN button
    And I see country picker button on Sign in screen
    And I enter random phone number
    Then I see invalid phone number alert

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @regression @id3843 @noAcceptAlert
  Scenario Outline: Verify impossibility to login with unregistered phone number [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I see sign in screen
    When I tap I HAVE AN ACCOUNT button
    And I see PHONE SIGN IN button
    And I tap on PHONE SIGN IN button
    And I see country picker button on Sign in screen
    And I enter random phone number
    Then I see invalid phone number alert

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @regression @id3852
  Scenario Outline: Verify adding email to the contact signed up with phone number [PORTRAIT]
    Given There is 1 user where <Name> is me with phone number only
    Given I see sign in screen
    When I tap I HAVE AN ACCOUNT button
    And I see PHONE SIGN IN button
    And I tap on PHONE SIGN IN button
    And I see country picker button on Sign in screen
    And I enter phone number for user <Name>
    And I see verification code page
    And I enter verification code for user <Name>
    And I see set email/password suggesstion page
    And I have entered login <Email>
    And I start activation email monitoring
    And I have entered password <Password>
    And I click DONE keyboard button
    And I see email verification reminder
    And I verify registration address
    Then I see Contact list with my name <Name>
    When I tap on my name <Name>
    Then I see email <Email> on Personal page

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @regression @id3853
  Scenario Outline: Verify adding email to the contact signed up with phone number [LANDSCAPE]
    Given There is 1 user where <Name> is me with phone number only
    Given I rotate UI to landscape
    Given I see sign in screen
    When I tap I HAVE AN ACCOUNT button
    And I see PHONE SIGN IN button
    And I tap on PHONE SIGN IN button
    And I see country picker button on Sign in screen
    And I enter phone number for user <Name>
    And I see verification code page
    And I enter verification code for user <Name>
    And I see set email/password suggesstion page
    And I have entered login <Email>
    And I start activation email monitoring
    And I have entered password <Password>
    And I click DONE keyboard button
    And I see email verification reminder
    And I verify registration address
    Then I see Contact list with my name <Name>
    When I tap on my name <Name>
    Then I see email <Email> on Personal page

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |
      
  @regression @id3864 @noAcceptAlert
  Scenario Outline: Verify error message appears in case of registering already taken email [PORTRAIT]
    Given There is 1 user where <Name> is me with phone number only
    Given I see sign in screen
    When I tap I HAVE AN ACCOUNT button
    And I tap on PHONE SIGN IN button
    Then I see country picker button on Sign in screen
    When I enter phone number for user <Name>
    Then I see verification code page
    When I enter verification code for user <Name>
    And I accept alert
    And I see set email/password suggesstion page
    When I have entered login <Email>
    And I have entered password <Password>
    When I click DONE keyboard button
    Then I see invalid email alert

    Examples: 
      | Email                     | Password      | Name      |
      | smoketester@wearezeta.com | user1Password | user1Name |
      
  @regression @id3865 @noAcceptAlert 
  Scenario Outline: Verify error message appears in case of registering already taken email [LANDSCAPE]
    Given There is 1 user where <Name> is me with phone number only
    Given I rotate UI to landscape
    Given I see sign in screen
    When I tap I HAVE AN ACCOUNT button
    And I tap on PHONE SIGN IN button
    Then I see country picker button on Sign in screen
    When I enter phone number for user <Name>
    Then I see verification code page
    When I enter verification code for user <Name>
    And I accept alert
    And I see set email/password suggesstion page
    When I have entered login <Email>
    And I have entered password <Password>
    When I click DONE keyboard button
    Then I see already registered email alert

    Examples: 
      | Email                     | Password      | Name      |
      | smoketester@wearezeta.com | user1Password | user1Name |

  @regression @id3858 @noAcceptAlert
  Scenario Outline: Verify error message appears in case of registering already taken email [PORTRAIT]
    Given There is 1 user where <Name> is me with phone number only
    Given I see sign in screen
    When I tap I HAVE AN ACCOUNT button
    And I tap on PHONE SIGN IN button
    Then I see country picker button on Sign in screen
    When I enter phone number for user <Name>
    Then I see verification code page
    When I enter verification code for user <Name>
    And I accept alert
    And I see set email/password suggesstion page
    When I have entered login <Email>
    And I have entered password <Password>
    When I click DONE keyboard button
    Then I see invalid email alert

    Examples: 
      | Email        | Password      | Name      |
      | invalidemail | user1Password | user1Name |

  @regression @id3859 @noAcceptAlert
  Scenario Outline: Verify error message appears in case of registering already taken email [LANDSCAPE]
    Given There is 1 user where <Name> is me with phone number only
    Given I rotate UI to landscape
    Given I see sign in screen
    When I tap I HAVE AN ACCOUNT button
    And I tap on PHONE SIGN IN button
    Then I see country picker button on Sign in screen
    When I enter phone number for user <Name>
    Then I see verification code page
    When I enter verification code for user <Name>
    And I accept alert
    And I see set email/password suggesstion page
    When I have entered login <Email>
    And I have entered password <Password>
    When I click DONE keyboard button
    Then I see invalid email alert

    Examples: 
      | Email        | Password      | Name      |
      | invalidemail | user1Password | user1Name |
