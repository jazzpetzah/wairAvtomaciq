Feature: File Transfer

  @C82815 @filetransfer
  Scenario Outline: Verify a file from filesystem is sent and received successfully in 1:1
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <Contact>
    When I open conversation with <Contact>
    Then I see file transfer button in conversation input
    When I send file <File> to the current conversation
    # Verify receiver is notified on upload start (might be moved to a second test)
    Then I verify icon of file <File> in the conversation view
    And I see file transfer for file <File> in the conversation view
    And I verify size of file <File> in the conversation view
    And I verify status of file <File> is UPLOADING in the conversation view
    # Verify file info (icon, name, size) is shown on receiver side
    # Verify receiver is notified when upload is finished (might be moved to a second test)

    Examples:
      | Login      | Password      | Name      | Contact   | File                      |
      | user1Email | user1Password | user1Name | user2Name | userpicture_landscape.jpg |