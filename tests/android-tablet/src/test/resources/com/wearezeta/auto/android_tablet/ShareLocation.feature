Feature: Share Location

  @C162657 @regression @rc
  Scenario Outline: Verify you can share Location from conversation view
    Given There is 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> sends encrypted message <Message> to user Myself
    Given I see the conversations list with conversations
    Given I tap the conversation <Contact>
    When I tap Share location button from cursor toolbar
    # Let it to find the location
    And I wait for 5 seconds
    And I tap Send button on Share Location page
    Then I see Share Location container in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

