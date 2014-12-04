Feature: Register new user

  #no camera on some Jenkins slaves
  @regression @id1080
  Scenario Outline: Register new user using front camera
    Given I am signed out from ZClient
    And I see Sign In screen
    When I start registration
    And I choose register using camera
    And I take registration picture from camera
    And I enter name <Name>
    And I enter email <Email>
    And I enter password <Password>
    And I submit registration data
    Then I see confirmation page
    And I verify registration address
    And I see contact list of registered user

    Examples: 
      | Email   | Password    | Name    |
      | aqaUser | aqaPassword | aqaUser |

  #ZOSX-2857
  @smoke @id177
  Scenario Outline: Register new user with image - landscape image
    Given I am signed out from ZClient
    And I see Sign In screen
    When I start registration
    And I choose register with image
    And I take registration picture from image file <ImageFile>
    And I enter name <Name>
    And I enter email <Email>
    And I enter password <Password>
    And I submit registration data
    Then I see confirmation page
    And I verify registration address
    And I see contact list of registered user

    Examples: 
      | Email   | Password    | Name    | ImageFile                 |
      | aqaUser | aqaPassword | aqaUser | userpicture_landscape.jpg |

  #ZOSX-2857
  @smoke @id177
  Scenario Outline: Register new user with image - portrait image
    Given I am signed out from ZClient
    And I see Sign In screen
    When I start registration
    And I choose register with image
    And I take registration picture from image file <ImageFile>
    And I enter name <Name>
    And I enter email <Email>
    And I enter password <Password>
    And I submit registration data
    Then I see confirmation page
    And I verify registration address
    And I see contact list of registered user

    Examples: 
      | Email   | Password    | Name    | ImageFile                |
      | aqaUser | aqaPassword | aqaUser | userpicture_portrait.jpg |

  @regression @id171
  Scenario Outline: Do not accept email with spaces
    Given I am signed out from ZClient
    And I see Sign In screen
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
    Given I am signed out from ZClient
    And I see Sign In screen
    When I start registration
    And I choose register with image
    And I take registration picture from image file userpicture_portrait.jpg
    And I enter invalid emails
    Then I see that all emails not accepted
