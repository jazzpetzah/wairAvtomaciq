Feature: Registration

  #@smoke @id276
  #Scenario Outline: Register new user using front camera (Real Device)
    #Given I see sign in screen
    #When I press Join button
    #And I dismiss Vignette overlay
    #And I take photo by front camera
    #And I See photo taken
    #And I confirm selection
    #And I input name <Name> and hit Enter
    #And I input email <Email> and hit Enter
    #And I input password <Password> and hit Enter
    #Then I see confirmation page
    #And I verify registration address
    #And I press continue registration
    #And I see Contact list with my name <Name>

    #Examples: 
      #| Email   | Password    | Name    |
      #| aqaUser | aqaPassword | aqaUser |

  @regression @id304 @deployPictures
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

  @regression @id304 @deployPictures
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

  @regression @id284 @deployPictures
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

  @regression @id282 @deployPictures
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

  @regression @id528 @id529 @id530 @deployPictures
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

  @regression @id589 @deployPictures
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

  #@staging @id288
  #Scenario Outline: Switch between vignette overlay and full color (Real Device)
    #Given I see sign in screen
    #When I press Join button
    #And I dismiss Vignette overlay
    #And I take photo by front camera
    #And I confirm selection
    #And I click Back button
    #And I see Vignette overlay
    #And I dismiss Vignette overlay
    #And I don't see Vignette overlay
    #And I see full color mode
    #And I click close full color mode button
    #Then I see Vignette overlay

    #Examples: 
      #| Email   | Password    | Name    |
      #| aqaUser | aqaPassword | aqaUser |

  @regression @id286
  Scenario Outline: Take or select a photo label validation
    Given I see sign in screen
    When I press Join button
    Then I see Take or select photo label and smile

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @regression @id285 @deployPictures
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

  #@staging @id285
  #Scenario Outline: Take or select a photo label not visible when picture is selected (Real Device)
    #Given I see sign in screen
    #When I press Join button
    #And I press Camera button on Registration page
    #And I click Vignette overlay
    #And I take photo by front camera
    #And I confirm selection
    #And I click Back button
    #Then I don't see Take or select photo label and smile

    #Examples: 
      #| Email   | Password    | Name    |
      #| aqaUser | aqaPassword | aqaUser |

  @regression @id273 @id301 @deployPictures
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

  @regression @id1392 @deployPictures
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

  #@staging @id275
  #Scenario Outline: Register new user using rear camera (Real Device)
    #Given I see sign in screen
    #When I press Join button
    #And I dismiss Vignette overlay
    #And I take photo by rear camera
    #And I confirm selection
    #And I input name <Name> and hit Enter
    #And I input email <Email> and hit Enter
    #And I input password <Password> and hit Enter
    #And I see confirmation page
    #And I verify registration address
    #And Contact list loads with only my name

    #Examples: 
      #| Email   | Password    | Name    |
      #| aqaUser | aqaPassword | aqaUser |

  @regression @id305 @deployPictures
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

  #@staging @id281
  #Scenario Outline: Change selected image during registratrion (Real Device)
    #Given I see sign in screen
    #When I press Join button
    #And I press Picture button
    #And I choose a picture from camera roll
    #And I See selected picture
    #And I reject selected picture
    #And I see Take or select photo label and smile
    #And I press Picture button
    #And I choose photo from album
    #And I See selected picture
    #And I confirm selection
    #And I click Back button
    #And I see selected image set as background
    #And I dismiss Vignette overlay
    #And I take photo by front camera
    #And I See photo taken
    #And I confirm selection
    #And I see photo set as background
    #Then I see background image is replaced

    #Examples: 
      #| Email   | Password    | Name    |
      #| aqaUser | aqaPassword | aqaUser |

  #@staging @id287
  #Scenario Outline: Verify photo made by rear camera during registration is not flipped (Real Device)
    #Given I see sign in screen
    #When I press Join button
    #And I dismiss Vignette overlay
    #And I take photo by rear camera
    #And I confirm selection
    #And I input name <Name> and hit Enter
    #And I input email <Email> and hit Enter
    #And I input password <Password> and hit Enter
    #And I see confirmation page
    #And I verify registration address
    #And Contact list loads with only my name
    #And I tap on my name <Name>
    #And I tap on personal screen
    #Then I see profile image is same as template

    #Examples: 
      #| Email   | Password    | Name    |
      #| aqaUser | aqaPassword | aqaUser |

  @regression @id298 @deployPictures
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

  @regression @id302 @deployPictures
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

  @regression @id798 @deployPictures
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