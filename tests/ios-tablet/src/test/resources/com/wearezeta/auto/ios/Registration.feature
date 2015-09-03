Feature: Registration

  @regression @rc @id1392
  Scenario Outline: Automatic email verification [PORTRAIT]
    Given I see sign in screen
    When I enter name <Name>
    And I enter email <Email>
    And I enter password <Password>
    And I start activation email monitoring
    And I click Create Account Button
    And I accept terms of service
    And I see confirmation page
    And I verify registration address
    And I press Picture button
    And I choose a picture from camera roll on iPad popover
    And I press Confirm button on iPad popover
    Then I see Contact list with my name <Name>

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @regression @id2938
  Scenario Outline: Automatic email verification [LANDSCAPE]
    Given I see sign in screen
    Given I rotate UI to landscape
    When I enter name <Name>
    And I enter email <Email>
    And I enter password <Password>
    And I start activation email monitoring
    And I click Create Account Button
    And I accept terms of service
    And I see confirmation page
    And I verify registration address
    And I press Picture button
    And I choose a picture from camera roll on iPad popover
    And I press Confirm button on iPad popover
    Then I see Contact list with my name <Name>

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @regression @rc @id2476
  Scenario Outline: Verify registration with email [PORTRAIT]
    Given I see sign in screen
    When I enter name <Name>
    And I enter email <Email>
    And I enter password <Password>
    And I start activation email monitoring
    And I click Create Account Button
    And I accept terms of service
    And I see confirmation page

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @regression @id2937
  Scenario Outline: Verify registration with email [LANDSCAPE]
    Given I see sign in screen
    Given I rotate UI to landscape
    When I enter name <Name>
    And I enter email <Email>
    And I enter password <Password>
    And I start activation email monitoring
    And I click Create Account Button
    And I accept terms of service
    And I see confirmation page

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |
