Feature: Link Preview

  @C169204 @smoke
  Scenario Outline: Verify you can see preview for sent link in 1:1
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And I write message <Link>
    Then I send message
    Then I see link <LinkInPreview> in link preview message
    And I see a title <LinkTitle> in link preview in the conversation view
    And I see a picture <LinkPreviewImage> from link preview
    And I see 2 messages in conversation

    Examples:
      | Login      | Password      | Name      | Contact   | Link              | LinkInPreview | LinkTitle                                                                            | LinkPreviewImage |
      | user1Email | user1Password | user1Name | user2Name | https://wire.com/ | wire.com      | Wire · Modern communication, full privacy. For iOS, Android, macOS, Windows and web. | linkpreview0.png |

  @C169205 @smoke
  Scenario Outline: Verify you can delete link preview
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <Contact>
    When I open conversation with <Contact>
    And Contact <Contact> sends message <Link> via device Device1 to user me
    Then I see link <LinkInPreview> in link preview message
    And I see a title <LinkTitle> in link preview in the conversation view
    And I see a picture <LinkPreviewImage> from link preview
    And I see 2 messages in conversation
    When I click context menu of the last message
    And I click delete in message context menu for my own message
    And I click confirm to delete message for me
    Then I do not see a title <LinkTitle> in link preview in the conversation view
    And I do not see a picture <LinkPreviewImage> from link preview
    And I see 1 messages in conversation

    Examples:
      | Login      | Password      | Name      | Contact   | Link             | LinkInPreview | LinkTitle                                                                            | LinkPreviewImage |
      | user1Email | user1Password | user1Name | user2Name | https://wire.com | wire.com      | Wire · Modern communication, full privacy. For iOS, Android, macOS, Windows and web. | linkpreview0.png |

  @C169208 @smoke
  Scenario Outline: Verify sending link previews for link mixed with text
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And I write message <TextWithLink>
    Then I send message
    Then I see link <LinkInPreview> in link preview message
    And I see a title <LinkTitle> in link preview in the conversation view
    And I see a picture <LinkPreviewImage> from link preview
    And I see text message <TextWithLink>

    Examples:
      | Login      | Password      | Name      | Contact   | TextWithLink                                          | LinkInPreview | LinkTitle                                                                            | LinkPreviewImage |
      | user1Email | user1Password | user1Name | user2Name | You can go to wire.com and download the best app ever | wire.com      | Wire · Modern communication, full privacy. For iOS, Android, macOS, Windows and web. | linkpreview0.png |