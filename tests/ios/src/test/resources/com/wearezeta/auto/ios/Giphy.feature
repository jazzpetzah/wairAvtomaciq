Feature: Giphy

  @C955 @regression @fastLogin
  Scenario Outline: Verify preview is opened after tapping on GIF button
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the "<GiphyTag>" message
    And I tap GIF button from input tools
    # Wait for GIF picture to be downloaded
    And I wait for 10 seconds
    And I select the first item from Giphy grid
    Then I see Giphy preview page

    Examples: 
      | Name      | Contact   | GiphyTag |
      | user1Name | user2Name | happy    |

  @C962 @clumsy @regression @rc @IPv6 @fastLogin
  Scenario Outline: Verify I can send gif from preview
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the "<GiphyTag>" message
    And I tap GIF button from input tools
    # Wait for GIF picture to be downloaded
    And I wait for 10 seconds
    And I select the first item from Giphy grid
    And I tap Send button on Giphy preview page
    # Wait for sync
    And I wait for 3 seconds
    Then I see 1 photo in the conversation view

    Examples: 
      | Name      | Contact   | GiphyTag |
      | user1Name | user2Name | happy    |

  @C959 @regression @fastLogin
  Scenario Outline: Verify opening grid of gifs clicking on giphy icon
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the "<GiphyTag>" message
    And I tap GIF button from input tools
    # FIXME: WD freezes on Giphy preview page
    Then I see Giphy grid preview

    Examples:
      | Name      | Contact   | GiphyTag |
      | user1Name | user2Name | hello    |