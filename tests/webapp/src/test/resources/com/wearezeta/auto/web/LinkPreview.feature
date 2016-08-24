Feature: Link Preview

  @C169241 @regression
  Scenario Outline: Verify you can see preview for link sent from mobile
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

    Examples:
      | Login      | Password      | Name      | Contact   | Link                                                                                                               | LinkInPreview                                                                                           | LinkTitle                                                              | LinkPreviewImage |
      | user1Email | user1Password | user1Name | user2Name | https://wire.com                                                                                                   | wire.com                                                                                                | Wire — modern, private communication.                                  | linkpreview0.png |
      | user1Email | user1Password | user1Name | user2Name | http://www.heise.de/newsticker/meldung/Wire-Neuer-WebRTC-Messenger-soll-WhatsApp-Co-Konkurrenz-machen-2477770.html | heise.de/newsticker/meldung/Wire-Neuer-WebRTC-Messenger-soll-WhatsApp-Co-Konkurrenz-machen-2477770.html | Wire: Neuer WebRTC-Messenger soll WhatsApp &amp; Co. Konkurrenz machen | linkpreview1.png |

  @C169235 @regression @WEBAPP-2998
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
    When I click context menu of the latest message
    And I click to delete message for me in context menu
    And I click confirm to delete message for me
    Then I do not see a title <LinkTitle> in link preview in the conversation view
    And I do not see a picture <LinkPreviewImage> from link preview

    Examples:
      | Login      | Password      | Name      | Contact   | Link             | LinkInPreview | LinkTitle                             | LinkPreviewImage |
      | user1Email | user1Password | user1Name | user2Name | https://wire.com | wire.com      | Wire — modern, private communication. | linkpreview0.png |