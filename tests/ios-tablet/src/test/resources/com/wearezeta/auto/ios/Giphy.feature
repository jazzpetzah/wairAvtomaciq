Feature: Giphy

  @C2686 @regression @id2961
  Scenario Outline: Verify preview is opened after tapping on GIF button [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I type tag for giphy preview <GiphyTag> and open preview overlay
    And I see giphy preview page

    Examples: 
      | Name      | Contact   | GiphyTag |
      | user1Name | user2Name | Wow      |

  @C2696 @regression @id3249
  Scenario Outline: Verify preview is opened after tapping on GIF button [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I type tag for giphy preview <GiphyTag> and open preview overlay
    And I see giphy preview page

    Examples: 
      | Name      | Contact   | GiphyTag |
      | user1Name | user2Name | Wow      |

  @C2694 @regression @id2978
  Scenario Outline: Verify I can send gif from preview [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
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

  @C2695 @regression @id2979
  Scenario Outline: Verify I can send gif from preview [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
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

  @C2697 @regression @id3293
  Scenario Outline: Verify opening grid of gifs clicking on giphy icon
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
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

  @C2698 @regression @id3294
  Scenario Outline: Verify opening grid of gifs clicking on giphy icon
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
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