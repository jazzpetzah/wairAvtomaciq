Feature: Registration

  @C667 @regression @rc
  Scenario Outline: Register new user by phone and set profile picture using camera
    Given I see welcome screen
    When I input a new phone number for user <Name>
    And I input the verification code
    And I input my name
    And I select to choose my own picture
    And I select Camera as picture source
    And I tap Take Photo button on Take Picture view
    And I tap Confirm button on Take Picture view
    Then I see Conversations list with no conversations

    Examples:
      | Name      |
      | user1Name |

  @C145970 @regression @rc @rc42
  Scenario Outline: Change self picture during registration using gallery
    Given I see welcome screen
    When I input a new phone number for user <Name>
    And I input the verification code
    And I input my name
    And I select to choose my own picture
    And I select Gallery as picture source
    And I tap Confirm button on Take Picture view
    Then I see Conversations list with no conversations

    Examples:
      | Name      |
      | user1Name |

  @C566 @regression @noAcceptAlert
  Scenario Outline: Wrong phone activation code is followed by correct error message
    Given I see welcome screen
    When I input a new phone number for user <Name>
    And I input random activation code
    Then I see invalid code alert

    Examples:
      | Name      |
      | user1Name |