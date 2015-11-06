Feature: Registration

  @regression @rc @id589
  Scenario Outline: Register new user using photo album
    Given I see sign in screen
    When I enter phone number for user <Name>
    And I enter activation code
    And I accept terms of service
    And I input name <Name> and hit Enter
    And I press Picture button
    And I choose a picture from camera roll
    And I See selected picture
    And I confirm selection
    And I see Contact list with my name <Name>

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @staging @id304
  Scenario Outline: Attempt to register an email with spaces
    Given I see sign in screen
    When I press Join button
    And I press Picture button
    And I choose a picture from camera roll
    And I See selected picture
    And I confirm selection
    And I input name <Name> and hit Enter
    And I attempt to enter an email with spaces <Email>
    Then I verify no spaces are present in email

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @staging @id304
  Scenario Outline: Attempt to register an email with incorrect format
    Given I see sign in screen
    When I press Join button
    And I press Picture button
    And I choose a picture from camera roll
    And I See selected picture
    And I confirm selection
    And I input name <Name> and hit Enter
    And I attempt to enter emails with known incorrect formats
    Then I verify that the app does not let me continue

    Examples: 
      | Name      |
      | user1Name |

  @staging @id284
  Scenario Outline: Conserve user input throughout registration
    Given I see sign in screen
    When I press Join button
    And I press Picture button
    And I choose a picture from camera roll
    And I See selected picture
    And I confirm selection
    And I input name <Name> and hit Enter
    And I input email <Email> and hit Enter
    And I enter password <Password>
    And I navigate from password screen back to Welcome screen
    Then I navigate throughout the registration pages and see my input

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @staging @id282
  Scenario Outline: Can return to email page to change email if input incorrectly
    Given I see sign in screen
    When I press Join button
    And I press Picture button
    And I choose a picture from camera roll
    And I See selected picture
    And I confirm selection
    And I input name <Name> and hit Enter
    And I input email <Incorrect> and hit Enter
    And I input password <Password> and hit Enter
    Then I see error page
    And I return to the email page from error page
    And I clear email input field on Registration page
    And I input email <Correct> and hit Enter
    And I input password <Password> and hit Enter
    And I see confirmation page

    Examples: 
      | Correct    | Password      | Name      | Incorrect           |
      | user1Email | user1Password | user1Name | error@wearezeta.com |

  @staging @id528 @id529 @id530
  Scenario Outline: Register new user using username with maximum characters allowed
    Given I see sign in screen
    When I press Join button
    And I press Picture button
    And I choose a picture from camera roll
    And I See selected picture
    And I confirm selection
    And I enter a username which is at most <MaxChars> characters long from <Language> alphabet
    And I click Back button
    Then I verify that my username is at most <MaxChars> characters long

    Examples: 
      | Email      | Password      | MaxChars | Language |
      | user1Email | user1Password | 72       | English  |

  @staging @id286
  Scenario Outline: Take or select a photo label validation
    Given I see sign in screen
    When I press Join button
    Then I see Take or select photo label and smile

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @staging @id285
  Scenario Outline: Take or select a photo label not visible when picture is selected
    Given I see sign in screen
    When I press Join button
    And I press Picture button
    And I choose a picture from camera roll
    And I confirm selection
    And I click Back button
    Then I don't see Take or select photo label and smile

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @staging @id273 @id301
  Scenario Outline: Next Button should not be visible on first registration step visit
    Given I see sign in screen
    When I press Join button
    And I press Picture button
    And I choose a picture from camera roll
    And I See selected picture
    And I confirm selection
    And I don't see Next button
    And I input name <Name> and hit Enter
    And I don't see Next button
    And I input email <Email> and hit Enter
    And I don't see Next button
    And I input password <Password> and hit Enter
    And I don't see Next button
    Then I see confirmation page
    And I don't see Next button

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @staging
  Scenario Outline: Automatic email verification
    Given I see sign in screen
    When I press Join button
    And I press Picture button
    And I choose a picture from camera roll
    And I See selected picture
    And I confirm selection
    And I input name <Name> and hit Enter
    And I input email <Email> and hit Enter
    And I input password <Password> and hit Enter
    And I see confirmation page
    And I verify registration address
    Then I see Contact list with my name <Name>

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @staging @id305
  Scenario Outline: Minimum 8 chars password requirement validation
    Given I see sign in screen
    When I press Join button
    And I press Picture button
    And I choose a picture from camera roll
    And I See selected picture
    And I confirm selection
    And I input name <Name> and hit Enter
    And I input email <Email> and hit Enter
    And I enter password <Password>
    Then I see Create Account button disabled

    Examples: 
      | Email      | Password | Name      |
      | user1Email | 1234567  | user1Name |

  @staging @id298
  Scenario Outline: Can re-send verification email from verification screen
    Given I see sign in screen
    When I press Join button
    And I press Picture button
    And I choose a picture from camera roll
    And I See selected picture
    And I confirm selection
    And I input name <Name> and hit Enter
    And I input email <Email> and hit Enter
    And I enter password <Password>
    Then I confirm that inbox contains 0 emails for current recipient
    And I click Create Account Button
    And I wait for 10 seconds
    Then I confirm that inbox contains 1 email for current recipient
    And I resend verification email
    And I wait for 10 seconds
    Then I confirm that inbox contains 2 emails for current recipient

    Examples: 
      | Email      | Password      | Name      | EmailCount |
      | user1Email | user1Password | user1Name | 20         |

  @staging @id302
  Scenario Outline: Verify back button during registration process
    Given I see sign in screen
    When I press Join button
    And I verify back button
    And I press Picture button
    And I choose a picture from camera roll
    And I See selected picture
    And I confirm selection
    And I verify back button
    And I input name <Name> and hit Enter
    And I verify back button
    And I input email <Email> and hit Enter
    And I verify back button
    And I enter password <Password>
    And I click Back button
    And I verify back button

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @staging @id798
  Scenario Outline: Email verification reminder is displayed when attempt is made to sign in with unverified email
    Given I see sign in screen
    When I press Join button
    And I press Picture button
    And I choose a picture from camera roll
    And I See selected picture
    And I confirm selection
    And I input name <Name> and hit Enter
    And I input email <Email> and hit Enter
    And I enter password <Password>
    And I click Create Account Button
    And I see confirmation page
    And I navigate back to welcome page
    And I press Sign in button
    And I have entered login <Email>
    And I have entered password <Password>
    And I attempt to press Login button
    Then I see email verification reminder

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @staging @id2468
  Scenario Outline: Verify user is logged in when trying to register with a phone already asigned to the email
    Given There is 1 user where <Name> is me
    Given I see sign in screen
    When I input phone number of already registered user <Name>
    And I enter verification code for user <Name>
    Then I see Contact list with my name <Name>

    Examples: 
      | Name      |
      | user1Name |

  @staging @noAcceptAlert @id1517
  Scenario Outline: Verify that it's impossible to proceed registration with more than 16 characters in Phone
    Given I see sign in screen
    When I enter 16 digits phone number
    Then I see invalid phone number alert

    Examples: 
      | Name      |
      | user1Name |

  @staging @noAcceptAlert @id2742
  Scenario Outline: Verify notification appearance in case of incorrect code
    Given I see sign in screen
    When I enter phone number for user <Name>
    And I input random activation code
    Then I see invalid code alert

    Examples: 
      | Name      |
      | user1Name |
