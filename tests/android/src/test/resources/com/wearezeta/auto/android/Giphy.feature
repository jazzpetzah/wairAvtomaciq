Feature: Giphy

  @C787 @regression @rc @legacy
  Scenario Outline: I can send giphy image by typing massage and tapping GIF cursor button and confirm the selection
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I tap on text input
    And I type the message "<Message>"
    And I tap Gif button from cursor toolbar
    And I see the giphy grid preview
    Then I see Giphy search field with text "<Message>"
    When I select a random gif from the grid preview
    # C250824
    Then I see Send button on Giphy Preview page
    And I see Cancel button on Giphy Preview page
    When I tap on the giphy Send button
    # C787
    Then I see a picture in the conversation view
    And I see the most recent conversation message is "<Message> · via giphy.com"

    Examples:
      | Name      | Contact   | Message |
      | user1Name | user2Name | Yo      |

  @C250822 @regression
  Scenario Outline: Verify I can see "No GIFs found" in case of incorrect search query and correct it then close giphy
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I tap Gif button from cursor toolbar
    And I type "<InvalidMessage>" in Giphy toolbar input field and hide keyboard
    Then I see giphy error message
    When I clear Giphy search input field
    And I type "<ValidMessage>" in Giphy toolbar input field and hide keyboard
    Then I do not see giphy error message
    When I select a random gif from the grid preview
    Then I see giphy preview page
    When I close Giphy Grid view
    Then I see cursor toolbar

    Examples:
      | Name      | Contact   | InvalidMessage | ValidMessage |
      | user1Name | user2Name | +++++++        | Yo           |

  @C250823 @regression
  Scenario Outline: Verify I can see updated search results after discard selected gif and change search query
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I tap Gif button from cursor toolbar
    And I type "<Message1>" in Giphy toolbar input field and hide keyboard
    And I remember the state of Giphy Image Container
    And I select a random gif from the grid preview
    And I tap on the giphy Cancel button
    And I see the giphy grid preview
    And I clear Giphy search input field
    And I type "<Message2>" in Giphy toolbar input field and hide keyboard
    Then I verify the state of Giphy Image Container is changed

    Examples:
      | Name      | Contact   | Message1 | Message2 |
      | user1Name | user2Name | TEST     | Yo       |

  @C250821 @regression
  Scenario Outline: I can search a GIF image by tapping GIF cursor button
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I tap Gif button from cursor toolbar
    Then I see the giphy grid preview
    When I type "<Message>" in Giphy toolbar input field and hide keyboard
    And I select a random gif from the grid preview
    When I tap on the giphy Send button
    Then I see a picture in the conversation view
    And I see the most recent conversation message is "<Message> · via giphy.com"

    Examples:
      | Name      | Contact   | Message |
      | user1Name | user2Name | Yo      |

  @C250825 @regression
  Scenario Outline: Verify Giphy grid stays opened when I receive a message in current/other conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And I tap Gif button from cursor toolbar
    And I see the giphy grid preview
    And User <Contact1> sends encrypted message "<Message1>" to user Myself
    Then I see the giphy grid preview
    And User <Contact2> sends encrypted message "<Message2>" to user Myself
    Then I see the giphy grid preview

    Examples:
      | Name      | Contact1  | Contact2  | Message1 | Message2 |
      | user1Name | user2Name | user3Name | Current  | Other    |

  @C250827 @regression
  Scenario Outline: Verify I can discard gif picture and select another in giphy grid
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I type the message "<Message>"
    And I tap Gif button from cursor toolbar
    And I see the giphy grid preview
    And I select 1st gif from the grid preview
    And I remember the state of Giphy Image Preview
    And I tap on the giphy Cancel button
    And I see the giphy grid preview
    And I select 2nd gif from the grid preview
    Then I verify the state of Giphy Image Preview is changed

    Examples:
      | Name      | Contact   | Message |
      | user1Name | user2Name | Yo      |