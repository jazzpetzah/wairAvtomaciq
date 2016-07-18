Feature: Link Preview

  @C167029 @staging
  Scenario Outline: Verify preview is shown for sent link (link only)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I post url link <Link>
    # Will uncomment this when iOS dev adds the identifiers
    # Then I see link preview container in the conversation view
    # This is to make sure the image appears in the preview (we had this bug) and good enough check here once in the test suite
    # Then I see link preview image in the conversation view

    Examples:
      | Name      | Contact   | Link                                                                                  |
      | user1Name | user2Name | http://www.mirror.co.uk/sport/football/match-centre/portugal-shock-france-1-0-8044835 |