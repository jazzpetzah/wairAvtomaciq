Feature: Settings

  @C669 @id67 @regression @rc
  Scenario Outline: Open and Close settings page
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with no contacts
    When I tap conversations list settings button
    Then I see settings page
    When I press back button
    Then I see Contact list with no contacts

    Examples:
      | Name      |
      | user1Name |

  @C670 @id92 @regression @rc
  Scenario Outline: Check About page in settings menu
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with no contacts
    When I tap conversations list settings button
    And I select "About" settings menu item
    Then I see "Wire Website" settings menu item

    Examples:
      | Name      |
      | user1Name |

  @C679 @id205 @regression @rc @rc42
  Scenario Outline: Change user picture with gallery image
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with no contacts
    When I take screenshot
    And I tap conversations list settings button
    And I select "Account" settings menu item
    And I select "Picture" settings menu item
    And I tap Gallery Camera button on Take Picture view
    And I tap Confirm button on Take Picture view
    And I press Back button
    And I press Back button
    Then I verify the previous and the current screenshots are different

    Examples:
      | Name      |
      | user1Name |

  @C691 @id328 @regression @rc @rc42
  Scenario Outline: I can change my name
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with no contacts
    When I tap conversations list settings button
    And I select "Account" settings menu item
    And I select "<Name>" settings menu item
    And I commit my new name "<NewName>"
    Then I see "<NewName>" settings menu item

    Examples:
      | Name      | NewName     |
      | user1Name | NewTestName |

  @C678 @id201 @regression @rc
  Scenario Outline: Change user picture using camera
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with no contacts
    When I take screenshot
    And I tap conversations list settings button
    And I select "Account" settings menu item
    And I select "Picture" settings menu item
    And I tap Take Photo button on Take Picture view
    And I tap Confirm button on Take Picture view
    And I press Back button
    And I press Back button
    Then I verify the previous and the current screenshots are different

    Examples:
      | Name      |
      | user1Name |
