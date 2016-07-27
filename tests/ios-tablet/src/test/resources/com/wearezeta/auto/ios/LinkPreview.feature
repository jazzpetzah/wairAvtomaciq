Feature: Link Preview

  @C169217 @regression
  Scenario Outline: Verify preview is shown for sent link (link only)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the "<Link>" message and send it
    Then I see link preview container in the conversation view

    Examples:
      | Name      | Contact   | Link                                                                                  |
      | user1Name | user2Name | http://www.mirror.co.uk/sport/football/match-centre/portugal-shock-france-1-0-8044835 |

  @C169219 @regression
  Scenario Outline: Verify deleting link preview
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the "<Link>" message and send it
    Then I see link preview container in the conversation view
    When I long tap on link preview in conversation view
    And I tap on Delete badge item
    # Sometimes the alert is not accepted automatically
    And I tap Delete button on the alert
    Then I do not see link preview container in the conversation view

    Examples:
      | Name      | Contact   | Link                                                                                  |
      | user1Name | user2Name | http://www.mirror.co.uk/sport/football/match-centre/portugal-shock-france-1-0-8044835 |

  @C169218 @regression
  Scenario Outline: Verify preview is shown for received link
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given User <Contact> sends encrypted message "<Link>" to user Myself
    Given I see conversations list
    When I tap on contact name <Contact>
    Then I see link preview container in the conversation view

    Examples:
      | Name      | Contact   | Link                                                                                  |
      | user1Name | user2Name | https://twitter.com/wire/status/752800171608535040 |