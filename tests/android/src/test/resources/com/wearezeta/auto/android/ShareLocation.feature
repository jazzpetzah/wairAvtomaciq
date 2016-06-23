Feature: Share Location

  @C150028 @staging
  Scenario Outline: AN-4188 Verify you can share your location
    Given There is 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I tap on contact name <Contact>
    When I tap Share location button from cursor toolbar
    # Let it to find the location
    And I wait for 5 seconds
    And I tap Send button on Share Location page
    Then I see Share Location container in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |
