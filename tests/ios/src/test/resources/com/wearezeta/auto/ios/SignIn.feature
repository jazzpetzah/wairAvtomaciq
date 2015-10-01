Feature: Sign In

  @regression @rc @id340
  Scenario Outline: Sign in to ZClient
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

  #Known issue is IOS-989, once it is fixed test should be updated
  #@staging @id524
  #Scenario Outline: I can change sign in user on iOS
  #Given I Sign in using login <Login> and password <Password>
  #And I see Contact list with my name <UserA>
  #And I tap on my name <UserA>
  #And I click on Settings button on personal page
  #And I click Sign out button from personal page
  #And I Sign in using login <UserB> and password <Password>
  #Then I see Personal page
  #And I see name <UserB> on Personal page
  #And I see email <UserB> on Personal page
  #Examples:
  #| Login   | Password    | UserA   | UserB       |
  #| aqaUser | aqaPassword | aqaUser | aqaContact1 |
  @regression @rc @id1398 @noAcceptAlert
  Scenario Outline: Notification if SignIn credentials are wrong
    Given I see sign in screen
    When I press Sign in button
    And I enter wrong email <WrongMail>
    And I enter wrong password <WrongPassword>
    And I attempt to press Login button
    Then I see wrong credentials notification

    Examples: 
      | WrongMail  | WrongPassword |
      | wrongwrong | wrong         |

  @staging @rc @id1479 @id1403
  Scenario Outline: Verify possibility of reseting password (welcome page)
    Given There is 1 user where <Name> is me
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

  @staging @id2719
  Scenario Outline: Verify phone sign in when email is assigned
    Given There is 1 user where <Name> is me
    Given I see sign in screen
    Given I see country picker button on Sign in screen
    When I enter phone number for user <Name>
    Then I see verification code page
    When I enter verification code for user <Name>
    Then I see Contact list with my name <Name>

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @staging @id2717
  Scenario Outline: Verify first time phone sign in when email is not assigned
    Given There is 1 user where <Name> is me with phone number only
    Given I see sign in screen
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

  @staging @id3813 @noAcceptAlert
  Scenario Outline: Verify impossibility to login with the wrong code
    Given There is 1 user where <Name> is me
    Given I see sign in screen
    And I see country picker button on Sign in screen
    And I enter phone number for user <Name>
    And I see verification code page
    When I enter random verification code
    Then I see wrong credentials notification

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @staging @id3838 @noAcceptAlert
  Scenario Outline: Verify impossibility to resend code within 10 min
    Given There is 1 user where <Name> is me
    Given I see sign in screen
    And I see country picker button on Sign in screen
    And I enter phone number for user <Name>
    And I see verification code page
    When I tap RESEND code button
    Then I see Resend will be possible after 10 min aleart

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @staging @id2724 @noAcceptAlert
  Scenario Outline: Verify impossibility to login with unregistered phone number
    Given There is 1 user where <Name> is me
    Given I see sign in screen
    When I see country picker button on Sign in screen
    And I enter random phone number
    Then I see invalid phone number alert

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @staging @id3851
  Scenario Outline: Verify first time phone sign in when email is not assigned
    Given There is 1 user where <Name> is me with phone number only
    Given I see sign in screen
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
      
  @staging @id3863 @noAcceptAlert
  Scenario Outline: Verify error message appears in case of registering already taken email
    Given There is 1 user where <Name> is me with phone number only
    Given I see sign in screen
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
