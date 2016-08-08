Feature: Giphy

  @C955 @regression @fastLogin
  Scenario Outline: Verify preview is opened after tapping on GIF button
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type tag for giphy preview <GiphyTag> and open preview overlay
    # Wait for GIF picture to be downloaded
    And I wait for 10 seconds
    And I see giphy preview page

    Examples: 
      | Name      | Contact   | GiphyTag |
      | user1Name | user2Name | Happy    |

  @C962 @clumsy @regression @rc @IPv6 @fastLogin
  Scenario Outline: Verify I can send gif from preview
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type tag for giphy preview <GiphyTag> and open preview overlay
    # Wait for GIF picture to be downloaded
    And I wait for 10 seconds
    And I send gif from giphy preview page
    Then I see 1 photo in the conversation view

    Examples: 
      | Name      | Contact   | GiphyTag |
      | user1Name | user2Name | Happy    |

  @C959 @regression @fastLogin
  Scenario Outline: Verify opening grid of gifs clicking on giphy icon
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type tag for giphy preview <GiphyTag> and open preview overlay
    And I wait for 5 seconds
    When I click more giphy button
    Then I see giphy grid preview

    Examples: 
      | Name      | Contact   | GiphyTag |
      | user1Name | user2Name | Hello    |