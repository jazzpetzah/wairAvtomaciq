Feature: Collections

  @C378049 @collection @staging
  Scenario Outline: Verify message is shown if no media is in collection
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I enable localytics via URL parameter
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact>
    And I click collection button in conversation
    #And I see localytics event <Event> without attributes
    Then I see info about no collection items

    Examples:
      | Email      | Password      | Name      | Contact   | Event                          |
      | user1Email | user1Password | user1Name | user2Name | collections.opened_collections |

  @C378050 @collection @staging
  Scenario Outline: Verify main overview shows media from all categories
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact>
    And I send picture <Picture> to the current conversation
    And I send <FileSize> sized file with name <FileName> to the current conversation
    And Contact <Contact> sends message <Link> via device Device1 to user me
    And I see link <LinkInPreview> in link preview message
    And I click collection button in conversation
    Then I see 1 picture in collection
    And I see 1 file in collection
    And I see 1 link in collection

    Examples:
      | Email      | Password      | Name      | Contact   | Picture                   | FileSize | FileName        | Link                                                                                                               | LinkInPreview                                                                                           |
      | user1Email | user1Password | user1Name | user2Name | userpicture_landscape.jpg | 1MB      | collections.txt | http://www.heise.de/newsticker/meldung/Wire-Neuer-WebRTC-Messenger-soll-WhatsApp-Co-Konkurrenz-machen-2477770.html | heise.de/newsticker/meldung/Wire-Neuer-WebRTC-Messenger-soll-WhatsApp-Co-Konkurrenz-machen-2477770.html |

  @C378052 @collection @staging
  Scenario Outline: Verify no pictures from different conversations are in the overview
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact1>
    And I send picture <Picture> to the current conversation
    And User <Contact1> sends image <Picture> to single user conversation <Name>
    Then I see only 2 pictures in the conversation
    When I click collection button in conversation
    Then I see 2 picture in collection
    When I open conversation with <Contact2>
    And I click collection button in conversation
    Then I see info about no collection items

    Examples:
      | Email      | Password      | Name      | Contact1  | Contact2  | Picture                   |
      | user1Email | user1Password | user1Name | user2Name | user3Name | userpicture_landscape.jpg |
  @C378053 @collection @staging
  Scenario Outline: Verify GIF pictures are not presented in library
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And I send picture <Gif> to the current conversation
    And I wait for 5 seconds
    And I write message <Text>
    And I click GIF button
    Then I see Giphy popup
    And I verify that the search of the Giphy popup contains <Text>
    And I see gif image in Giphy popup
    When I click send button in Giphy popup
    Then I see sent gif in the conversation view
    And I see only 2 pictures in the conversation
    When I click collection button in conversation
    Then I see info about no collection items

    Examples:
      | Email      | Password      | Name      | Contact   | Gif         | Text |
      | user1Email | user1Password | user1Name | user2Name | example.gif | test |
