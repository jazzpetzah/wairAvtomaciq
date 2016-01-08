Feature: E2EE

  @C3226 @staging @torun
  Scenario Outline: Verify you can receive encrypted and non-encrypted messages in 1:1 chat
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to Myself
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When Contact <Contact> sends encrypted message <EncryptedMessage> to user Myself
    And Contact <Contact> sends message <SimpleMessage> to user Myself
    And I tap on contact name <Contact>
    Then I see non-encrypted message <SimpleMessage> 1 time in the conversation view
    And I see encrypted message <EncryptedMessage> 1 time in the conversation view

    Examples:
      | Name      | Contact   | EncryptedMessage | SimpleMessage |
      | user1Name | user2Name | EncryptedYo      | SimpleYo      |
