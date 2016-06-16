Feature: Self Profile

  @C679 @id205 @regression @rc @rc42
  Scenario Outline: Change user picture with gallery image
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with no contacts
    When I tap conversations list settings button
    And I tap on personal info screen
    And I remember my current profile picture
    And I tap Gallery Camera button on Take Picture view
    And I tap Confirm button on Take Picture view
    And I tap on personal info screen
    Then I verify that my current profile picture is different from the previous one

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
    And I tap on my name
    Then I see edit name field with my name
    When I clear name field
    And I change my name to <NewName>
    Then I see my new name <NewName>

    Examples:
      | Name      | NewName     |
      | user1Name | NewTestName |

  @C678 @id201 @regression @rc
  Scenario Outline: Change user picture using camera
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with no contacts
    When I tap conversations list settings button
    And I tap on personal info screen
    And I remember my current profile picture
    And I tap Change Photo button on Take Picture view
    And I tap Take Photo button on Take Picture view
    And I tap Confirm button on Take Picture view
    And I tap on personal info screen
    Then I verify that my current profile picture is different from the previous one

    Examples:
      | Name      |
      | user1Name |

  @C145963 @staging
  Scenario Outline: I should be moved to right view on canceling new profile picture
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with no contacts
    When I tap conversations list settings button
    And I tap on personal info screen
    And I tap Gallery Camera button on Take Picture view
    And I tap Cancel button on Take Picture view
    Then I see Change Photo button on Take Picture view
    And I see Gallery Camera button on Take Picture view
    When I tap Change Photo button on Take Picture view
    And I tap Take Photo button on Take Picture view
    And I tap Cancel button on Take Picture view
    Then I see Take Photo button on Take Picture view

    Examples:
      | Name      |
      | user1Name |
