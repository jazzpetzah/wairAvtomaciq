Feature: Registration

  @C1019 @clumsy @regression @rc
  Scenario Outline: Register new user using photo album
    Given I see sign in screen
    When I enter phone number for <Name>
    And I enter activation code
    And I accept terms of service
    And I input name <Name> and commit it
    And I accept alert if visible
    And I tap Choose Own Picture button
    And I tap Choose Photo button
    # Wait for animation
    And I wait for 5 seconds
    And I accept alert if visible
    And I select the first picture from Camera Roll
    And I accept alert if visible
    And I tap Share Contacts button on Share Contacts overlay
    And I accept alert if visible
    Then I see conversations list

    Examples:
      | Name      |
      | user1Name |

  @C14321 @regression
  Scenario Outline: Verify that it's impossible to proceed registration with more than 16 characters in Phone
    Given I see sign in screen
    When I enter <Count> digits phone number
    Then I see alert contains text <ExpectedText>

    Examples:
      | Count | ExpectedText               |
      | 16    | enter a valid phone number |

  @C2652 @regression
  Scenario Outline: Verify notification appearance in case of incorrect code
    Given I see sign in screen
    When I enter phone number for <Name>
    And I enter random verification code for <Name>
    Then I see invalid code alert

    Examples:
      | Name      |
      | user1Name |

  @C3166 @real @real_rc
  Scenario Outline: Verify taking photo with a front camera
    Given I see sign in screen
    When I enter phone number for <Name>
    And I enter activation code
    And I accept terms of service
    And I input name <Name> and commit it
    And I accept alert if visible
    And I tap Choose Own Picture button
    And I tap Take Photo button
    And I accept alert if visible
    And I tap Take Photo button on Camera page
    And I tap Confirm button on Picture preview page
    And I accept alert if visible
    # Sometimes setting of profile picture takes longer
    And I wait for 5 seconds
    And I tap Share Contacts button on Share Contacts overlay
    And I accept alert if visible
    And I tap settings gear button
    And I select settings item Account
    And I select settings item Picture
    Then I see Take Photo button on Camera page

    Examples:
      | Name      |
      | user1Name |

  @C1009 @regression
  Scenario Outline: Verify registering new user with Arabic name
    Given I see sign in screen
    Given I enter phone number for <Name>
    Given I enter activation code
    Given I accept terms of service
    Given I input custom name <ArabicName> and commit it
    Given I accept alert if visible
    Given I tap Keep This One button
    When I tap Not Now button on Share Contacts overlay
    Then I see conversations list
    When I tap settings gear button
    And I select settings item Account
    Then I verify the value of settings item Name equals to "<ArabicName>"

    Examples:
      | Name      | ArabicName |
      | user1Name | عبد العزيز |

  @C1004 @regression
  Scenario Outline: Verify resending code
    Given I see sign in screen
    When I enter phone number for <Name>
    Then I do not see RESEND button
    And I see NO CODE TO SHOW UP label
    When I wait for <Timeout> seconds
    Then I do not see NO CODE TO SHOW UP label
    And I see RESEND button
    When I tap RESEND code button
    Then I do not see RESEND button
    And I see NO CODE TO SHOW UP label

    Examples:
      | Name      | Timeout |
      | user1Name | 30      |