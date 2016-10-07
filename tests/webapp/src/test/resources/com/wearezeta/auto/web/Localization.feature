Feature: Localization

  @C77945 @regression
  Scenario Outline: Verify registration screen has German-localized strings
    Given I switch to Sign In page
    When I switch language to <Language>
    And I switch to registration page
    Then I see Registration page
    And I see a string <CreateAccountLink> on the page
    And I see a placeholder <NamePlaceholder> on the page
    And I see a placeholder <EmailPlaceholder> on the page
    And I see a placeholder <PasswordPlaceholder> on the page
    And I see a button with <RegisterButton> on the page
    And I see a string <ToCText> on the page

    Examples:
      | Language | CreateAccountLink | NamePlaceholder | EmailPlaceholder | PasswordPlaceholder          | RegisterButton | ToCText                                |
      | de       | KONTO ERSTELLEN   | Name            | E-Mail-Adresse   | Passwort (min. acht Zeichen) | Erstellen      | Ich akzeptiere die Nutzungsbedingungen |

  @C77947 @regression
  Scenario Outline: Verify login screen has German-localized strings
    Given I switch to Sign In page
    Given I switch language to <Language>
    Given I switch to Sign In page
    Then I see a string <LoginLink> on the page
    And I see a placeholder <EmailPlaceholder> on the page
    And I see a placeholder <PasswordPlaceholder> on the page
    And I see a string <RememberMeText> on the page
    And I see a button with <SignInButton> on the page
    And I see a string <ForgotPasswordLink> on the page

    Examples:
      | Language | LoginLink | EmailPlaceholder | PasswordPlaceholder | RememberMeText     | SignInButton | ForgotPasswordLink |
      | de       | LOGIN     | E-Mail-Adresse   | Passwort            | ANGEMELDET BLEIBEN | Login        | Passwort vergessen |

  @C131208 @regression
  Scenario Outline: Verify conversation view and list has German-localized strings
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I switch language to <Language>
    And I see Contact list with name <Contact>
    And I see a string <ConversationViewText> on the page
    And I see a string <ContactListText> on the page
    When I open search by clicking the people button
    Then I see a placeholder <SearchPlaceHolder> on the page
    When I close People Picker
    And I click on options button for conversation <Contact>
    Then I see a conversation option <ConvOption1> on the page
    And I see a conversation option <ConvOption2> on the page
    And I see a conversation option <ConvOption3> on the page
    And I see a conversation option <ConvOption4> on the page

    Examples:
      | Login      | Password      | Name      | Contact   | Language | ConversationViewText | ContactListText  | SearchPlaceHolder                | ConvOption1   | ConvOption2 | ConvOption3 | ConvOption4 |
      | user1Email | user1Password | user1Name | user2Name | de       | HINZUGEFÜGT          | KONTAKTE         | Namen oder E-Mail-Adresse suchen | Stummschalten | Archivieren | Löschen     | Blockieren  |

  @C136458 @regression
  Scenario Outline: Verify support pages are opened in language <Language>
    Given There is 1 user where <Name> is me
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I switch language to <Language>
    Then I see Search is opened
    And I close People Picker
    Then I open preferences by clicking the gear button
    And I select <SupportButton> menu item on self profile page
    And I switch to support page tab
    Then I see ask support link
    And I see a title <PageTitle> on the page
    And I see a placeholder <SearchFieldPlaceholder> on the page
    And I see localized <Language> support page

    Examples:
      | Login      | Password      | Name      | Language | SupportButton | PageTitle      | SearchFieldPlaceholder |
      | user1Email | user1Password | user1Name | de       | Hilfe         | Wire Hilfe     | Gib ein Schlagwort ein |
      | user1Email | user1Password | user1Name | en       | Support       | Wire – Support | Enter a keyword        |

  @C150023 @regression
  Scenario Outline: Verify registration email is <Language>
    Given I see Registration page
    When I switch language to <Language>
    And I enter user name <Name> on Registration page
    And I enter user email <Email> on Registration page
    And I enter user password "<Password>" on Registration page
    And I accept the Terms of Use
    And I start activation email monitoring
    And I submit registration form
    Then I verify that an envelope icon is shown
    And I see email <Email> on Verification page
    And I see verification mail in <Language> with <Message>

    Examples:
      | Email      | Password      | Name      | Language | Message                                                                 |
      | user1Email | user1Password | user1Name | de       | Wenn du kein Wire-Benutzerkonto mit dieser E-Mail-Adresse erstellt hast |
      | user1Email | user1Password | user1Name | en       | If you didn't create a Wire account using this email address            |

  @C165102 @regression
  Scenario Outline: Verify new device email is <Language>
    Given I see Registration page
    When I switch language to <Language>
    And I enter user name <Name> on Registration page
    And I enter user email <Email> on Registration page
    And I enter user password "<Password>" on Registration page
    And I accept the Terms of Use
    And I start activation email monitoring
    And I submit registration form
    Then I verify that an envelope icon is shown
    And I see email <Email> on Verification page
    When I activate user by URL
    And I confirm keeping picture on Welcome page
    And I switch language to en
    And <Name> starts listening for new device mail
    And user <Name> adds a new device Device with label Label
    Then I see new device mail in <Language> with <Message>

    Examples:
      | Email      | Password      | Name      | Language    | Message                                                     |
      | user1Email | user1Password | user1Name | de          | Ein neues Gerät wurde deinem Wire-Benutzerkonto hinzugefügt |
      | user1Email | user1Password | user1Name | en          | Your Wire account was used on                               |

  @C234619 @regression
  Scenario Outline: Verify password reset email is <Language>
    Given I see Registration page
    When I switch language to <Language>
    And I enter user name <Name> on Registration page
    And I enter user email <Email> on Registration page
    And I enter user password "<Password>" on Registration page
    And I accept the Terms of Use
    And I start activation email monitoring
    And I submit registration form
    Then I verify that an envelope icon is shown
    And I see email <Email> on Verification page
    When I activate user by URL
    And I confirm keeping picture on Welcome page
    And I switch language to en
    And I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click logout button on clear data dialog
    And I see Sign In page
    And I click Change Password button
    And I see Password Change Request page
    And I enter email <Email> on Password Change Request page
    And <Name> starts listening for password change confirmation
    And I click Change Password button on Password Change Request page
    Then I see password change mail in <Language> with <Message>

    Examples:
      | Email      | Password      | Name      | Language | Message                                                                               |
      | user1Email | user1Password | user1Name | de       | wir haben eine Anfrage zur Änderung des Passworts deines Wire-Benutzerkontos bekommen |
      | user1Email | user1Password | user1Name | en       | We've received a request to change the password for your Wire account                 |