Feature: Registration

  @C1019 @regression @rc @id589
  Scenario Outline: Register new user using photo album
    Given I see sign in screen
    When I enter phone number for user <Name>
    And I enter activation code
    And I accept terms of service
    And I input name <Name> and hit Enter
    And I press Choose Own Picture button
    And I press Choose Photo button
    And I choose a picture from camera roll
    Then I see Contact list with my name <Name>

    Examples:
      | Name      |
      | user1Name |

  @C1002 @regression @id2468
  Scenario Outline: Verify user is logged in when trying to register with a phone already assigned to the email
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
    When I enter <Count> digits phone number
    Then I see invalid phone number alert

    Examples:
      | Count |
      | 16    |

  @C2652 @regression @noAcceptAlert @id2742
  Scenario Outline: Verify notification appearance in case of incorrect code
    Given I see sign in screen
    When I enter phone number for user <Name>
    And I input random activation code
    Then I see invalid code alert

    Examples:
      | Name      |
      | user1Name |

  @C1008 @staging @id295
  Scenario Outline: Verify cutting spaces from the beginning and ending the name
    Given I see sign in screen
    When I enter phone number for user <Name>
    And I enter activation code
    And I accept terms of service
    And I fill in name <Name> with leading and trailing spaces and hit Enter
    And I press Picture button
    And I choose a picture from camera roll
    And I See selected picture
    And I confirm selection
    Then I see Contact list with my name <Name>
    When I tap on my name <Name>
    Then I see user name doesnt contains spaces

    Examples:
      | Name      |
      | user1Name |

  @staging @id2467
  Scenario Outline: Verify user is logged in when trying to register with already registered phone
    Given There is 1 user where <Name> is me with phone number only
    Given I see sign in screen
    When I input phone number of already registered user <Name>
    And I enter verification code for user <Name>
    Then I see Contact list with my name <Name>

    Examples:
      | Name      |
      | user1Name |