Feature: Register new user

  #no camera on some Jenkins slaves
  #  @regression @id1080
  #  Scenario Outline: Register new user using front camera
  #    And I see Welcome screen
  #    When I start registration
  #    And I choose register using camera
  #    And I take registration picture from camera
  #    And I enter name <Name>
  #    And I enter email <Email>
  #    And I enter password <Password>
  #    And I submit registration data
  #    Then I see confirmation page
  #    And I verify registration address
  #    And I see self profile of registered user
  #    Examples:
  #      | Email   | Password    | Name    |
  #      | aqaUser | aqaPassword | aqaUser |
  
  @smoke @id177
  Scenario Outline: Register new user with image - landscape image
    And I see Welcome screen
    When I start registration
    And I choose register with image
    And I take registration picture from image file <ImageFile>
    And I enter name <Name>
    And I enter email <Email>
    And I enter password <Password>
    And I submit registration data
    Then I see confirmation page
    And I verify registration address
    And I see self profile of registered user

    Examples: 
      | Email      | Password      | Name      | ImageFile                 |
      | user1Email | user1Password | user1Name | userpicture_landscape.jpg |

  @smoke @id177
  Scenario Outline: Register new user with image - portrait image
    And I see Welcome screen
    When I start registration
    And I choose register with image
    And I take registration picture from image file <ImageFile>
    And I enter name <Name>
    And I enter email <Email>
    And I enter password <Password>
    And I submit registration data
    Then I see confirmation page
    And I verify registration address
    And I see self profile of registered user

    Examples: 
      | Email      | Password      | Name      | ImageFile                |
      | user1Email | user1Password | user1Name | userpicture_portrait.jpg |

  @regression @id171
  Scenario Outline: Do not accept email with spaces
    And I see Welcome screen
    When I start registration
    And I choose register with image
    And I take registration picture from image file userpicture_portrait.jpg
    And I enter email <Email>
    Then I see that email invalid

    Examples: 
      | Email                            |
      | email with spaces@weare zeta.com |

  @regression @id171
  Scenario: Fail registration on incorrect email
    And I see Welcome screen
    When I start registration
    And I choose register with image
    And I take registration picture from image file userpicture_portrait.jpg
    And I enter invalid emails
    Then I see that all emails not accepted

  @regression @1964
  Scenario Outline: Verify automatic email verification is performed
    And I see Welcome screen
    When I start registration
    And I choose register with image
    And I take registration picture from image file <ImageFile>
    And I enter name <Name>
    And I enter email <Email>
    And I enter password <Password>
    And I submit registration data
    And I see confirmation page
    And I open activation link in browser
    Then I see that user activated
    And I see self profile of registered user

    Examples: 
      | Email      | Password      | Name      | ImageFile                 |
      | user1Email | user1Password | user1Name | userpicture_landscape.jpg |
