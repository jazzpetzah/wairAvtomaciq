Feature: Giphy

  @C2696 @rc @regression @fastLogin
  Scenario Outline: Verify preview is opened after tapping on GIF button [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the "<GiphyTag>" message
    And I tap GIF button from input tools
    # Wait for GIF grid to be downloaded
    And I wait for 10 seconds
    And I select the first item from Giphy grid
    Then I see Giphy preview page

    Examples: 
      | Name      | Contact   | GiphyTag |
      | user1Name | user2Name | Wow      |

  @C2695 @regression @fastLogin
  Scenario Outline: Verify I can send gif from preview [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the "<GiphyTag>" message
    And I tap GIF button from input tools
    # Wait for GIF grid to be downloaded
    And I wait for 10 seconds
    And I select the first item from Giphy grid
    And I tap Send button on Giphy preview page
    Then I see 1 photo in the conversation view

    Examples: 
      | Name      | Contact   | GiphyTag |
      | user1Name | user2Name | Happy    |

  @C2698 @regression @fastLogin
  Scenario Outline: Verify opening grid of gifs clicking on giphy icon
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the "<GiphyTag>" message
    And I tap GIF button from input tools
    Then I see Giphy grid preview

    Examples: 
      | Name      | Contact   | GiphyTag |
      | user1Name | user2Name | Hello    |