Feature: Giphy

  @regression @id2787
  Scenario Outline: Verify preview is opened after tapping on GIF button
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I type tag for giphy preview <GiphyTag> and open preview overlay
    And I see giphy preview page

    Examples: 
      | Name      | Contact   | GiphyTag |
      | user1Name | user2Name | Happy    |

  @regression @rc @IPv6 @id2977
  Scenario Outline: Verify I can send gif from preview
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I type tag for giphy preview <GiphyTag> and open preview overlay
    And I wait for 5 seconds
    And I send gif from giphy preview page
    And I wait for 5 seconds
    And I see dialog page
    Then I see new photo in the dialog

    Examples: 
      | Name      | Contact   | GiphyTag |
      | user1Name | user2Name | Happy    |

  @regression @id2791
  Scenario Outline: Verify opening grid of gifs clicking on giphy icon
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I type tag for giphy preview <GiphyTag> and open preview overlay
    And I wait for 5 seconds
    When I click more giphy button
    Then I see giphy grid preview

    Examples: 
      | Name      | Contact   | GiphyTag |
      | user1Name | user2Name | Hello    |
