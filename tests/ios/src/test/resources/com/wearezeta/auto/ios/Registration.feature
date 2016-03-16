Feature: Registration

  @C1019 @clumsy @regression @rc @id589
  Scenario Outline: Register new user using photo album
    Given I see sign in screen
    When I enter phone number for user <Name>
    And I enter activation code
    And I accept terms of service
    And I input name <Name> and hit Enter
    And I press Choose Own Picture button
    And I press Choose Photo button
    And I choose a picture from camera roll
    And I tap Share Contacts button on Share Contacts overlay
    Then I see People picker page

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

  @C3166 @real
  Scenario Outline: Verify taking photo with a front camera
    Given I see sign in screen
    When I enter phone number for user <Name>
    And I enter activation code
    And I accept terms of service
    And I input name <Name> and hit Enter
    And I press Choose Own Picture button
    And I press Take Photo button
    And I tap Camera Shutter button
    And I remember current screen state
    And I confirm my choice
    And I see People picker page
    And I click clear button
    And I tap my avatar
    And I tap on personal screen
    Then I verify that current screen similarity score is more than <Score> within <Timeout> seconds

    Examples:
      | Name      | Score | Timeout |
      | user1Name | 0.4   | 15      |