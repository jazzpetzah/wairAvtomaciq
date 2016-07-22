Feature: Link Preview

  @C167029 @regression
  Scenario Outline: Verify preview is shown for sent link (link only)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I post url link <Link>
    # This is to make the keyboard invisible as sometimes the keyboard is still visible after posting the link
    And I navigate back to conversations list
    And I tap on contact name <Contact>
    Then I see link preview container in the conversation view
    # This is to make sure the image appears in the preview (we had this bug) and good enough check here once in the test suite
    Then I see link preview image in the conversation view

    Examples:
      | Name      | Contact   | Link                                                                                  |
      | user1Name | user2Name | http://www.mirror.co.uk/sport/football/match-centre/portugal-shock-france-1-0-8044835 |

  @C167030 @C167032 @regression
  Scenario Outline: Verify preview is shown for mixed link and text
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    # Check link + text
    And I post url link <Link> <Text>
    # This is to make the keyboard invisible
    And I navigate back to conversations list
    And I tap on contact name <Contact>
    Then I see link preview container in the conversation view
    And I see the conversation view contains message <Link> <Text>
    # Check text + link
    When I post url link <Text1> <Link>
    # This is to make the keyboard invisible
    And I navigate back to conversations list
    And I tap on contact name <Contact>
    Then I see the conversation view contains message <Text1>
    And I do not see the conversation view contains message <Text1> <Link>
    And I see link preview container in the conversation view
    # Check text + link + text
    When I post url link <Text1> <Link> <Text>
    # This is to make the keyboard invisible
    And I navigate back to conversations list
    And I tap on contact name <Contact>
    Then I see link preview container in the conversation view
    And I see the conversation view contains message <Text1> <Link> <Text>

    Examples:
      | Name      | Contact   | Link                                                                                  | Text       | Text1      |
      | user1Name | user2Name | http://www.mirror.co.uk/sport/football/match-centre/portugal-shock-france-1-0-8044835 | My text    | Text first |

  @C169224 @regression
  Scenario Outline: Verify preview is shown for shortened URL
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    When I post url link <Shortenlink>
    And I navigate back to conversations list
    And I tap on contact name <Contact>
    Then I see link preview container in the conversation view

    Examples:
      | Name      | Contact   | Shortenlink          |
      | user1Name | user2Name | http://goo.gl/pA9mgH |

  @C167039 @regression
  Scenario Outline: Verify preview is shown for different formats of link
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    When I post url link <Link>
    And I navigate back to conversations list
    And I tap on contact name <Contact>
    Then I see link preview container in the conversation view
    When I long tap on link preview in conversation view
    And I tap on Delete badge item
    # Sometimes the alert is not accepted automatically
    And I tap Delete button on the alert
    When I post url link <Link1>
    And I navigate back to conversations list
    And I tap on contact name <Contact>
    Then I see link preview container in the conversation view
    When I long tap on link preview in conversation view
    And I tap on Delete badge item
    # Sometimes the alert is not accepted automatically
    And I tap Delete button on the alert
    When I post url link <Link2>
    And I navigate back to conversations list
    And I tap on contact name <Contact>
    Then I see link preview container in the conversation view
    When I long tap on link preview in conversation view
    And I tap on Delete badge item
    # Sometimes the alert is not accepted automatically
    And I tap Delete button on the alert
    When I post url link <Link3>
    And I navigate back to conversations list
    And I tap on contact name <Contact>
    Then I see link preview container in the conversation view
    When I long tap on link preview in conversation view
    And I tap on Delete badge item
    # Sometimes the alert is not accepted automatically
    And I tap Delete button on the alert
    When I post url link <Link4>
    And I navigate back to conversations list
    And I tap on contact name <Contact>
    Then I see link preview container in the conversation view

    Examples:
      | Name      | Contact   | Link                | Link1                | Link2                   | Link3               | Link4               |
      | user1Name | user2Name | http://facebook.com | https://facebook.com | http://www.facebook.com | Http://facebook.com | HTTP://FACEBOOK.COM |

  @C167038 @regression
  Scenario Outline: Verify copying link preview
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>, <Contact1>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I post url link <Link>
    Then I see link preview container in the conversation view
    When I long tap on link preview in conversation view
    And I tap on Copy badge item
    And I navigate back to conversations list
    And I tap on contact name <Contact1>
    And I paste and commit the text
    And I navigate back to conversations list
    When I tap on contact name <Contact1>
    Then I see link preview container in the conversation view

    Examples:
      | Name      | Contact   | Contact1  | Link                                                                                  |
      | user1Name | user2Name | user3Name | http://www.mirror.co.uk/sport/football/match-centre/portugal-shock-france-1-0-8044835 |