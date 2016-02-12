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
    Then I see conversations list

    Examples:
      | Name      |
      | user1Name |

  @C14321 @noAcceptAlert @regression
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
