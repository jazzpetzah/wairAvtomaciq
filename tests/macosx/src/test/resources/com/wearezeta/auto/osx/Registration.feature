Feature: Register new user

  #no camera on some Jenkins slaves
  #  @regression @id1080
  #  Scenario Outline: Register new user using front camera
  #    Given I see Welcome screen
  #    When I start registration
  #    And I choose register using camera
  #    And I take registration picture from camera
  #	   And I accept taken picture
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
    Given I see Welcome screen
    When I start registration
    And I choose register with image
    And I take registration picture from image file <ImageFile>
    And I accept taken picture
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
    Given I see Welcome screen
    When I start registration
    And I choose register with image
    And I take registration picture from image file <ImageFile>
    And I accept taken picture
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
    Given I see Welcome screen
    When I start registration
    And I choose register with image
    And I take registration picture from image file userpicture_portrait.jpg
    And I accept taken picture
    And I enter email <Email>
    Then I see that email invalid

    Examples: 
      | Email                            |
      | email with spaces@weare zeta.com |

  @regression @id171
  Scenario: Fail registration on incorrect email
    Given I see Welcome screen
    When I start registration
    And I choose register with image
    And I take registration picture from image file userpicture_portrait.jpg
    And I accept taken picture
    And I enter invalid emails
    Then I see that all emails not accepted

  @regression @id1096
  Scenario Outline: Verify automatic email verification is performed
    Given I see Welcome screen
    When I start registration
    And I choose register with image
    And I take registration picture from image file <ImageFile>
    And I accept taken picture
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
