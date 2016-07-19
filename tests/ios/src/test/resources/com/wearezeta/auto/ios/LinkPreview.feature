Feature: Link Preview

  @C167029 @staging
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

  @C167030 @C167032 @C169224 @staging
  Scenario Outline: Verify preview is shown for mixed link and text
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    # Check link + text
    And I post url link <Link> <Text>
    And I wait for 1 seconds
    # This is to make the keyboard invisible as sometimes the keyboard is still visible after posting the link
    And I navigate back to conversations list
    And I tap on contact name <Contact>
    Then I see link preview container in the conversation view
    And I see the conversation view contains message <Link> <Text>
    # Check text + link
    When I post url link <Text1> <Link>
    And I wait for 1 seconds
    # This is to make the keyboard invisible as sometimes the keyboard is still visible after posting the link
    And I navigate back to conversations list
    And I tap on contact name <Contact>
    Then I see the conversation view contains message <Text1>
    And I do not see the conversation view contains message <Text1> <Link>
    And I see link preview container in the conversation view
    # Check text + link + text
    When I post url link <Text1> <Shortenlink> <Text>
    And I wait for 1 seconds
    # This is to make the keyboard invisible as sometimes the keyboard is still visible after posting the link
    And I navigate back to conversations list
    And I tap on contact name <Contact>
    Then I see link preview container in the conversation view
    And I see the conversation view contains message <Text1> <Shortenlink> <Text>

    Examples:
      | Name      | Contact   | Link                                                                                  | Text       | Text1      | Shortenlink          |
      | user1Name | user2Name | http://www.mirror.co.uk/sport/football/match-centre/portugal-shock-france-1-0-8044835 | My text    | Text first | http://goo.gl/pA9mgH |