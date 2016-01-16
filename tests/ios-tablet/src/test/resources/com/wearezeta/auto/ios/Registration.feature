Feature: Registration

  @C2761 @regression @rc @id1392
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
    Then I see conversations list

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @C2768 @regression @id2938
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
    Then I see conversations list

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @C2763 @regression @rc @id2476
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

  @C2767 @regression @id2937
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

  @C2769 @staging @id4049
  Scenario Outline: Verify cutting spaces from the beginning and ending the name
    Given I see sign in screen
    When I fill in name <Name> with leading and trailing spaces on iPad
    And I enter email <Email>
    And I start activation email monitoring
    And I enter password <Password>
    And I click Create Account Button
    And I accept terms of service
    And I see confirmation page
    And I verify registration address
    And I press Picture button
    And I choose a picture from camera roll on iPad popover
    And I press Confirm button on iPad popover
    Then I see conversations list
    When I tap on my name <Name>
    Then I see user name doesnt contains spaces

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |